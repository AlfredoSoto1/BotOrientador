/**
 * 
 */
package services.bot.entry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import application.core.ApplicationThread;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import services.bot.adapters.ListenerAdapterManager;
import services.bot.orientador.controllers.contacts.ContactsControl;
import services.bot.orientador.controllers.fileuploads.FileUploadControl;
import services.bot.orientador.controllers.info.InfoControl;
import services.bot.orientador.controllers.links.LinksControl;
import services.bot.orientador.controllers.login.WelcomeControl;
import services.bot.orientador.controllers.profanity.ProfanityControl;

/**
 * @author Alfredo
 *
 */
public class BotEntry extends ApplicationThread {

	public static volatile long upTime = 0L;
	public static volatile boolean isRunning = true;
	
	// Discord bot token (Must be private for security reasons)
	private String token;
	
	private JDA discordJDA;
	private JDABuilder discordBuilder;
	
	// Declare default listener adapter manager
	private ListenerAdapterManager listenerAdapterManager;
	
	private InfoControl infoControl;
	private LinksControl linkControl;
	private WelcomeControl welcomeManager;
	private ContactsControl contactsManager;
	private ProfanityControl profanityManager;
	private FileUploadControl fileUploadControl;
	
	public BotEntry() {

	}

	/**
	 * 
	 */
	@Override
	public void init() {
		
		// Obtain the token from assets folder
		// you must include a token inside the bot-token.tkn file
		// Otherwise it will throw an exception and program will not start.
		try {
			loadToken();
		} catch(IOException e) {
			e.printStackTrace();
			isRunning = false;
			return;
		}
		
		// Create and prepare the listener adapter which is going to
		// handle all input from Discord server from any user, command, message, etc...
		this.listenerAdapterManager = new ListenerAdapterManager();
		
		// Create and prepare the component adapters to be executed inside the
		// listener adapters properly
		this.infoControl = new InfoControl();
		this.linkControl = new LinksControl();
		this.welcomeManager = new WelcomeControl();
		this.contactsManager = new ContactsControl();
		this.profanityManager = new ProfanityControl();
		this.fileUploadControl = new FileUploadControl();

		// Load the components to the listener adapters
		this.listenerAdapterManager.loadComponentAdapters(infoControl.getComponents());
		this.listenerAdapterManager.loadComponentAdapters(linkControl.getComponents());
		this.listenerAdapterManager.loadComponentAdapters(welcomeManager.getComponents());
		this.listenerAdapterManager.loadComponentAdapters(contactsManager.getComponents());
		this.listenerAdapterManager.loadComponentAdapters(profanityManager.getComponents());
		this.listenerAdapterManager.loadComponentAdapters(fileUploadControl.getComponents());
		
		try {
			// Create Discord bot
			createBotConnection();
			
			// Prepare permissions
			prepareBotPermissions();
			
			// Start running the bot on the server
			startBot();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		while(isRunning) {
			// Keep running
			upTime++;
		}
		System.out.println("Bot ended successfully Time:" + upTime);
	}

	/**
	 * 
	 */
	@Override
	public void dispose() {
		// Shutdown the bot
		discordJDA.shutdown();
		
		// Free resources
		infoControl.dispose();
		linkControl.dispose();
		welcomeManager.dispose();
		contactsManager.dispose();
		profanityManager.dispose();
		fileUploadControl.dispose();
		
		listenerAdapterManager.dispose();
	}

	/**
	 * 
	 * @throws LoginException
	 */
	private void createBotConnection() throws LoginException {
		// Create new Discord connection with token
		discordBuilder = JDABuilder.createDefault(token);
		
		// Set custom bot status
		discordBuilder.setStatus(OnlineStatus.ONLINE);
//		discordBuilder.setActivity(Activity.playing("Aquí para ayudar siempre!"));
	}

	/**
	 * 
	 * @throws LoginException
	 */
	private void prepareBotPermissions() throws LoginException {
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
		
		// Provide to bot cache policy
		discordBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
	}
	
	/**
	 * 
	 * @throws LoginException
	 */
	private void startBot() throws LoginException {
		// Prepare all the listeners the bot requires
		// to function properly
		discordBuilder.addEventListeners(
			// Get all the adapters from the adapter's manager
			listenerAdapterManager.getAdapters()
		);
		
		// Build the Discord bot
		discordJDA = discordBuilder.build();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void loadToken() throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader("assets/bot-token/bot-token.tkn"));
		
		StringBuilder tokenBuilder = new StringBuilder();
		
		String line = null;
		while((line = reader.readLine()) != null)
			tokenBuilder.append(line);
		reader.close();

		this.token = tokenBuilder.toString();
	}
}
