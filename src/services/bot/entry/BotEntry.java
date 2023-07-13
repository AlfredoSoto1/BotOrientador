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
import services.bot.buttons.ButtonManager;
import services.bot.commands.CommandManager;
import services.bot.messages.MessageManager;
import services.bot.modals.ModalManager;
import services.bot.orientador.contacts.AsesoriaAcademica;
import services.bot.orientador.contacts.AsistenciaEconomica;
import services.bot.orientador.contacts.DCSP;
import services.bot.orientador.contacts.DecanatoDeEstudiantes;
import services.bot.orientador.contacts.Departamento;
import services.bot.orientador.contacts.GuardiaUniversitaria;
import services.bot.orientador.data.Curriculum;
import services.bot.orientador.data.GuiaPrepistica;
import services.bot.orientador.links.MadeWeb;
import services.bot.orientador.links.SuperLinks;
import services.bot.orientador.misc.Calendario;
import services.bot.orientador.misc.EstudiantesOrientadores;
import services.bot.orientador.misc.FAQ;
import services.bot.orientador.misc.Help;
import services.bot.orientador.misc.Map;
import services.bot.orientador.misc.Projects;
import services.bot.orientador.misc.Rules;
import services.bot.orientador.misc.Salon;
import services.bot.orientador.welcoming.LogInModal;
import services.bot.startup.StartupManager;

/**
 * @author Alfredo
 *
 */
public class BotEntry extends ApplicationThread {

	public static volatile boolean isRunning = true;
	public static volatile long upTime = 0L;
	
	// Discord bot token (Must be private for security reasons)
	private String token;
	
	private JDA discordJDA;
	private JDABuilder discordBuilder;
	
	// Declare default listener adapter manager
	private ListenerAdapterManager listenerAdapterManager;
	
	public BotEntry() {

	}

	@Override
	public void init() {
		try {
			loadToken();
		} catch(IOException e) {
			e.printStackTrace();
			isRunning = false;
			return;
		}
		
		this.listenerAdapterManager = new ListenerAdapterManager();
		
		this.listenerAdapterManager.createAdapters(this::createCustomAdapters);
		
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

	@Override
	public void run() {
		while(isRunning) {
			// Keep running
			upTime++;
		}
		System.out.println("Bot ended successfully Time:" + upTime);
	}

	@Override
	public void dispose() {
		// Shutdown the bot
		discordJDA.shutdown();
		
		// Free resources
		listenerAdapterManager.dispose();
	}

	private void createBotConnection() throws LoginException {
		// Create new Discord connection with token
		discordBuilder = JDABuilder.createDefault(token);
		
		// Set custom bot status
		discordBuilder.setStatus(OnlineStatus.ONLINE);
//		discordBuilder.setActivity(Activity.playing("Aquí para ayudar siempre!"));
	}

	private void prepareBotPermissions() throws LoginException {
		// Give the bot some permissions
		discordBuilder.enableIntents(
			GatewayIntent.GUILD_MESSAGES,
			GatewayIntent.GUILD_MESSAGE_TYPING,
			GatewayIntent.GUILD_MEMBERS, 
			GatewayIntent.GUILD_PRESENCES
		);
		
		// Provide to bot cache policy
		discordBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
	}
	
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
	
	private void createCustomAdapters(
			ModalManager modalManager,
			ButtonManager buttonManager,
			CommandManager commandManager,
			MessageManager messageManager,
			StartupManager startupManager) 
	{
		// Insert into the managers the elements

		LogInModal loginModal = new LogInModal();
		modalManager.add(loginModal);
		buttonManager.add(loginModal);
		messageManager.add(loginModal);
		commandManager.add(loginModal);
		startupManager.add(loginModal);
		
		commandManager.add(new Help());
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
		commandManager.add(new EstudiantesOrientadores());
	}

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
