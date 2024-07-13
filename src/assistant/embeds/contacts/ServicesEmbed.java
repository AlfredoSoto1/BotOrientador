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
package assistant.embeds.contacts;

import java.awt.Color;

import assistant.rest.dto.ServiceDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Alfredo
 */
public class ServicesEmbed {
	
	public MessageEmbed buildInfoPanel(Color color, ServiceDTO service) {
		StringBuilder additional = new StringBuilder();
		StringBuilder contacts = new StringBuilder();
		
		service.getAdditional().forEach((name, info) -> {
			additional.append("> - " + name + " \n");
			for (String data : info)
				additional.append(">   " + data + "\n");
		});
		
		service.getContact().getWebpages().forEach(webpage -> {
			contacts.append("> - " + webpage.getDescription() + ": " + webpage.getUrl() + "\n");
		});
		
		service.getContact().getSocialmedias().forEach(social -> {
			contacts.append("> - " + social.getPlatform() + ": " + social.getUrl() + "\n");
		});
		
		EmbedBuilder embed = new EmbedBuilder()
			.setColor(color)
			.setTitle(service.getName())
			.setDescription(service.getDescription());
		
		if (!service.getOffering().isEmpty())
			embed.addField("Offering", String.format(
				"""
				%s
				""", String.join(", ", service.getOffering())), false);
		
		embed.addField("Details", String.format(
			"""
			> - Department: %s
			> - Edificio: %s | Code: %s
			> - %s
			""",
			service.getDepartmentAbbreviation(),
			service.getBuildingName(), service.getBuildingCode().toUpperCase(),
			service.getAvailability()), false);
		
		if (!contacts.isEmpty())
			embed.addField("Web & Socialmedia", String.format(
				"""
				%s
				""", contacts.toString()), false);
		if (!additional.isEmpty())
			embed.addField("Additional", String.format(
				"""
				%s
				""", additional.toString()), false);
		
		return embed.build();
	}
}
