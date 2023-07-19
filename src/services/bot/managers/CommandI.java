/**
 * 
 */
package services.bot.managers;

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
	 * Changes the command status of being
	 * attached to a guild to now being global
	 * @param isGlobal
	 */
	public void setGlobal(boolean isGlobal);
	
	/**
	 * Returns the name of the command
	 * 
	 * @return String name
	 */
	public String getCommandName();
	
	/**
	 * Returns the description of the command
	 * 
	 * @return String description
	 */
	public String getDescription();
	
	/**
	 * Returns the options attached to the command
	 * 
	 * @return List of options
	 */
	public List<OptionData> getOptions();
	
	/**
	 * Executes the command
	 * 
	 * @param event
	 */
	public void execute(SlashCommandInteractionEvent event);
	
}
