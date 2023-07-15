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
import services.bot.managers.command.CommandI;

/**
 * @author Alfredo
 *
 */
public class DecanatoDeEstudiantes implements CommandI {
	
	private List<OptionData> options;
	
	public DecanatoDeEstudiantes() {
		this.options = new ArrayList<>();
	}
	
	@Override
	public void dispose() {
		options.clear();
	}
	
	@Override
	public String getCommandName() {
		return "decanato-estudiantes";
	}

	@Override
	public String getDescription() {
		return "Información acerca del Decanato de estudiantes";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Información de Departamento del Decanato de Estudiantes");

		embedBuilder.addField(
			"Nombre del Dept.",
			"""
			Oficina Virtual Del Decano De Estudiantes
			""", false);
		
		embedBuilder.addField(
			"Descripción",
			"""
			Proveer al estudiante los recursos y servicios necesarios para
			contribuir a su desarrollo físico, social, emocional, cultural,
			educativo y ocupacional-profesional, como complemento a su
			formación intelectual, académica y ética.
			""", true);
		
		embedBuilder.addField(
			"Servicios Provistos",
			"""
			• Acomodo Razonable
			• Recomendación Del Decano
			• Apelación De Beca
			• Reglamento De Estudiantes Del Rum
			• Certificación De Progreso Académico
			• Protocolo De Excusas
			""", true);
		
		embedBuilder.addField("","", false);
		
		embedBuilder.addField(
			"Oficina",
			"""
			Edificio De Decanato de Estudiantes - Primer piso
			""", true);
		
		embedBuilder.addField(
			"Teléfono(s)",
			"""
			• (787) 265-3862
			""", true);

		embedBuilder.addField(
			"Horas de Trabajo",
			"""
			Lunes – Viernes | 7:45AM - 4:30PM
			""", true);
		
		embedBuilder.addField("","", false);
		
		embedBuilder.addField(
			"Localización en Google Maps",
			"""
			https://goo.gl/maps/4o7eRkwZu6yoUhVv9
			""", true);

		embedBuilder.addField(
			"Página oficial",
			"""
			https://www.uprm.edu/decestu/
			""", true);
		
		embedBuilder.addField(
			"Rédes Sociales",
			"""
			• Twitter - @EstudiantesUPRM | https://twitter.com/EstudiantesUPRM?lang=en
			• Youtube - Prensa RUM | https://www.youtube.com/user/videocolegio
			• Facebook - Decanato De Estudiantes Uprm | https://www.facebook.com/decano.estudiantes/
			""", false);

		event.replyEmbeds(embedBuilder.build()).queue();
	}
}
