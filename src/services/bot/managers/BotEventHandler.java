/**
 * 
 */
package services.bot.managers;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.events.session.ReadyEvent;

/**
 * @author Alfredo
 *
 */
public abstract class BotEventHandler {
	
	/**
	 * 
	 */
    private static final Map<String, Integer> methodCallCount = new HashMap<>();

    /**
     * 
     * @param instance
     */
    public static void registerInitEvent(BotEventHandler instance) {
        Class<? extends BotEventHandler> clazz = instance.getClass();
        methodCallCount.put("Init:" + clazz.hashCode(), methodCallCount.getOrDefault(clazz, 0) + 1);
    }
    
    public static void registerDisposeEvent(BotEventHandler instance) {
        Class<? extends BotEventHandler> clazz = instance.getClass();
        methodCallCount.put("Dispose:" + clazz.hashCode(), methodCallCount.getOrDefault(clazz, 0) + 1);
    }
    
    /**
     * 
     * @param clazz
     * @return
     */
    public static boolean validateEventInit(Class<? extends BotEventHandler> clazz) {
    	return methodCallCount.getOrDefault("Init:" + clazz.hashCode(), 0) == 0;
    }
    
    /**
     * 
     * @param clazz
     * @return
     */
    public static boolean validateEventDispose(Class<? extends BotEventHandler> clazz) {
    	return methodCallCount.getOrDefault("Dispose:" + clazz.hashCode(), 0) == 0;
    }
	
	/**
	 * @param event
	 */
	public abstract void init(ReadyEvent event);
	
	/**
	 * Disposes everything that composes the command
	 */
	public abstract void dispose();
	
	/**
	 * 
	 */
	public BotEventHandler() {
		
	}
}
