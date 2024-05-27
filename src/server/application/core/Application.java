/**
 * 
 */
package server.application.core;

import server.application.annotations.RegisterApplication;

/**
 * @author Alfredo
 * 
 */
public abstract class Application {
	
	private static Application singleton;
	
	/**
	 * Initiates the application
	 */
	public abstract void init();

	/**
	 * Shuts down the application
	 * 
	 * This gets called manually or ends with unexpected behavior
	 */
	public abstract void shutdown();
	
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
	public static void run(Application application) {
		if(Application.singleton != null)
			throw new RuntimeException("Application already exists");
		Application.singleton = application;

		// Initialize application
		Application.singleton.initialize();
	}
	
	private void initialize() {
		// Print to the console the application
		if(this.getClass().isAnnotationPresent(RegisterApplication.class)) {
			RegisterApplication applicationRegistration = this.getClass().getAnnotation(RegisterApplication.class);
			System.out.println("[Application] : " + applicationRegistration.name() + " | version: " + applicationRegistration.version());
		}
		
		// Initiates the application content
		this.init();

		System.out.println("[Application] : STARTED");
	}
}
