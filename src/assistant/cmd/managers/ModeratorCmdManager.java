/**
 * 
 */
package assistant.cmd.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import assistant.cmd.moderation.BotServiceCmd;
import services.bot.core.BotApplication;
import services.bot.interactions.InteractableEvent;

/**
 * @author Alfredo
 *
 */
@Deprecated
public class ModeratorCmdManager {

	private List<InteractableEvent> componentAdapters;
	
	/**
	 * 
	 * @param bot
	 */
	public ModeratorCmdManager(BotApplication bot) {
		this.componentAdapters = new ArrayList<>();
		
		/*
		 * Create the commands, preferably load them
		 * directly from a json file and upsert them to the jda.
		 */
//		componentAdapters.add(new LoginCmd());
		componentAdapters.add(new BotServiceCmd(bot));
		
		// Do a registration server command
	}
	
	/**
	 * @return Collection of component adapters
	 */
	public Collection<InteractableEvent> getComponents() {
		return componentAdapters;
	}
}
