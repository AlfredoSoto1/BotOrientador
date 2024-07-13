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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import assistant.database.DatabaseCredentials;

/**
 * @author Alfredo
 */
@Component
@Configuration
@PropertySource("classpath:/config/application.properties")
public class AppConfiguration {
	
	@Value("${app.name}") 
	private String name;

	@Value("${app.version}") 
	private String version;

	@Value("${app.database.url}") 
	private String dbUrl;

	@Value("${app.database.username}") 
	private String dbUsername;

	@Value("${app.database.password}") 
	private String dbPassword;
	
	@Value("${app.database.driver}") 
	private String dbDriver;

	@Value("${app.discord.bot.token}") 
	private String botToken;

	@Value("${app.rest.controller.token}") 
	private String restToken;

	@Bean
	public TokenHolder createRestToken() {
		return new TokenHolder(TokenType.REST_TOKEN, restToken);
	}
	
	@Bean
	public TokenHolder createBotToken() {
		return new TokenHolder(TokenType.DISCORD_TOKEN, botToken);
	}

	@Bean
	public TokenHolder createDatabaseToken() {
		return new TokenHolder(TokenType.DATABASE_TOKEN, "");
	}
	
	@Bean
	public DatabaseCredentials createDatabaseCredentials() {
		return new DatabaseCredentials(dbUrl, dbUsername, dbPassword, dbDriver);
	}
}
