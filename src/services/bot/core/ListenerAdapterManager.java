/**
 * 
 */
package services.bot.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import services.bot.interactions.ButtonI;
import services.bot.interactions.CommandI;
import services.bot.interactions.InteractableEvent;
import services.bot.interactions.MessengerI;
import services.bot.interactions.ModalI;

/**
 * @author Alfredo
 *
 */
public class ListenerAdapterManager extends ListenerAdapter {
	
	private CountDownLatch latch;
	
	private Map<String, ModalI> modals;
	private Map<String, ButtonI> buttons;
	private Map<String, CommandI> commands;
	private List<MessengerI> messages;
	private List<InteractableEvent> interactableEvents;
	
	/**
	 * Create and prepare a list of listener adapters
	 * to be directly inserted into the JDA's listener
	 * adapter collection and executed after listening 
	 * for any callback inside the Discord server
	 */
	public ListenerAdapterManager(CountDownLatch latch) {
		this.latch = latch;
		this.modals = new HashMap<>();
		this.buttons = new HashMap<>();
		this.commands = new HashMap<>();
		this.messages = new ArrayList<>();
		this.interactableEvents = new ArrayList<>();
		
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
	 * @param components
	 */
	public void upsertInteractions(Collection<InteractableEvent> interactions) {
		// Each interaction will work independently from one
		// another. The key here is to also store the parent
		// InteractableEvent to use as the general case for
		// the event whenever used.
		for(InteractableEvent interaction : interactions) {
//			if (interaction instanceof ModalI modal)
//				this.modals.add(modal);
//			if (interaction instanceof ButtonI button)
//				this.buttons.add(button.get);
			if (interaction instanceof CommandI command)
				this.commands.put(command.getCommandName(), command);
			if (interaction instanceof MessengerI messenger)
				this.messages.add(messenger);
			
			// Add the interaction interface itself
			interactableEvents.add(interaction);
		}
	}
	
	/**
	 * Free all memory after bot shuts down
	 */
	public void dispose() {
		for(InteractableEvent interaction : interactableEvents)
			interaction.dispose();
		
		modals.clear();
		buttons.clear();
		commands.clear();
		messages.clear();
		interactableEvents.clear();
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		for(InteractableEvent interaction : interactableEvents) {
			if (interaction instanceof CommandI)
				upsertCommand(event);
			
			interaction.init(event);
		}
	}
	
	@Override
	public void onShutdown(ShutdownEvent event) {
		// Start the count down to let know the main
		// thread when to dispose all content generated
		// asynchronously from the bot application
		latch.countDown();
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
	public void onModalInteraction(ModalInteractionEvent event) {
//		for(ModalI modal : modals) {
//			// Check if the modal that currently is having an action to
//			// be executed to run the modal results
//			if(modal.getModalIDs().contains(event.getModalId())) {
//				modal.modalResults(event);
//				// Once the modal results have been produced, we can
//				// exit the loop since we don't need to keep looking
//				// for more events. Since the JDA execute this onModalInteraction() method
//				// in queue after each command triggered the modal
//				return;
//			}
//		}
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
//		for(ButtonI button : buttons) {
//			if(button.getButtonIDs().contains(event.getButton().getId()))
//				button.onButtonEvent(event.getButton().getId(), event);
//		}
		
		ButtonI button = buttons.get(event.getId());
		
		if(button != null)
			button.onButtonEvent(event.getId(), event);
		else
			event.reply("The button that you are interacting with is not registered!").queue();
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
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot())
			return;
		
		for(MessengerI messenger : messages)
			messenger.messageReceived(event);
	}
	
	private void upsertCommand(ReadyEvent event) {
		for(Guild server : event.getJDA().getGuilds()) {
			for(CommandI command : commands.values()) {
				/*
				 * Commands that are global, are commands
				 * that are accessible throughout all servers
				 * and private channels that the bot can interact with the user.
				 */
				if(command.isGlobal()) {
					// For global commands, they get inserted
					// directly into the JDA when it loads.
					// This action can take a long time to see
					// the command fully activated in the server.
					event.getJDA().upsertCommand(
						command.getCommandName(), 
						command.getDescription()
					)
					.addOptions(command.getOptions())
					.queue();
				} else {
					// For non global commands, they get
					// inserted directly into the server.
					// This has no delay when looking for the
					// command in the server where the bot is.
					server.upsertCommand(
						command.getCommandName(),
						command.getDescription()
					)
					.addOptions(command.getOptions())
					.queue();
				}
			}
		}
	}
	
}
