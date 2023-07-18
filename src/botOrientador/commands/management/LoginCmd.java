/**
 * 
 */
package botOrientador.commands.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import application.records.MemberRecord;
import botOrientador.entry.BotConfigs;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.modals.Modal;
import services.bot.controllers.LoginButton;
import services.bot.controllers.LoginModalPrompt;
import services.bot.dbaccess.DBLoginManager;
import services.bot.dbaccess.DBRoleManager;
import services.bot.managers.ButtonI;
import services.bot.managers.CommandI;
import services.bot.managers.MessengerI;
import services.bot.managers.ModalI;
import services.bot.orientador.messages.SpecialTokens;
import services.bot.orientador.messages.WelcomeMessages;

/**
 * @author Alfredo
 *
 */
public class LoginCmd implements CommandI, ButtonI, ModalI, MessengerI {

	// Defines the option for the slash command
	// that will be used for entering the other commands
	private static final String COMMAND_LABEL = "developer-command";

	// Defines the ID for the slash command that ONLY
	// Bot developers can use for testing the log-in modal
	private static final String LOGIN_ME_ID = "log-me-id";
	private static final String LOG_ME_OFF_ID = "log-me-off-id";
	private static final String LOG_OFF_ALL_ID = "log-off-all-id";
	
	private boolean isGlobal;
	private List<OptionData> options;
	private Map<String, LoginButton> loginButtons;
	private Map<String, LoginModalPrompt> loginPrompts;
	
	private Guild uniqueServer;
	
	private DBRoleManager roleManager;
	private DBLoginManager loginManager;
	
	public LoginCmd() {
		this.options = new ArrayList<>();
		this.loginButtons = new HashMap<>();
		this.loginPrompts = new HashMap<>();

		this.roleManager = new DBRoleManager();
		this.loginManager = new DBLoginManager();
		
		this.options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "Choose a command", true)
			.addChoice("log-me", LOGIN_ME_ID)
			.addChoice("log-me-off", LOG_ME_OFF_ID)
			.addChoice("log-off-all", LOG_OFF_ALL_ID)
		);
	}
	
	@Override
	public void init(ReadyEvent event) {
		// Get the server that matches the server ID 
		uniqueServer = event.getJDA().getGuildById(BotConfigs.SERVER_ID);
		// extract the roles from the server
		roleManager.extractRolesFromDiscord(uniqueServer);
		// Prepare all the roles stored in database to be
		// later used in this log-in modal
		loginManager.prepareRolesFromDatabase();
	}

	@Override
	public boolean isGlobal() {
		return isGlobal;
	}

	@Override
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	@Override
	public String getCommandName() {
		return "server-management-log-in";
	}

	@Override
	public String getDescription() {
		return "Log-in to TM server!";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
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
	public void dispose() {
		options.clear();
		loginButtons.clear();
		loginPrompts.clear();
		
		roleManager.dispose();
		loginManager.dispose();
	}

	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		// Open a private channel with the user that joined
		// recently, and prepare a welcoming message
		event.getUser().openPrivateChannel().queue(this::receiveMemberPrivately);
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
		boolean success = roleManager.applyRoles(uniqueServer, member.get());
		
		if(!success)
			event.getHook()
			.sendMessage("Cannot change nickname since you have a higher role than the bot.")
			.setEphemeral(true).queue();
		
		// After the bot has assigned the roles and gave the small tutorial
		// we can remove from the chat history the button and the modal
		loginPrompts.remove(event.getModalId());
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		if(!roleManager.roleValdiation(BotConfigs.DEVELOPER_ROLE_NAME, event.getMember().getRoles())) {
			event.reply("You don't have permissions to use this command")
			.setEphemeral(false)
			.queue();
			return;
		}
		
		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		switch(programOption.getAsString()) {
		case LOGIN_ME_ID: logInMe(event); break;
		case LOG_ME_OFF_ID: logOffMe(event); break;
		case LOG_OFF_ALL_ID: logOffAll(event); break;
		}
	}
	
	private void logInMe(SlashCommandInteractionEvent event) {
		// Open a private channel with user that executed the command
		// and prepare a welcoming message to introduce him to the server and
		// provide him/her a button to open the log-in modal
		event.getUser().openPrivateChannel().queue(this::receiveMemberPrivately);
		
		// Reply to the user from where he/she called the slash command.
		// This message cannot be viewed by others in the same text-channels
		event.reply("Check your private DMs to continue to log-in to the server")
			.setEphemeral(true)
			.queue();
	}
	
	private void logOffMe(SlashCommandInteractionEvent event) {
		// Log off the user who used this command
		boolean logOffSuccess = loginManager.logOffMember(event.getUser().getName());
		
		// Reply to the user from where he/she called the slash command.
		// This message cannot be viewed by others in the same text-channels
		if(logOffSuccess) {
			event.reply(event.getUser().getAsMention() + "You've been logged-off successfully")
			.setEphemeral(true)
			.queue();
			
			boolean success = roleManager.removeRoles(uniqueServer, event.getUser().getName());
			if(!success)
				event.getHook().sendMessage("Cannot change nickname since you have a higher role than the bot.")
				.setEphemeral(true)
				.queue();
		} else {
			event.reply(event.getUser().getAsMention() + "Cannot find your Discord user in database to log-off.")
			.setEphemeral(true)
			.queue();
		}
	}
	
	private void logInWho(SlashCommandInteractionEvent event) {
		
	}
	
	private void logOffWho(SlashCommandInteractionEvent event) {
		
	}

	private void logOffAll(SlashCommandInteractionEvent event) {
		// update all records in database to log-in == false
		loginManager.logOffAllMemberRecords();
		
		// Reply user with a confirmation message
		// This reply will be visible only to the
		// developer who used this command
		event.reply(event.getUser().getAsMention() + "All members have been logged off successfully")
			.setEphemeral(true)
			.queue();
	}

	@Override
	@Deprecated
	public void messageReceived(MessageReceivedEvent event) {
		
	}
}
