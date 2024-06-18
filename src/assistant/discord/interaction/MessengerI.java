/**
 * 
 */
package assistant.discord.interaction;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * @author Alfredo
 *
 */
public interface MessengerI {

	/**
	 * 
	 * @param event
	 */
	public void memberJoin(GuildMemberJoinEvent event);
	
	/**
	 * 
	 * @param event
	 */
	public void messageReceived(MessageReceivedEvent event);
}
