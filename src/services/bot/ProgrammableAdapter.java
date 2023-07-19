/**
 * 
 */
package services.bot;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.session.ReadyEvent;

/**
 * @author Alfredo
 *
 */
public interface ProgrammableAdapter<E> {

	/**
	 * 
	 * @param event
	 */
	public void init(ReadyEvent event);
	
	/**
	 * 
	 */
	public void dispose();

	/**
	 * 
	 * @param e
	 */
	public void add(E e);
	
	/**
	 * 
	 * @param genericEvent
	 */
	public void onInteraction(Event genericEvent);
}
