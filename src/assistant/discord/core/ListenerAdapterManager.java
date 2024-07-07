package assistant.discord.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import assistant.discord.interaction.ButtonActionEvent;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.discord.interaction.MessengerI;
import assistant.discord.interaction.ModalActionEvent;
import assistant.discord.interaction.SelectMenuActionEvent;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionDisconnectEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * @author Alfredo
 *
 */
public class ListenerAdapterManager extends ListenerAdapter {
	
	private CountDownLatch latch;
	
	private List<MessengerI> messages;
	private Map<String, CommandI> commands;
	private Map<Long, MessengerI> messageReactions;

	private Map<String, ModalActionEvent> modals;
	private Map<String, ButtonActionEvent> buttons;
	private Map<String, SelectMenuActionEvent> selectMenus;
	
	private List<InteractionModel> interactions;

	/**
	 * Create and prepare a list of listener adapters
	 * to be directly inserted into the JDA's listener
	 * adapter collection and executed after listening 
	 * for any callback inside the Discord server
	 * 
	 * @param latch
	 */
	public ListenerAdapterManager(CountDownLatch latch) {
		this.latch = latch;
		this.modals = new HashMap<>();
		this.buttons = new HashMap<>();
		this.selectMenus = new HashMap<>();
		
		this.commands = new HashMap<>();
		this.messages = new ArrayList<>();
		this.messageReactions = new HashMap<>();
		
		this.interactions = new ArrayList<>();
		
		/*
		 * Initialize the managers
		 * 
		 * - Modals; are the user promps that appear on screen.
		 *   These can have buttons, fields, etc...
		 * 
		 * - Buttons; can be embedded into text messages
		 * 
		 * - Commands; are instructions that the client can give to
		 *   the bot and it will respond to them directly.
		 *   
		 * - Message; lets the bot listen and handle any kind of
		 *   message sent or received in the server by any client.
		 */
	}
	
	/**
	 * 
	 * @param interactions
	 */
	public void upsertInteractions(Collection<InteractionModel> interactions) {
		// We want to add to store each interaction individually.
		// This is so that each interaction gets its proper work flow and handle.
		for (InteractionModel interaction : interactions) {
			// Add the command for interaction
			if (interaction instanceof CommandI command)
				this.commands.put(command.getCommandName(), command);
			
			// Add all messenger interactions
			if (interaction instanceof MessengerI messenger)
				this.messages.add(messenger);
			
			this.interactions.add(interaction);
			
			// Add all registered interaction events
			modals.putAll(interaction.getRegisteredModals());
			buttons.putAll(interaction.getRegisteredButtons());
			selectMenus.putAll(interaction.getRegisteredSelectMenu());
		}
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		// Delete all commands before re-upserting them
		// This ensures there is no residual command from testing
		event.getJDA().updateCommands().queue();
		event.getJDA().getGuilds().forEach(server -> server.updateCommands().queue());
		
		for(InteractionModel interaction : interactions) {
			if (interaction instanceof CommandI command) {
				// Global commands will get created/updated
				// when the bot starts running
				if (command.isGlobal())
					command.upsertJDACommand(event.getJDA());
				else
					// Start the interaction model for each server
					// Each server should handle the creation of the command
					event.getJDA().getGuilds().forEach(command::upsertGuildCommand);
			}
			// Initiate each interaction
			interaction.onJDAInit(event);
			event.getJDA().getGuilds().forEach(interaction::onGuildInit);
		}
	}
	
	@Override
	public void onShutdown(ShutdownEvent event) {
		for(InteractionModel interaction : interactions)
			interaction.onDispose();
		
		// Start the count down to let know the main
		// thread when to dispose all content generated
		// asynchronously from the bot application
		latch.countDown();
		
		// Free memory
		modals.clear();
		buttons.clear();
		selectMenus.clear();
		commands.clear();
		messages.clear();
		messageReactions.clear();
		interactions.clear();
	}
	
	@Override
    public void onSessionDisconnect(SessionDisconnectEvent event) {
		// Put on hold all tasks when disconnected
		// NOTE: only put on hold tasks that involve
		// client request. NOT inner processes
	}
	
	@Override
    public void onSessionResume(SessionResumeEvent event) {
		// Resume any tasks here that got interrupted
	}
	
    @Override
    public void onStatusChange(StatusChangeEvent event) {
    	// Handle the status of the bot here.
    	// A status is the current connection that it has
    	// to the servers. i.e. CONNECTED, DISCONNECTED, CONNECTING
    }
    
    @Override
    public void onException(ExceptionEvent event) {
    	// This gets called when an exception happens within JDA
    }
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if(event.getUser().isBot())
			return;
		// This results as a message output to a
		// user when it joins the server
		for(MessengerI messenger : messages)
			messenger.memberJoin(event);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot())
			return;
		for(MessengerI messenger : messages)
			messenger.messageReceived(event);
	}
	
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if(event.getUser().isBot())
			return;
		// Check for the interaction to be a messenger
		// with this we can store the reaction id of each message
		for(MessengerI messenger : messages)
			for (int i = 0; i < messenger.getMessageID().size();i++)
				messenger.onMessageReaction(event);
	}
    
	@Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		if(event.getUser().isBot())
			return;
		// Check for the interaction to be a messenger
		// with this we can store the reaction id of each message
		for(MessengerI messenger : messages)
			for (int i = 0; i < messenger.getMessageID().size();i++)
				messenger.onMessageReaction(event);
	}

	@Override
	public void onModalInteraction(ModalInteractionEvent event) {
		ModalActionEvent action = modals.get(event.getModalId());
		
		if(action != null)
			action.modalResults(event);
		else
			event.reply("The modal that you are interacting with is not registered!").queue();
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		ButtonActionEvent action = buttons.get(event.getComponentId());
		
		if(action != null)
			action.onAction(event);
		else
			event.reply("The button that you are interacting with is not registered!").queue();
	}
	
	@Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
		SelectMenuActionEvent action = selectMenus.get(event.getComponentId());
		
		if(action != null)
			action.onMenuSelection(event);
		else
			event.reply("The Select Menu that you are interacting with is not registered!").queue();
    }
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		// Look for the command in table by name
		// if the command is not registered it will
		// throw an error message.
		CommandI command = commands.get(event.getName());
		
		if(command != null)
			command.execute(event);
		else
			event.reply("The command you entered is not registered!").queue();
	}
}
