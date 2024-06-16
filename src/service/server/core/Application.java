/**
 * 
 */
package service.server.core;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import assistant.core.ECEAssistant;
import service.database.DatabaseConfiguration;
import service.database.DatabaseConnection;
import service.discord.core.BotConfiguration;
import service.server.entry.AssistantAppEntry;

/**
 * @author Alfredo
 * 
 */
public abstract class Application {
	
	private static Application singleton;
	
	private ECEAssistant assistant;
	private DatabaseConnection databaseConnection;
	private ConfigurableApplicationContext context;
	
	public abstract void onBotStart();
	public abstract void onRestStart();
	public abstract void onDatabaseStart();

	public abstract void onBotShutdown();
	public abstract void onRestShutdown();
	public abstract void onDatabaseShutdown();
	
	/**
	 * @return application singleton
	 */
	public static Application instance() {
		return singleton;
	}
	
	/**
	 * Starts running the given application
	 * 
	 * @param application
	 */
	public static void run(Application application, String[] args) {
		if(Application.singleton != null)
			throw new RuntimeException("Application already exists");
		Application.singleton = application;

		// Initialize application
		Application.singleton.initialize(args);
	}
	
	public DatabaseConnection getDatabaseConnection() {
		 return databaseConnection;
	}
	
	private void initialize(String[] args) {
		if(this.getClass().isAnnotationPresent(RegisterApplication.class)) {
			// Check application registration (Optional)
			RegisterApplication applicationRegistration = this.getClass().getAnnotation(RegisterApplication.class);
			System.out.println("[Application] : " + applicationRegistration.name() + " | version: " + applicationRegistration.version());
		}
		// Create new spring application
		context = SpringApplication.run(AssistantAppEntry.class, args);
		// Create a new database connection
		databaseConnection = new DatabaseConnection(context.getBean(DatabaseConfiguration.class));
		// Create new Assistant bot
		// TODO: you have to automate this to support multiple bots
		assistant = new ECEAssistant(context.getBean(BotConfiguration.class));
		
		onRestStart();
		onDatabaseStart();
		onBotStart();

		assistant.start();
		
		onBotShutdown();
		onDatabaseShutdown();
		onRestShutdown();
		
		// Disconnect the database and exit the spring application
		databaseConnection.disconnect();
		SpringApplication.exit(context);
	}
}
