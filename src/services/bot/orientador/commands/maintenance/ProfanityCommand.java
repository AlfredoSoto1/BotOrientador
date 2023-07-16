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
import services.bot.dbaccess.DBProfanityManager;
import services.bot.dbaccess.DBRoleManager;
import services.bot.entry.BotConfigs;
import services.bot.managers.command.CommandI;

/**
 * @author Alfredo
 *
 */
public class ProfanityCommand implements CommandI {

	// Defines the option for the slash command
	// that will be used for entering the other commands
	private static final String COMMAND_OPTION = "type";
	private static final String COMMAND_WORD_STATEMENT = "word";
	
	private static final String ADD_PROFANITY_ID = "add-prof-id";
	
	private DBRoleManager roleManager;
	private DBProfanityManager profanityManager;
	
	private List<OptionData> options;
	
	/**
	 * Creates and initializes the profanity command.
	 * At the moment this can add new words to the database
	 * to later be extracted and evaluated for every message
	 * the bot reads.
	 * 
	 * @param profanityManager
	 * @param roleManager
	 */
	public ProfanityCommand(DBProfanityManager profanityManager, DBRoleManager roleManager) {
		this.options = new ArrayList<>();
		
		this.roleManager = roleManager;
		this.profanityManager = profanityManager;
		
		options.add(new OptionData(OptionType.STRING, COMMAND_OPTION, "Choose a command", true)
			.addChoice("add", ADD_PROFANITY_ID)
		);
		
		options.add(new OptionData(OptionType.STRING, COMMAND_WORD_STATEMENT, "add a word", false));
	}
	
	@Override
	public String getCommandName() {
		return "server-management-profanity";
	}

	@Override
	public String getDescription() {
		return "manages profanities in the server";
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
			event.reply("You dont have the permissions to run this command")
			.setEphemeral(true).queue();
			// Exit failure
			return;
		}
			
		OptionMapping programOption = event.getOption(COMMAND_OPTION);
		OptionMapping secondOptionVal = event.getOption(COMMAND_WORD_STATEMENT);
		
		switch(programOption.getAsString()) {
		case ADD_PROFANITY_ID:
			if(secondOptionVal != null) {
				
				profanityManager.pushWord(secondOptionVal.getAsString());
				
				event.reply("Word added to database. " + secondOptionVal.getAsString())
				.setEphemeral(true).queue();
			}else {
				event.reply("Word could not be added to database")
				.setEphemeral(true).queue();
			}
			break;
		}
	}
}
