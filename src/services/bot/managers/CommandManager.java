package services.bot.managers;

import java.util.ArrayList;
import java.util.List;

import botOrientador.entry.BotConfigs;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import services.bot.adapters.ProgrammableAdapter;

/**
 * 
 * @author Alfredo
 *
 */
public class CommandManager implements ProgrammableAdapter<CommandI> {
	
	private List<CommandI> commands;
	
	public CommandManager() {
		this.commands = new ArrayList<>();
		
	}
	
	@Override
	public void init(ReadyEvent event) {
		
		// For all the servers the bot has joined,
		// prepare the slash commands required

//		List<Command> allCommands = event.getJDA().retrieveCommands().complete();
//        for (Command command : allCommands) {
//        	System.out.println(command.getName());
//        	command.delete().queue();
//        }
        
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
		
		for(CommandI command : commands)
			command.init(event);
	}

	@Override
	public void dispose() {
		for(CommandI command : commands)
			command.dispose();
		commands.clear();
	}
	
	@Override
	public void add(CommandI command) {
		commands.add(command);
	}

	@Override
	public void onInteraction(Event genericEvent) {
		SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) genericEvent;
		// FIXME: NOT WORKING ON PRIVATE CHANNELS

		for(Role role : event.getMember().getRoles()) {
			if(event.getName().equals("end") && role.getName().equalsIgnoreCase(BotConfigs.DEVELOPER_ROLE_NAME)) {
				event.reply("Disconnected").queue();
				
				// Disconnect bot and make this command a server-management
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
