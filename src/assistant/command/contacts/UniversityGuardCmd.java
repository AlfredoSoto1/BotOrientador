/**
 * 
 */
package assistant.command.contacts;

import java.awt.Color;
import java.util.List;

import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class UniversityGuardCmd extends InteractionModel implements CommandI {

	private boolean isGlobal;

	public UniversityGuardCmd() {
		
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
		return "contact-guardia-univ";
	}

	@Override
	public String getDescription() {
		return "Información acerca de la guardia universitaria";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Info Guardia Universitaria");

		embedBuilder.addField(
			"Nombre del Dept.", "Dpto. De Transito Y Vigilancia", false);
		
		embedBuilder.addField(
			"Descripción",
			"""
			El departamento se compone de la Sección de Vigilancia, Oficina de Retén,
			Oficina del Director y la Seccion de Trancito.
			""", true);
		
		embedBuilder.addField(
			"Servicios Provistos",
			"""
			Se destacan en: Orientación, escolta, vigilancia preventiva peatonal,
			ciclistas y en vehículos de motor, coordinar el tránsito en diferentes
			actividades, primeros auxilios, entre otros.
			""", true);
		
		embedBuilder.addField("", "", false);
		
		embedBuilder.addField(
			"Oficina", "Vagones al costado del Edificio del Dpto. de Enfermería", true);
		
		embedBuilder.addField(
			"Teléfono(s)",
			"""
			• (787) 832-4040
			""", true);
		
		embedBuilder.addField(
			"Extensión(es)",
			"""
			• Retén Exts. 3263,3620
			• Sección de Tránsito – Exts. 3275,3597
			• Oficina Director – Exts. 2462, 3538, 2458
			""", true);
		
		embedBuilder.addField(
			"Horas de Trabajo",
			"""
			Lunes a Viernes | 7:45 A.M. a 11:45 A.M. | 1:00 P.M. a 4:30 P.M.
			""", true);
		
		embedBuilder.addField(
			"Localización en Google Maps",
			"""
			https://goo.gl/maps/q1poMfAh7rthfDah8
			""", true);

		embedBuilder.addField(
			"Más Información Utíl",
			"""
			• Emergencias Médicas Municipal y Bomberos 787-834-8585 | Exts. 2061/2062
			• Línea Directa: 787-265-1785/787-265-3872
			• Policía Estatal 787-832-2020 (Linea Confidencial)/787-832-9696 (Comandancia Estatal)
			• Policía Municipal 787-834-8585 Ext. 2025
			""", false);

		embedBuilder.addField(
			"Enlaces Útiles",
			"""
			https://www.uprm.edu/transitoyvigilancia/
			""", true);
		
		event.replyEmbeds(embedBuilder.build()).setEphemeral(event.isFromGuild()).queue();
	}
}
