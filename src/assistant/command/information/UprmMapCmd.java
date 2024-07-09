/**
 * 
 */
package assistant.command.information;

import java.util.List;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.rest.service.GameService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class UprmMapCmd extends InteractionModel implements CommandI {

	private boolean isGlobal;
	
	private GameService commandEventService;
	
	public UprmMapCmd() {
		this.commandEventService = Application.instance().getSpringContext().getBean(GameService.class);
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
		return "map";
	}

	@Override
	public String getDescription() {
		return "Provee un link al Mapa del UPRM";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.reply(
			"""
			UPRM Map
			https://www.uprm.edu/portales/mapa/
			""").setEphemeral(event.isFromGuild()).queue();
		
		if (event.isFromGuild()) {
			// Update the user points stats when he uses the command
			commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
		}
	}

}
