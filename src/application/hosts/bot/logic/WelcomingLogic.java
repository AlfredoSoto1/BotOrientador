///**
// * 
// */
//package application.hosts.bot.logic;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//
//import application.admin.accounts.UserRecord;
//import application.admin.remote.DatabaseConnections;
//import application.hosts.bot.messages.SpecialTokens;
//import application.hosts.bot.messages.WelcomeMessages;
//import net.dv8tion.jda.api.entities.User;
//import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//
///**
// * @author Alfredo
// *
// */
//public class WelcomingLogic {
//
//	private Map<User, Integer> interactedWithUsers;
//	private Map<String, UserRecord> loadedEmails;
//	private BlockingQueue<UserRecord> queuedUserRecords;
//	
//	public WelcomingLogic() {
//		this.loadedEmails = new HashMap<>();
//		this.interactedWithUsers = new HashMap<>();
//		this.queuedUserRecords = new LinkedBlockingQueue<>();
//		
//		// Load all Student Emails to memory
//		// This is because we don't want to run
//		// in parallel the events of writing/reading from
//		// database. Things might go weird
//		try {
//			loadStudentEmail();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void queryEvents() {
//		
//		if(queuedUserRecords.isEmpty())
//			return;
//		
//		// Update database with this user's record
//		try {
//			updateMembersDiscordUser(queuedUserRecords.poll());
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void initiateUser(User user) {
//		// If user is Bot, ignore him.
//		if (user.isBot())
//			return;
//
//		// First try checking if member has already joined server
//		try {
//			if (!isServerMember(user))
//				return;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		// Open a private channel and send the messages
//		user.openPrivateChannel().queue(channel -> {
//			// Prepare private DMS to set up the welcoming member
//			String message = WelcomeMessages.WELCOME_MESSAGE
//				.replaceAll(SpecialTokens.MEMBER_USER_NAME, channel.getUser().getAsMention());
//			
//			channel.sendMessage(message).queue();
//		});
//	}
//	
//	public void prepareNewMember(MessageReceivedEvent event) {
//		
//		// Get User from event of receiving a message
//		User user = event.getAuthor();
//		
//		// If the user who received the message
//		// is a Bot ignore him.
//		if(user.isBot())
//			return;
//		
//		// try to verify that the user that bot
//		// is going to prepare as a new member is not
//		// a member already
//		try {
//			if (!isServerMember(user))
//				return;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		PrivateChannel privateChannel = event.getChannel();
//		String message = event.getMessage().getContentRaw();
//		
//		// validate the email itself before processing
//		if(!isEmailValid(privateChannel, message)) {
//			if(interactedWithUsers.get(user) == null) {
//				interactedWithUsers.put(user, 0);
//				return;
//			}
//			interactedWithUsers.put(user, interactedWithUsers.get(user) + 1);
//			return;
//		}
//		
//		// validate if the email entered is in database
//		if(!isUPRMmember(privateChannel, message))
//			return;
//		
//		// Get record from user
//		UserRecord userRecord = loadedEmails.get(message);
//		
//		// Obtain the complete message presentation
//		String completePresentation = WelcomeMessages.PRESENTATION_MESSAGE;
//		
//		// Replace the reserved name tokens
//		completePresentation = completePresentation.replace(SpecialTokens.MEMBER_FIRST_NAME, userRecord.getFirstName());
//		completePresentation = completePresentation.replace(SpecialTokens.MEMBER_FULL_NAME, userRecord.getActualName());
//		
//		// Reply to the DM message
//		privateChannel.sendMessage(completePresentation).queue();
//		
//		// remove from interaction
//		interactedWithUsers.remove(user);
//		
//		// Store the discord user in record
//		userRecord.setDiscordUser(user.getName());
//		
//		// add to queue to later be updated
//		queuedUserRecords.add(userRecord);
//	}
//	
//	private boolean isUPRMmember(PrivateChannel privateChannel, String message) {
//		// If the email is not in database, tell the user to retry
//		if(loadedEmails.containsKey(message))
//			return true;
//	
//		privateChannel.sendMessage(WelcomeMessages.ERROR_MESSAGE_NOT_FOUND).queue();
//		return false;
//	}
//	
//	private boolean isEmailValid(PrivateChannel privateChannel, String message) {
//		
//		if(interactedWithUsers.get(privateChannel.getUser()) != null)
//			// Check if user has tried more than 3 times
//			if(interactedWithUsers.get(privateChannel.getUser()) > 3) {
//				// reset count
//				interactedWithUsers.put(privateChannel.getUser(), 0);
//				privateChannel.sendMessage(WelcomeMessages.ERROR_MESSAGE_RETRY).queue();
//				return false;
//			}
//		
//		if(message.contains(" ")) {
//			privateChannel.sendMessage(WelcomeMessages.ERROR_MESSAGE_NOSPACES).queue();
//			return false;
//		}
//		
//		if(message.contains("@uprm.edu")) {
//			privateChannel.sendMessage(WelcomeMessages.ERROR_MESSAGE_MUST_CONTAIN).queue();
//			return false;
//		}
//
//		if(message.contains("@uprm.com")) {
//			privateChannel.sendMessage(WelcomeMessages.ERROR_MESSAGE_MUST_END).queue();
//			return false;
//		}
//
//		if(message.contains("@gmail.com")) {
//			privateChannel.sendMessage(WelcomeMessages.ERROR_MESSAGE_NO_GMAIL).queue();
//			return false;
//		}
//
//		if(!message.contains("@upr.edu")) {
//			privateChannel.sendMessage(WelcomeMessages.ERROR_MESSAGE_MUST_CONTAIN).queue();
//			return false;
//		}
//		
//		return true;
//	}
//	
//	private boolean isServerMember(User user) throws SQLException {
//		// Connect to database
//		DatabaseConnections.instance().getTeamMadeConnection().connect();
//		
//		// Set SQL statement to be executed
//		String SQLStatemet = "SELECT DiscordUser FROM allMembers WHERE DiscordUser = ?";
//		
//		// Create a new Prepared Statement instance
//		PreparedStatement stmt = DatabaseConnections.instance()
//				.getTeamMadeConnection() 		// Get TeamMade's Database connection
//				.getConnection()				// Get raw connection
//				.prepareStatement(SQLStatemet); // Prepare the SQL statement
//		
//		// Prepare the values for the SQL statement
//		stmt.setString(1, user.getName());
//		
//		// Execute and process result set
//		ResultSet results = stmt.executeQuery();
//		
//		boolean hasJoined = false;
//		
//		if(results.next()) {
//			hasJoined = false;
//		} else {
//			hasJoined = true;
//		}
//		// Free resources
//		results.close();
//		stmt.close();
//
//		// Disconnect from database
//		DatabaseConnections.instance().getTeamMadeConnection().disconnect();
//		
//		// Return flag
//		return hasJoined;
//	}
//	
//	private void loadStudentEmail() throws SQLException {
//		// Connect to database
//		DatabaseConnections.instance().getTeamMadeConnection().connect();
//		
//		// Set SQL statement to be executed
//		String SQLStatemet = "SELECT MemberID, Email, ActualName, DiscordUser FROM allMembers";
//		
//		// Create a new Prepared Statement instance
//		PreparedStatement stmt = DatabaseConnections.instance()
//				.getTeamMadeConnection() 		// Get TeamMade's Database connection
//				.getConnection()				// Get raw connection
//				.prepareStatement(SQLStatemet); // Prepare the SQL statement
//		
//		// Execute and process result set
//		ResultSet results = stmt.executeQuery();
//		
//		// Store all E-mails from database
//		while(results.next()) {
//			
//			// Create a new User
//			UserRecord user = new UserRecord(
//				results.getInt(1),		// Member ID
//				results.getString(2),	// Email
//				results.getString(3),	// Actual name
//				results.getString(4)	// Discord User
//			);
//			
//			// put inside the map the user referenced to its email
//			loadedEmails.put(results.getString(2), user);
//		}
//		
//		// Free resources
//		results.close();
//		stmt.close();
//
//		// Disconnect from database
//		DatabaseConnections.instance().getTeamMadeConnection().disconnect();
//	}
//	
//	private void updateMembersDiscordUser(UserRecord userRecord) throws SQLException {
//		// Connect to database
//		DatabaseConnections.instance().getTeamMadeConnection().connect();
//
//		// Set SQL statement to be executed
//		String SQLStatemet = "UPDATE allMembers SET DiscordUser = ? WHERE Email = ?";
//
//		// Create a new Prepared Statement instance
//		PreparedStatement stmt = DatabaseConnections.instance()
//				.getTeamMadeConnection() 		// Get TeamMade's Database connection
//				.getConnection() 				// Get raw connection
//				.prepareStatement(SQLStatemet); // Prepare the SQL statement
//
//		// Set values for SQL prepared statement
//		stmt.setString(1, userRecord.getDiscordUser());
//		stmt.setString(2, userRecord.getEmail());
//		
//		stmt.execute();
//		
//		// Free resources
//		stmt.close();
//
//		// Disconnect from database
//		DatabaseConnections.instance().getTeamMadeConnection().disconnect();
//	}
//}
