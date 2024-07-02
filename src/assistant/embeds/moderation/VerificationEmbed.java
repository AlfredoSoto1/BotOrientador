/**
 * 
 */
package assistant.embeds.moderation;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

/**
 * @author Alfredo
 */
public class VerificationEmbed {
	
	public VerificationEmbed() {
		
	}
	
	public MessageEmbed buildVerificationPrompt(Role modRole, Role bdeRole) {
		
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(40, 130, 138))
			.setTitle("**¡Bienvenido al servidor!** :wave:")
			.setDescription(
				"""
			    Para poder acceder a todas las áreas del servidor y participar en las conversaciones, necesitamos verificar que eres un estudiante de nuevo ingreso. 
			    Este proceso nos ayuda a mantener un ambiente seguro y exclusivo para los estudiantes.
			    """);
		
		embedBuider.addField("**Pasos para la verificación:**",
				"""
				1. **Presiona el botón "Verify"**:
				Al presionar el botón de verificación que se encuentra abajo, iniciarás el proceso de verificación.
				
				2. **Provee tu correo institucional**:
				Se te pedirá que ingreses tu correo electrónico institucional (el que termina en __@upr.edu__). 
				Este correo es utilizado únicamente para confirmar tu identidad como estudiante.
				""", false);
		
		embedBuider.addBlankField(false);
		
		embedBuider.addField("**¿Problemas con la verificación?**", String.format(
			"""
			- Asegúrate de haber ingresado correctamente tu correo institucional.
			- Si aún tienes problemas, comunícate con un %s o %s para obtener ayuda.
			
			¡Gracias por unirte y esperamos que disfrutes tu tiempo en el servidor! :blush:
			""", bdeRole.getAsMention(), modRole.getAsMention()), false);
		
		return embedBuider.build();
	}
	
	public MessageEmbed buildWelcomePrompt() {
		// TODO COMPLETE THIS
		return null;
	}
}
