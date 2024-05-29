package services.bot.managers;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import services.bot.core.ProgrammableAdapter;

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
		
		// TODO: Validate this
		for(Guild server : event.getJDA().getGuilds()) {
			
			for(CommandI command : commands) {
				if(command.isGlobal()) {
					// Upsert the command if and only if the command is also global
					event.getJDA().upsertCommand(
						command.getCommandName(), 
						command.getDescription()
					)
					.addOptions(command.getOptions())
					.queue();
				} else {
					// Create the commands that will be visible
					// for the server the bot is connected currently
					server.upsertCommand(
						Commands.slash(command.getCommandName(), command.getDescription())
						.addOptions(command.getOptions())
					)
					.queue();
				}
			}
		}
		
		for(CommandI command : commands)
			if(command instanceof BotEventHandler handler)
				handler.init(event);
	}

	@Override
	public void dispose() {
		for(CommandI command : commands)
			if(command instanceof BotEventHandler handler)
				handler.dispose();
		commands.clear();
	}
	
	@Override
	public void add(CommandI command) {
		commands.add(command);
	}

	@Override
	public void onInteraction(Event genericEvent) {
		SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) genericEvent;
		// Do linear search to find which command is currently being called
		for(CommandI command : commands)
			if(command.getCommandName().equals(event.getName())) {
				command.execute(event);
				return;
			}
	}
}
