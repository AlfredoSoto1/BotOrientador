/**
 * 
 */
package assistant.cmd.moderation;

import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.core.BotApplication;
import services.bot.interactions.CommandI;
import services.bot.interactions.InteractionModel;

/**
 * @author Alfredo
 *
 */
public class AssistantCmd extends InteractionModel implements CommandI {

	private static final String COMMAND_LABEL = "service";
	private static final String OPTION_DISCONNECT = "disconnect";
	
	private boolean isGlobal;
	private BotApplication bot;
	
	public AssistantCmd(BotApplication bot) {
		this.bot = bot;
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
		return "assistant";
	}

	@Override
	public String getDescription() {
		return "Manage the bot service on server";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of(
			new OptionData(OptionType.STRING, COMMAND_LABEL, "Choose a command", true)
				.addChoice("disconnect", OPTION_DISCONNECT)
			);
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		if(!super.validateCommandUse(event))
			return;
		
		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		if (programOption.getAsString().equals(OPTION_DISCONNECT)) {
			event.reply("Shutting down...").setEphemeral(true).queue();
			bot.shutdown();
		} else {
			// skip this action if no reply was provided
			event.reply("Mmhh this command does nothing, try again with another one").setEphemeral(true).queue();
		}
	}
}
