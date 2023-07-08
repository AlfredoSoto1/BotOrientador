/**
 * 
 */
package application.services.botOrientador.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import application.hosts.bot.commands.CommandI;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

/**
 * @author Alfredo
 *
 */
public class Calendario implements CommandI {

	private File googleCalendar;
	
	private List<OptionData> options;
	
	public Calendario() {
		this.options = new ArrayList<>();
		
		googleCalendar = new File("assets/images/google_add_calendar.png");
	}
	
	@Override
	public String getCommandName() {
		return "calendario";
	}

	@Override
	public String getDescription() {
		return "Provee un enlace rápido al Calendario Académico de UPRM";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.reply(
			"""
			Hola """ + event.getUser().getAsMention() + """
			Aquí adjunto el calendario académico de UPRM.
			**Calendario Académico:** https://www.uprm.edu/decestu/calendario/
			También puedes añadir este calendario a tu calendario personal.
			Presta atención a la esquina inferior derecha del calendario.
			se ve asi:
			"""
		).queue();
		
		event.getChannel().sendFiles(FileUpload.fromData(googleCalendar)).queue();
	}
}
