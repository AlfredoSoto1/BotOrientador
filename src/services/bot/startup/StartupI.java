/**
 * 
 */
package services.bot.startup;

import net.dv8tion.jda.api.events.session.ReadyEvent;

/**
 * @author Alfredo
 *
 */
public interface StartupI {

	/**
	 * 
	 * @param event
	 */
	public void onStartup(ReadyEvent event);
	
	/**
	 * Disposes everything when bot 'dies'
	 */
	public void onDisposing();
}
