/**
 * 
 */
package services.bot.interactions;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

/**
 * @author Alfredo
 *
 */
public interface ModalActionEvent {
	
	/**
	 *  This gets called when the modal has been submitted 
	 *  
	 * @param event
	 */
	public void modalResults(ModalInteractionEvent event);
}
