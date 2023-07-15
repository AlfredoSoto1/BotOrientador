/**
 * 
 */
package services.bot.managers.button;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import services.bot.adapters.ProgrammableAdapter;

/**
 * @author Alfredo
 *
 */
public class ButtonManager extends ListenerAdapter implements ProgrammableAdapter {

	private List<ButtonI> buttons;
	
	public ButtonManager() {
		
	}
	
	public void add(ButtonI button) { 
		buttons.add(button);
	}
	
	@Override
	public void init() {
		this.buttons = new ArrayList<>();
	}

	@Override
	public void dispose() {
		for(ButtonI button : buttons)
			button.dispose();
		buttons.clear();
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		for(ButtonI button : buttons)
			if(button.getButtonIDs().contains(event.getButton().getId()))
				button.onButtonEvent(event.getButton().getId(), event);
	}

}
