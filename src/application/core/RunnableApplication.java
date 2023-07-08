/**
 * 
 */
package application.core;

/**
 * @author Alfredo
 *
 */
public interface RunnableApplication {
	
	/**
	 * Initiates an* application
	 * 
	 */
	public void init();
	
	/**
	 * Runs the application
	 */
	public void run();
	
	/**
	 * Disposes and frees from memory
	 * the application
	 */
	public void dispose();
}
