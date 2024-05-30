/**
 * 
 */
package services.bot.interactions;

import net.dv8tion.jda.api.events.session.ReadyEvent;

/**
 * @author Alfredo
 *
 */
public interface InteractableEvent {
	
	/**
	 * Initiates the component when the
	 * bot application starts.
	 * 
	 * @param event
	 */
	public void init(ReadyEvent event);
	
	/**
	 * Disposes all content from memory
	 * when the bot application shuts down.
	 */
	public void dispose();
}
