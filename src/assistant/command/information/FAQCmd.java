/**
 * 
 */
package assistant.command.information;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.Optional;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.discord.object.MemberPosition;
import assistant.embeds.information.FAQEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.service.GameService;
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
	
	private GameService commandEventService;
	
	private boolean isGlobal;
	
	public FAQCmd() {
		this.embed = new FAQEmbed();
		this.commandEventService = Application.instance().getSpringContext().getBean(GameService.class);
	}
	
	@Override
	public void onGuildInit(Guild server) {
		this.teamMade = new File("assistant/images/FAQ_Banner_TEAM-MADE.png");
		this.insociic = new File("assistant/images/FAQ_Banner_INSO_CIIC.png");
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
		return "faq";
	}

	@Override
	public String getDescription() {
		return "Frequently Asked Questions por Prepas";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of(
			new OptionData(OptionType.INTEGER, "page", "Enter page of FAQ", true)
				.setMinValue(0));
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
				.setEmbeds(embed.buildFAQ(color, imageUrl_TeamMade, bdeRole.get().getAsMention(), esoRole.get().getAsMention(), page))
				.setEphemeral(true).queue();
		} else {
			event.replyFiles(FileUpload.fromData(insociic))
				.setEmbeds(embed.buildFAQ(color, imageUrl_InsoCiic, bdeRole.get().getAsMention(), esoRole.get().getAsMention(), page))
				.setEphemeral(true).queue();
		}
		
		// Update the user points stats when he uses the command
		commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
	}
	
	private void fromDM(SlashCommandInteractionEvent event) {
		int page = event.getOption("page").getAsInt();
		event.replyEmbeds(embed.buildFAQDM(Color.GRAY, "BotDeveloper", "EstudianteOrientador", page)).queue();
	}
}
