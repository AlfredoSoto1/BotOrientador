///**
// * 
// */
//package application.hosts.bot.logic;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//
///**
// * @author Alfredo
// *
// */
//public class ProfanityFilter implements MessageInterpreter {
//	
//	private static final String PROFANITIES_FILE_PATH = "assets/utils/keywords/profanities.txt";
//	
//	private interface RunnableCommand {
//		public void run(MessageReceivedEvent event, String message);
//	}
//	
//	/*
//	 * Private members
//	 */
//	
//	private Map<String, RunnableCommand> commands;
//	private Set<String> profanities;
//	
//	/**
//	 * 
//	 */
//	public ProfanityFilter() {
//		this.commands = new HashMap<>();
//		this.profanities = new HashSet<>();
//		
//		loadCommands();
//		
//		loadProfanityKeyWords();
//	}
//
//	/**
//	 * Free resources from 'this' instance object
//	 */
//	public void dispose() {
//		commands.clear();
//		profanities.clear();
//	}
//
//	@Override
//	public void validateMessage(MessageReceivedEvent event, String messageFromUser) {
//		
//		if(!profanities.contains(messageFromUser.toLowerCase()))
//			return; // exit method since no profanity was mentioned
//		
//		// Delete the message
//		event.getMessage().delete().queue();
//		
//		// send warning message
//		event.getChannel().sendMessage("No profanity words here in the server, please").queue();
//	}
//	
//	@Override
//	public void validateCommand(MessageReceivedEvent event, String message) {
//		
//		// obtain command
//		String command = message.split(" ")[0];
//		
//		// obtain matching command to run from the one
//		// user provided
//		RunnableCommand runnableCommand = commands.get(command);
//		
//		// if true, exit this method
//		if(runnableCommand == null)
//			return; 
//		
//		// obtain the profanity word / message apart from command
//		String profanityWord = message.replace(command + " ", "");
//		
//		// run the command
//		runnableCommand.run(event, profanityWord);
//	}
//	
//	private void loadCommands() {
//		
//		commands.put("!add-profanity", (event, message) -> {
//
//			if (message.contains(",")) {
//
//				message = message.replace(" ", "");
//
//				String[] words = message.split(",");
//				for (String word : words) {
//					// add profanity to set
//					profanities.add(word);
//				}
//			} else {
//				// add profanity to set
//				profanities.add(message);
//			}
//
//			// message
//			event.getChannel().sendMessage("I will remember the profanity word").queue();
//		});
//
//		commands.put("!tell-profanity", (event, message) -> {
//			String outMessage = "The profanities I know are:\n";
//			for (String word : profanities) {
//				outMessage += word + "\n";
//			}
//			event.getChannel().sendMessage(outMessage).queue();
//		});
//		
//		commands.put("!remove-profanity", (event, message) -> {
//			
//			if (message.contains(",")) {
//
//				message = message.replace(" ", "");
//
//				String[] words = message.split(",");
//				for (String word : words) {
//					// add profanity to set
//					profanities.remove(word);
//				}
//
//				event.getChannel().sendMessage("Successfully removed the all profanities you told me").queue();
//			} else {
//				// add profanity to set
//				profanities.remove(message);
//				event.getChannel().sendMessage("Successfully removed the profanity you told me").queue();
//			}
//		});
//	}
//	
//	/**
//	 * 
//	 */
//	private void loadProfanityKeyWords() {
//		
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(PROFANITIES_FILE_PATH));
//			
//			// read all lines from file
//			String line;
//			while ((line = reader.readLine()) != null)
//				profanities.add(line);
//			
//			// free resources after use
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
