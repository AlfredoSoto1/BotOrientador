/**
 * 
 */
package application.services.botOrientador.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import application.hosts.bot.commands.CommandI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class Map implements CommandI {

	private List<OptionData> options;
	
	public Map() {
		this.options = new ArrayList<>();
		
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
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Mapa del UPRM");

		embedBuider.addField(
			"Mapa:",
			"https://www.uprm.edu/portales/mapa/", false);
		
		event.replyEmbeds(embedBuider.build()).queue();
	}
}
