/**
 * 
 */
package ece.assistant.cmd.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ece.assistant.cmd.moderation.BotServiceCmd;
import services.bot.core.BotApplication;
import services.bot.managers.BotEventHandler;

/**
 * @author Alfredo
 *
 */
public class ModeratorCmdManager {

	private List<BotEventHandler> componentAdapters;
	
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
	public Collection<BotEventHandler> getComponents() {
		return componentAdapters;
	}
	
	/**
	 * Disposes all content from 'this' instance
	 */
	public void dispose() {
		for(BotEventHandler handler : componentAdapters)
			handler.dispose();
		componentAdapters.clear();
	}
}
