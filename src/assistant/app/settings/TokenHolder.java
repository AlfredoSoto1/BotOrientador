/**
 * 
 */
package assistant.app.settings;

import java.util.List;

import assistant.discord.app.BotApplication;
import net.dv8tion.jda.api.JDABuilder;

/**
 * @author Alfredo
 */
public class TokenHolder {

	private final String token;
	private final TokenType type;
	
	protected TokenHolder(TokenType type, String token) {
		this.type = type;
		this.token = token;
	}
	
	public TokenType getType() {
		return type;
	}
	
	public boolean is(String testToken) {
		// Remove the Bearer provided by the header
		// on receiving the token from controller request
		return token.equals(testToken.replace("Bearer ", ""));
	}
	
	public static JDABuilder buildJDAFromToken(Class<?> botApp, TokenHolder botToken) {
		if(BotApplication.class.isAssignableFrom(botApp) && botToken.type == TokenType.DISCORD_TOKEN)
			return JDABuilder.createDefault(botToken.token);
		return null;
	}
	
	public static boolean authenticateREST(String token, List<TokenHolder> tokens) {
		TokenHolder restToken = tokens.stream()
				.filter(tkholder -> tkholder.getType() == TokenType.REST_TOKEN)
				.findFirst().get();
		
		// Returns true if failed authentication
		return (token == null || !restToken.is(token));
	}
}
