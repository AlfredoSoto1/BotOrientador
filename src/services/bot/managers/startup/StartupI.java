/**
 * 
 */
package services.bot.managers.startup;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import services.bot.adapters.ComponentAdapter;

/**
 * @author Alfredo
 *
 */
public interface StartupI extends ComponentAdapter {

	/**
	 * 
	 * @param event
	 */
	public void onStartup(ReadyEvent event);
	
	/**
	 * Disposes everything when bot 'dies'
	 */
	public void dispose();
}
