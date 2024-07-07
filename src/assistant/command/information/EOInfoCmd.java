/**
 * 
 */
package assistant.command.information;

import java.awt.Color;
import java.util.List;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.discord.object.MemberRetrievement;
import assistant.embeds.information.EOEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.MemberDTO;
import assistant.rest.service.MemberService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class EOInfoCmd extends InteractionModel implements CommandI {

	private EOEmbed embed;
	private MemberService service;
	
	private boolean isGlobal;
	
	public EOInfoCmd() {
		this.embed = new EOEmbed();
		this.service = Application.instance().getSpringContext().getBean(MemberService.class);
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
		return "estudiantes-orientadores";
	}

	@Override
	public String getDescription() {
		return "Get EOs de un programa";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of(
			new OptionData(OptionType.INTEGER, "page", "select the page", true)
				.setMinValue(0));
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		int page = event.getOption("page").getAsInt();
		
		Guild server = event.getGuild();
		DiscordServerDTO discordServer = super.getServerOwnerInfo(server.getIdLong());
		String department = discordServer.getDepartment();
		Color color = Color.decode("#" + discordServer.getColor());
		
		List<MemberDTO> member = service.getAllMembers(page, 3, MemberRetrievement.VERIFIED_ORIENTADOR, server.getIdLong());
		long maxPages = service.memberCount(MemberRetrievement.VERIFIED_ORIENTADOR, server.getIdLong()) / 3;
		
		if (member.isEmpty()) {
			event.reply(String.format(
					"""
					Hmm no creo que hallan demasiados estudiantes ortientadores en esta página,
					trata con un rango de páginas de [0-%s]
					""", maxPages))
				.setEphemeral(true).queue();
			return;
		}
		
		event.replyEmbeds(embed.buildEO(color, department, member, page, maxPages))
			.setEphemeral(true).queue();
	}
}
