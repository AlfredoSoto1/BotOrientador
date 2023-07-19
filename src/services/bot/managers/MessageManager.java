/**
 * 
 */
package services.bot.managers;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import services.bot.ProgrammableAdapter;

/**
 * @author Alfredo
 *
 */
public class MessageManager implements ProgrammableAdapter<MessengerI> {
	
	private List<MessengerI> messengers;
	
	public MessageManager() {
		this.messengers = new ArrayList<>();
	}
	
	@Override
	public void init(ReadyEvent event) {
		for(MessengerI messenger : messengers)
			if(messenger instanceof BotEventHandler handler)
				handler.init(event);
	}

	@Override
	public void dispose() {
		for(MessengerI messenger : messengers)
			if(messenger instanceof BotEventHandler handler)
				handler.dispose();
		messengers.clear();
	}

	@Override
	public void add(MessengerI messenger) {
		messengers.add(messenger);
	}
	
	@Override
	public void onInteraction(Event genericEvent) {
		
		MessageReceivedEvent event = (MessageReceivedEvent) genericEvent;
		
		// Ignore bot message
		if(event.getAuthor().isBot())
			return;
		for(MessengerI messenger : messengers)
			messenger.messageReceived(event);
	}
	
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		// Ignore if a bot enters the server
		if(event.getUser().isBot())
			return;
		for(MessengerI messenger : messengers)
			messenger.memberJoin(event);
	}
}
