/**
 * 
 */
package assistant.command.games;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import assistant.app.core.Application;
import assistant.discord.interaction.InteractionModel;
import assistant.discord.interaction.MessengerI;
import assistant.rest.service.DiscordService;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;

/**
 * @author Alfredo
 */
public class ProfanityMessenger extends InteractionModel implements MessengerI {
	
	private static final String[] WARNING_MESSAGES = {
			"%s dijo una profanidad, borré el mensaje...",
			"%s, kuidao con ese lenguaje.. no nos emocionemos de esa forma :innocent:",
			"%s weeepa kuidao... cojanlo suave, he borrado una profanidad",
			"""
			``The Bot has joined the conversation``
			:cloud_lightning: He llegado a salvarlos de una profanidad que dijo %s. :cloud_lightning: 	
			"""
	};
	
	private Random random;
	private DiscordService service;
	
	private Set<String> profanities;
	
	public ProfanityMessenger() {
		this.profanities = new HashSet<>();
		this.random = new Random(System.currentTimeMillis());
		this.service = Application.instance().getSpringContext().getBean(DiscordService.class);
	}

	@Override
	public void onJDAInit(ReadyEvent event) {
		super.onJDAInit(event);
		// Load all the bad words to memory
		profanities.addAll(service.getProfanities());
	}
	
	@Override
	public List<Long> getMessageID() {
		return List.of();
	}

	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		// Do nothing
	}

	@Override
	public void messageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		TextChannel channel = event.getChannel().asTextChannel();
        
        for (String word : message.getContentRaw().split("\\W+")) {
            if (!profanities.contains(word.toLowerCase()))
            	continue;
            message.delete().queue(
            	success -> channel.sendMessageFormat(WARNING_MESSAGES[random.nextInt(0, WARNING_MESSAGES.length)], event.getAuthor().getAsMention()).queue());
        	break;
        }
	}

	@Override
	public void onMessageReaction(GenericMessageReactionEvent event) {
		// Do nothing
	}
	
}
