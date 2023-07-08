/**
 * 
 */
package application.hosts.bot.modals;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

/**
 * @author Alfredo
 *
 */
public interface ModalI {
	
	/**
	 * Returns the modal ID
	 * 
	 * @return String - ID
	 */
	public String getModalID();
	
	/**
	 * This gets called when the modal
	 * has been submitted 
	 */
	public void modalResults(ModalInteractionEvent event);
}
