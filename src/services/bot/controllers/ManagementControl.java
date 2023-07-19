/**
 * 
 */
package services.bot.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import botOrientador.behavior.ProfanityFilter;
import botOrientador.commands.management.BotServiceCmd;
import botOrientador.commands.management.LoginCmd;
import botOrientador.commands.management.ProfanityCmd;
import services.bot.GenericBot;
import services.bot.managers.BotEventHandler;

/**
 * @author Alfredo
 *
 */
public class ManagementControl {

	private LoginCmd loginCmd;
	private ProfanityFilter filter;
	private ProfanityCmd profanityCmd;
	private BotServiceCmd serviceCmd;
	
	private List<BotEventHandler> componentAdapters;
	
	/**
	 * Creates and initializes all the profanity related
	 * content. With this instance, we can load up all
	 * the components to the corresponding listener adapters
	 */
	public ManagementControl(GenericBot genericBot) {
		this.componentAdapters = new ArrayList<>();
		
		// Create and prepare the profanity command
		// This is only accessible for bot developers
		this.loginCmd = new LoginCmd();
		this.filter = new ProfanityFilter();
		this.profanityCmd = new ProfanityCmd();

		this.serviceCmd = new BotServiceCmd(genericBot);

		componentAdapters.add(filter);
		componentAdapters.add(loginCmd);
		componentAdapters.add(serviceCmd);
		componentAdapters.add(profanityCmd);
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
		loginCmd.dispose();
		serviceCmd.dispose();
		profanityCmd.dispose();
		componentAdapters.clear();
	}
}
