/**
 * 
 */
package services.bot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import services.bot.managers.BotEventHandler;
import services.bot.managers.ButtonI;
import services.bot.managers.ButtonManager;
import services.bot.managers.CommandI;
import services.bot.managers.CommandManager;
import services.bot.managers.MessageManager;
import services.bot.managers.MessengerI;
import services.bot.managers.ModalI;
import services.bot.managers.ModalManager;

/**
 * @author Alfredo
 *
 */
public class ListenerBuilder extends ListenerAdapter {
	
	// Instantiation of all the programmable listener
	// adapters that this bot can make use of
	private ModalManager modalManager;
	private ButtonManager buttonManager;
	private CommandManager commandManager;
	private MessageManager messageManager;
	
	private Map<Class<?>, ProgrammableAdapter<?>> programmableAdapters;
	
	/**
	 * Create and prepare a list of listener adapters
	 * to be directly inserted into the JDA's listener
	 * adapter collection and executed after listening 
	 * for any callback inside the Discord server
	 */
	public ListenerBuilder() {
		this.programmableAdapters = new HashMap<>();
		
		// Initialize the managers
		this.modalManager = new ModalManager();
		this.buttonManager = new ButtonManager();
		this.commandManager = new CommandManager();
		this.messageManager = new MessageManager();
		
		this.programmableAdapters.put(ModalI.class, modalManager);
		this.programmableAdapters.put(ButtonI.class, buttonManager);
		this.programmableAdapters.put(CommandI.class, commandManager);
		this.programmableAdapters.put(MessengerI.class, messageManager);
	}
	
	/**
	 * @param components
	 */
	public void addComponents(Collection<BotEventHandler> components) {
		for(BotEventHandler handler : components) {
			if(handler instanceof ModalI modal)
				this.modalManager.add(modal);
			if(handler instanceof ButtonI button)
				this.buttonManager.add(button);
			if(handler instanceof CommandI command)
				this.commandManager.add(command);
			if(handler instanceof MessengerI message)
				this.messageManager.add(message);
		}
	}
	
	/**
	 * Free all memory after bot shuts down
	 */
	public void dispose() {
		for(ProgrammableAdapter<?> progAdapters : programmableAdapters.values())
			progAdapters.dispose();
		programmableAdapters.clear();
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		for(ProgrammableAdapter<?> progAdapters : programmableAdapters.values())
			progAdapters.init(event);
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		messageManager.onGuildMemberJoin(event);
	}
	
	@Override
	public void onModalInteraction(ModalInteractionEvent event) {
		programmableAdapters.get(ModalI.class).onInteraction(event);
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		programmableAdapters.get(ButtonI.class).onInteraction(event);
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		programmableAdapters.get(CommandI.class).onInteraction(event);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		programmableAdapters.get(MessengerI.class).onInteraction(event);
	}
}
