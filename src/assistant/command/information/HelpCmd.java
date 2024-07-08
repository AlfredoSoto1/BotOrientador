/**
 * 
 */
package assistant.command.information;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.Optional;

import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.discord.object.MemberPosition;
import assistant.embeds.information.HelpEmbed;
import assistant.rest.dto.DiscordServerDTO;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

/**
 * @author Alfredo
 *
 */
public class HelpCmd extends InteractionModel implements CommandI {

	private File teamMade;
	private File insociic;
	private HelpEmbed embed;
	
	private boolean isGlobal;
	
	public HelpCmd() {
		this.embed = new HelpEmbed();
	}
	
	@Override
	public void onGuildInit(Guild server) {
		this.teamMade = new File("assistant/images/Help_Banner_TEAM-MADE.png");
		this.insociic = new File("assistant/images/Help_Banner_INSO_CIIC.png");
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
		return "help";
	}

	@Override
	public String getDescription() {
		return "Menu de ayuda";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of(
			new OptionData(OptionType.INTEGER, "page", "Enter page of Help", true)
				.setMinValue(0));
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		int page = event.getOption("page").getAsInt();
		if (event.isFromGuild())
			fromServer(page, event);
		else
			fromDM(page, event);
	}
	
	private void fromServer(int page, SlashCommandInteractionEvent event) {
		// Mentioned Roles in embedded message
		Optional<Role> esoRole = super.getEffectiveRole(MemberPosition.ESTUDIANTE_ORIENTADOR, event.getGuild());
		
		DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
		String department = discordServer.getDepartment();
		Color color = Color.decode("#" + discordServer.getColor());
		
		String imageUrl_TeamMade = "attachment://Help_Banner_TEAM-MADE.png";
		String imageUrl_InsoCiic = "attachment://Help_Banner_INSO_CIIC.png";
		
		if ("ECE".equalsIgnoreCase(department)) {
			event.replyFiles(FileUpload.fromData(teamMade))
				.setEmbeds(embed.buildHelp(color, imageUrl_TeamMade, esoRole.get(), page))
				.setEphemeral(event.isFromGuild()).queue();
		} else {
			event.replyFiles(FileUpload.fromData(insociic))
				.setEmbeds(embed.buildHelp(color, imageUrl_InsoCiic, esoRole.get(), page))
				.setEphemeral(event.isFromGuild()).queue();
		}
	}
	
	private void fromDM(int page, SlashCommandInteractionEvent event) {
		event.replyEmbeds(embed.buildHelpDM("Estudiante Orientador", page)).queue();
	}
}
