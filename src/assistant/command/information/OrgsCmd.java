/**
 * 
 */
package assistant.command.information;

import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.embeds.information.OrganizationsEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.OrganizationDTO;
import assistant.rest.service.OrganizationsService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class OrgsCmd extends InteractionModel implements CommandI {

	private static final String COMMAND_LABEL = "select-orgs";
	
	private OrganizationsEmbed embed;
	private OrganizationsService service;
	
	private boolean isGlobal;

	public OrgsCmd() {
		this.embed = new OrganizationsEmbed();
		this.service = Application.instance().getSpringContext().getBean(OrganizationsService.class);
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
		return "ls_student_orgs";
	}

	@Override
	public String getDescription() {
		return "Obten una lista te todas las organizaciones para tí";
	}

	@Override
	public List<OptionData> getOptions() {
		
		List<String> names = service.getOrganizationNames(0, 15);
		
		List<Choice> choices = names.stream()
				.map(name -> new Choice(name, name))
				.collect(Collectors.toList());
		
		return List.of(
			new OptionData(OptionType.STRING, COMMAND_LABEL, "Escoje una organización", true)
				.addChoices(choices)
			);
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		String selectedProject = event.getOption(COMMAND_LABEL).getAsString();
		
		DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
		Color color = Color.decode("#" + discordServer.getColor());
		
		Optional<OrganizationDTO> project = service.getOrganization(selectedProject);
		
		if (project.isPresent()) {
			event.replyEmbeds(embed.buildOrganization(color, project.get()))
				.setEphemeral(event.isFromGuild()).queue();
		} else {
			event.reply("Hmm creo que la organización que me diste no existe en mi base de datos :confused:")
				.setEphemeral(event.isFromGuild()).queue();
		}
	}
}
