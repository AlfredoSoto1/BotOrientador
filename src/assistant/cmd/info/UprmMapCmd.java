/**
 * 
 */
package assistant.cmd.info;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.interactions.CommandI;
import services.bot.interactions.InteractionModel;

/**
 * @author Alfredo
 *
 */
public class UprmMapCmd extends InteractionModel implements CommandI {

	private boolean isGlobal;
	
	public UprmMapCmd() {
		
	}
	
	@Override
	public boolean isGlobal() {
		return isGlobal;
	}

	@Override
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	@Override
	public String getCommandName() {
		return "map";
	}

	@Override
	public String getDescription() {
		return "Provee un link al Mapa del UPRM";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Mapa del UPRM");

		embedBuider.addField(
			"Mapa:",
			"https://www.uprm.edu/portales/mapa/", false);
		
		event.replyEmbeds(embedBuider.build()).setEphemeral(event.isFromGuild()).queue();
	}

}
