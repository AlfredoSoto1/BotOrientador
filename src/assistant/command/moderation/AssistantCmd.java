/**
 * 
 */
package assistant.command.moderation;

import java.util.List;

import assistant.discord.app.BotApplication;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.rest.dto.AssistantOptions;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class AssistantCmd extends InteractionModel implements CommandI {
	
	private static final String COMMAND_LABEL = "service";
	
	private BotApplication bot;
	private boolean isGlobal;
	
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
				.addChoice("disconnect", AssistantOptions.DISCONNECT.getOption())
			);
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		if(!super.validateCommandUse(event))
			return;
		
		AssistantOptions option = AssistantOptions.asOption(event.getOption(COMMAND_LABEL).getAsString());
		
		switch(option) {
		case AssistantOptions.DISCONNECT:
			event.reply("Shutting down...").setEphemeral(true).queue();
			bot.shutdown();
			break;
		default:
			// skip this action if no reply was provided
			event.reply("Mmhh this command does nothing, try again with another one").setEphemeral(true).queue();
		}
	}
}
