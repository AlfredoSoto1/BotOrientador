/**
 * 
 */
package services.bot.orientador.profanityControl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import application.database.DatabaseConnections;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import services.bot.managers.startup.StartupI;
import services.bot.messages.MessengerI;

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
	
	public ProfanityFilter() {
		this.badWords = new HashSet<>();
		
		this.random = new Random();
	}

	@Override
	public void onStartup(ReadyEvent event) {
		DatabaseConnections.instance()
		.getTeamMadeConnection()
		.joinConnection(()->{
			
			String ExtractWords_SQL = "SELECT ProfanityWord FROM Profanities";
			
			PreparedStatement stmt = DatabaseConnections.instance()
					.getTeamMadeConnection()
					.getConnection()
					.prepareStatement(ExtractWords_SQL);
			
			ResultSet result = stmt.executeQuery();
			
			while(result.next())
				badWords.add(result.getString(1));
			
			result.close();
			stmt.close();
		});
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
