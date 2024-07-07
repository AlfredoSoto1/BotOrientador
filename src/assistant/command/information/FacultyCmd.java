/**
 * 
 */
package assistant.command.information;

import java.awt.Color;
import java.util.List;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.embeds.information.FacultyEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.FacultyDTO;
import assistant.rest.service.FacultyService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class FacultyCmd extends InteractionModel implements CommandI {

	private FacultyEmbed embed;
	private FacultyService service;
	
	public FacultyCmd() {
		this.embed = new FacultyEmbed();
		this.service = Application.instance().getSpringContext().getBean(FacultyService.class);
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
		return "facultad";
	}

	@Override
	public String getDescription() {
		return "Información acerca de nuestra facultad";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of(
			new OptionData(OptionType.INTEGER, "page", "select the page", true)
				.setRequired(true)
				.setMinValue(0));
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		int page = event.getOption("page").getAsInt();
		
		DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
		String department = discordServer.getDepartment();
		Color color = Color.decode("#" + discordServer.getColor());
		
		List<FacultyDTO> faculty = service.getFaculty(page, 3, department);
		long maxPages = service.getRecordCount(department) / 3;
		
		if (faculty.isEmpty()) {
			event.reply(String.format(
					"""
					Hmm no creo que hallan demasiados profesores en esta página,
					trata con un rango de páginas de [0-%s]
					""", maxPages))
				.setEphemeral(true).queue();
			return;
		}
		
		event.replyEmbeds(embed.buildFaculty(color, department, faculty, page, maxPages))
			.setEphemeral(true).queue();
	}
}
