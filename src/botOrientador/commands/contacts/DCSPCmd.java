/**
 * 
 */
package botOrientador.commands.contacts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class DCSPCmd implements CommandI {
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	public DCSPCmd() {
		this.options = new ArrayList<>();
		
	}
	
	@Override
	public void init(ReadyEvent event) {
		
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
	public void dispose() {
		options.clear();
	}
	
	@Override
	public String getCommandName() {
		return "contact-dcsp";
	}

	@Override
	public String getDescription() {
		return "Información acerca de DCPS";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Información de Departamento de Consejería y Servicios Psicológicos (DCSP)");

		embedBuilder.addField(
			"Nombre del Dept.",
			"""
			Departamento de Consejería y Servicios Psicológicos(DCSP)
			""", false);
		
		embedBuilder.addField(
			"Descripción",
			"""
			El Departamento de Consejería y Servicios Psicológicos (DCSP) es
			una unidad docente, adscrita al Decanato de Estudiantes del
			Recinto Universitario de Mayagüez de la Universidad de Puerto Rico.
			""", true);
		
		embedBuilder.addField(
			"Servicios Provistos",
			"""
			• Consejería individual y grupal
			• Psicoterapia individual y grupal
			• Administración e interpretación de inventarios de intereses vocacionales
			• Consultoría
			• Grupos de Apoyo
			""", true);
		
		embedBuilder.addField("","", false);
		
		embedBuilder.addField(
			"Oficina",
			"""
			Centro de Estudiantes - Oficina 501 (5to piso)
			""", true);
		
		embedBuilder.addField(
			"Teléfono(s)",
			"""
			• (787) 265 3864
			""", true);

		embedBuilder.addField(
			"Extensión(es)",
			"""
			• Ext. 3863
			""", true);
		
		embedBuilder.addField(
			"Horas de Trabajo",
			"""
			Lunes – Viernes | 7:30 AM – 4:30 PM
			""", true);
		
		embedBuilder.addField(
				"Localización en Google Maps",
			"""
			https://goo.gl/maps/VQNoPR3qRsPoicJz8
			""", true);

		embedBuilder.addField(
			"Página oficial",
			"""
			https://www.uprm.edu/dcsp/
			""", true);
		
		embedBuilder.addField(
			"Contáctanos",
			"""
			https://www.uprm.edu/dcsp/contactanos/
			""", true);
		
		embedBuilder.addField(
			"Enlaces Rápidos",
			"""
			• Citas & Referidos: https://www.uprm.edu/dcsp/citas-referidos/
			• Manejo de Crisis: https://www.uprm.edu/dcsp/manejo-de-crisis-2/
			• Consejería Profesional: https://www.uprm.edu/dcsp/consejeria/
			• Servicios Psicológicos: https://www.uprm.edu/dcsp/servicios-psicologicos/
			""", false);

		embedBuilder.addField(
			"Fuera de horas laborables:",
			"""
			Nuestros profesionales de ayuda no están disponibles fuera de horas laborales.
			Si la situación es tal que no puede esperar a ser atendida el próximo día laboral,
			puedes usar los siguientes recursos en la comunidad que tienen líneas de ayuda 24 horas:
			""", false);

		embedBuilder.addField(
			"Líneas de Ayuda",
			"""
			• Línea PAS (Primera Ayuda Psicológica): 1-800-981-0023
			• Sistema 911
			• Suicide Prevention Life Line 1-888-628-9454
			• Centro de Ayuda a Víctimas de Violación 1-800-981-5721
			• Control de Envenenamiento 1-800-222-1222
			""", false);
	
		embedBuilder.addField(
			"Clínicas de Salud Mental",
			"""
			• Centro de Salud Conductual Menonita CIMA 1-800-981-1218
			• Clínicas Ambulatorias de APS (Vital) 787-641-9133
			• Hospital Metro Pavía- Salud Conductual 787-851-0833
			• Hospital Panamericano: 1-800-981-1218
			• Sistema San Juan Capestrano: 787-760-0222
			""", false);
		
		event.replyEmbeds(embedBuilder.build()).setEphemeral(event.isFromGuild()).queue();
	}

}
