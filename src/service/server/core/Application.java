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
		this.consoleLogRegistration();
		
		// Create new spring application
		context = SpringApplication.run(AssistantAppEntry.class, args);
		// Create a new database connection
		databaseConnection = new DatabaseConnection(context.getBean(DatabaseConfiguration.class));
		// Create new Assistant bot
		// TODO: you have to automate this to support multiple bots
		assistant = new ECEAssistant(context.getBean(BotConfiguration.class));
		
		System.out.println("[Application] Initialized");
		// Handle start of the application for customization
		onRestStart();
		onDatabaseStart();
		onBotStart();

		System.out.println("[Application] Started");
		
		// Start the bot
		assistant.start();

		System.out.println("[Application] Bot ended");
		
		// Shutdown everything
		onBotShutdown();
		onDatabaseShutdown();
		onRestShutdown();

		System.out.println("[Application] Shutting down");
		
		// Disconnect the database and exit the spring application
		databaseConnection.disconnect();
		SpringApplication.exit(context);

		System.out.println("[Application] Ended");
	}
	
	private void consoleLogRegistration() {
		if(!this.getClass().isAnnotationPresent(RegisterApplication.class))
			return;
		// Check application registration (Optional)
		RegisterApplication applicationRegistration = this.getClass().getAnnotation(RegisterApplication.class);
		System.out.println("[Application] " + applicationRegistration.name() + " | version: " + applicationRegistration.version());
	}
}
