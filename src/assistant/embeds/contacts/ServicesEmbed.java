/**
 * 
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
