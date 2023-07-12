/**
 * 
 */
package services.bot.orientador.welcoming;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

/**
 * @author Alfredo
 *
 */
public class LoginPrompt {
	
	private String userID;
	private String funfID;
	private String modalID;
	
	private String username;
	private String funFacts;
	private String promptTitle;

	private String usernameDescription;
	private String funFactsDescription;
	
	/**
	 * Creates a login prompt that the user can 
	 * type their user name and write a fun fact about
	 * them.
	 * 
	 * @param promptTitle
	 * @param username
	 * @param funFacts
	 */
	public LoginPrompt(String promptTitle, String username, String funFacts) {
		this.promptTitle = promptTitle;
		this.username = username;
		this.funFacts = funFacts;
		
		// Create unique ID for the user and fun fact
		// fields to be displayed on modal
		this.userID = username + username.hashCode();
		this.funfID = funFacts + funFacts.hashCode();
		
		this.modalID = "Login-" + username.hashCode() + "-" + funFacts.hashCode();
	}

	public String getModalID() {
		return modalID;
	}
	
	public void setUsernameDescription(String usernameDescription) {
		this.usernameDescription = usernameDescription;
	}

	public void setFunFactsDescription(String funFactsDescription) {
		this.funFactsDescription = funFactsDescription;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFunFacts() {
		return funFacts;
	}

	public void setFunFacts(String funFacts) {
		this.funFacts = funFacts;
	}
	
	public String getUsernameValue(ModalInteractionEvent event) {
		return event.getValue(userID).getAsString();
	}
	
	/**
	 * Builds the Modal to be displayed on discord
	 */
	public Modal build() {
		// Create an Email field to be displayed inside the modal
		TextInput email = TextInput.create(userID, username, TextInputStyle.SHORT)
				.setMinLength(1)
				.setMaxLength(64)
				.setRequired(true)
				.setPlaceholder(usernameDescription)
				.build();
		
		// Create a FunFacts field to be displayed inside the modal
		TextInput funfacts = TextInput.create(funfID, funFacts, TextInputStyle.PARAGRAPH)
				.setMinLength(1)
				.setMaxLength(255)
				.setRequired(false)
				.setPlaceholder(funFactsDescription)
				.build();
		
		// Create a simple modal containing two text fields
		// in which the user will enter his email to log-in and
		// a fun fact about them
		return Modal.create(modalID, promptTitle)
				.addComponents(ActionRow.of(email), ActionRow.of(funfacts))
				.build();
	}
}
