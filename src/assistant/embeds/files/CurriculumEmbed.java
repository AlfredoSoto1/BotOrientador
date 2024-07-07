/**
 * 
 */
package assistant.embeds.files;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

/**
 * @author Alfredo
 */
public class CurriculumEmbed {
	
	public CurriculumEmbed() {
		
	}
	
	public MessageEmbed buildCurriculum(Color color) {
		return new EmbedBuilder().setColor(color)
			.setTitle("**¡Bienvenido al servidor!** :wave:")
			.setDescription(
				"""
			    Para poder acceder a todas las áreas del servidor y participar en las conversaciones, necesitamos verificar que eres un estudiante de nuevo ingreso. 
			    Este proceso nos ayuda a mantener un ambiente seguro y exclusivo para los estudiantes.
			    """)
			.build();
	}
}
