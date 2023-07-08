/**
 * 
 */
package application.services.botOrientador.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import application.hosts.bot.commands.CommandI;
import application.services.botOrientador.messages.CurriculumMessages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

/**
 * @author Alfredo
 *
 */
public class Curriculum implements CommandI {

	private File INELcurriculum;
	private File ICOMcurriculum;
	private File INSOcurriculum;
	private File CIICcurriculum;
	
	private List<OptionData> options;
	
	public Curriculum() {
		this.options = new ArrayList<>();
		
		// Load the curriculums
		this.INELcurriculum = new File("assets/pdfs/curriculos/INEL.pdf");
		this.ICOMcurriculum = new File("assets/pdfs/curriculos/ICOM.pdf");
		this.INSOcurriculum = new File("assets/pdfs/curriculos/INSO.pdf");
		this.CIICcurriculum = new File("assets/pdfs/curriculos/CIIC.pdf");
		
		options.add(new OptionData(OptionType.STRING, "program", "Escoje un programa de estudio", true)
			.addChoice("INEL - Electrical Engineering", "INEL")
			.addChoice("ICOM - Computer Engineering", "ICOM")
			.addChoice("INSO - Software Engineering", "INSO")
			.addChoice("CIIC - Computer Science & Engineering", "CIIC")
		);
	}
	
	@Override
	public String getCommandName() {
		return "curriculo";
	}

	@Override
	public String getDescription() {
		return "PDF del currículo del Programa de Estudio";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		// Give the pdf file of the corresponding curriculum
		OptionMapping programOption = event.getOption("program");
		
		event.getChannel().sendTyping().queue();
		
		if(programOption.getAsString().equals("INEL")) {
			event.reply(CurriculumMessages.GIVE_INEL_CURRICULUM).queue();
			event.getChannel().sendFiles(FileUpload.fromData(INELcurriculum)).queue();
		} else if(programOption.getAsString().equals("ICOM")) {
			event.reply(CurriculumMessages.GIVE_ICOM_CURRICULUM).queue();
			event.getChannel().sendFiles(FileUpload.fromData(ICOMcurriculum)).queue();
		} else if(programOption.getAsString().equals("INSO")) {
			event.reply(CurriculumMessages.GIVE_INSO_CURRICULUM).queue();
			event.getChannel().sendFiles(FileUpload.fromData(INSOcurriculum)).queue();
		} else if(programOption.getAsString().equals("CIIC")) {
			event.reply(CurriculumMessages.GIVE_CIIC_CURRICULUM).queue();
			event.getChannel().sendFiles(FileUpload.fromData(CIICcurriculum)).queue();
		} else {
			event.reply(CurriculumMessages.NOT_FOUND_CURRICULUM).queue();
		}
	}
}
