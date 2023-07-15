/**
 * 
 */
package services.bot.modals;

import java.util.Set;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import services.bot.adapters.ComponentAdapter;

/**
 * @author Alfredo
 *
 */
public interface ModalI extends ComponentAdapter {
	
	/**
	 * Returns the modal ID
	 * 
	 * @return String - ID
	 */
	public Set<String> getModalIDs();
	
	/**
	 * Disposes modal content
	 */
	public void dispose();
	
	/**
	 * This gets called when the modal
	 * has been submitted 
	 */
	public void modalResults(ModalInteractionEvent event);
}
