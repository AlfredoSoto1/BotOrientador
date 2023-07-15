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
import services.bot.commands.CommandI;

/**
 * @author Alfredo
 *
 */
public class ProfanityCommand implements CommandI {

	// Defines the option for the slash command
	// that will be used for entering the other commands
	private static final String COMMAND_OPTION = "developer-command-profanity";
	
	private static final String ADD_PROFANITY_ID = "add-prof-id";
	
	private List<OptionData> options;
	
	public ProfanityCommand() {
		this.options = new ArrayList<>();
		
		options.add(new OptionData(OptionType.STRING, COMMAND_OPTION, "Choose a command", true)
			.addChoice("add", ADD_PROFANITY_ID)
		);
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
		OptionMapping programOption = event.getOption(COMMAND_OPTION);
		
		event.reply("Word added to database.")
		.setEphemeral(true).queue();
	}
}
