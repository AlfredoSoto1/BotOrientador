package assistant.command.information;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import service.discord.interaction.CommandI;
import service.discord.interaction.InteractionModel;

/**
 * 
 * @author Alfredo
 *
 */
public class RulesCmd extends InteractionModel implements CommandI {

	private boolean isGlobal;
	
	public RulesCmd() {
		
	}
	
	@Override
	public boolean isGlobal() {
		return isGlobal;
	}

	@Override
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	@Override
	public String getCommandName() {
		return "rules";
	}

	@Override
	public String getDescription() {
		return "Provee las reglas del servidor";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		// Mentioned Roles in embedded message
		Role staRole = event.getGuild().getRolesByName("Staff", true).get(0);
		Role modRole = event.getGuild().getRolesByName("Moderator", true).get(0);
		Role bdeRole = event.getGuild().getRolesByName("BotDeveloper", true).get(0);
		Role admRole = event.getGuild().getRolesByName("Administrator", true).get(0);
		Role conRole = event.getGuild().getRolesByName("ConsejeroProfesional", true).get(0);
		Role esoRole = event.getGuild().getRolesByName("EstudianteOrientador", true).get(0);
		Role botRole = event.getGuild().getRolesByName("Bots", true).get(0);
		
		/*
		 * Embedded messages to display - General rules
		 */
		String rules_title = ":straight_ruler: Estas son las reglas a seguir para tener un ambiente sano.";
		String rules_description = 
			"""
			Estas reglas se tienen que seguir para evitar sansiones y ser baneado o expulsado del servidor.
			Para reportar algun problema por favor comunicarse con algun %s.
			""";

		rules_description = String.format(rules_description, esoRole.getAsMention());
		
		String rule_1_title = "1. :grinning: Trata a todos con respeto";
		String rule_1_description = 
			"""
			No se tolerará absolutamente ningún acoso, sexismo, racismo o incitación al odio.
			""";
		String rule_2_title = "2. :speaking_head:  Cuida tu lenguaje";
		String rule_2_description = 
			"""
			Palabras soeces están __**PROHIBIDAS**__. Los %s censuran cualquier palabra soez 
			detectada así que mucho juicio con su vocabulario.
			""";
		String rule_3_title = "3. :loudspeaker: No self-promotion or advertisements";
		String rule_3_description = 
			"""
			No se permite autopromoción (invitaciones al servidor, anuncios, etc.) sin el permiso de los %s o %s.
			""";
		String rule_4_title = "4. :incoming_envelope: No spam";
		String rule_4_description = 
			"""
			Evita mensajes excesivos en reuniones virtuales o presenciales.
			""";
		String rule_5_title = "5. :shield: Cuidado con el contenido que pones";
		String rule_5_description = 
			"""
			**No** contenido obsceno o con restricción de edad. Esto incluye texto, imágenes o enlaces que muestren desnudez,
			sexo, violencia dura u otro contenido gráficamente perturbador.
			""";
		String rule_6_title = "6. :bangbang: Reportar a un problema";
		String rule_6_description = 
			"""
			Si ves algo que va en contra de las reglas o algo que te hace sentir inseguro, díselo 
			a un %s, %s o %s. ¡Queremos que este servidor sea un espacio acogedor!
			""";
		
		/*
		 * Embedded messages to display - Server Usage
		 */
		String server_usage_title = "Uso del servidor";
		
		String server_usage_1_title = "1. :hash: Channels";
		String server_usage_1_description = 
			"""
			El servidor tiene channels para los topics que se tienden hablar con mas frecuencia, 
			please use them accordingly!!
			""";
		String server_usage_2_title = "2. :pencil: Conversaciones adicionales";
		String server_usage_2_description = 
			"""
			En caso de que se quiera hablar de un topic distinto que no esté dentro de los que se muestran, 
			pueden acercarse a un @Moderador o %s y presentar su idea para así crear un nuevo channel 
			para atender esos nuevos temas de conversación.
			""";
		String server_usage_3_title = "3. :bulb: Problemas con el Bot";
		String server_usage_3_description = 
			"""
			Si tienes algún problema con el Bot, habla directamente con uno de los %s.
			""";
		String server_usage_4_title = "4. :frame_photo: Problemas con el perfil";
		String server_usage_4_description = 
			"""
			Si tienes algún problema con tu perfil o con algo en el servidor de Discord, hable directamente con %s o %s.
			""";
		
		/*
		 * Assistant Usage Rules
		 */
		String assistant_title = "Uso del Assistant";
		
		String assistant_1_title = "1. :question: Preguntas a la comunidad";
		String assistant_1_description = 
			"""
			No hay pregunta tonta, haga su pregunta sin miedo, pero antes pregúntele al Bot 
			si lo puede ayudar con los comandos que se les proveyeron.
			""";
		String assistant_2_title = "2. :question: Qué comandos tiene el Bot?";
		String assistant_2_description = 
			"""
			- Para saber que comandos tiene el bot puede escribir en cualquier parte del 
			  servidor `/help` y el bot le dará una lista con todos los comandos disponibles.
				
			- Cualquier duda con un comando le pueden preguntar a los %s directamente.
			""";
		String assistant_3_title = "3. :question: Ayuda directa";
		String assistant_3_description = 
			"""
			En caso de que el Bot no pueda ayudarlo, contacte a su líder de grupo o a un %s.
			""";
		String assistant_4_title = "4. :question: Conoce a nuestros estudiantes orientadores";
		String assistant_4_description = 
			"""
			Si quiere saber que estudiantes orientadores fuera de sus líderes son de su departamento, 
			utilize el comando `/estudiantes-orientadores`.
			""";
		
		rule_2_description = String.format(rule_2_description, botRole.getAsMention());
		rule_3_description = String.format(rule_3_description, modRole.getAsMention(), staRole.getAsMention());
		rule_6_description = String.format(rule_6_description, staRole.getAsMention(), admRole.getAsMention(), conRole.getAsMention());

		server_usage_2_description = String.format(server_usage_2_description, staRole.getAsMention());
		server_usage_3_description = String.format(server_usage_3_description, bdeRole.getAsMention());
		server_usage_4_description = String.format(server_usage_4_description, modRole.getAsMention(), bdeRole.getAsMention());

		assistant_2_description = String.format(assistant_2_description, bdeRole.getAsMention());
		assistant_3_description = String.format(assistant_3_description, esoRole.getAsMention());
		
		/*
		 * Rules embed reply
		 */
		EmbedBuilder rulesEmbed = new EmbedBuilder();

		rulesEmbed.setTitle(rules_title);
		rulesEmbed.setDescription(rules_description);
		rulesEmbed.setColor(new Color(40, 130, 138));

		rulesEmbed.addField(rule_1_title, rule_1_description, false);
		rulesEmbed.addField(rule_2_title, rule_2_description, false);
		rulesEmbed.addField(rule_3_title, rule_3_description, false);
		rulesEmbed.addField(rule_4_title, rule_4_description, false);
		rulesEmbed.addField(rule_5_title, rule_5_description, false);
		rulesEmbed.addField(rule_6_title, rule_6_description, false);
		
		/*
		 * Server Usage embed reply
		 */
		EmbedBuilder serverUsageEmbed = new EmbedBuilder();
		
		serverUsageEmbed.setTitle(server_usage_title);
		serverUsageEmbed.setColor(new Color(40, 130, 138));
		
		serverUsageEmbed.addField(server_usage_1_title, server_usage_1_description, false);
		serverUsageEmbed.addField(server_usage_2_title, server_usage_2_description, false);
		serverUsageEmbed.addField(server_usage_3_title, server_usage_3_description, false);
		serverUsageEmbed.addField(server_usage_4_title, server_usage_4_description, false);
	
		/*
		 * Assistant Usage embed reply
		 */
		EmbedBuilder assistantUsageEmbed = new EmbedBuilder();

		assistantUsageEmbed.setTitle(assistant_title);
		assistantUsageEmbed.setColor(new Color(40, 130, 138));

		assistantUsageEmbed.addField(assistant_1_title, assistant_1_description, false);
		assistantUsageEmbed.addField(assistant_2_title, assistant_2_description, false);
		assistantUsageEmbed.addField(assistant_3_title, assistant_3_description, false);
		assistantUsageEmbed.addField(assistant_4_title, assistant_4_description, false);
		
		event.replyEmbeds(rulesEmbed.build(), serverUsageEmbed.build(), assistantUsageEmbed.build()).queue();
	}
}
