/**
 * 
 */
package service.server.entry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import service.database.DatabaseConnection;
import service.database.DatabaseConnectionManager;
import service.server.core.Application;
import service.server.core.Configs;
import service.server.core.RegisterApplication;

/**
 * @author Alfredo
 * 
 * TODO: 
 * 
 * - Create WebApplication that can gather the necessary 
 *   data to insert into the database.
 *   
 * 
 * - finish the other commands that require data from db
 * 
 * - Complete the role selection display
 * - make the streamlit application for data insertion into the db
 * 
 */
@SpringBootApplication
@ComponentScan("service.rest")
//@PropertySource("classpath:main.resources/application.properties")
@RegisterApplication(name = "Discord Assistants", version = "v2024.2.SO4")
public class AssistantAppEntry extends Application {
	
	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		// Create a new assistant application to start running
//		Application.run(new AssistantAppEntry());
		
		SpringApplication.run(AssistantAppEntry.class, args);
		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		context.close();
    }

	@Override
	public void start() {
		DatabaseConnectionManager.instance().addDatabaseConnection(
			/**
			 *  Create a new database connection
			 *  
			 *  This is the database that the entire program will be based on.
			 *  Database location is not accessible anywhere inside the project.
			 */
			new DatabaseConnection(Configs.DB_CONNECTION, Configs.DB_DRIVER, Configs.databaseCredentials())
		);

		// Initiate the Discord Application
//		ECEAssistant BOT_ASSISTANT = new ECEAssistant(Configs.token());
//		BOT_ASSISTANT.start();
		
	}
	
	@Override
	public void shutdown() {
		DatabaseConnectionManager.dispose();
	}
}
