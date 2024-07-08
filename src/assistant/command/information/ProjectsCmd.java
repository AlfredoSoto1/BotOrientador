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
import assistant.embeds.information.ProjectsEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.ProjectDTO;
import assistant.rest.service.ProjectsService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class ProjectsCmd extends InteractionModel implements CommandI {

	private static final String COMMAND_LABEL = "select-projects";
	
	private boolean isGlobal;
	private ProjectsEmbed embed;
	private ProjectsService service;
	
	public ProjectsCmd() {
		this.embed = new ProjectsEmbed();
		this.service = Application.instance().getSpringContext().getBean(ProjectsService.class);
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
		return "ls_projects";
	}

	@Override
	public String getDescription() {
		return "Información de los proyectos aqui en el RUM para ustedes de INEL/ICOM/INSO/CIIC";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		List<String> names = service.getProjectNames(0, 10);
		
		List<Choice> choices = names.stream()
				.map(name -> new Choice(name, name))
				.collect(Collectors.toList());
		
		return List.of(
			new OptionData(OptionType.STRING, COMMAND_LABEL, "Escoje un departamento", true)
				.addChoices(choices));
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		String selectedProject = event.getOption(COMMAND_LABEL).getAsString();
		
		if (event.isFromGuild()) {
			fromServer(event, selectedProject);
		} else {
			fromDM(event, selectedProject);
		}
	}
	
	public void fromServer(SlashCommandInteractionEvent event, String selectedProject) {
		DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
		Color color = Color.decode("#" + discordServer.getColor());
		
		Optional<ProjectDTO> project = service.getProject(selectedProject);
		
		if (project.isPresent()) {
			event.replyEmbeds(embed.buildProject(color, project.get()))
			.setEphemeral(event.isFromGuild()).queue();
		} else {
			event.reply("Hmm creo que el proyecto que me diste no existe en mi base de datos :confused:")
			.setEphemeral(event.isFromGuild()).queue();
		}
	}

	public void fromDM(SlashCommandInteractionEvent event, String selectedProject) {
		Optional<ProjectDTO> project = service.getProject(selectedProject);
		
		if (project.isPresent()) {
			event.replyEmbeds(embed.buildProject(Color.GRAY, project.get())).queue();
		} else {
			event.reply("Hmm creo que el proyecto que me diste no existe en mi base de datos :confused:").queue();
		}
	}
}
