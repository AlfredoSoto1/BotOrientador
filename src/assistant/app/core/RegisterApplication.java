/**
 * 
 */
package assistant.app.core;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Alfredo
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface RegisterApplication {
	/**
	 * @return Name of the application
	 */
	String name() default "Unregistered-Application";
	
	/**
	 * @return Current version of the application
	 */
	String version() default "v0.0.0";
}
