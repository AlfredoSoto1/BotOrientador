/**
 * 
 */
package services.bot.orientador.loginControl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import services.bot.dbaccess.DBLoginManager;
import services.bot.dbaccess.DBRoleManager;
import services.bot.entry.BotConfigs;
import services.bot.managers.startup.StartupI;

/**
 * @author Alfredo
 *
 */
public class LoginStartup implements StartupI {

	private Guild uniqueServer;
	private DBLoginManager loginManager;
	private DBRoleManager roleManager;
	
	public LoginStartup(DBLoginManager loginManager, DBRoleManager roleManager) {
		this.roleManager = roleManager;
		this.loginManager = loginManager;
	}
	
	public Guild getServer() {
		return uniqueServer;
	}
	
	@Override
	public void onStartup(ReadyEvent event) {
		// Get the server that matches the server ID 
		uniqueServer = event.getJDA().getGuildById(BotConfigs.SEVER_ID);
		
		if(uniqueServer == null)
			return;
		
		// extract the roles from the server
		roleManager.extractRolesFromDiscord(uniqueServer);
		
		// Prepare all the roles stored in database to be
		// later used in this log-in modal
		loginManager.prepareRolesFromDatabase();
	}

	@Override
	public void dispose() {
		
	}
}
