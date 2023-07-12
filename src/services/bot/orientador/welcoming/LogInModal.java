/**
 * 
 */
package services.bot.orientador.welcoming;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.records.MemberRecord;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
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
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import services.bot.buttons.ButtonI;
import services.bot.commands.CommandI;
import services.bot.messages.MessengerI;
import services.bot.modals.ModalI;
import services.bot.orientador.messages.BotServer;
import services.bot.orientador.messages.SpecialTokens;
import services.bot.orientador.messages.WelcomeMessages;
import services.bot.startup.StartupI;

/**
 * @author Alfredo
 *
 */
public class LogInModal implements ModalI, CommandI, ButtonI, MessengerI, StartupI {

	/*
	 * 
	 * Definitions for the id's for the discord components
	 * 
	 */
	
	// Defines the ID for the log in button
	// when user joins server for the first time or via slash commands
	private static final String LOGIN_BUTTON_ID = "id-button-login";

	// Defines the ID for the modal used to enter the
	// personal info to log-in for the first time
	private static final String MODAL_ID = "id-login";
	
	// Defines the ID for the fields inside the modal
	private static final String ENTER_EMAIL_ID = "id-email";
	private static final String ENTER_PARAGRAPH_ID = "id-paragraph";

	// Defines the ID for the slash command that ONLY
	// Bot developers can use for testing the log-in modal
	private static final String LOGIN_ME_ID = "log-me-id";
	private static final String LOG_ME_OFF_ID = "log-me-off-id";
	private static final String LOG_OFF_ALL_ID = "log-off-all-id";
	
	// Defines the option for the slash command
	// that will be used for entering the other commands
	private static final String COMMAND_OPTION = "developer-command";
	
	private List<OptionData> options;
	
	// Validation for log-in users via database connections
	private RoleValidation roleValidation;
	private LoginValidation loginValidation;
	
	public LogInModal() {
		this.options = new ArrayList<>();
		this.roleValidation = new RoleValidation();
		this.loginValidation = new LoginValidation();
		
		options.add(new OptionData(OptionType.STRING, COMMAND_OPTION, "Choose a command", true)
			.addChoice("log-me", LOGIN_ME_ID)
			.addChoice("log-me-off", LOG_ME_OFF_ID)
			.addChoice("log-off-all", LOG_OFF_ALL_ID)
		);
	}
	
	@Override
	public String getModalID() {
		return MODAL_ID;
	}
	
	@Override
	public String[] getButtonIDs() {
		return new String[] {LOGIN_BUTTON_ID};
	}
	
	@Override
	public String getCommandName() {
		return "log-in";
	}

	@Override
	public String getDescription() {
		return "Log-in to our server!";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void onStartup(ReadyEvent event) {
		
		// Get the server that matches the server name 
		Guild uniqueServer = event.getJDA().getGuildsByName(BotServer.SERVER_NAME, true).get(0);
		
		if(uniqueServer == null)
			return;
		
		// extract the roles from the server
		roleValidation.extractRolesFromDiscord(uniqueServer);
		
		// Prepare all the roles stored in database to be
		// later used in this log-in modal
		loginValidation.prepareRolesFromDatabase();
	}
	
	@Override
	public void onDisposing() {
		options.clear();
		roleValidation.dispose();
		loginValidation.dispose();
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		// Iterate over all roles from the member who called this
		// command. If the member is not a developer, cancel the 
		// command execution operation
		if(event.getMember().getRoles().isEmpty()) {
			event.reply("You don't have permissions to use this command")
			.setEphemeral(true)
			.queue();
			return;
		}
		
		for (Role role : event.getMember().getRoles()) {
			// Validate if the member has the developer role
			if (role.getName().equalsIgnoreCase(BotServer.DEVELOPER)) break;
			else {
				// Reply to the member who typed the command. Everyone
				// can see that he used that command. :)
				event.reply("You don't have permissions to use this command")
				.setEphemeral(false)
				.queue();
				// Exit failure
				return;
			}
		}
		
		OptionMapping programOption = event.getOption(COMMAND_OPTION);
		
		switch(programOption.getAsString()) {
		case LOGIN_ME_ID:
			// Open a private channel with user that executed the command
			// and prepare a welcoming message to introduce him to the server and
			// provide him/her a button to open the log-in modal
			event.getUser().openPrivateChannel().queue(this::prepareWelcomeMessage);
			
			// Reply to the user from where he/she called the slash command.
			// This message cannot be viewed by others in the same text-channels
			event.reply("Check your private DMs to continue to log-in to the server")
				.setEphemeral(true)
				.queue();
			break;
			
		case LOG_ME_OFF_ID:
			// Log off the user who used this command
			boolean logOffSuccess = loginValidation.logOffMember(event.getUser().getName());
			
			// Reply to the user from where he/she called the slash command.
			// This message cannot be viewed by others in the same text-channels
			if(logOffSuccess) {
				event.reply(event.getUser().getAsMention() + "You've been logged-off successfully")
				.setEphemeral(true)
				.queue();
				
				Guild server = event.getJDA()
						.getGuildsByName(BotServer.SERVER_NAME, true)
						.get(0);
				
				boolean success = roleValidation.removeRoles(server, event.getUser().getName());
				if(!success)
					event.getHook().sendMessage("Cannot change nickname since you have a higher role than the bot.")
					.setEphemeral(true)
					.queue();
			} else {
				event.reply(event.getUser().getAsMention() + "Cannot find your Discord user in database to log-off.")
				.setEphemeral(true)
				.queue();
			}
			break;
			
		case LOG_OFF_ALL_ID:
			// update all records in database to log-in == false
			loginValidation.logOffAllMemberRecords();
			
			// Reply user with a confirmation message
			// This reply will be visible only to the
			// developer who used this command
			event.reply(event.getUser().getAsMention() + "All members have been logged off successfully")
				.setEphemeral(true)
				.queue();
			break;
		}
	}

	@Override
	public void modalResults(ModalInteractionEvent event) {
		
		// Find on database the student
		Optional<MemberRecord> member = loginValidation.getStudent(event.getValue(ENTER_EMAIL_ID).getAsString());
		
		if(member.isEmpty()) {
			String presentationMessage = WelcomeMessages.ERROR_MESSAGE_NOT_FOUND;
			// Send the official welcoming message
			event.reply(presentationMessage).queue();
			// Exit failure
			return;
		}
		
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
		
		loginValidation.logInMember(member.get(), event.getUser().getName());
		
		// Set the corresponding roles to the logged member
		boolean success = roleValidation.applyRoles(
			event.getJDA()
				.getGuildsByName(BotServer.SERVER_NAME, true)
				.get(0),
			member.get()
		);
		
		if(!success)
			event.getHook().sendMessage("Cannot change nickname since you have a higher role than the bot.")
			.setEphemeral(true)
			.queue();
	}
	
	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		// Open a private channel with the user that joined
		// recently, and prepare a welcoming message
		event.getUser().openPrivateChannel().queue(this::prepareWelcomeMessage);
	}
	
	private void prepareWelcomeMessage(PrivateChannel channel) {
		
		// Prepare the welcome message string
		String welcomeMessage = WelcomeMessages.WELCOME_MESSAGE;
		welcomeMessage = welcomeMessage.replace(SpecialTokens.MEMBER_USER_NAME, channel.getUser().getAsMention());
		
		// Create a button that bot will use to reply to the user
		// This button when clicked it will open a new modal with 
		// all the fields required to log-in the user
		Button loginButton = Button.success(LOGIN_BUTTON_ID, "Login to server");
		
		// Add button to a new message
		MessageCreateData message = new MessageCreateBuilder()
			.addComponents(ActionRow.of(loginButton))
			.build();
		
		// Send the private message with the welcoming message
		// and the log-in button attached to it
		channel.sendMessage(welcomeMessage).queue();
		channel.sendMessage(message).queue();
	}
	
	@Override
	public void onButtonEvent(String buttonID, ButtonInteractionEvent event) {
		
		// Create an Email field to be displayed inside the modal
		TextInput email = TextInput.create(ENTER_EMAIL_ID, "Email", TextInputStyle.SHORT)
				.setMinLength(1)
				.setMaxLength(64)
				.setRequired(true)
				.setPlaceholder("Provide your institutional Email")
				.build();
		
		// Create a FunFacts field to be displayed inside the modal
		TextInput funfacts = TextInput.create(ENTER_PARAGRAPH_ID, "Fun facts about you :)", TextInputStyle.PARAGRAPH)
				.setMinLength(1)
				.setMaxLength(255)
				.setRequired(false)
				.setPlaceholder("Provide some fun facts so that other members can relate with you!")
				.build();
		
		// Create a simple modal containing two text fields
		// in which the user will enter his email to log-in and
		// a fun fact about them
		
		Modal modal = Modal.create(MODAL_ID, "Log-in as UPRM Student")
				.addComponents(ActionRow.of(email), ActionRow.of(funfacts))
				.build();
		
		// reply to the user with the modal
		event.replyModal(modal).queue();
	}
	
	@Override
	@Deprecated
	public void messageReceived(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
	}
}
