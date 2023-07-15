/**
 * 
 */
package services.bot.orientador.loginControl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import application.records.MemberRecord;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.modals.Modal;
import services.bot.dbaccess.DBLoginManager;
import services.bot.dbaccess.DBRoleManager;
import services.bot.managers.button.ButtonI;
import services.bot.managers.modal.ModalI;
import services.bot.messages.MessengerI;
import services.bot.orientador.messages.SpecialTokens;
import services.bot.orientador.messages.WelcomeMessages;

/**
 * @author Alfredo
 *
 */
public class LoginOnJoin implements MessengerI, ButtonI, ModalI {

	private Map<String, LoginButton> loginButtons;
	private Map<String, LoginModalPrompt> loginPrompts;

	private DBRoleManager roleManager;
	private DBLoginManager loginManager;
	
	private LoginStartup botStartup;
	
	/**
	 * Create the log-in prompt that will
	 * display for any user that joins the discord server
	 * 
	 * @param loginManager
	 * @param roleManager
	 * @param botStartup
	 */
	public LoginOnJoin(DBLoginManager loginManager, DBRoleManager roleManager, LoginStartup botStartup) {
		this.loginButtons = new HashMap<>();
		this.loginPrompts = new HashMap<>();
		
		this.roleManager = roleManager;
		this.loginManager = loginManager;
		
		this.botStartup = botStartup;
	}
	
	@Override
	public void dispose() {
		loginButtons.clear();
		loginPrompts.clear();
	}
	
	@Override
	public Set<String> getModalIDs() {
		return loginPrompts.keySet();
	}
	
	@Override
	public Set<String> getButtonIDs() {
		return loginButtons.keySet();
	}
	
	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		// Open a private channel with the user that joined
		// recently, and prepare a welcoming message
		event.getUser().openPrivateChannel().queue(this::receiveMemberPrivately);
	}

	@Override
	@Deprecated
	public void messageReceived(MessageReceivedEvent event) {
		
	}
	
	public void receiveMemberPrivately(PrivateChannel channel) {
		
		// Prepare the welcome message string
		String welcomeMessage = WelcomeMessages.WELCOME_MESSAGE;
		welcomeMessage = welcomeMessage.replace(SpecialTokens.MEMBER_USER_NAME, channel.getUser().getAsMention());
		
		LoginButton loginButton = new LoginButton("Login to server");
		
		// Store the generated button temporarily
		loginButtons.put(loginButton.getButtonID(), loginButton);
		
		// Send the private message with the welcoming message
		// and the log-in button attached to it
		channel.sendMessage(welcomeMessage).queue();
		channel.sendMessage(loginButton.build()).queue();
	}
	
	@Override
	public void onButtonEvent(String buttonID, ButtonInteractionEvent event) {
		
		LoginModalPrompt loginPrompt = new LoginModalPrompt("Log-in as UPRM Student", "Email", "Fun facts about you :)");
		
		// Store the generated modal temporarily
		this.loginPrompts.put(loginPrompt.getModalID(), loginPrompt);
		
		// Set up some descriptions for the fields to be displayed
		loginPrompt.setUsernameDescription("Provide your institutional Email");
		loginPrompt.setFunFactsDescription("Provide some fun facts so that other members can relate with you!");
		
		// Build the modal from the login-prompt
		Modal modal = loginPrompt.build();
		
		// reply to the user with the modal
		event.replyModal(modal).queue();
	}
	
	@Override
	public void modalResults(ModalInteractionEvent event) {
		
		// Find on database the student
		Optional<MemberRecord> member = loginManager.getStudent(loginPrompts.get(event.getModalId()).getUsernameValue(event));
		
		// Tell user to retry if email was not found
		if(member.isEmpty()) {
			String presentationMessage = WelcomeMessages.ERROR_MESSAGE_NOT_FOUND;
			// Send the official welcoming message
			event.reply(presentationMessage).queue();
			// Exit failure
			return;
		}
		
		// Test if user has already logged in
		if(member.get().isLogged()) {
			// Send message of already logged into server
			event.reply(WelcomeMessages.ALREADY_LOGGED_IN).queue();
			return; // Exit failure
		}
		
		if(member.get().getRoles().isPrepa()) {
			String presentationMessage = WelcomeMessages.PRESENTATION_MESSAGE_PREPA;
			presentationMessage = presentationMessage.replace(SpecialTokens.MEMBER_USER_NAME, event.getUser().getAsMention());
			presentationMessage = presentationMessage.replace(SpecialTokens.MEMBER_FULL_NAME, member.get().getFullName());
			
			// Send the official welcoming message to prepas
			event.reply(presentationMessage).queue();
		} else {
			String presentationMessage = WelcomeMessages.PRESENTATION_MESSAGE_EOS;
			presentationMessage = presentationMessage.replace(SpecialTokens.MEMBER_USER_NAME, event.getUser().getAsMention());
			
			// Send the official welcoming message to EOs
			event.reply(presentationMessage).queue();
		}
		
		// Set a fun fact from the login-prompt
		// to later save into database
		member.get().setBriefInfo(loginPrompts.get(event.getModalId()).getFunFactsValue(event));
		
		// Login user and save any additional data
		// that the member may have
		loginManager.logInMember(member.get(), event.getUser().getName());
		
		// Set the corresponding roles to the logged member
		boolean success = roleManager.applyRoles(botStartup.getServer(), member.get());
		
		if(!success)
			event.getHook()
			.sendMessage("Cannot change nickname since you have a higher role than the bot.")
			.setEphemeral(true).queue();
		
		// After the bot has assigned the roles and gave the small tutorial
		// we can remove from the chat history the button and the modal
		loginPrompts.remove(event.getModalId());
	}
}
