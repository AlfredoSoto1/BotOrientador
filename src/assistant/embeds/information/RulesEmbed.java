/**
 * 
 */
package assistant.embeds.information;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Alfredo
 */
public class RulesEmbed {
	
	public MessageEmbed buildGeneralRules(Color color, String esoRole, String botRole, String modRole, String staRole, String admRole, String conRole) {
		return new EmbedBuilder()
			.setColor(color)
			.setTitle(":straight_ruler: Estas son las reglas a seguir para tener un ambiente sano.")
			.setDescription(String.format(
				"""
				Estas reglas se tienen que seguir para evitar sansiones y ser baneado o expulsado
				del servidor. Para reportar algun problema por favor comunicarse con algun %s.	
				""", esoRole))
			
			.addField("1. :grinning: Trata a todos con respeto",
				"""
				No se tolerará absolutamente ningún acoso, sexismo, racismo o incitación al odio.
				""", false)
			.addField("2. :speaking_head:  Cuida tu lenguaje", String.format(
				"""
				Palabras soeces están __**PROHIBIDAS**__. Los %s censuran cualquier palabra soez 
				detectada así que mucho juicio con su vocabulario.
				""", botRole), false)
			.addField("3. :loudspeaker: No self-promotion or advertisements", String.format(
				"""
				No se permite autopromoción (invitaciones al servidor, anuncios, etc.) 
				sin el permiso de los %s o %s.
				""", modRole, staRole), false)
			.addField("4. :incoming_envelope: No spam",
				"""
				Evita mensajes excesivos en reuniones virtuales o presenciales.
				""", false)
			.addField("5. :shield: Cuidado con el contenido que pones",
				"""
				**No** contenido obsceno o con restricción de edad. Esto incluye texto, imágenes o
				enlaces que muestren desnudez, sexo, violencia dura u otro contenido gráficamente perturbador.
				""", false)
			.addField("6. :bangbang: Reportar a un problema", String.format(
				"""
				Si ves algo que va en contra de las reglas o algo que te hace sentir inseguro, díselo
				a un %s, %s o %s. ¡Queremos que este servidor sea un espacio acogedor!
				""", staRole, admRole, conRole), false)
			.build();
	}
	
	public MessageEmbed buildServerUsageRules(Color color, String bdeRole, String modRole, String staRole) {
		return new EmbedBuilder()
				.setColor(color)
				.setTitle("Uso del servidor")
				
				.addField("1. :hash: Channels",
				"""
				El servidor tiene channels para los topics que se tienden hablar con
				mas frecuencia, please use them accordingly!!
				""", false)
				.addField("2. :pencil: Conversaciones adicionales", String.format(
				"""
				En caso de que se quiera hablar de un topic distinto que no esté dentro de los que
				se muestran, pueden acercarse a un %s o %s y presentar su idea para así crear un 
				nuevo channel para atender esos nuevos temas de conversación.
				""", modRole, staRole), false)
				.addField("3. :bulb: Problemas con el Bot", String.format(
				"""
				Si tienes algún problema con el Bot, habla directamente con uno de los %s.
				""", bdeRole), false)
				.addField("4. :frame_photo: Problemas con el perfil", String.format(
				"""
				Si tienes algún problema con tu perfil o con algo en el servidor de Discord, hable directamente con %s o %s.
				""", modRole, bdeRole), false)
				.build();
	}
	
	public MessageEmbed buildBotUsageRules(Color color, String bdeRole, String esoRole) {
		return new EmbedBuilder()
			.setColor(color)
			.setTitle("Uso del Assistant")
			
			.addField("1. :question: Preguntas a la comunidad",
				"""
				No hay pregunta tonta, haga su pregunta sin miedo, pero antes pregúntele al Bot 
				si lo puede ayudar con los comandos que se les proveyeron.
				""", false)
			.addField("2. :question: Qué comandos tiene el Bot?", String.format(
				"""
				- Para saber que comandos tiene el bot puede escribir en cualquier parte del 
				  servidor `/help` y el bot le dará una lista con todos los comandos disponibles.
					
				- Cualquier duda con un comando le pueden preguntar a los %s directamente.
				""", bdeRole), false)
			.addField("3. :question: Ayuda directa", String.format(
				"""
				En caso de que el Bot no pueda ayudarlo, contacte a su líder de grupo o a un %s.
				""", esoRole), false)
			.addField("4. :question: Conoce a nuestros estudiantes orientadores",
				"""
				Si quiere saber que estudiantes orientadores fuera de sus líderes son de su
				departamento, utilize el comando `/estudiantes-orientadores`.
				""", false)
			.build();
	}
}
