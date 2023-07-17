/**
 * 
 */
package services.bot.orientador.commands.info;

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
public class HelpMenu implements CommandI {

	private List<OptionData> options;
	
	public HelpMenu() {
		this.options = new ArrayList<>();
		
	}
	
	@Override
	public void dispose() {
		options.clear();
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
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Lista de Commandos");

		prepareGeneralCommands(embedBuilder);
		
		prepareDepartmentCommands(embedBuilder);
		
		event.replyEmbeds(embedBuilder.build()).queue();
	}
	
	private void prepareGeneralCommands(EmbedBuilder embedBuilder) {
		embedBuilder.addField(
			":pushpin: Comandos generales", "", false);
		
		embedBuilder.addField(
			"1) ``/reglas``", "Provee las reglas del servidor:scroll:", true);
		
		embedBuilder.addField(
			"2) ``/help``", "Muestra una lista de comandos disponibles:mag:", true);
		
		embedBuilder.addField(
			"3) ``/map``",
			"Provee un enlace a el Mapa de UPRM:map:", true);
		
		embedBuilder.addField(
			"4) ``/links``",
			"Provee un PDF con todos los links importantes del UPRM:link::link:", true);

		embedBuilder.addField(
			"5) ``/calendario``",
			"""
			Provee un enlace rapido al Calendario Académico de UPRM.:calendar_spiral:
			""", true);
		embedBuilder.addField(
			"6) ``/made-web``",
			"""
			Provee el enlace para accesar a la página web de la consejera de **INEL/ICOM/INSO/CIIC**, Madeline Rodríguez:globe_with_meridians:
			""", true);
		
		embedBuilder.addField(
			"7) ``/guia-prepistica``", "Guia para prepas:straight_ruler:", true);
		
		embedBuilder.addField(
			"", "", false);
		
	}
	
	private void prepareDepartmentCommands(EmbedBuilder embedBuilder) {
		embedBuilder.addField(
			"""
			:pushpin: Comandos que proveen información acerca de las facilidades del RUM
			(*INEL, ICOM, INSO, CIIC*)
			""",
			"", false);
		
		embedBuilder.addField(
			"8) ``/salon <letra>``",
			"""
			Provee información sobre el edificio donde se puede encontrar ese salón.
			""", true);
		
		embedBuilder.addField(
			"9) ``/curriculo <departamento>``",
			"""
			Te proveera un PDF del curriculo del departamento que tú
			escojas **(INEL/ICOM/INSO/CIIC)**
			""", true);
		
		embedBuilder.addField(
			"10) ``/ls_projects <select-projects>``",
			"""
			Provee información sobre proyectos e investigaciones relacionadas a **INEL/ICOM/INSO/CIIC**
			""", false);
		
		embedBuilder.addField(
			"11) ``/estudiantes-orientadores <departamento>``",
			"""
			Provee una lista los usernames (@'s) de los Estudiantes Orientadores de ese DEPT. Puedes escoger entre: **INEL, ICOM, INSO o CIIC**
			""", true);
		
		embedBuilder.addField(
			"12) ``/ls_student_orgs <select-orgs>``",
			"""
			Provee información sobre organizaciones estudiantiles relacionadas a 
			**INEL/ICOM/INSO/CIIC**,
			*(IEEE/EMC/HKN/RAS_CSS/COMP_SOC/CAS/PES/WIE/ACM_CSE/CAHSI/SHPE/ALPHA_AST/EMB/PHOTONICS)*
			""", false);
		
		embedBuilder.addField(
			"13) ``/contact <who?>``",
			"""
			Mostrara una lista de todos los contactos que tengo disponible para ofrecerte. Ej:
			**Asesoría académica**,
			**Asistencia académica**,
			**DCSP**,
			**Decanato de Estudiantes**,
			**Departamento (INEL/ICOM/INSO/CIIC)**,
			**Guardia Universitaria**
			""", true);
	}
}
