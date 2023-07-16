/**
 * 
 */
package services.bot.orientador.commands.info;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import services.bot.managers.command.CommandI;

/**
 * @author Alfredo
 *
 */
public class Calendar implements CommandI {

	private File googleCalendar;
	
	private List<OptionData> options;
	
	public Calendar() {
		this.options = new ArrayList<>();
		
		googleCalendar = new File("assets/images/google_add_calendar.png");
	}
	
	@Override
	public void dispose() {
		options.clear();
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
