/**
 * 
 */
package botOrientador.commands.links;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class MadeWeb implements CommandI {

	private File madeImage;
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	public MadeWeb() {
		this.options = new ArrayList<>();
		
	}
	
	@Override
	public void init(ReadyEvent event) {
		this.madeImage = new File("assets/images/MadeWeb.png");

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
	public void dispose() {
		options.clear();
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
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.reply(
			"""
			Aquí el enlace para la página web de Made! :green_heart:
			https://sites.google.com/upr.edu/maderodriguez/
			"""
		).queue();
		
		event.getChannel().sendTyping();
		event.getChannel().sendFiles(FileUpload.fromData(madeImage)).queue();
	}

}
