/**
 * 
 */
package assistant.embeds.information;

import java.awt.Color;
import java.util.List;

import assistant.rest.dto.MemberDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Alfredo
 */
public class EOEmbed {

	public EOEmbed() {

	}
	
	public MessageEmbed buildEO(Color color, String department, List<MemberDTO> members, long page, long maxPages) {
		EmbedBuilder embed = new EmbedBuilder()
				.setColor(color)
				.setTitle(department + " orientadores")
				.setDescription("");
		
		for (MemberDTO member : members) {
			embed.addField(member.getFirstname() + " " + member.getLastname(), String.format(
				"""
				> %s
				> \u200B
				> - Program: **%s**
				""", 
				member.getFunfact(), 
				member.getProgram().getLiteral()), false);
		}
		embed.addField("", String.format(
			"""
			%s of %s
			""",
			page, maxPages), false);
		return embed.build();
	}
}
