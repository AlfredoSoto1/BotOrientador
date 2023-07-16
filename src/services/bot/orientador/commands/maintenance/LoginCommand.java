/**
 * 
 */
package services.bot.orientador.commands.maintenance;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.dbaccess.DBLoginManager;
import services.bot.dbaccess.DBRoleManager;
import services.bot.entry.BotConfigs;
import services.bot.managers.command.CommandI;
import services.bot.orientador.controllers.login.LoginOnJoin;
import services.bot.orientador.controllers.login.LoginStartup;

/**
 * @author Alfredo
 *
 */
public class LoginCommand implements CommandI {

	// Defines the ID for the slash command that ONLY
	// Bot developers can use for testing the log-in modal
	private static final String LOGIN_ME_ID = "log-me-id";
	private static final String LOG_ME_OFF_ID = "log-me-off-id";
	private static final String LOG_OFF_ALL_ID = "log-off-all-id";
	
	// Defines the option for the slash command
	// that will be used for entering the other commands
	private static final String COMMAND_OPTION = "developer-command";
	
	private List<OptionData> options;
	
	private DBRoleManager roleManager;
	private DBLoginManager loginManager;
	
	private LoginStartup botStartup;
	private LoginOnJoin joiningSession;
	
	/**
	 * Create the log-in related commands to access
	 * log-in prompt easier and have extra developer functionalities
	 * 
	 * @param loginManager
	 * @param roleManager
	 * @param botStartup
	 * @param joiningSession
	 */
	public LoginCommand(DBLoginManager loginManager, DBRoleManager roleManager, LoginStartup botStartup, LoginOnJoin joiningSession) {
		this.options = new ArrayList<>();
		
		this.roleManager = roleManager;
		this.loginManager = loginManager;
		
		this.botStartup = botStartup;
		this.joiningSession = joiningSession;
		
		options.add(new OptionData(OptionType.STRING, COMMAND_OPTION, "Choose a command", true)
			.addChoice("log-me", LOGIN_ME_ID)
			.addChoice("log-me-off", LOG_ME_OFF_ID)
			.addChoice("log-off-all", LOG_OFF_ALL_ID)
		);
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
	public void dispose() {
		options.clear();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		if(!roleManager.roleValdiation(BotConfigs.DEVELOPER_ROLE_NAME, event.getMember().getRoles())) {
			event.reply("You don't have permissions to use this command")
			.setEphemeral(false)
			.queue();
			return;
		}
		
		OptionMapping programOption = event.getOption(COMMAND_OPTION);
		
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
		event.getUser().openPrivateChannel().queue(joiningSession::receiveMemberPrivately);
		
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
			
			boolean success = roleManager.removeRoles(botStartup.getServer(), event.getUser().getName());
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
}
