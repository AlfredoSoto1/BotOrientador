/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
