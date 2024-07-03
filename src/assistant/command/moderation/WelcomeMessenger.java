/**
 * 
 */
package assistant.command.moderation;

import assistant.discord.interaction.InteractionModel;
import assistant.discord.interaction.MessengerI;
import assistant.embeds.moderation.VerificationEmbed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * @author Alfredo
 */
public class WelcomeMessenger extends InteractionModel implements MessengerI {

	private VerificationEmbed embed;
	
	public WelcomeMessenger() {
		this.embed = new VerificationEmbed();
	}
	
	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		// TODO do a query which reads from database which department is from to do custom message
		Guild server = event.getGuild();
		
		event.getMember()
		.getUser().openPrivateChannel().queue(
				privateChannel -> {
					privateChannel.sendMessageEmbeds(embed.buildServerBanner()).queue();
					privateChannel.sendMessageEmbeds(embed.buildWelcomePrompt(server)).queue();
				});
	}

	@Override
	public void messageReceived(MessageReceivedEvent event) {
		
	}
}
