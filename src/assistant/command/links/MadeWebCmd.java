/**
 * 
 */
package assistant.command.links;

import java.io.File;
import java.util.List;

import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

/**
 * @author Alfredo
 *
 */
public class MadeWebCmd extends InteractionModel implements CommandI {

	private boolean isGlobal;
	
	public MadeWebCmd() {
		
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
		return "made-web";
	}

	@Override
	public String getDescription() {
		return "Provee el link para accesar la página web de Madeline Rodríguez";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.reply(
			"""
			Aquí el enlace para la página web de Made! :green_heart:
			https://sites.google.com/upr.edu/maderodriguez/
			"""
		).queue();
		
		// Load the file dynamically, this is done so
		// that resources can get changed on the fly.
		// TODO: this needs to be improved to reduce latency of loading resources
		// every time the command gets called
		event.getChannel().sendTyping();
		event.getChannel().sendFiles(FileUpload.fromData(new File("assets/images/MadeWeb.png"))).queue();
	}
}
