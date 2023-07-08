/**
 * 
 */
package application.hosts.bot.messages;

import java.util.ArrayList;
import java.util.List;

import application.hosts.bot.adapters.ProgrammableAdapter;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * @author Alfredo
 *
 */
public class MessageManager extends ListenerAdapter implements ProgrammableAdapter {
	
	private List<MessengerI> messengers;
	
	public MessageManager() {
		
	}
	
	public void add(MessengerI messenger) {
		messengers.add(messenger);
	}
	
	@Override
	public void init() {
		this.messengers = new ArrayList<>();
	}

	@Override
	public void dispose() {
		messengers.clear();
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		// Prepare a message for all members when bot is ready
		// at start-up
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		for(MessengerI messenger : messengers)
			messenger.memberJoin(event);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		// Ignore bot message
		if(event.getAuthor().isBot())
			return;
		
		for(MessengerI messenger : messengers)
			messenger.messageReceived(event);
	}
}
