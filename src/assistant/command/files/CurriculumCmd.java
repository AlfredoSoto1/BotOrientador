/**
 * 
 */
package assistant.command.files;

import java.awt.Color;
import java.io.File;
import java.util.List;

import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.embeds.files.CurriculumEmbed;
import assistant.rest.dto.DiscordServerDTO;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

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
	
	private CurriculumEmbed embed;
	
	private boolean isGlobal;
	
	public CurriculumCmd() {
		this.embed = new CurriculumEmbed();
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
				.addChoice("ICOM - Computer Engineering",   OPTION_CHOICE_ICOM)
				.addChoice("INSO - Software Engineering",   OPTION_CHOICE_INSO)
				.addChoice("CIIC - Computer Science & Engineering", OPTION_CHOICE_CIIC)
			);
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		Guild server = event.getGuild();
		DiscordServerDTO discordServer = super.getServerOwnerInfo(server.getIdLong());
		String department = discordServer.getDepartment();
		Color color = Color.decode("#" + discordServer.getColor());
		
		File INELcurriculum = new File("assistant/curriculos/INEL.pdf");
		File ICOMcurriculum = new File("assistant/curriculos/ICOM.pdf");
		File INSOcurriculum = new File("assistant/curriculos/INSO.pdf");
		File CIICcurriculum = new File("assistant/curriculos/CIIC.pdf");
		
		FileUpload uploadINEL = FileUpload.fromData(INELcurriculum);
		FileUpload uploadICOM = FileUpload.fromData(ICOMcurriculum);
		FileUpload uploadINSO = FileUpload.fromData(INSOcurriculum);
		FileUpload uploadCIIC = FileUpload.fromData(CIICcurriculum);
		
//		if ("ECE".equalsIgnoreCase(department)) {
//			
//			event.replyFiles(uploadTeamMade)
//				.setEmbeds(embed.buildServerBanner(imageUrl_TeamMade, color)).queue();
//		} else {
//			event.sendFiles(uploadInsoCiic)
//				.setEmbeds(embed.buildServerBanner(imageUrl_InsoCiic, color)).queue();
//		}
//		
		switch(event.getOption(COMMAND_LABEL).getAsString()) {
		case OPTION_CHOICE_INEL:
			event.reply(GIVE_INEL_CURRICULUM)
				.addFiles(FileUpload.fromData(INELcurriculum))
				.setEphemeral(true).queue();
			break;
		case OPTION_CHOICE_ICOM:
			event.reply(GIVE_ICOM_CURRICULUM)
				.addFiles(FileUpload.fromData(ICOMcurriculum))
				.setEphemeral(true).queue();
			break;
		case OPTION_CHOICE_INSO:
			event.reply(GIVE_INSO_CURRICULUM)
				.addFiles(FileUpload.fromData(INSOcurriculum))
				.setEphemeral(true).queue();
			break;
		case OPTION_CHOICE_CIIC:
			event.reply(GIVE_CIIC_CURRICULUM)
				.addFiles(FileUpload.fromData(CIICcurriculum))
				.setEphemeral(true).queue();
			break;
		}
	}
}
