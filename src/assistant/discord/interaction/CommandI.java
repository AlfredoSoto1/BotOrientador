/**
 * 
 */
package assistant.discord.interaction;

import java.util.List;

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
	public List<OptionData> getOptions();
	
	/**
	 * Executes the command
	 * 
	 * @param event
	 */
	public void execute(SlashCommandInteractionEvent event);
	
}
