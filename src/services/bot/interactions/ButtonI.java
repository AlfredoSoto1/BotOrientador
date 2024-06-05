/**
 * 
 */
package services.bot.interactions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

/**
 * @author Alfredo
 *
 */
@Deprecated
public interface ButtonI extends InteractableEvent {
	
	/**
	 * 
	 * @param buttonID
	 * @param event
	 */
	public void onButtonEvent(String buttonID, ButtonInteractionEvent event);
}
