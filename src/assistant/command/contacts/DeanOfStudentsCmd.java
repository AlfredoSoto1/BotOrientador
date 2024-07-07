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
import assistant.rest.service.ServicesService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class DeanOfStudentsCmd extends InteractionModel implements CommandI {
	
	private ServicesEmbed embed;
	private ServicesService service;
	
	private boolean isGlobal;
	
	public DeanOfStudentsCmd() {
		this.embed = new ServicesEmbed();
		this.service = Application.instance().getSpringContext().getBean(ServicesService.class);
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
		return "contact-decanato-estudiantes";
	}

	@Override
	public String getDescription() {
		return "Información acerca del Decanato de estudiantes";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		// Obtain the service data related to the Dean of Students
		Optional<ServiceDTO> result = service.getService("Decanato de Estudiantes");
		
		// Check if the command was called from a server
		if (event.isFromGuild()) {
			DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
			Color color = Color.decode("#" + discordServer.getColor());
			
			event.replyEmbeds(embed.buildInfoPanel(color, result.get()))
				.setEphemeral(true).queue();
		} else {
			event.replyEmbeds(embed.buildInfoPanel(Color.GRAY, result.get())).queue();
		}
	}
}
