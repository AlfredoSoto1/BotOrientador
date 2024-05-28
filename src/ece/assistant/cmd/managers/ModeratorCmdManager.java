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

//	private LoginCmd loginCmd;
	private BotServiceCmd serviceCmd;
	
	private List<BotEventHandler> componentAdapters;
	
	/**
	 * 
	 * @param bot
	 */
	public ModeratorCmdManager(BotApplication bot) {
		this.componentAdapters = new ArrayList<>();
		
//		this.loginCmd = new LoginCmd();
		this.serviceCmd = new BotServiceCmd(bot);

//		componentAdapters.add(loginCmd);
		componentAdapters.add(serviceCmd);
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
//		loginCmd.dispose();
		serviceCmd.dispose();
		componentAdapters.clear();
	}
}
