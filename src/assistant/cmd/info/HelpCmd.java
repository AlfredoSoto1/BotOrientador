/**
 * 
 */
package assistant.cmd.info;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.interactions.CommandI;
import services.bot.interactions.InteractionModel;

/**
 * @author Alfredo
 *
 */
public class HelpCmd extends InteractionModel implements CommandI {
	
	private boolean isGlobal;

	public HelpCmd() {
		
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
		return "help";
	}

	@Override
	public String getDescription() {
		return "Menu de ayuda";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of();
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		// Mentioned Roles in embedded message
		Role esoRole = event.getGuild().getRolesByName("EstudianteOrientador", true).get(0);
		
		/*
		 * Embedded messages
		 */
		String help_title = "Lista de Comandos";
		
		String help_1_title = "1) `/help`";
		String help_1_description = 
			"""
			:mag:Muestra una lista de comandos disponibles.
			""";
		String help_2_title = "2) `/reglas`";
		String help_2_description = 
			"""
			:scroll:Provee las reglas del servidor.
			""";
		String help_3_title = "3) `/map`";
		String help_3_description = 
			"""
			:map:Provee un enlace a el Mapa de UPRM.
			""";
		String help_4_title = "4) `/links`";
		String help_4_description = 
			"""
			:link:Provee un PDF con todos los links importantes del UPRM.
			""";
		String help_5_title = "5. `/calendario`";
		String help_5_description = 
			"""
			:calendar_spiral:Provee un enlace rapido al Calendario Académico de UPRM.
			""";
		String help_6_title = "6) `/made-web`";
		String help_6_description = 
			"""
			:globe_with_meridians:Provee el enlace para accesar a la página web de la consejera de **INEL/ICOM**, Madeline Rodríguez
			""";
		String help_7_title = "7. `/guia-prepistica`";
		String help_7_description = 
			"""
			:straight_ruler:Guia para prepas.
			""";
		String help_8_title = "8. `/salon`";
		String help_8_description = 
			"""
			Provee información sobre el edificio donde se puede encontrar ese salón.
			""";
		String help_9_title = "9. `/curriculo`";
		String help_9_description = 
			"""
			Te proveera un PDF del curriculo de tu departamento.
			""";
		String help_10_title = "10. `/ls_projects'";
		String help_10_description = 
			"""
			Provee información sobre proyectos e investigaciones relacionadas a **INEL/ICOM/INSO/CIIC**
			""";
		String help_11_title = "11. `/estudiantes-orientadores`";
		String help_11_description = 
			"""
			Provee una lista los nombres de los %s de ese DEPT. 
			Puedes escoger entre: **INEL, ICOM, INSO o CIIC**
			""";
		String help_12_title = "12. `/ls_student_orgs`";
		String help_12_description = 
			"""
			Provee información sobre organizaciones estudiantiles relacionadas a 
			**INEL/ICOM/INSO/CIIC**,
			*(IEEE/EMC/HKN/RAS_CSS/COMP_SOC/CAS/PES/WIE/ACM_CSE/CAHSI/SHPE/ALPHA_AST/EMB/PHOTONICS)*
			""";
		String help_13_title = "13. `/contact`";
		String help_13_description = 
			"""
			Mostrara una lista de todos los contactos que tengo disponible para ofrecerte. Ej:
			**Asesoría académica**,
			**Asistencia académica**,
			**DCSP**,
			**Decanato de Estudiantes**,
			**Departamento (INEL/ICOM/INSO/CIIC)**,
			**Guardia Universitaria**
			""";
		
		help_11_description = String.format(help_11_description, esoRole.getAsMention());
		
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(40, 130, 138));
		embedBuilder.setTitle(help_title);

		embedBuilder.addField(help_1_title, help_1_description, true);
		embedBuilder.addField(help_2_title, help_2_description, true);
		
		embedBuilder.addBlankField(false);
		
		embedBuilder.addField(help_3_title, help_3_description, true);
		embedBuilder.addField(help_4_title, help_4_description, true);
		
		embedBuilder.addBlankField(false);
		
		embedBuilder.addField(help_5_title, help_5_description, true);
		embedBuilder.addField(help_6_title, help_6_description, true);
		
		embedBuilder.addBlankField(false);
		
		embedBuilder.addField(help_6_title, help_6_description, true);
		embedBuilder.addField(help_7_title, help_7_description, true);
		
		embedBuilder.addBlankField(false);
		
		embedBuilder.addField(help_8_title, help_8_description, true);
		embedBuilder.addField(help_9_title, help_9_description, true);
		
		embedBuilder.addBlankField(false);
		
		embedBuilder.addField(help_10_title, help_10_description, true);
		embedBuilder.addField(help_11_title, help_11_description, true);
		
		embedBuilder.addBlankField(false);
		
		embedBuilder.addField(help_12_title, help_12_description, true);
		embedBuilder.addField(help_13_title, help_13_description, true);
		
		event.replyEmbeds(embedBuilder.build()).queue();
	}
}
