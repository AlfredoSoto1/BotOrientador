/**
 * 
 */
package ece.assistant.cmd.contacts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.managers.BotEventHandler;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class AcademicAdvisoryCmd extends BotEventHandler implements CommandI {

	private static final String COMMAND_LABEL = "department";
	
	private static final String OPTION_SELECTED_INEL_ICOM = "INEL/ICOM";
	private static final String OPTION_SELECTED_INSO_CIIC = "INSO/CIIC";
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	public AcademicAdvisoryCmd() {
		this.options = new ArrayList<>();
		
		options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "Escoje un departamento", true)
				.addChoice("INEL/ICOM - Department", OPTION_SELECTED_INEL_ICOM)
				.addChoice("INSO/CIIC - Department", OPTION_SELECTED_INSO_CIIC)
		);
		
	}
	
	@Override
	@Deprecated
	public void init(ReadyEvent event) {
	
	}
	
	@Override
	public void dispose() {
		if(!BotEventHandler.validateEventDispose(this.getClass()))
			return;
		
		options.clear();
		
		BotEventHandler.registerDisposeEvent(this);
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
		return "contact-asesoria-academica";
	}

	@Override
	public String getDescription() {
		return "Información y contactos de consejería y asesoría academica para *INEL/ICOM/INSO/CIIC*";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {

		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		EmbedBuilder embedBuilder = null;
		
		switch(programOption.getAsString()) {
		case OPTION_SELECTED_INEL_ICOM: embedBuilder = createINELICOM_Embed(); 
			break;
		case OPTION_SELECTED_INSO_CIIC: embedBuilder = createINSOCIIC_Embed(); 
			break;
		default:
			event.reply("Departamento incorrecto, intenta de nuevo").queue();
			return;
		}
		
		event.replyEmbeds(embedBuilder.build()).setEphemeral(event.isFromGuild()).queue();
	}
	
	private EmbedBuilder createINELICOM_Embed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Asesoria Académica del Departamento de INEL/ICOM");

		embedBuilder.addField(
			"Servicio", "**INEL/ICOM**", false);
		
		embedBuilder.addField(
				"Descripción",
			"""
			Ayudar y dirigir al estudiante en la planificación de su programa académico,
			para que cumpla con los requisitos de graduación y pueda así, completar su grado académico.		
			""", true);
		
		embedBuilder.addField(
			"Servicios Provistos",
			"""
			Asesoría y evaluación semestral o anual a los estudiantes en procesos de:
			__matrícula, admisión, reclasificación, traslado, permiso especial, readmisión,
			transferencias, baja parcial, baja total, bajo aprovechamiento
			académico (probatorias, suspensiones) y graduación.__
			""", true);
		
		embedBuilder.addField(
				"", "", false);
		
		embedBuilder.addField(
			"Oficina", "Stefani Building - Office S224", true);
		
		embedBuilder.addField(
			"Teléfono(s)",
			"""
			• (787) 832-4040
			• FAX: (787) 831-7564
			""", true);
		
		embedBuilder.addField(
			"Extensión(es)",
			"""
			• Ext. 3182
			""", true);
		
		embedBuilder.addField(
			"Horas de Trabajo",
			"""
			Lunes-Viernes | 7:30 AM - 11:30 AM & 1:30 PM - 4:30 PM
			""", true);
		embedBuilder.addField(
				"Localización en Google Maps",
			"""
			https://goo.gl/maps/Jb43w1iy2VfjMeSR6
			""", true);

		embedBuilder.addField(
			"Sistema de reserva de citas ECE",
			"""
			https://appointments.ece.uprm.edu/
			""", true);

		embedBuilder.addField("","", false);
		
		embedBuilder.addField(
			"Folletos Informativos",
			"""
			• Español - https://ece.uprm.edu/wp-content/uploads/Brochure-INEL-ICOM-Spanish-Rev.-2015.pdf
			• English - https://ece.uprm.edu/wp-content/uploads/Brochure-INEL-ICOM-English-Rev.-2014.pdf
			""", true);
		
		embedBuilder.addField(
			"Más Información",
			"""
			https://www.uprm.edu/asuntosacademicos/orientacion-academica-y-consejeria-profesional/
			""", true);
		
		return embedBuilder;
	}

	private EmbedBuilder createINSOCIIC_Embed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Asesoria Académica del Departamento de INSO/CIIC");

		embedBuilder.addField(
				"Servicio", "**INSO/CIIC**", false);
			
		embedBuilder.addField(
			"Descripción",
			"""
			Ayudar y dirigir al estudiante en la planificación de su programa académico,
			para que cumpla con los requisitos de graduación y pueda así, completar su grado académico.	
			""", true);
		
		embedBuilder.addField(
			"Servicios Provistos",
			"""
			Asesoría y evaluación semestral o anual a los estudiantes en procesos de:
			__matrícula, admisión, reclasificación, traslado, permiso especial, readmisión,
			transferencias, baja parcial, baja total, bajo aprovechamiento académico (probatorias, suspensiones) y graduación.__
			""", true);
		
		embedBuilder.addField(
				"", "", false);
		
		embedBuilder.addField(
			"Oficina", "Stefani Building - Office S220", true);
		
		embedBuilder.addField(
			"Teléfono(s)",
			"""
			•(787) 832-4040
			""", true);
		
		embedBuilder.addField(
			"Extensión(es)",
			"""
			• Ext. 5597
			""", true);
		
		embedBuilder.addField(
			"Horas de Trabajo",
			"""
			Lunes-Viernes | 7:30 AM - 11:30 AM & 1:30 PM - 4:30 PM
			""", true);
		embedBuilder.addField(
				"Localización en Google Maps",
			"""
			https://goo.gl/maps/Jb43w1iy2VfjMeSR6
			""", true);

		embedBuilder.addField(
			"Cuando Puedo Ir al Departamento?",
			"""
			Cuando quieras! Siempre y cuando Celines o uno de los directores este para atenderte y no estén ocupados
			""", true);

		embedBuilder.addField(
			"CSE Dept. Website",
			"""
			Website - https://www.uprm.edu/cse/
			""", true);
		
		embedBuilder.addField(
			"Más Información",
			"""
			https://www.uprm.edu/asuntosacademicos/orientacion-academica-y-consejeria-profesional/
			""", true);
		
		return embedBuilder;
	}

}
