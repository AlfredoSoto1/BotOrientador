/**
 * 
 */
package services.bot.orientador.commands.fileuploads;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import services.bot.managers.command.CommandI;

/**
 * @author Alfredo
 *
 */
public class GuiaPrepistica implements CommandI {

	private File guiaPrepistica;
	
	private List<OptionData> options;
	
	public GuiaPrepistica() {
		this.options = new ArrayList<>();
		
		// Load the curriculums
		this.guiaPrepistica = new File("assets/pdfs/guia-prepistica/GuiaPrepistica.pdf");
	}
	
	@Override
	public void dispose() {
		options.clear();
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
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		// Give the pdf file of the corresponding curriculum
		
		event.reply("Aquí tienes la guía prepística. Espero que te sea súper util!! :grin:").queue();
		event.getChannel().sendFiles(FileUpload.fromData(guiaPrepistica)).queue();
	}
}
