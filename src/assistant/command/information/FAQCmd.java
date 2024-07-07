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
import assistant.embeds.information.FAQEmbed;
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
public class FAQCmd extends InteractionModel implements CommandI {

	private File teamMade;
	private File insociic;
	private FAQEmbed embed;
	
	public FAQCmd() {
		this.embed = new FAQEmbed();
	}
	
	@Override
	public void onGuildInit(Guild server) {
		this.teamMade = new File("assistant/images/FAQ_Banner_TEAM-MADE.png");
		this.insociic = new File("assistant/images/FAQ_Banner_INSO_CIIC.png");
	}
	
	@Override
	public boolean isGlobal() {
		return false;
	}

	@Override
	@Deprecated
	public void setGlobal(boolean isGlobal) {
		// This is a server command
	}
	
	@Override
	public String getCommandName() {
		return "faq";
	}

	@Override
	public String getDescription() {
		return "Frequently Asked Questions por Prepas";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of(
			new OptionData(OptionType.INTEGER, "page", "Enter page of FAQ")
				.setRequired(true)
				.setMinValue(0));
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		int page = event.getOption("page").getAsInt();
		
		// Mentioned Roles in embedded message
		Optional<Role> bdeRole = super.getEffectiveRole(MemberPosition.BOT_DEVELOPER, event.getGuild());
		Optional<Role> esoRole = super.getEffectiveRole(MemberPosition.ESTUDIANTE_ORIENTADOR, event.getGuild());
		
		DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
		String department = discordServer.getDepartment();
		Color color = Color.decode("#" + discordServer.getColor());
		
		String imageUrl_TeamMade = "attachment://FAQ_Banner_TEAM-MADE.png";
		String imageUrl_InsoCiic = "attachment://FAQ_Banner_INSO_CIIC.png";
		
		if ("ECE".equalsIgnoreCase(department)) {
			event.replyFiles(FileUpload.fromData(teamMade))
				.setEmbeds(embed.buildFAQ(color, imageUrl_TeamMade, bdeRole.get(), esoRole.get(), page))
				.setEphemeral(true).queue();
		} else {
			event.replyFiles(FileUpload.fromData(insociic))
				.setEmbeds(embed.buildFAQ(color, imageUrl_InsoCiic, bdeRole.get(), esoRole.get(), page))
				.setEphemeral(true).queue();
		}
	}
}
