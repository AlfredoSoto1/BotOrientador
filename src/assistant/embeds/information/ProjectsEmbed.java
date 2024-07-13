/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
