/**
 * 
 */
package services.bot.interactions;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

/**
 * @author Alfredo
 *
 */
public interface ButtonActionEvent {
	/**
	 * This gets called when the button gets clicked
	 * 
	 * @param event
	 */
	public void onAction(ButtonInteractionEvent event);
}
