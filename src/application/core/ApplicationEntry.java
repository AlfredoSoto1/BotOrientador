/**
 * 
 */
package application.core;

import application.database.DatabaseConnections;
import botOrientador.entry.BotOrientador;

/**
 * @author Alfredo
 *
 */
public class ApplicationEntry implements RunnableApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create Application
		Application.create(new ApplicationEntry());
		
		// Start Application
		Application.start();
	}

	@Override
	public void init() {
		
		// Create a new Bot entry
		Application.instance().createThread(new BotOrientador());
//		Application.instance().createThread(new AtendanceListCreator());
		
		// Initiate database connections
		DatabaseConnections.initiate();
	}

	@Override
	public void run() {
		// Build all sub application threads
		Application.instance().build();
	}

	@Override
	public void dispose() {
		// Dispose all database connections
		DatabaseConnections.dispose();
	}
}
