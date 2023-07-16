/**
 * 
 */
package services.bot.orientador.controllers.login;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * @author Alfredo
 *
 */
public class LoginButton {
	
	private String buttonID;
	private String buttonName;
	
	/**
	 * Creates a login button to later
	 * call a new Modal prompt with the login
	 * information to be entered.
	 * 
	 * @param buttonName
	 */
	public LoginButton(String buttonName) {
		this.buttonName = buttonName;
		
		this.buttonID = buttonName + buttonName.hashCode();
	}
	
	public String getName() {
		return buttonName;
	}
	
	public String getButtonID() {
		return buttonID;
	}
	
	public MessageCreateData build() {
		// Create a new button instance that when is triggered,
		// it will launch a login prompt
		Button loginButton = Button.success(buttonID, buttonName);
		
		// Add button to a new message
		return new MessageCreateBuilder()
			.addComponents(ActionRow.of(loginButton))
			.build();
	}
}
