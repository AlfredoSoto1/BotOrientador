/**
 * 
 */
package services.bot.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	
	private List<ModalI> modals;
	private List<ButtonI> buttons;
	private List<CommandI> commands;
	private List<MessengerI> messages;
	
	/**
	 * Create and prepare a list of listener adapters
	 * to be directly inserted into the JDA's listener
	 * adapter collection and executed after listening 
	 * for any callback inside the Discord server
	 */
	public ListenerAdapterManager(CountDownLatch latch) {
		this.latch = latch;
		this.modals = new ArrayList<>();
		this.buttons = new ArrayList<>();
		this.commands = new ArrayList<>();
		this.messages = new ArrayList<>();
		
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
		for(InteractableEvent interaction : interactions) {
			if (interaction instanceof ModalI modal)
				this.modals.add(modal);
			if (interaction instanceof ButtonI button)
				this.buttons.add(button);
			if (interaction instanceof CommandI command)
				this.commands.add(command);
			if (interaction instanceof MessengerI messenger)
				this.messages.add(messenger);
		}
	}
	
	/**
	 * Free all memory after bot shuts down
	 */
	public void dispose() {
		for(ModalI modal : modals)
			modal.dispose();
		for(ButtonI button : buttons)
			button.dispose();
		for(CommandI command : commands)
			command.dispose();
		for(MessengerI messenger : messages)
			messenger.dispose();
		
		modals.clear();
		buttons.clear();
		commands.clear();
		messages.clear();
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		for(ModalI modal : modals) {
			modal.init(event);
		}
		for(ButtonI button : buttons) {
			button.init(event);
		}
		for(CommandI command : commands) {
			upsertCommand(event);
			command.init(event);
		}
		for(MessengerI messenger : messages) {
			messenger.init(event);
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
		for(ModalI modal : modals) {
			// Check if the modal that currently is having an action to
			// be executed to run the modal results
			if(modal.getModalIDs().contains(event.getModalId())) {
				modal.modalResults(event);
				// Once the modal results have been produced, we can
				// exit the loop since we don't need to keep looking
				// for more events. Since the JDA execute this onModalInteraction() method
				// in queue after each command triggered the modal
				return;
			}
		}
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		for(ButtonI button : buttons) {
			if(button.getButtonIDs().contains(event.getButton().getId()))
				button.onButtonEvent(event.getButton().getId(), event);
		}
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		for(CommandI command : commands) {
			if(!command.getCommandName().equals(event.getName()))
				continue;
			command.execute(event);
			return;
		}
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
			for(CommandI command : commands) {
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
