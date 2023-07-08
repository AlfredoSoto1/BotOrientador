package application.hosts.bot.commands;

import java.util.ArrayList;
import java.util.List;

import application.hosts.bot.adapters.ProgrammableAdapter;
import application.services.botOrientador.discord.BotServer;
import application.services.botOrientador.entry.BotEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author Alfredo
 *
 */
public class CommandManager extends ListenerAdapter implements ProgrammableAdapter {
	
	private List<CommandI> commands;
	
	/**
	 * 
	 */
	public CommandManager() {
		
	}
	
	/**
	 * Adds a command to the manager
	 * 
	 * @param command
	 */
	public void add(CommandI command) {
		commands.add(command);
	}

	@Override
	public void init() {
		this.commands = new ArrayList<>();
	}

	@Override
	public void dispose() {
		commands.clear();
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		// For all the servers the bot has joined,
		// prepare the slash commands required
		for(Guild server : event.getJDA().getGuilds()) {
			for(CommandI command : commands) {
				// Create the commands that will be visible
				// for the server the bot is connected currently
				server.upsertCommand(
						command.getCommandName(),
						command.getDescription()
						)
				.addOptions(command.getOptions())
				.queue();
				
				// Create a shutdown command for every server
				server.upsertCommand("end", "Shutdown").queue();
			}
		}
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		
		for(Role role : event.getMember().getRoles()) {
			if(event.getName().equals("end") && role.getName().equalsIgnoreCase(BotServer.DEVELOPER)) {
				event.reply("Disconnected").queue();
				
				BotEntry.isRunning = false;
				return;
			}
		}
		
		
		// Do linear search to find which command is currently being called
		for(CommandI command : commands)
			if(command.getCommandName().equals(event.getName())) {
				command.execute(event);
				return;
			}
	}
}
