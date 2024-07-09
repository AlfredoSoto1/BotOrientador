/**
 * 
 */
package assistant.command.contacts;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.embeds.contacts.ServicesEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.ServiceDTO;
import assistant.rest.service.GameService;
import assistant.rest.service.ServicesService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class AcademicAdvisoryCmd extends InteractionModel implements CommandI {

	private ServicesEmbed embed;
	private ServicesService service;
	private GameService commandEventService;
	
	public AcademicAdvisoryCmd() {
		this.embed = new ServicesEmbed();
		this.service = Application.instance().getSpringContext().getBean(ServicesService.class);
		this.commandEventService = Application.instance().getSpringContext().getBean(GameService.class);
	}

	@Override
	public boolean isGlobal() {
		return false;
	}

	@Override
	@Deprecated
	public void setGlobal(boolean isGlobal) {
		// Server only command
	}
	
	@Override
	public String getCommandName() {
		return "contact-asesoria-academica";
	}

	@Override
	public String getDescription() {
		return "Información y contactos de consejería y asesoría academica para *INEL/ICOM/INSO/CIIC*";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
		String department = discordServer.getDepartment();
		Color color = Color.decode("#" + discordServer.getColor());
		
		if ("ECE".equalsIgnoreCase(department)) {
			Optional<ServiceDTO> result = service.getService("Asesoría Academica del Departamento INEL/ICOM");
			
			event.replyEmbeds(embed.buildInfoPanel(color, result.get()))
				.setEphemeral(true).queue();
		} else {
			Optional<ServiceDTO> result = service.getService("Asesoria Académica del Departamento de INSO/CIIC");
			
			event.replyEmbeds(embed.buildInfoPanel(color, result.get()))
				.setEphemeral(true).queue();
		}
		
		// Update the user points stats when he uses the command
		commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
	}
}
