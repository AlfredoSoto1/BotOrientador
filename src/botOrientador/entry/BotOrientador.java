/**
 * 
 */
package botOrientador.entry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import application.core.ApplicationThread;
import services.bot.GenericBot;
import services.bot.ListenerBuilder;
import services.bot.controllers.ContactsControl;
import services.bot.controllers.FileUploadControl;
import services.bot.controllers.InfoControl;
import services.bot.controllers.LinksControl;
import services.bot.controllers.ManagementControl;

/**
 * @author Alfredo
 *
 */
public class BotOrientador extends ApplicationThread {

	public static volatile long upTime = 0L;
	
	private GenericBot discordBot;
	
	private InfoControl infoControl;
	private LinksControl linkControl;
	private ContactsControl contactsControl;
	private ManagementControl managementControl;
	private FileUploadControl fileUploadControl;
	
	public BotOrientador() {

	}

	/**
	 * 
	 */
	@Override
	public void init() {
		// Create a new generic bot. This bot will
		// be the BotOrientador
		this.discordBot = new GenericBot();
		
		// Load the token from the assets folder,
		// refer to documentation on GitHub on how-to
		// set up the bot token before running the software
		Optional<String> botToken = this.loadToken();
		
		if(botToken.isEmpty())
			return;
		
		// Prepare the listener adapters which is going to
		// handle all input from Discord server from any user, command, message, etc...
		ListenerBuilder botListener = this.discordBot.getListener();
		
		this.infoControl = new InfoControl();
		this.linkControl = new LinksControl();
		this.contactsControl = new ContactsControl();
		this.fileUploadControl = new FileUploadControl();
		this.managementControl = new ManagementControl(discordBot);

		botListener.addComponents(infoControl.getComponents());
		botListener.addComponents(linkControl.getComponents());
		botListener.addComponents(contactsControl.getComponents());
		botListener.addComponents(managementControl.getComponents());
		botListener.addComponents(fileUploadControl.getComponents());
		
		this.discordBot.start(botToken.get());
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		while(discordBot.isRunning()) {
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
		this.discordBot.shutdown();
		
		this.infoControl.dispose();
		this.linkControl.dispose();
		this.contactsControl.dispose();
		this.managementControl.dispose();
		this.fileUploadControl.dispose();
	}

	/**
	 * 
	 * @throws IOException
	 */
	private Optional<String> loadToken() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("assets/bot-token/bot-token.tkn"));
			
			StringBuilder tokenBuilder = new StringBuilder();
			
			String line = null;
			while((line = reader.readLine()) != null)
				tokenBuilder.append(line);
			reader.close();

			return Optional.of(tokenBuilder.toString());
		} catch(IOException e) {
			e.printStackTrace();
			
			return Optional.empty();
		}
	}
}
