/**
 * 
 */
package assistant.app.settings;

import assistant.discord.app.BotApplication;

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
	
	public String obtainToken(BotApplication botApp) {
		if(botApp.getClass() == BotApplication.class && type == TokenType.DISCORD_TOKEN)
			return token;
		return null;
	}
	
	public boolean is(String testToken) {
		// Remove the Bearer provided by the header
		// on receiving the token from controller request
		return token.equals(testToken.replace("Bearer ", ""));
	}
}
