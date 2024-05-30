/**
 * 
 */
package services.bot.interactions;

import java.util.Set;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

/**
 * @author Alfredo
 *
 */
public interface ModalI extends InteractableEvent {
	
	/**
	 * Returns the modal ID
	 * 
	 * @return String - ID
	 */
	public Set<String> getModalIDs();
	
	/**
	 * This gets called when the modal
	 * has been submitted 
	 */
	public void modalResults(ModalInteractionEvent event);
}
