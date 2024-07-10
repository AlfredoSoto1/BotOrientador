/**
 * 
 */
package assistant.command.information;

import java.awt.Color;
import java.util.List;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.embeds.information.LabEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.LabDTO;
import assistant.rest.service.BuildingService;
import assistant.rest.service.GameService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class FindLabCmd extends InteractionModel implements CommandI {

	private static final String COMMAND_LABEL = "location";
	
	private LabEmbed embed;
	private BuildingService service;
	private GameService commandEventService;

	private boolean isGlobal;
	
	public FindLabCmd() {
		this.embed = new LabEmbed();
		this.service = Application.instance().getSpringContext().getBean(BuildingService.class);
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
		return "lab";
	}

	@Override
	public String getDescription() {
		return "Obten información acerca de donde queda un lab de estudio";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of(
			new OptionData(OptionType.STRING, COMMAND_LABEL, "Dispone la localización del lab en el que se encuentra", true));
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		if (event.isFromGuild()) {
			fromServer(event);
		} else {
			fromDM(event);
		}
	}
	
	private void fromServer(SlashCommandInteractionEvent event) {
		DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
		Color color = Color.decode("#" + discordServer.getColor());
		
		// Obtain building codification from command
		String roomCode = event.getOption(COMMAND_LABEL).getAsString();
		
		// Look for the building associated to the room code provided
		List<LabDTO> labs = service.getLabsFrom(roomCode);
		
		event.replyEmbeds(embed.buildLab(color, roomCode, labs))
			.setEphemeral(true).queue();
		
		// Update the user points stats when he uses the command
		commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
	}
	
	private void fromDM(SlashCommandInteractionEvent event) {
		// Obtain building codification from command
		String roomCode = event.getOption(COMMAND_LABEL).getAsString();
		
		// Get the labs from given code
		List<LabDTO> labs = service.getLabsFrom(roomCode);
		
		// Reply in form of embed
		event.replyEmbeds(embed.buildLab(Color.GRAY, roomCode, labs)).queue();
	}
}
