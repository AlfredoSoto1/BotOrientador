/**
 * 
 */
package application.hosts.bot.buttons;

import java.util.ArrayList;
import java.util.List;

import application.hosts.bot.adapters.ProgrammableAdapter;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
		buttons.clear();
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		for(ButtonI button : buttons)
			for(String buttonID : button.getButtonIDs()) {
				if(buttonID.equals(event.getButton().getId()))
					button.onButtonEvent(buttonID, event);
			}
	}

}
