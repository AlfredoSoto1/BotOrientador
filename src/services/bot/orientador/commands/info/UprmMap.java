/**
 * 
 */
package services.bot.orientador.commands.info;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.managers.command.CommandI;

/**
 * @author Alfredo
 *
 */
public class UprmMap implements CommandI {

	private List<OptionData> options;
	
	public UprmMap() {
		this.options = new ArrayList<>();
		
	}
	
	@Override
	public void dispose() {
		options.clear();
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
