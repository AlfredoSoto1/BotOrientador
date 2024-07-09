/**
 * 
 */
package assistant.command.files;

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
public class FreshmanGuideCmd extends InteractionModel implements CommandI {

	private GameService commandEventService;
	
	private boolean isGlobal;
	
	public FreshmanGuideCmd() {
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
		return "guia-prepistica";
	}

	@Override
	public String getDescription() {
		return "Guia para aprender del colegio";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.reply(
			"""
			Aquí tienes la guía prepística. Espero que te sea súper util!! :grin:
			https://sites.google.com/upr.edu/guiaprepisticauprm/inicio
			""")
		.setEphemeral(event.isFromGuild()).queue();
		
		if (event.isFromGuild()) {
			// Update the user points stats when he uses the command
			commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
		}
	}
}
