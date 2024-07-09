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
public class DCSPCmd extends InteractionModel implements CommandI {
	
	private ServicesEmbed embed;
	private ServicesService service;
	private GameService commandEventService;
	
	private boolean isGlobal;
	
	public DCSPCmd() {
		this.embed = new ServicesEmbed();
		this.service = Application.instance().getSpringContext().getBean(ServicesService.class);
		this.commandEventService = Application.instance().getSpringContext().getBean(GameService.class);
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
		return "contact-dcsp";
	}

	@Override
	public String getDescription() {
		return "Información acerca de DCPS";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		// Obtain the service data related to the DCSP
		Optional<ServiceDTO> result = service.getService("Departamento de Consejería y Servicios Psicológicos");
		
		// Check if the command was called from a server
		if (event.isFromGuild()) {
			DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
			Color color = Color.decode("#" + discordServer.getColor());
			
			event.replyEmbeds(embed.buildInfoPanel(color, result.get()))
				.setEphemeral(event.isFromGuild()).queue();

			// Update the user points stats when he uses the command
			commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
		} else {
			event.replyEmbeds(embed.buildInfoPanel(Color.GRAY, result.get())).queue();
		}
	}
}
