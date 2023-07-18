/**
 * 
 */
package services.bot.managers;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import services.bot.adapters.ProgrammableAdapter;

/**
 * @author Alfredo
 *
 */
public class ButtonManager implements ProgrammableAdapter<ButtonI> {

	private List<ButtonI> buttons;
	
	public ButtonManager() {
		this.buttons = new ArrayList<>();
		
	}
	
	@Override
	public void init(ReadyEvent event) {

	}
	
	@Override
	public void dispose() {
		for(ButtonI button : buttons)
			button.dispose();
		buttons.clear();
	}
	
	@Override
	public void add(ButtonI button) { 
		buttons.add(button);
	}
	
	@Override
	public void onInteraction(Event genericEvent) {
		
		ButtonInteractionEvent event = (ButtonInteractionEvent) genericEvent;
		
		for(ButtonI button : buttons)
			if(button.getButtonIDs().contains(event.getButton().getId()))
				button.onButtonEvent(event.getButton().getId(), event);
	}
}
