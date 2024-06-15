/**
 * 
 */
package java.assistant.command.information;

import java.awt.Color;
import java.service.discord.interaction.CommandI;
import java.service.discord.interaction.InteractionModel;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class FAQCmd extends InteractionModel implements CommandI {

	private boolean isGlobal;
	
	public FAQCmd() {

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
		return "faq";
	}

	@Override
	public String getDescription() {
		return "Frequently Asked Questions por Prepas";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of(); // Nothing
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		// Mentioned Roles in embedded message
		Role bdeRole = event.getGuild().getRolesByName("BotDeveloper", true).get(0);
		Role esoRole = event.getGuild().getRolesByName("EstudianteOrientador", true).get(0);
		
		/*
		 * Embedded messages
		 */
		String faq_title = "Frequently Asked Questions";

		String dq_1_title = "1. ¿Cómo puedo adiestrarme mejor en Discord?";
		String dq_1_description = 
			"""
			Puede leer el PDF que se le proveyó o comunicarse con algunos de los %s.
			""";
		String dq_2_title = "2. ¿Por qué no puedo ver los canales de otros grupos?";
		String dq_2_description = 
			"""
			Se diseño de esta manera para tener una comunicación efectiva 
			entre los integrantes de cada grupo individual y sus lideres.
			""";
		String dq_3_title = "3. Tengo una idea para el bot. ¿Con quién me puedo contactar?";
		String dq_3_description = 
			"""
			Con los %s o los %s.
			""";
		String dq_4_title = "4. ¿Cómo consigo los comandos para el bot?";
		String dq_4_description = 
			"""
			Escribe `/help` en el chat de su preferencia.
			""";
		String dq_5_title = "5. ¿Cómo crearon el bot?";
		String dq_5_description = 
			"""
			El bot fue programado inicialmente por nuestros Ex-alumnos Fernando y Gabriel 
			con el lenguaje Python. Pero para esta nueva versión con la que estas interactuando 
			está programado en Java!! Cualquier pregunta le pueden escribir a Alfredo o a cualquier 
			%s. Ellos te pueden explicar y enseñar algunos detalles de como se hizo si estas interesad@!
			""";
		String dq_6_title = "6. Me siento indeciso sobre mi departamento. ¿Con quién hablo?";
		String dq_6_description = 
			"""
			Hable con su asesor académico de su departamento el día de ajustes de matrícula, 
			con uno de sus líderes o con Madeline Rodríguez.
			""";
		String dq_7_title = "7. Tengo unas clases convalidadas por el CEEB ¿Con quién hablo?";
		String dq_7_description = 
			"""
			Atienda bien a las orientaciones de la semana en como se convalidan esos cursos, 
			sino hable con su departamento en ajustes.
			""";
		String dq_8_title = "8. ¿A quién voy para conseguir mi curriculo?";
		String dq_8_description = 
			"""
			Preguntale al bot! El te lo provee en PDF si escribes `/curriculo`.
			""";
		String dq_9_title = "9. ¿Qué significa la letra al lado de la sección de una clase?";
		String dq_9_description = 
			"""
			D - a distancia, sin horario fijo
			E - a distancia, con horario fijo
			H - híbrida
			""";
		String dq_10_title = "10. ¿Cuántos créditos necesito?";
		String dq_10_description = 
			"""
			Para ser estudiante regular se necesita un mínimo de 12 créditos.
			""";
		String dq_11_title = "11. ¿Qué es Moodle?";
		String dq_11_description = 
			"""
			Moodle es la plataforma principal para clases virtuales :link: [Moodle](https://online.upr.edu)
			""";
		String dq_12_title = "12. ¿Dónde me puedo estacionar?";
		String dq_12_description = 
			"""
			Zoológico de Mayagüez, Palacio de Recreación y Deportes. 
			Luego de las 4:30 se puede estacionar el cualquier lado.
			""";
		String dq_13_title = "13. ¿Cuál es la clave del WIFI del Colegio?";
		String dq_13_description = 
			"""
			Colegio2019
			""";
		
		dq_1_description = String.format(dq_1_description, bdeRole.getAsMention());
		dq_3_description = String.format(dq_3_description, bdeRole.getAsMention(), esoRole.getAsMention());
		dq_5_description = String.format(dq_5_description, bdeRole.getAsMention());
		
		
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(40, 130, 138));
		embedBuilder.setTitle(faq_title);

		embedBuilder.addField(dq_1_title, dq_1_description, true);
		embedBuilder.addField(dq_2_title, dq_2_description, true);
		
		embedBuilder.addBlankField(false);

		embedBuilder.addField(dq_3_title, dq_3_description, true);
		embedBuilder.addField(dq_4_title, dq_4_description, true);
		
		embedBuilder.addBlankField(false);

		embedBuilder.addField(dq_5_title, dq_5_description, true);
		embedBuilder.addField(dq_6_title, dq_6_description, true);
		
		embedBuilder.addBlankField(false);
		
		embedBuilder.addField(dq_7_title, dq_7_description, true);
		embedBuilder.addField(dq_8_title, dq_8_description, true);
		
		embedBuilder.addBlankField(false);
		
		embedBuilder.addField(dq_9_title, dq_9_description, true);
		embedBuilder.addField(dq_10_title, dq_10_description, true);
		
		embedBuilder.addBlankField(false);
		
		embedBuilder.addField(dq_11_title, dq_11_description, true);
		embedBuilder.addField(dq_12_title, dq_12_description, true);
		
		embedBuilder.addBlankField(false);

		embedBuilder.addField(dq_13_title, dq_13_description, true);

		event.replyEmbeds(embedBuilder.build()).setEphemeral(event.isFromGuild()).queue();
	}
}
