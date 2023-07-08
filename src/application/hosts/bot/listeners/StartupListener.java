///**
// * 
// */
//package application.hosts.bot.listeners;
//
//import java.util.List;
//
//import application.hosts.bot.client.BotEntry;
//import application.hosts.bot.logic.WelcomingLogic;
//import net.dv8tion.jda.api.entities.Guild;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.User;
//import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
//import net.dv8tion.jda.api.events.session.ReadyEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//
///**
// * @author Alfredo
// *
// */
//public class StartupListener extends ListenerAdapter {
//
//	private WelcomingLogic welcomeLogic;
//	
//	public StartupListener() {
//		welcomeLogic = new WelcomingLogic();
//	}
//
//	public void runQueuedEvents() {
//		// Query all database related stuff here
//		// This is the place where it gets called
//		// safely without having weird results due to reading/writing collision
//		welcomeLogic.queryEvents();
//	}
//	
//	/*
//	 * 
//	 * FIXME THIS IS A TEST METHOD
//	 * 
//	 */
//	@Override
//	public void onReady(ReadyEvent event) {
//		Guild guild = event.getJDA().getGuilds().get(0);
//		List<Member> members = guild.getMembers();
//		for (Member member : members)
//			welcomeLogic.initiateUser(member.getUser());
//	}
//	
//	@Override
//	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
//		// Initiate user to Team Made's server
//		User user = event.getUser();
//		
//		// Initiate the user when joins for the first time
//		welcomeLogic.initiateUser(user);
//	}
//	
//	@Override
//	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
//		
//		// Temporal command
//		if(event.getAuthor().getName().equals("classicalfr")) {
//			if(event.getMessage().getContentRaw().equals("end")) {
//				BotEntry.isRunning = false;
//				return;
//			}
//		}
//		
//		// Prepares the user to be able to use the server
//		welcomeLogic.prepareNewMember(event);
//	}
//	
//}
