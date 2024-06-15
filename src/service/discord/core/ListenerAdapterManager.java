package service.discord.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionDisconnectEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import service.discord.interaction.ButtonActionEvent;
import service.discord.interaction.CommandI;
import service.discord.interaction.InteractionModel;
import service.discord.interaction.MessengerI;
import service.discord.interaction.ModalActionEvent;
import service.discord.interaction.SelectMenuActionEvent;

/**
 * @author Alfredo
 *
 */
public class ListenerAdapterManager extends ListenerAdapter {
	
	private CountDownLatch latch;
	
	private List<MessengerI> messages;
	private Map<String, CommandI> commands;

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
		for(InteractionModel interaction : interactions) {
			if (interaction instanceof CommandI command)
				this.commands.put(command.getCommandName(), command);
			if (interaction instanceof MessengerI messenger)
				this.messages.add(messenger);
			
			this.interactions.add(interaction);
			
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
			interaction.upsertCommand(event);
			interaction.onInit(event);
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
		// TODO
	}
    
	@Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		// TODO
	}

	@Override
    public void onMessageReactionRemoveAll(MessageReactionRemoveAllEvent event) {
		// TODO
	}
	
	@Override
    public void onMessageReactionRemoveEmoji(MessageReactionRemoveEmojiEvent event) {
		// TODO
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
