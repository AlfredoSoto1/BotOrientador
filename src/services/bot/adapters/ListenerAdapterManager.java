/**
 * 
 */
package services.bot.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import services.bot.buttons.ButtonI;
import services.bot.buttons.ButtonManager;
import services.bot.commands.CommandI;
import services.bot.commands.CommandManager;
import services.bot.messages.MessageManager;
import services.bot.messages.MessengerI;
import services.bot.modals.ModalI;
import services.bot.modals.ModalManager;
import services.bot.startup.StartupI;
import services.bot.startup.StartupManager;

/**
 * @author Alfredo
 *
 */
public class ListenerAdapterManager {

	// Instantiation of all the programmable listener
	// adapters that this bot can make use of
	private ModalManager modalManager;
	private ButtonManager buttonManager;
	private CommandManager commandManager;
	private MessageManager messageManager;
	private StartupManager startupManager;

	private List<ListenerAdapter> listenerAdapters;
	
	/**
	 * Create and prepare a list of listener adapters
	 * to be directly inserted into the JDA's listener
	 * adapter collection and executed after listening 
	 * for any callback inside the Discord server
	 */
	public ListenerAdapterManager() {
		this.listenerAdapters = new ArrayList<>();

		// Initialize the managers
		this.modalManager = new ModalManager();
		this.buttonManager = new ButtonManager();
		this.commandManager = new CommandManager();
		this.messageManager = new MessageManager();
		this.startupManager = new StartupManager();

		// Add to list
		this.listenerAdapters.add(modalManager);
		this.listenerAdapters.add(buttonManager);
		this.listenerAdapters.add(commandManager);
		this.listenerAdapters.add(messageManager);
		this.listenerAdapters.add(startupManager);
		
		// Initialize every listener
		for(ListenerAdapter adapter : listenerAdapters)
			if(adapter instanceof ProgrammableAdapter programableAdaper)
				programableAdaper.init();
	}
	
	/**
	 * @return Listener adapter array
	 */
	public ListenerAdapter[] getAdapters() {
		return listenerAdapters.toArray(new ListenerAdapter[] {});
	}
	
	/**
	 * 
	 * @param components
	 */
	public void loadComponentAdapters(Collection<ComponentAdapter> components) {
		for(ComponentAdapter component : components) {
			if(component instanceof ModalI modal)
				this.modalManager.add(modal);
			if(component instanceof ButtonI button)
				this.buttonManager.add(button);
			if(component instanceof CommandI command)
				this.commandManager.add(command);
			if(component instanceof MessengerI message)
				this.messageManager.add(message);
			if(component instanceof StartupI startup)
				this.startupManager.add(startup);
		}
	}
	
	/**
	 * Free all memory after bot shuts down
	 */
	public void dispose() {
		for(ListenerAdapter adapter : listenerAdapters)
			if(adapter instanceof ProgrammableAdapter programableAdaper)
				programableAdaper.dispose();
		listenerAdapters.clear();
	}
}
