/**
 * 
 */
package services.bot.messages;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import services.bot.adapters.ProgrammableAdapter;

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
		for(MessengerI messenger : messengers)
			messenger.dispose();
		messengers.clear();
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		
		// Ignore if a bot enters the server
		if(event.getUser().isBot())
			return;
		
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
