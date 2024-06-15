/**
 * 
 */
package assistant.command.files;

import java.io.File;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import service.discord.interaction.CommandI;
import service.discord.interaction.InteractionModel;

/**
 * @author Alfredo
 *
 */
public class CurriculumCmd extends InteractionModel implements CommandI {

	private static final String COMMAND_LABEL = "program";
	
	private static final String OPTION_CHOICE_INEL = "INEL";
	private static final String OPTION_CHOICE_ICOM = "ICOM";
	private static final String OPTION_CHOICE_INSO = "INSO";
	private static final String OPTION_CHOICE_CIIC = "CIIC";
	
	public static final String GIVE_INEL_CURRICULUM = 
			"Here is the Electrical Engineering Curriculum";
	
	public static final String GIVE_ICOM_CURRICULUM = 
			"Here is the Computer Engineering Curriculum";

	public static final String GIVE_INSO_CURRICULUM = 
			"Here is the Software Engineering Curriculum";
	public static final String GIVE_CIIC_CURRICULUM = 
			"Here is the Computer Science & Engineering Curriculum";
	
	public static final String NOT_FOUND_CURRICULUM = 
			"""
			El curriculo que me pediste no está en mi base de datos.
			Trata una de las opciones que ofrece el ``/curriculo``.
			""";
	
	private File INELcurriculum;
	private File ICOMcurriculum;
	private File INSOcurriculum;
	private File CIICcurriculum;
	
	private boolean isGlobal;
	
	public CurriculumCmd() {
		// TODO This are not dynamically set. Find a way of
		// load it every time the command gets called, but reduce the command output time
		this.INELcurriculum = new File("assets/pdfs/curriculos/INEL.pdf");
		this.ICOMcurriculum = new File("assets/pdfs/curriculos/ICOM.pdf");
		this.INSOcurriculum = new File("assets/pdfs/curriculos/INSO.pdf");
		this.CIICcurriculum = new File("assets/pdfs/curriculos/CIIC.pdf");
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
		return "curriculo";
	}

	@Override
	public String getDescription() {
		return "PDF del currículo del Programa de Estudio";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of(
			new OptionData(OptionType.STRING, COMMAND_LABEL, "Escoje un programa de estudio", true)
				.addChoice("INEL - Electrical Engineering", OPTION_CHOICE_INEL)
				.addChoice("ICOM - Computer Engineering", OPTION_CHOICE_ICOM)
				.addChoice("INSO - Software Engineering", OPTION_CHOICE_INSO)
				.addChoice("CIIC - Computer Science & Engineering", OPTION_CHOICE_CIIC)
			);
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		// Give the pdf file of the corresponding curriculum
		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		// Show user that the bot is typing
		event.getChannel().sendTyping().queue();
		
		switch(programOption.getAsString()) {
		case OPTION_CHOICE_INEL:
			event.reply(GIVE_INEL_CURRICULUM).queue();
			event.getChannel().sendFiles(FileUpload.fromData(INELcurriculum)).queue();
			break;
		case OPTION_CHOICE_ICOM:
			event.reply(GIVE_ICOM_CURRICULUM).queue();
			event.getChannel().sendFiles(FileUpload.fromData(ICOMcurriculum)).queue();
			break;
		case OPTION_CHOICE_INSO:
			event.reply(GIVE_INSO_CURRICULUM).queue();
			event.getChannel().sendFiles(FileUpload.fromData(INSOcurriculum)).queue();
			break;
		case OPTION_CHOICE_CIIC:
			event.reply(GIVE_CIIC_CURRICULUM).queue();
			event.getChannel().sendFiles(FileUpload.fromData(CIICcurriculum)).queue();
			break;
		default:
				event.reply(NOT_FOUND_CURRICULUM).queue();
		}
	}
}
