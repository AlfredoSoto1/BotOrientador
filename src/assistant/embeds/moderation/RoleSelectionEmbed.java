/**
 * 
 */
package assistant.embeds.moderation;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Alfredo
 */
public class RoleSelectionEmbed {
	
	public RoleSelectionEmbed() {

	}
	
	public MessageEmbed buildGamingBuffet(Color color, String listOfGames) {
		return new EmbedBuilder()
			.setColor(color)
			.setTitle(":video_game: Gaming Role Selection :joystick:")
			.setFooter("React below to choose your favorite games you play")
			.setDescription(String.format(
				"""
				Select your favorite games to get the corresponding roles! Click the reactions below to choose.
				
				%s
				""", listOfGames))
			.build();
	}
}
