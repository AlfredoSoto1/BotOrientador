/**
 * 
 */
package services.bot.orientador.commands.contacts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.commands.CommandI;

/**
 * @author Alfredo
 *
 */
public class AsistenciaEconomica implements CommandI {
	
	private List<OptionData> options;
	
	public AsistenciaEconomica() {
		this.options = new ArrayList<>();
		
	}
	
	@Override
	public void dispose() {
		options.clear();
	}
	
	@Override
	public String getCommandName() {
		return "asistencia-economica";
	}

	@Override
	public String getDescription() {
		return "Información acerca de la oficina de asistencia economica";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Info Asistencia Económica");

		embedBuilder.addField(
			"Nombre del Dept.",
			"""
			Oficina de Asistencia Económica
			""", false);
		
		embedBuilder.addField(
			"Oficina",
			"""
			Lobby Decanato de Estudiantes
			""", true);
		
		embedBuilder.addField(
			"Teléfono(s)",
			"""
			• (787) 832-4040
			""", true);

		embedBuilder.addField(
			"Extensión(es)",
			"""
			• Ext. 3863
			""", true);
		
		embedBuilder.addField(
			"Horas de Trabajo",
			"""
			Lunes a Viernes | 7:45 – 11:45 A.M. | 1:00 – 4:30 P.M.
			""", true);
		
		embedBuilder.addField(
			"Localización en Google Maps",
			"""
			https://goo.gl/maps/pyAjRnaKZ1gE99PA7
			""", true);
		
		embedBuilder.addField(
			"Fechas Importantes (Prestamos, Beca, etc.)",
			"""
			https://www.uprm.edu/asistenciaeconomica/fechas-importantes/
			""", true);
		
		event.replyEmbeds(embedBuilder.build()).queue();
	}
}
