/**
 * 
 */
package java.service.discord.interaction;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

/**
 * @author Alfredo
 */
public interface SelectMenuActionEvent {
	
	/**
	 * This gets called when there has been a selected
	 * element from menu.
	 * 
	 * @param event
	 */
	public void onMenuSelection(StringSelectInteractionEvent event);
}
