/**
 * 
 */
package services.bot.orientador.loginControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import services.bot.adapters.ComponentAdapter;
import services.bot.dbaccess.DBLoginManager;
import services.bot.dbaccess.DBRoleManager;
import services.bot.orientador.commands.maintenance.LoginCommand;

/**
 * @author Alfredo
 *
 */
public class WelcomingManager {
	
	private DBRoleManager roleManager;
	private DBLoginManager loginManager;
	
	private LoginCommand command;
	private LoginStartup botStartUp;
	private LoginOnJoin joiningSession;
	
	private List<ComponentAdapter> componentAdapters;

	/**
	 * 
	 */
	public WelcomingManager() {
		this.componentAdapters = new ArrayList<>();
		
		// Initialize the database managers
		// These access the databases and provides special
		// methods for the login startup, on-join and command
		this.roleManager = new DBRoleManager();
		this.loginManager = new DBLoginManager();
		
		// Prepare the login startup 
		this.botStartUp = new LoginStartup(loginManager, roleManager);
		
		// Prepare the on-join 'listener' when a member joins
		// the server and later is prompt to log-in
		this.joiningSession = new LoginOnJoin(loginManager, roleManager, botStartUp);
		
		// Prepare the log-in command
		this.command = new LoginCommand(loginManager, roleManager, botStartUp, joiningSession);
		
		// Add to list the component adapters to be
		// implemented later by their corresponding manager
		componentAdapters.add(botStartUp);
		componentAdapters.add(joiningSession);
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
		loginManager.dispose();
		componentAdapters.clear();
	}
}
