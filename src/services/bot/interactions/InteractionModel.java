/**
 * 
 */
package services.bot.interactions;

import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

/**
 * 
 */
public abstract class InteractionModel {
	
	private List<OptionData> options;
	
	protected interface ButtonActionEvent {
		void onAction(ButtonInteractionEvent event);
	}
	
	public InteractionModel() {
		
	}
	
	public abstract void init(ReadyEvent event);
	public abstract void dispose();
	
	public void cleanUp() {
		dispose();
		options.clear();
	}
	
	protected OptionData addCommandOption(OptionType type, String label, String description, boolean isRequired) {
		OptionData opData = new OptionData(type, label, description, isRequired);
		// add to a set
		
		return opData;
	}
	
	protected Button registerButton(ButtonActionEvent action, Button nativeButton) {
		
		return nativeButton;
	}
}
