//package application.hosts.bot.listeners;
//
//import java.util.List;
//
//import application.hosts.bot.logic.ProfanityFilter;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.Role;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//
///**
// * 
// * @author Alfredo
// *
// */
//public class BotCommandListener extends ListenerAdapter {
//	
//	private ProfanityFilter profanityFilter;
//	
//	/**
//	 * 
//	 */
//	public BotCommandListener() {
//		profanityFilter = new ProfanityFilter();
//	}
//	
//	@Override
//	public void onMessageReceived(MessageReceivedEvent event) {
//		if (event.getAuthor().isBot())
//			return; // Ignore messages from other bots
//		
//		String userMessage = event.getMessage().getContentRaw();
//		
//		// validate commands
//		profanityFilter.validateCommand(event, userMessage);
//
//		// validate profanity messages
//		profanityFilter.validateMessage(event, userMessage);
//		
//		
//		// Retrieve the member object associated with the user who sent the message
//        Member member = event.getMember();
//        
//        // Retrieve the list of roles for the member
//        List<Role> roles = member.getRoles();
//
//        // Print the names of the roles
//        for (Role role : roles) {
//            System.out.println(role.getName());
//        }
//	}
//}
