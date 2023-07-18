/**
 * 
 */
package services.bot.managers;

import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.adapters.ComponentAdapter;

/**
 * @author Alfredo
 *
 */
public interface CommandI extends ComponentAdapter {
	
	/**
	 * Initiates the command
	 * 
	 * @param event
	 */
	public void init(ReadyEvent event);
	
	/**
	 * Disposes everything that composes the command
	 */
	public void dispose();
	
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
