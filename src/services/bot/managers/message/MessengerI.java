/**
 * 
 */
package services.bot.managers.message;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import services.bot.adapters.ComponentAdapter;

/**
 * @author Alfredo
 *
 */
public interface MessengerI extends ComponentAdapter {

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
