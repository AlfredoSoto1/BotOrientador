/**
 * 
 */
package service.discord.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Alfredo
 */
@Component
public class BotConfiguration {
	
	@Value("${bot.token}")
	private String token;

	/**
	 * Returns the Bot token
	 * 
	 * @return token
	 */
	public String getToken() {
		return token;
	}
}
