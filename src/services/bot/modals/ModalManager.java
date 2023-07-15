/**
 * 
 */
package services.bot.modals;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import services.bot.adapters.ProgrammableAdapter;

/**
 * @author Alfredo
 *
 */
public class ModalManager extends ListenerAdapter implements ProgrammableAdapter {
	
	private List<ModalI> modals;
	
	/**
	 * 
	 */
	public ModalManager() {
		
	}
	
	/**
	 * Adds a command to the manager
	 * 
	 * @param command
	 */
	public void add(ModalI modal) {
		modals.add(modal);
	}

	@Override
	public void init() {
		this.modals = new ArrayList<>();
	}

	@Override
	public void dispose() {
		for(ModalI modal : modals)
			modal.dispose();
		modals.clear();
	}
	
	@Override
	public void onModalInteraction(ModalInteractionEvent event) {
		// Do linear search to find which modal is currently being called
		for(ModalI modal : modals)
			// Check if the modal that currently is having an action to
			// be executed to run the modal results
			if(modal.getModalIDs().contains(event.getModalId())) {
				modal.modalResults(event);
				// Once the modal results have been produced, we can
				// exit the loop since we don't need to keep looking
				// for more events. Since the JDA execute this onModalInteraction() method
				// in queue after each command triggered the modal
				return;
			}
	}
}
