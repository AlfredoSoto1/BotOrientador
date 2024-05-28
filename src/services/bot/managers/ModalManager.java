/**
 * 
 */
package services.bot.managers;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import services.bot.core.ProgrammableAdapter;

/**
 * @author Alfredo
 *
 */
public class ModalManager implements ProgrammableAdapter<ModalI> {
	
	private List<ModalI> modals;
	
	public ModalManager() {
		this.modals = new ArrayList<>();
		
	}
	
	@Override
	public void init(ReadyEvent event) {
		for(ModalI modal : modals)
			if(modal instanceof BotEventHandler handler)
				handler.init(event);
	}

	@Override
	public void dispose() {
		for(ModalI modal : modals)
			if(modal instanceof BotEventHandler handler)
				handler.dispose();
		modals.clear();
	}

	@Override
	public void add(ModalI modal) {
		modals.add(modal);
	}
	
	@Override
	public void onInteraction(Event genericEvent) {
		
		ModalInteractionEvent event = (ModalInteractionEvent) genericEvent;
		
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
