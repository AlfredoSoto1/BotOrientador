/**
 * 
 */
package services.bot.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import botOrientador.commands.management.LoginCmd;
import botOrientador.commands.management.ProfanityCmd;
import services.bot.adapters.ComponentAdapter;
import services.bot.dbaccess.DBRoleManager;
import services.bot.orientador.controllers.profanity.ProfanityFilter;

/**
 * @author Alfredo
 *
 */
public class ManagementControl {

	private DBRoleManager roleManager;
	
	private LoginCmd loginCmd;
	private ProfanityFilter filter;
	private ProfanityCmd profanityCmd;
	
	private List<ComponentAdapter> componentAdapters;
	
	/**
	 * Creates and initializes all the profanity related
	 * content. With this instance, we can load up all
	 * the components to the corresponding listener adapters
	 */
	public ManagementControl() {
		this.componentAdapters = new ArrayList<>();
		
		this.roleManager = new DBRoleManager();
		
		// Create and prepare the profanity command
		// This is only accessible for bot developers
		this.loginCmd = new LoginCmd(roleManager);
		this.filter = new ProfanityFilter();
		this.profanityCmd = new ProfanityCmd(roleManager);

		componentAdapters.add(loginCmd);
		componentAdapters.add(filter);
		componentAdapters.add(profanityCmd);
	}
	
	/**
	 * @return Collection of component adapters
	 */
	public Collection<ComponentAdapter> getComponents() {
		return componentAdapters;
	}
	
	/**
	 * Disposes all content from 'this' instance
	 */
	public void dispose() {
		loginCmd.dispose();
		roleManager.dispose();
		profanityCmd.dispose();
		componentAdapters.clear();
	}
}
