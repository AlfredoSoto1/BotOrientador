/**
 * 
 */
package assistant.command.moderation;

import assistant.discord.interaction.InteractionModel;
import assistant.discord.interaction.MessengerI;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * @author Alfredo
 */
public class WelcomeMessenger extends InteractionModel implements MessengerI {

	public WelcomeMessenger() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageReceived(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}
}
