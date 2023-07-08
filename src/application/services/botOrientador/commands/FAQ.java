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
public class FAQ implements CommandI {

	private List<OptionData> options;
	
	public FAQ() {
		this.options = new ArrayList<>();

	}
	
	@Override
	public String getCommandName() {
		return "faq";
	}

	@Override
	public String getDescription() {
		return "Frequently Asked Questions por Prepas";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Frequently Asked Questions");
		embedBuider.setDescription("Aquí puedes encontrar ciertas preguntas que pueden surgir durante la semana");

		embedBuider.addField(
			"Preguntas acerca del Discord", "", false);
		
		embedBuider.addField(
			"""
			1) ¿Cómo puedo adiestrarme mejor en Discord?
			No entiendo muchas cosas sobre la plataforma
			""",
			"""
			Puede leer el PDF que se le proveyó o comunicarse con algunos de los Bot-Developers
			""", true);
		
		embedBuider.addField(
			"2) ¿Por qué no puedo ver los canales de otros grupos?",
			"""
			Se diseño de esta manera para tener una comunicación efectiva entre los integrantes de cada grupo individual y sus lideres
			""", true);
		
		embedBuider.addField(
			"3) Tengo una idea para el bot. ¿Con quién me puedo contactar?",
			"Con los Bot-Developers o los Estudiantes Orientadores", true);
		
		embedBuider.addField(
			"4) ¿Cómo consigo los comandos para el bot?",
			"Escribe ``/help`` en el chat de su preferencia", true);
		
		embedBuider.addField(
			"5) ¿Cómo crearon el bot?",
			"""
			El bot fue programado inicialmente por nuestros Ex-alumnos Fernando y Gabriel con el lenguaje Python.
			Pero para esta nueva versión con la que estas interactuando está programado en Java!! Cualquier pregunta
			le pueden escribir a Alfredo o a cualquier Bot-Developer. Ellos te pueden explicar y enseñar algunos
			detalles de como se hizo si estas interesad@!
			""", false);
		
		embedBuider.addField(
			"", "", false);
		
		embedBuider.addField(
			"Preguntas acerca del departamento, facilidades y académico", "", false);
		
		embedBuider.addField(
			"6) Me siento indeciso sobre mi departamento. ¿Con quién hablo?",
			"""
			Hable con su asesor académico de su departamento el día de ajustes de matrícula, con uno de sus líderes o con Madeline Rodríguez
			""", true);
		embedBuider.addField(
			"7) Tengo unas clases convalidadas por el CEEB ¿Con quién hablo?",
			"""
			Atienda bien a las orientaciones de la semana en como se convalidan esos cursos, sino hable con su departamento en ajustes
			""", true);
		embedBuider.addField(
			"8) ¿A quién voy para conseguir mi curriculo?",
			"""
			Preguntale al bot! El te lo provee en PDF si escribes ``/curriculo``
			""", true);
	
		embedBuider.addField(
			"""
			9) ¿Qué significa la letra al lado de la sección de una clase?
			""",
			"""
			D - a distancia, sin horario fijo, E - a distancia, con horario fijo, H - híbrida
			""", true);
		embedBuider.addField(
			"""
			10) ¿Cuántos créditos necesito?
			""",
			"""
			Para ser estudiante regular se necesita un mínimo de 12 créditos
			""", true);
		embedBuider.addField(
			"""
			11) ¿Qué es Moodle?
			""",
			"""
			Moodle es la plataforma principal para clases virtuales (https://online.upr.edu)
			""", true);
		embedBuider.addField(
			"""
			12) ¿Dónde me puedo estacionar?
			""",
			"""
			Zoológico de Mayagüez, Palacio de Recreación y Deportes. Luego de las 4:30 se puede estacionar el cualquier lado
			""", true);
		embedBuider.addField(
			"""
			13) ¿Cuál es la clave del WIFI del Colegio?
			""",
			"""
			Colegio2019
			""", true);

		event.replyEmbeds(embedBuider.build()).queue();
	}
}
