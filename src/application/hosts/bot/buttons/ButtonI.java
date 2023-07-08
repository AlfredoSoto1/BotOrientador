/**
 * 
 */
package application.hosts.bot.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

/**
 * @author Alfredo
 *
 */
public interface ButtonI {
	
	/**
	 * 
	 * @return
	 */
	public String[] getButtonIDs();
	
	/**
	 * 
	 * @param buttonID
	 * @param event
	 */
	public void onButtonEvent(String buttonID, ButtonInteractionEvent event);
}
