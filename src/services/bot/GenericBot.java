/**
 * 
 */
package services.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

/**
 * @author Alfredo
 *
 */
public class GenericBot {

	private JDA discordApi;
	private JDABuilder discordBuilder;
	private ListenerBuilder listenerBuilder;

	private volatile boolean isRunning;
	
	public GenericBot() {
		this.isRunning = false;
		this.listenerBuilder = new ListenerBuilder();
	}
	
	public ListenerBuilder getListener() {
		return listenerBuilder;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public void start(String token) {
		this.isRunning = true;
		
		// Create new Discord connection with token
		discordBuilder = JDABuilder.createDefault(token);
		// Set custom bot status
		discordBuilder.setStatus(OnlineStatus.ONLINE);
//		discordBuilder.setActivity(Activity.playing("Aquí para ayudar siempre!"));
		
		// Give the bot some permissions
		discordBuilder.enableIntents(
				GatewayIntent.GUILD_MEMBERS, 
				GatewayIntent.GUILD_MESSAGES,
				GatewayIntent.GUILD_PRESENCES,
				GatewayIntent.MESSAGE_CONTENT,
				GatewayIntent.GUILD_MODERATION,
				GatewayIntent.GUILD_MESSAGE_TYPING,
				GatewayIntent.GUILD_WEBHOOKS,
				GatewayIntent.GUILD_MESSAGE_REACTIONS,
				GatewayIntent.GUILD_EMOJIS_AND_STICKERS
		);
		
		// Cache all members from guild
		discordBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
		// Prepare the listeners the bot requires to function properly
		discordBuilder.addEventListeners(listenerBuilder);
		// Build the Discord bot
		this.discordApi = discordBuilder.build();
	}
	
	public void interrupt() {
		this.isRunning = false;
	}
	
	public void shutdown() {
		discordApi.shutdown();
	}
}
