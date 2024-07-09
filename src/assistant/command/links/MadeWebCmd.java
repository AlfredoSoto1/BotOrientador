/**
 * 
 */
package assistant.command.links;

import java.io.File;
import java.util.List;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.rest.service.GameService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

/**
 * @author Alfredo
 *
 */
public class MadeWebCmd extends InteractionModel implements CommandI {

	private GameService commandEventService;
	
	public MadeWebCmd() {
		this.commandEventService = Application.instance().getSpringContext().getBean(GameService.class);
	}
	
	@Override
	public boolean isGlobal() {
		return false;
	}

	@Override
	@Deprecated
	public void setGlobal(boolean isGlobal) {
		// This is a server only command
	}
	
	@Override
	public String getCommandName() {
		return "made-web";
	}

	@Override
	public String getDescription() {
		return "Provee el link para accesar la página web de Madeline Rodríguez";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.reply(
			"""
			Aquí el enlace para la página web de Made! :green_heart:
			https://sites.google.com/upr.edu/maderodriguez/
			""")
		.addFiles(FileUpload.fromData(new File("assets/images/MadeWeb.png"))).queue();
		
		// Update the user points stats when he uses the command
		commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
	}
}
