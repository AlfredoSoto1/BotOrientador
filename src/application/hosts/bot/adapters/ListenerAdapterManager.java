/**
 * 
 */
package application.hosts.bot.adapters;

import java.util.ArrayList;
import java.util.List;

import application.hosts.bot.buttons.ButtonManager;
import application.hosts.bot.commands.CommandManager;
import application.hosts.bot.messages.MessageManager;
import application.hosts.bot.modals.ModalManager;
import application.hosts.bot.startup.StartupManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * @author Alfredo
 *
 */
public class ListenerAdapterManager {
	
	/**
	 * 
	 * @author Alfredo
	 *
	 */
	public interface CustomAdapterCreator {
		void create(
			ModalManager modalManager,
			ButtonManager buttonManager,
			CommandManager commandManager,
			MessageManager messageManager,
			StartupManager startupManager
		);
	}
	
	private List<ListenerAdapter> listenerAdapters;
	
	// Declare managers
	private ModalManager modalManager;
	private ButtonManager buttonManager;
	private CommandManager commandManager;
	private MessageManager messageManager;
	private StartupManager startupManager;
	
	public ListenerAdapterManager() {
		this.listenerAdapters = new ArrayList<>();

		// Initialize the managers
		this.modalManager = new ModalManager();
		this.buttonManager = new ButtonManager();
		this.commandManager = new CommandManager();
		this.messageManager = new MessageManager();
		this.startupManager = new StartupManager();

		this.listenerAdapters.add(modalManager);
		this.listenerAdapters.add(buttonManager);
		this.listenerAdapters.add(commandManager);
		this.listenerAdapters.add(messageManager);
		this.listenerAdapters.add(startupManager);
		
		for(ListenerAdapter adapter : listenerAdapters)
			if(adapter instanceof ProgrammableAdapter programableAdaper)
				programableAdaper.init();
	}
	
	public ListenerAdapter[] getAdapters() {
		return listenerAdapters.toArray(new ListenerAdapter[] {});
	}
	
	/**
	 * Create the custom adapters
	 * for the bot 
	 * 
	 * @param customAdapters
	 */
	public void createAdapters(CustomAdapterCreator customAdapters) {
		customAdapters.create(
			modalManager,
			buttonManager,
			commandManager,
			messageManager,
			startupManager
		);
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
