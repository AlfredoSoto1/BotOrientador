/**
 * 
 */
package assistant.embeds.information;

import java.awt.Color;
import java.util.List;

import assistant.rest.dto.ProjectDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Alfredo
 */
public class ProjectsEmbed {

	public ProjectsEmbed() {

	}
	
	public MessageEmbed buildProject(Color color, ProjectDTO project) {
		StringBuilder socialMediaBuilder = new StringBuilder();

        List<String> platforms = project.getPlatforms();
        List<String> urlHandles = project.getUrlhandle();

        if (platforms.size() == urlHandles.size()) {
            for (int i = 0; i < platforms.size(); i++) {
                socialMediaBuilder.append(String.format("> - %s: %s\n", platforms.get(i), platforms.get(i) + ".com/" + urlHandles.get(i)));
            }
        } else {
            socialMediaBuilder.append("> - Mismatch in platforms and URLs\n");
        }
		
		return new EmbedBuilder()
			.setColor(color)
			.setTitle(project.getName())
			.setDescription(project.getDescription())
			.addField("Contact Info & Social Media", String.format(
				"""
				> - Email: %s	
				> - Website: %s	
				%s
				""",
				project.getEmail(),
				project.getWebsite(),
				socialMediaBuilder.toString()), false)
			.build();
	}
}
