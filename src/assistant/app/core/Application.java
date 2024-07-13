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

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import assistant.app.entry.AssistantAppEntry;
import assistant.app.settings.TokenHolder;
import assistant.database.DatabaseConnection;
import assistant.database.DatabaseCredentials;
import assistant.discord.app.ECEAssistant;

/**
 * @author Alfredo
 * 
 */
public abstract class Application {
	
	private static Application singleton;
	
	private DebugConfiguration configuration;
	
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
	public static void run(Application application, String[] args, DebugConfiguration configuration) {
		if(Application.singleton != null)
			throw new RuntimeException("Application already exists");
		Application.singleton = application;
		Application.singleton.configuration = configuration;

		// Initialize application
		Application.singleton.initialize(args);
	}
	
	public DatabaseConnection getDatabaseConnection() {
		return databaseConnection;
	}
	
	public ConfigurableApplicationContext getSpringContext() {
		return context;
	}
	
	private void initialize(String[] args) {
		// Create new spring application
		context = SpringApplication.run(AssistantAppEntry.class, args);
		
		// Create a new database connection
		databaseConnection = new DatabaseConnection(context.getBean("createDatabaseCredentials", DatabaseCredentials.class));
		
		if (configuration == DebugConfiguration.BOT_ENABLED)
			assistant = new ECEAssistant(context.getBean("createBotToken", TokenHolder.class));
		
		onStart();
		onShutdown();
	}
	
	private void onStart() {
		System.out.println("[Application] Initialized");
		onRestStart();
		onDatabaseStart();
		
		if (configuration == DebugConfiguration.BOT_ENABLED) {
			onBotStart();
			System.out.println("[Application] Started");
			assistant.start();
		}
	}
	
	private void onShutdown() {
		if (configuration != DebugConfiguration.BOT_ENABLED)
			return;
		System.out.println("[Application] Bot ended");
		onBotShutdown();
		onDatabaseShutdown();
		onRestShutdown();
		System.out.println("[Application] Shutting down");
		
		// Disconnect the database and exit the spring application
		databaseConnection.disconnect();
		SpringApplication.exit(context);
		System.out.println("[Application] Ended");
	}
}