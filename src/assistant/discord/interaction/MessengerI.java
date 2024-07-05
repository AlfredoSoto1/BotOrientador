/**
 * 
 */
package assistant.discord.interaction;

import java.util.List;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

/**
 * @author Alfredo
 *
 */
public interface MessengerI {

	/**
	 * @return Id of the messenger
	 */
	public List<Long> getMessageID();
	
	/**
	 * @param event
	 */
	public void memberJoin(GuildMemberJoinEvent event);
	
	/**
	 * @param event
	 */
	public void messageReceived(MessageReceivedEvent event);
	
	/**
	 * @param event
	 */
    public void onMessageReaction(GenericMessageReactionEvent event);
}
