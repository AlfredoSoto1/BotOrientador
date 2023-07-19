/**
 * 
 */
package services.bot.managers;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;

/**
 * @author Alfredo
 *
 */
public interface MessengerI {

	/**
	 * 
	 * @param event
	 */
	public void init(ReadyEvent event);
	
	/**
	 * Disposes all content from messenger
	 */
	public void dispose();
	
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
