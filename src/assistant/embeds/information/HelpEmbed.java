/**
 * 
 */
package assistant.embeds.information;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.Role;

/**
 * @author Alfredo
 */
public class HelpEmbed {
	
	public MessageEmbed buildHelp(Color color, String banner, Role esoRole, int page) {
		
		List<Field> fields = getFieldPage(esoRole.getAsMention());
		
		int fieldsPerPage = 6;
		int maxPage = fields.size() / fieldsPerPage;
		
		EmbedBuilder embed = new EmbedBuilder()
			.setImage(banner)
			.setColor(color)
			.setTitle("Lista de commandos")
			.setFooter(page + " of "+ maxPage);
		
		if (page > maxPage) {
			embed.addField("Oh no! 404", String.format(
				"""
				Hmm no creo que hallan demasiados commandos que enceñar,
				trata con un rango de páginas de [0-%s]
				""", maxPage), false);
			return embed.build();
		}
		
		for (int i = page * fieldsPerPage; i < (page + 1) * fieldsPerPage; i++) {
			if (i >= fields.size())
				continue;
			if (i % 2 == 0) {
				embed.addField(fields.get(i));
			}
			else {
				embed.addField(fields.get(i));
				embed.addBlankField(false);
			}
		}
		return embed.build();
	}
	
	public MessageEmbed buildHelpDM(String esoRole, int page) {
		
		List<Field> fields = getFieldPage(esoRole);
		
		int fieldsPerPage = 6;
		int maxPage = fields.size() / fieldsPerPage;
		
		EmbedBuilder embed = new EmbedBuilder()
			.setTitle("Lista de commandos")
			.setFooter(page + " of "+ maxPage);
		
		if (page > maxPage) {
			embed.addField("Oh no! 404", String.format(
				"""
				Hmm no creo que hallan demasiados commandos que enceñar,
				trata con un rango de páginas de [0-%s]
				""", maxPage), false);
			return embed.build();
		}
		
		for (int i = page * fieldsPerPage; i < (page + 1) * fieldsPerPage; i++) {
			if (i >= fields.size())
				continue;
			if (i % 2 == 0) {
				embed.addField(fields.get(i));
			}
			else {
				embed.addField(fields.get(i));
				embed.addBlankField(false);
			}
		}
		return embed.build();
	}
	
	private List<Field> getFieldPage(String estudianteOrientador) {
		return List.of(
			new Field(":mag: `/help`",
				"""
				> Muestra una lista de comandos disponibles.
				""", true),
		
			new Field(":question: `/faq`", 
				"""
				> Frequently asked questions
				> **(Solo se usa en el servidor)**
				""", true),
		
			new Field(":scroll: `/reglas`", 
				"""
				> Provee las reglas del servidor.
				> **(Solo se usa en el servidor)**
				""", true),
				
			new Field(":map: `/map`",
				"""
				> Provee un enlace a el Mapa de UPRM.
				""", true),
					
			new Field(":link: `/links`",
				"""
				> Provee un PDF con todos los links importantes del UPRM.
				""", true),
				
			new Field(":calendar_spiral: `/calendario`",
				"""
				> Provee un enlace rapido al Calendario Académico de UPRM.
				""", true),
			
			new Field(":globe_with_meridians: `/made-web`",
				"""
				> Provee el enlace para accesar a la página web de la consejera de **INEL/ICOM**, Madeline Rodríguez
				> **(Solo se usa en el servidor)**
				""", true),
				
			new Field(":straight_ruler: `/guia-prepistica`",
				"""
				> Guia para prepas.
				""", true),
			
			new Field(":school: `/salon`",
				"""
				> Provee información sobre el edificio donde se puede encontrar ese salón.
				""", true),
				
			new Field(":page_with_curl: `/curriculo`",
				"""
				> Te proveera un PDF del curriculo de tu departamento.
				> **(Solo se usa en el servidor)**
				""", true),
			
			new Field(":wrench: `/ls_projects`",
				"""
				> Provee información sobre proyectos e investigaciones relacionadas a **INEL/ICOM/INSO/CIIC**
				""", true),
					
			new Field("`/estudiantes-orientadores`", String.format(
				"""
				> Provee una lista los nombres de los %s de tu departamento.
				> **(Solo se usa en el servidor)**
				""", estudianteOrientador), true),
			
			new Field("`/ls_student_orgs`",
				"""
				> Provee información sobre organizaciones estudiantiles relacionadas a:
				> **INEL/ICOM/INSO/CIIC**,
				> *(IEEE/EMC/HKN/RAS_CSS/COMP_SOC/CAS/PES/WIE/ACM_CSE/CAHSI/SHPE/ALPHA_AST/EMB/PHOTONICS)*
				""", true),
					
			new Field(":link: `/contact`",
				"""
				> Mostrara una lista de todos los contactos que tengo disponible para ofrecerte. Ej:
				> - **Asesoría académica**,
				>    _**(Solo se usa en el servidor)**_
				> - **Asistencia académica**,
				> - **DCSP**,
				> - **Decanato de Estudiantes**,
				> - **Departamento (INEL/ICOM/INSO/CIIC)**,
				> - **Guardia Universitaria**
				""", true));
	}
}
