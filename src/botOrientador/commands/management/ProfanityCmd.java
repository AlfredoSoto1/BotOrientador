/**
 * 
 */
package botOrientador.commands.management;

import java.util.ArrayList;
import java.util.List;

import botOrientador.entry.BotConfigs;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.dbaccess.DBProfanityManager;
import services.bot.dbaccess.DBRoleManager;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class ProfanityCmd implements CommandI {

	// Defines the option for the slash command
	// that will be used for entering the other commands
	private static final String COMMAND_LABEL = "type";

	private static final String ADD_PROFANITY_ID = "add-prof-id";
	private static final String COMMAND_WORD_STATEMENT = "word";
	
	private DBRoleManager roleManager;
	private DBProfanityManager profanityManager;
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	public ProfanityCmd() {
		this.options = new ArrayList<>();
		this.roleManager = new DBRoleManager();
		
		options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "Choose a command", true)
				.addChoice("add", ADD_PROFANITY_ID)
		);
		
		options.add(new OptionData(OptionType.STRING, COMMAND_WORD_STATEMENT, "add a word", false));
	}
	
	@Override
	public void init(ReadyEvent event) {
		
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
			
		OptionMapping programOption = event.getOption(COMMAND_LABEL);
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
