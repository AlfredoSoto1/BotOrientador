/**
 * 
 */
package services.bot.orientador.controllers.profanity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import services.bot.dbaccess.DBProfanityManager;
import services.bot.managers.message.MessengerI;
import services.bot.managers.startup.StartupI;

/**
 * @author Alfredo
 *
 */
public class ProfanityFilter implements MessengerI, StartupI {

	private static final String[] warningMessages = {
			"@member dijo una profanidad, borré el mensaje...",
			"@member, kuidao con ese lenguaje.. no nos emocionemos de esa forma :innocent:",
			"weeepa kuidao... cojanlo suave, he borrado una profanidad",
			"""
			``The Bot has joined the conversation``
			:cloud_lightning: He llegado a salvarlos de una profanidad que dijo @member. :cloud_lightning: 	
			"""
	};
	
	private Random random;
	private Set<String> badWords;

	private DBProfanityManager profanityManager;
	
	public ProfanityFilter(DBProfanityManager profanityManager) {
		this.random = new Random();
		this.badWords = new HashSet<>();
		
		this.profanityManager = new DBProfanityManager();
	}

	@Override
	public void onStartup(ReadyEvent event) {
		badWords = profanityManager.pullProfanities();
	}
	
	@Override
	public void dispose() {
		badWords.clear();
	}
	
	@Override
	@Deprecated
	public void memberJoin(GuildMemberJoinEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void messageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot())
			return;
		
		String message = event.getMessage().getContentRaw();
		
		String[] words = message.split(" ");
		
		for(String word : words) {
			if(badWords.contains(word.toLowerCase())) {
				// contains bad word... remove the message and reply to the user
				
				String messageToSend = warningMessages[random.nextInt(0, warningMessages.length)]
						.replace("@member", event.getAuthor().getAsMention());
				
				event.getMessage().delete().queue();
				
				event.getMessage().reply(messageToSend).queue();
			}
		}
	}
}
