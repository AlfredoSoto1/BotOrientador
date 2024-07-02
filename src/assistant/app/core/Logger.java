/**
 * 
 */
package assistant.app.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author alfredo
 *
 */
public class Logger {
	
	public enum LogFeedback {
		WARNING,
		SUCCESS,
		INFO,
		ERROR
	}
	
	private static final String LOG_CONFIG_PATH = "assistant/logs/";
	private static Logger instance;
	
	private String fileName;
	
	public static Logger instance() {
		if(instance == null)
			return instance = new Logger();
		return instance;
	}
	
	private Logger() {
        // Create the file name with the formatted date
        this.fileName = "logs.txt";
        
        // Create the file object
        File logFile = new File(LOG_CONFIG_PATH, fileName);
        
        try {
        	if(!logFile.getParentFile().exists()) {
        		logFile.getParentFile().mkdirs();
        	}
            // Create the log file
        	logFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating log file: " + e.getMessage());
            e.printStackTrace();
        }
	}
	
	public void logFile(LogFeedback logLevel, String fmessage, Object ...parameters) {
		File backupLogConfig = new File(LOG_CONFIG_PATH, this.fileName);
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(backupLogConfig, true));
			
			// Get the current time
	        LocalDateTime currentTime = LocalDateTime.now();
	        // Format the current time
	        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	        // Pad the log level to a fixed width
	        String paddedLevel = String.format("%-" + 10 + "s", "[" + logLevel + "]");
	        
	        // Format the message with parameters and include the current time
	        String formattedMessage = String.format("[%s] %s %s", formattedTime, paddedLevel, String.format(fmessage, parameters));
            
			// Write content to the file
			writer.write(formattedMessage);
			writer.newLine();
            writer.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
}
