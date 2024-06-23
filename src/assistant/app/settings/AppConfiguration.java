/**
 * 
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
		return new TokenHolder(TokenType.DISCORD_TOKEN, "");
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
