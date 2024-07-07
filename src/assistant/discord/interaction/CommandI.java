/**
 * 
 */
package assistant.discord.interaction;

import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public interface CommandI {
	
	/**
	 * @return true if the command is global
	 */
	public boolean isGlobal();

	/**
	 * Changes the command status of being attached
	 * to a guild to now being global this means,
	 * that you can DM the bot and use such command.
	 * 
	 * @param isGlobal
	 */
	public void setGlobal(boolean isGlobal);
	
	/**
	 * @return name of the command
	 */
	public String getCommandName();
	
	/**
	 * @return description of the command
	 */
	public String getDescription();

	/**
	 * @return the options that the command has
	 */
	public List<OptionData> getOptions(Guild server);
	
	/**
	 * Executes the command
	 * 
	 * @param event
	 */
	public void execute(SlashCommandInteractionEvent event);
	
	default void upsertJDACommand(JDA jda) {
		// For global commands, they get inserted
		// directly into the JDA when it loads.
		// This action can take a long time to see
		// the command fully activated in the server.
		jda.upsertCommand(this.getCommandName(), this.getDescription())
			.addOptions(this.getOptions(null)).queue(
				success -> System.out.println(success),
				error   -> System.out.println(error.getLocalizedMessage()));
	}
	
	default void upsertGuildCommand(Guild server) {
		// For non global commands, they get
		// inserted directly into the server.
		// This has no delay when looking for the
		// command in the server where the bot is.
		server.upsertCommand(this.getCommandName(), this.getDescription())
			.addOptions(this.getOptions(server)).queue(
				success -> System.out.println(success),
				error   -> System.out.println(error.getLocalizedMessage()));
	}
}
