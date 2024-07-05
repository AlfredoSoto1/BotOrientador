/**
 * 
 */
package assistant.embeds.moderation;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

/**
 * @author Alfredo
 */
public class VerificationEmbed {
	
	public VerificationEmbed() {
		
	}
	
	public MessageEmbed buildVerificationPrompt(Role modRole, Role bdeRole, Color color) {
		return new EmbedBuilder().setColor(color)
			.setTitle("**¡Bienvenido al servidor!** :wave:")
			.setDescription(
				"""
			    Para poder acceder a todas las áreas del servidor y participar en las conversaciones, necesitamos verificar que eres un estudiante de nuevo ingreso. 
			    Este proceso nos ayuda a mantener un ambiente seguro y exclusivo para los estudiantes.
			    """)
			.addField("**Pasos para la verificación:**",
				"""
				1. **Presiona el botón "Verify"**:
				Al presionar el botón de verificación que se encuentra abajo, iniciarás el proceso de verificación.
				
				2. **Provee tu correo institucional**:
				Se te pedirá que ingreses tu correo electrónico institucional (el que termina en __@upr.edu__). 
				Este correo es utilizado únicamente para confirmar tu identidad como estudiante.
				""", false)
			.addBlankField(false)
			.addField("**¿Problemas con la verificación?**", String.format(
				"""
				- Asegúrate de haber ingresado correctamente tu correo institucional.
				- Si aún tienes problemas, comunícate con un %s o %s para obtener ayuda.
				
				¡Gracias por unirte y esperamos que disfrutes tu tiempo en el servidor! :blush:
				""", bdeRole.getAsMention(), modRole.getAsMention()), false)
			.build();
	}
	
	public MessageEmbed buildServerBanner(String imageUrl, Color color) {
		return new EmbedBuilder().setColor(color).setImage(imageUrl).build();
	}
	
	public MessageEmbed buildWelcomePrompt(Guild server, String department, Color color) {
		String organization = "ECE".equalsIgnoreCase(department) ? "TEAM-MADE" : "INSO/CIIC";
		
		return new EmbedBuilder().setColor(color)
			.setTitle(String.format(
				"""
				Bienvenido al %s**COLEGIO**%s, y al Discord de **%s**:tada::tada::raised_hands_tone3: 
				""", 
				server.getEmojisByName("Huella", true).get(0).getAsMention(), 
				server.getEmojisByName("Huella", true).get(0).getAsMention(),
				organization))
			.setDescription(String.format(
				"""
				Para poder acceder al servidor es necesario que te verifiques. Para esto,
				tienes que ir al canal de :link: %s donde ahí continuarás para acceder el resto del server.	
				""", server.getTextChannelsByName("✅verification", true).get(0).getAsMention()))
			.build();
	}
}
