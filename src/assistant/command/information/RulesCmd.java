package assistant.command.information;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.discord.object.MemberPosition;
import assistant.embeds.information.RulesEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.service.GameService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * 
 * @author Alfredo
 *
 */
public class RulesCmd extends InteractionModel implements CommandI {

	private RulesEmbed embed;
	private GameService commandEventService;
	
	private boolean isGlobal;
	
	public RulesCmd() {
		this.embed = new RulesEmbed();
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
		return "rules";
	}

	@Override
	public String getDescription() {
		return "Provee las reglas del servidor";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		if (event.isFromGuild())
			fromServer(event);
		else
			fromDM(event);
	}
	
	private void fromServer(SlashCommandInteractionEvent event) {
		// Mentioned Roles in embedded message
		Optional<Role> staRole = super.getEffectiveRole(MemberPosition.STAFF, event.getGuild());
		Optional<Role> modRole = super.getEffectiveRole(MemberPosition.MODERATOR, event.getGuild());
		Optional<Role> bdeRole = super.getEffectiveRole(MemberPosition.BOT_DEVELOPER, event.getGuild());
		Optional<Role> conRole = super.getEffectiveRole(MemberPosition.CONSEJERO_PROFESIONAL, event.getGuild());
		Optional<Role> esoRole = super.getEffectiveRole(MemberPosition.ESTUDIANTE_ORIENTADOR, event.getGuild());
		Optional<Role> botRole = super.getEffectiveRole(MemberPosition.BOTS, event.getGuild());
		
		Role admRole = event.getGuild().getRolesByName("Administrator", true).get(0);
		
		DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
		Color color = Color.decode("#" + discordServer.getColor());
		
		event.replyEmbeds(embed.buildGeneralRules(color, 
				esoRole.get().getAsMention(),
				botRole.get().getAsMention(),
				modRole.get().getAsMention(),
				staRole.get().getAsMention(),
				admRole.getAsMention(),
				conRole.get().getAsMention()),
				
			embed.buildServerUsageRules(color, 
				bdeRole.get().getAsMention(),
				modRole.get().getAsMention(),
				staRole.get().getAsMention()),
			
			embed.buildBotUsageRules(color, 
				bdeRole.get().getAsMention(),
				esoRole.get().getAsMention())).queue();
		
		// Update the user points stats when he uses the command
		commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
	}
	
	private void fromDM(SlashCommandInteractionEvent event) {
		event.replyEmbeds(embed.buildGeneralRules(Color.GRAY, 
				"Estudiante Orientador",
				"Bots",
				"Moderador",
				"Staff",
				"Administrator",
				"Consejero Profesional"),
				
			embed.buildServerUsageRules(Color.GRAY, 
				"BotDeveloper",
				"Moderador",
				"Staff"),
			
			embed.buildBotUsageRules(Color.GRAY, 
				"BotDeveloper",
				"Estudiante Orientador")).queue();
	}
}
