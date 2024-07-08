/**
 * 
 */
package assistant.command.information;

import java.io.File;
import java.util.List;

import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

/**
 * @author Alfredo
 *
 */
public class CalendarCmd extends InteractionModel implements CommandI {

	private boolean isGlobal;
	
	public CalendarCmd() {

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
		return "calendario";
	}

	@Override
	public String getDescription() {
		return "Provee un enlace rápido al Calendario Académico de UPRM";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of();
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.reply(String.format(
			"""
			Hola %s
			Aquí adjunto el calendario académico de UPRM.
			**Calendario Académico:** https://www.uprm.edu/decestu/calendario/
			También puedes añadir este calendario a tu calendario personal.
			Presta atención a la esquina inferior derecha del calendario.
			se ve asi:
			""", event.getUser().getAsMention()))
		.addFiles(FileUpload.fromData(new File("assistant/images/google_add_calendar.png")))
			.setEphemeral(event.isFromGuild()).queue();
	}
}
