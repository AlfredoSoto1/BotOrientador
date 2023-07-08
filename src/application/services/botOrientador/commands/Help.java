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
public class Help implements CommandI {

	private List<OptionData> options;
	
	public Help() {
		this.options = new ArrayList<>();
		
	}
	
	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Menu de ayuda";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Lista de Commandos");

		embedBuider.addField(
			"""
			1) ``/help``
			""",
			"""
			Muestra la lista de comandos disponibles
			""", true);
		
		embedBuider.addField(
			"""
			2) ``/curriculo``:``dept``
			""",
			"""
			Te proveéra un PDF del curriculo del departamento que tú
			escojas **(INEL/ICOM/INSO/CIIC)**
			""", true);
		
		embedBuider.addField(
			"""
			3) ``/map``
			""",
			"""
			Provee un enlace a el Mapa de UPRM
			""", true);
		
		embedBuider.addField(
			"""
			4) ``/links``
			""",
			"""
			Provee un PDF con todos los links importantes del UPRM
			""", true);
		embedBuider.addField(
			"""
			5) ``/salon``:``#``
			""",
			"""
			Provee información sobre el edificio donde se puede encontrar ese salón.
			""", true);
		embedBuider.addField(
			"""
			6) ``/calendario``
			""",
			"""
			Provee un enlace rapido al Calendario Académico de UPRM.
			""", true);
		embedBuider.addField(
			"""
			7) ``/telephone_guide``
			""",
			"""
			Mostrara una lista de todos los teléfonos que tengo disponible para ofrecerte.
			""", true);
		embedBuider.addField(
			"""
			8) ``/ls_projects``
			""",
			"""
			Provee información sobre proyectos e investigaciones relacionadas a **INEL/ICOM/INSO/CIIC**
			""", true);
		embedBuider.addField(
			"""
			9) ``/estudiantes-orientadores``:``dep``
			""",
			"""
			Provee una lista los usernames (@'s) de los Estudiantes Orientadores de ese DEPT. Puedes escoger entre: **INEL, ICOM, INSO o CIIC**
			""", true);
		embedBuider.addField(
			"""
			10) ``/ls_student_orgs``
			""",
			"""
			Provee información sobre organizaciones estudiantiles relacionadas a 
			**INEL/ICOM/INSO/CIIC**,
			*(IEEE/EMC/HKN/RAS_CSS/COMP_SOC/CAS/PES/WIE/ACM_CSE/CAHSI/SHPE/ALPHA_AST/EMB/PHOTONICS)*
			""", true);
		embedBuider.addField(
			"""
			11) ``/reglas``
			""",
			"""
			Provee las reglas del servidor
			""", true);
		embedBuider.addField(
			"""
			13) ``/guia_prepistica``
			""",
			"""
			Guia para prepas
			""", true);
		embedBuider.addField(
			"""
			14) ``/made-web``
			""",
			"""
			Provee el enlace para accesar a la página web de la consejera de **INEL/ICOM/INSO/CIIC**, Madeline Rodríguez
			""", true);

		event.replyEmbeds(embedBuider.build()).queue();
	}
}
