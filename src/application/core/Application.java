/**
 * 
 */
package application.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alfredo
 *
 */
public final class Application {
	
	private static Application instance = null;
	
	public static final void create(RunnableApplication runnableApplication) {
		if(instance != null)
			throw new IllegalAccessError("Application has already initiated");
		// Create instance of application
		instance = new Application(runnableApplication);

		// Initiate application
		instance.runnableApplication.init();
	}
	
	public static final void start() {
		if(instance == null)
			throw new IllegalAccessError("There is no application to start");
		// Start running application
		instance.runnableApplication.run();
		
		// Free all the application data
		instance.runnableApplication.dispose();
		
		// Free the application
		instance.free();
	}
	
	public static final Application instance() {
		return instance;
	}
	
	/*
	 * 
	 * Private members
	 * 
	 */
	private List<ApplicationThread> localThreads;
	
	private RunnableApplication runnableApplication;
	
	private Application(RunnableApplication runnableApplication) {
		this.runnableApplication = runnableApplication;
		
		// Initiate private members
		this.localThreads = new ArrayList<>();
	}
	
	private void free() {
		// Free from memory all threads
		localThreads.clear();
	}

	
	/**
	 * Creates a thread for this application
	 * 
	 * @param dispatchedApp
	 */
	public void createThread(ApplicationThread dispatchedApp) {
		localThreads.add(dispatchedApp);
	}
	
	/**
	 * Build the threads created dispatched to application
	 * 
	 */
	public void build() {
		// Start the local threads
		for (ApplicationThread applicationThread : localThreads) {
			applicationThread.init();
			applicationThread.start();
		}
		
		// Join each thread and free the memory
		for (ApplicationThread applicationThread : localThreads) {
			try {
				applicationThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				// Always dispose thread
				applicationThread.dispose();
			}
		}
	}
}
