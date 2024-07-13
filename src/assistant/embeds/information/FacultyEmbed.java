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
import java.util.stream.Collectors;

import assistant.rest.dto.ExtensionDTO;
import assistant.rest.dto.FacultyDTO;
import assistant.rest.dto.WebpageDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Alfredo
 */
public class FacultyEmbed {

	public FacultyEmbed() {
	
	}
	
	public MessageEmbed buildFaculty(Color color, String department, List<FacultyDTO> faculty, long page, long maxPages) {
		
		EmbedBuilder embed = new EmbedBuilder()
			.setColor(color)
			.setTitle(department + " faculty")
			.setDescription("");
		
		
		for (FacultyDTO professor : faculty) {
			List<String> webPages = professor.getContact().getWebpages()
							.stream()
							.map(WebpageDTO::getUrl)
							.collect(Collectors.toList());

			embed.addField(professor.getName(), String.format(
				"""
				> %s
				> \u200B
				> - **%s**
				> - _%s_
				> - Office: _%s_
				> - Ext. %s
				%s
				""", 
				professor.getDescription(), 
				professor.getJobentitlement(), 
				professor.getContact().getEmail(),
				professor.getOffice(),
				String.join(", ", 
						professor.getContact().getExtensions().stream()
							.map(ExtensionDTO::getExt)
							.collect(Collectors.toList())),
				
				webPages.isEmpty() ? "" :
					"> - Página oficial: " + String.join(", ", webPages)
				), false);
		}
			
		embed.addField("", String.format(
			"""
			%s of %s
			Para más contactos de Facultad
			%s
			""",
			page, maxPages,
			department.equalsIgnoreCase("ECE") ? "https://ece.uprm.edu/people/faculty/#cn-top" : "https://www.uprm.edu/cse/faculty/"), false);
	
		return embed.build();
	}
}
