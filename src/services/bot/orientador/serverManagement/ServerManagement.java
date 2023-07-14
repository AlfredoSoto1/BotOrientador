/**
 * 
 */
package services.bot.orientador.serverManagement;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.commands.CommandI;

/**
 * @author Alfredo
 *
 */
public class ServerManagement implements CommandI {

	// Defines the option for the slash command
	// that will be used for entering the other commands
	private static final String SINGLE_OPTION_CMD = "single-command";
	private static final String DOUBLE_OPTION_CMD = "double-command";
	
	private static final String LOGIN_ME_ID = "log-me-id";
	private static final String LOG_ME_OFF_ID = "log-me-off-id";
	private static final String LOG_IN_WHO_ID = "log-in-who-id";
	private static final String LOG_OFF_WHO_ID = "log-off-who-id";
	private static final String LOG_OFF_ALL_ID = "log-off-all-id";

	private static final String PROFANITY_ADD_ID = "profanity-add-id";
	private static final String LOG_ATTENDANCE_ID = "log-attendance-id";

	private static final String CONNECT_DB_ID = "connect-db-id";
	private static final String DISCONNECT_DB_ID = "disconnect-db-id";

	private static final String SHUTDOWN_ID = "shutdown-id";
	
	private List<OptionData> options;
	
	public ServerManagement() {
		this.options = new ArrayList<>();
		
		options.add(new OptionData(OptionType.STRING, SINGLE_OPTION_CMD, "Choose a command", true)
			.addChoice("log-me", LOGIN_ME_ID)
			.addChoice("log-me-off", LOG_ME_OFF_ID)
			.addChoice("log-in-who", LOG_IN_WHO_ID)
			.addChoice("log-off-who", LOG_OFF_WHO_ID)
			.addChoice("log-off-all", LOG_OFF_ALL_ID)
			
			.addChoice("profanity-add", PROFANITY_ADD_ID)
			.addChoice("log-attendance", LOG_ATTENDANCE_ID)
			
			.addChoice("connect-db", CONNECT_DB_ID)
			.addChoice("disconenct-db", DISCONNECT_DB_ID)
			
			.addChoice("shutdown", SHUTDOWN_ID)
		);
		
		options.add(new OptionData(OptionType.STRING, DOUBLE_OPTION_CMD, "Choose a command", true)
			.addChoice("log-in-who", LOG_IN_WHO_ID)
			.addChoice("log-off-who", LOG_OFF_WHO_ID)

			.addChoice("profanity-add", PROFANITY_ADD_ID)
			
			.addChoice("connect-db", CONNECT_DB_ID)
		);
	}
	
	@Override
	public String getCommandName() {
		return "server_management";
	}

	@Override
	public String getDescription() {
		return "manages the server, only works for Bot Developers";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		OptionMapping programOption = event.getOption(SINGLE_OPTION_CMD);
				
		switch(programOption.getAsString()) {
		case LOGIN_ME_ID:
			break;
		case LOG_ME_OFF_ID:
			break;
		case LOG_IN_WHO_ID:
			break;
		case LOG_OFF_WHO_ID:
			break;
		case LOG_OFF_ALL_ID:
			break;

		case PROFANITY_ADD_ID:
			break;
		case LOG_ATTENDANCE_ID:
			break;

		case CONNECT_DB_ID:
			break;
		case DISCONNECT_DB_ID:
			break;

		case SHUTDOWN_ID:
			break;
			
		}
		
		event.reply("command").setEphemeral(true).queue();
	}
}
