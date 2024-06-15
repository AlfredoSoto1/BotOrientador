/**
 * 
 */
package assistant.command.files;

import java.io.File;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import service.discord.interaction.CommandI;
import service.discord.interaction.InteractionModel;

/**
 * @author Alfredo
 *
 */
public class FreshmanGuideCmd extends InteractionModel implements CommandI {

	private boolean isGlobal;
	
	public FreshmanGuideCmd() {
		
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
	public List<OptionData> getOptions() {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		// Give the pdf file of the corresponding curriculum
		event.reply("Aquí tienes la guía prepística. Espero que te sea súper util!! :grin:").queue();
		event.getChannel().sendFiles(FileUpload.fromData(new File("assets/pdfs/guia-prepistica/GuiaPrepistica.pdf"))).queue();
	}
}
