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
import services.bot.orientador.loginControl.WelcomingManager;
import services.bot.orientador.profanityControl.ProfanityManager;

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
	
	private WelcomingManager welcomeManager;
	private ProfanityManager profanityManager;
	
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
		this.welcomeManager = new WelcomingManager();
		this.profanityManager = new ProfanityManager();
		
		// Load the components to the listener adapters
		this.listenerAdapterManager.loadComponentAdapters(welcomeManager.getComponents());
		this.listenerAdapterManager.loadComponentAdapters(profanityManager.getComponents());
		
//		ProfanityFilter filter = new ProfanityFilter();
//		startupManager.add(filter);
//		messageManager.add(filter);
//		commandManager.add(filter);
		
//		commandManager.add(new Help());
//		commandManager.add(new Map());
//		commandManager.add(new FAQ());
//		commandManager.add(new Rules());
//		commandManager.add(new Salon());
//		commandManager.add(new MadeWeb());
//		commandManager.add(new Calendario());
//		commandManager.add(new SuperLinks());
//		commandManager.add(new Curriculum());
//		commandManager.add(new GuiaPrepistica());
//		commandManager.add(new DCSP());
//		commandManager.add(new Projects());
//		commandManager.add(new Departamento());
//		commandManager.add(new AsesoriaAcademica());
//		commandManager.add(new AsistenciaEconomica());
//		commandManager.add(new GuardiaUniversitaria());
//		commandManager.add(new DecanatoDeEstudiantes());
//		commandManager.add(new EstudiantesOrientadores());
		
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
		welcomeManager.dispose();
		
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
			GatewayIntent.GUILD_MESSAGES,
			GatewayIntent.GUILD_MESSAGE_TYPING,
			GatewayIntent.GUILD_MEMBERS, 
			GatewayIntent.GUILD_PRESENCES,
			GatewayIntent.MESSAGE_CONTENT
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
			tokenBuilder.append(line.replace(" ", "").replace("BOT_TOKEN=", ""));
		reader.close();

		this.token = tokenBuilder.toString();
	}
}
