/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
