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
public class DepartmentCmd extends InteractionModel implements CommandI {

	private ServicesEmbed embed;
	private ServicesService service;
	
	public DepartmentCmd() {
		this.embed = new ServicesEmbed();
		this.service = Application.instance().getSpringContext().getBean(ServicesService.class);
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
		return "contact-department";
	}

	@Override
	public String getDescription() {
		return "Información acerca de los departamentos";
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
			Optional<ServiceDTO> result = service.getService("Electrical and Computer Engineering");
			
			event.replyEmbeds(embed.buildInfoPanel(color, result.get()))
				.setEphemeral(true).queue();
		} else {
			Optional<ServiceDTO> result = service.getService("Computer Science & Engineering");
			
			event.replyEmbeds(embed.buildInfoPanel(color, result.get()))
				.setEphemeral(true).queue();
		}
	}
}
