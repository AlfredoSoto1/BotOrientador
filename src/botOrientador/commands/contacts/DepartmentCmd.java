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
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class DepartmentCmd implements CommandI {

	private static final String COMMAND_LABEL = "department";
	
	private static final String OPTION_SELECTED_INEL_ICOM = "INEL/ICOM";
	private static final String OPTION_SELECTED_INSO_CIIC = "INSO/CIIC";
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	public DepartmentCmd() {
		this.options = new ArrayList<>();
		
		options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "Escoje un departamento", true)
				.addChoice("INEL/ICOM - Department", OPTION_SELECTED_INEL_ICOM)
				.addChoice("INSO/CIIC - Department", OPTION_SELECTED_INSO_CIIC)
		);
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
		return "contact-department";
	}

	@Override
	public String getDescription() {
		return "Información acerca de los departamentos";
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
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Información del departamento de ECE");

		embedBuider.addField(
			"Nombre del Dept.", "Departamento de Ingeniería Electrica y de Computadoras", true);
		
		embedBuider.addField(
			"Descripción",
			"""
			Información, contactos y horario del departamento de INEL-ICOM		
			""", true);
		
		embedBuider.addField(
			"Servicios Provistos",
			"""
			Multiples servicios para estudiantes en el departamento de INEL-ICOM
			""", true);
		
		embedBuider.addField(
				"", "", false);
		
		embedBuider.addField(
			"Oficina", "Edificio Stefani - Oficina 125A", true);
		
		embedBuider.addField(
			"Teléfono(s)",
			"""
			• (787) 832-4040
			""", true);
		
		embedBuider.addField(
			"Extensión(es)",
			"""
			• Ext. 3086
			• Ext. 3821
			• Ext. 3090
			• Ext. 3094
			• Ext. 3121
			• Ext. 2170
			""", false);
		
		embedBuider.addField(
			"Horas de Trabajo",
			"""
			Lunes - Viernes | 7:30 AM - 11:30 AM & 1:30 PM - 4:30 PM
			""", true);
		embedBuider.addField(
				"Localización en Google Maps",
			"""
			https://goo.gl/maps/Jb43w1iy2VfjMeSR6
			""", true);
		
		return embedBuider;
	}

	private EmbedBuilder createINSOCIIC_Embed() {
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Información del departamento de CSE");

		embedBuider.addField(
			"Nombre del Dept.", "Departamento de Ingenieria de Software & Ciencias de la Computación", true);
			
		embedBuider.addField(
			"Descripción",
			"""
			Información, contactos y horario del departamento de INSO-CIIC
			""", true);
		
		embedBuider.addField(
			"Servicios Provistos",
			"""
			Multiples servicios para estudiantes en el departamento de INSO-CIIC
			""", true);
		
		embedBuider.addField(
			"", "", false);
		
		embedBuider.addField(
			"Oficina", "Edificio Stefani - Oficina S220", true);
		
		embedBuider.addField(
			"Teléfono(s)",
			"""
			• (787) 832-4040
			""", true);
		
		embedBuider.addField(
			"Extensión(es)",
			"""
			• Ext. 5864 (Acting Director – Dr. Pedro I. Rivera Vega)
			• Ext. 5864 (Associate Director – Dr. Manuel Rodriguez Martinez)
			• Ext. 5997 (Student Affairs Officer - Celines Alfaro Almeyda
			• Ext. 5864 & 6476 Administrative Officer – Sarah Ferrer)
			• Ext. 5864 (Administrative Secretary – Gedyeliz Zoe Valle)
			""", false);
		
		embedBuider.addField(
			"Horas de Trabajo",
			"""
			Lunes - Viernes | 7:30 AM - 11:30 AM & 1:30 PM - 4:30 PM
			""", true);
		embedBuider.addField(
			"Localización en Google Maps",
			"""
			https://goo.gl/maps/Jb43w1iy2VfjMeSR6
			""", true);
		
		return embedBuider;
	}

}
