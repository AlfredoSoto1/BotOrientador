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
		modals.clear();
	}
	
	@Override
	public void onModalInteraction(ModalInteractionEvent event) {
		// Do linear search to find which modal is currently being called
		for(ModalI modal : modals)
			if(modal.getModalID().equals(event.getModalId())) {
				modal.modalResults(event);
				return;
			}
	}
}
