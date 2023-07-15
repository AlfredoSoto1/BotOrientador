/**
 * 
 */
package services.bot.orientador.profanityControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import services.bot.adapters.ComponentAdapter;
import services.bot.dbaccess.DBProfanityManager;
import services.bot.dbaccess.DBRoleManager;
import services.bot.orientador.commands.maintenance.ProfanityCommand;

/**
 * @author Alfredo
 *
 */
public class ProfanityManager {

	private DBRoleManager roleManager;
	private DBProfanityManager profanityManager;
	
	private ProfanityFilter filter;
	private ProfanityCommand command;
	
	private List<ComponentAdapter> componentAdapters;
	
	/**
	 * 
	 */
	public ProfanityManager() {
		this.componentAdapters = new ArrayList<>();
		
		this.roleManager = new DBRoleManager();
		this.profanityManager = new DBProfanityManager();
		
		// Create and prepare the profanity filter
		this.filter = new ProfanityFilter(profanityManager);
		
		// Create and prepare the profanity command
		// This is only accessible for bot developers
		this.command = new ProfanityCommand(profanityManager, roleManager);
		
		// Add the components to the list
		componentAdapters.add(filter);
		componentAdapters.add(command);
	}
	
	/**
	 * @return Collection of component adapters
	 */
	public Collection<ComponentAdapter> getComponents() {
		return componentAdapters;
	}
	
	/**
	 * 
	 */
	public void dispose() {
		roleManager.dispose();
		profanityManager.dispose();
		componentAdapters.clear();
	}
}
