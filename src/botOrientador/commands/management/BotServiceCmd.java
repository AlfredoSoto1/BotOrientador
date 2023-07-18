/**
 * 
 */
package botOrientador.commands.management;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class BotServiceCmd implements CommandI {

	private boolean isGlobal;
	
	private List<OptionData> options;
	
	@Override
	public void init(ReadyEvent event) {
		this.options = new ArrayList<>();
	}
	
	@Override
	public void dispose() {
		options.clear();
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
		return "server-management-service";
	}

	@Override
	public String getDescription() {
		return "Manage the bot service on server";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.reply("Command executed").setEphemeral(true).queue();
	}

}
