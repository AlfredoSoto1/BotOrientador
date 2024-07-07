/**
 * 
 */
package assistant.embeds.information;

import java.awt.Color;
import java.util.List;

import assistant.rest.dto.OrganizationDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Alfredo
 */
public class OrganizationsEmbed {

	public OrganizationsEmbed() {

	}
	
	public MessageEmbed buildOrganization(Color color, OrganizationDTO organization) {
		StringBuilder socialMediaBuilder = new StringBuilder();

        List<String> platforms = organization.getPlatforms();
        List<String> urlHandles = organization.getUrlhandle();

        if (platforms.size() == urlHandles.size()) {
            for (int i = 0; i < platforms.size(); i++) {
                socialMediaBuilder.append(String.format("> - %s: %s\n", platforms.get(i), urlHandles.get(i)));
            }
        } else {
            socialMediaBuilder.append("> - Mismatch in platforms and URLs\n");
        }
		
		return new EmbedBuilder()
			.setColor(color)
			.setTitle(organization.getName())
			.setDescription(organization.getDescription())
			.addField("Contact Info & Social Media", String.format(
				"""
				> - Email: %s	
				> - Website: %s	
				%s
				""",
				organization.getEmail(),
				organization.getWebsite(),
				socialMediaBuilder.toString()), false)
			.build();
	}
}
