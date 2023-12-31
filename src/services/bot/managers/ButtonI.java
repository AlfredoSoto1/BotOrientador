/**
 * 
 */
package services.bot.managers;

import java.util.Set;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

/**
 * @author Alfredo
 *
 */
public interface ButtonI {
	
	/**
	 * Return a set containing all the buttonIDs
	 * 
	 * @return
	 */
	public Set<String> getButtonIDs();
	
	/**
	 * Disposes button content
	 */
	public void dispose();
	
	/**
	 * 
	 * @param buttonID
	 * @param event
	 */
	public void onButtonEvent(String buttonID, ButtonInteractionEvent event);
}
