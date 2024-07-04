/**
 * 
 */
package assistant.command.moderation;

import java.awt.Color;
import java.io.File;

import assistant.discord.interaction.InteractionModel;
import assistant.discord.interaction.MessengerI;
import assistant.embeds.moderation.VerificationEmbed;
import assistant.rest.dto.DiscordServerDTO;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

/**
 * @author Alfredo
 */
public class WelcomeMessenger extends InteractionModel implements MessengerI {

	private VerificationEmbed embed;
	
	public WelcomeMessenger() {
		this.embed = new VerificationEmbed();
	}
	
	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		event.getMember()
			.getUser().openPrivateChannel().queue(privateChannel -> onPrivateChannel(event.getGuild(), privateChannel));
	}

	@Override
	public void messageReceived(MessageReceivedEvent event) {
		
//		Guild server = event.getGuild();
//		DiscordServerDTO discordServer = super.getServerOwnerInfo(server.getIdLong());
//		
//		event.getMember().getUser()
//			.openPrivateChannel().queue(privateChannel -> {
//				
//				privateChannel.sendMessage(String.format(
//					"""
//					¡Increíble **%s**! Ahora eres un %s **COLEGIAL** %s :tada::tada::raised_hands_tone3::raised_hands_tone3:
//					
//					¿Fácil, no?
//					Bueno, ahora sí me presento formalmente.
//					
//					Hola %s **%s** %s,
//					
//					Me alegra mucho que estés aquí en el Colegio.
//					Yo soy el **Smart Assistant** del servidor y formo parte del equipo de **%s**, 
//					donde estamos organizados en sub-equipos para ayudarte mejor.
//					
//					Permíteme presentarte al equipo **_%s_**, uno de nuestros sub-equipos más destacados durante esta semana de orientación!. 
//					Juntos, nos esforzamos para ofrecerte el mejor soporte y resolver cualquier duda que tengas.
//					
//					Para que tengas una idea, te puedo ayudar a:
//					
//					> ### :mag_right:Búsqueda de lugares y edificios
//					> - Encontrar edificios
//					> - Encontrar sitios de comer
//					> - Salones de estudio
//					
//					> ### :bulb:Información de contactos
//					> - Oficinas importantes
//					> - Departamentos y facultades 
//					> - Administración y servicios
//					
//					> ### :link:Links
//					> - Guía prepística
//					> - Proyectos y organizaciones
//					> - Enlaces para complementar información
//					
//					Espero ser de gran ayuda para tí, recuerda aquí siempre a la orden!!
//					
//					Si quieres, puedes empezar por utilizando los *slash commands*.
//					Para empezar, puedes intentar ``/help`` y veras como te sale un menú donde
//					podrás ver varios de mis comandos que tengo.
//					""",
//					privateChannel.getUser().getEffectiveName(),
//					server.getEmojisByName("Huella", true).get(0).getAsMention(),
//					server.getEmojisByName("Huella", true).get(0).getAsMention(),
//					server.getEmojisByName("Huella", true).get(0).getAsMention(),
//					privateChannel.getUser().getEffectiveName(),
//					server.getEmojisByName("Huella", true).get(0).getAsMention(),
//					"ECE".equalsIgnoreCase(discordServer.getDepartment()) ? "TEAM-MADE" : "INSO/CIIC",
//					"Aliens"))
//					.queue();
//			});
		
	}
	
	private void onPrivateChannel(Guild server, PrivateChannel privateChannel) {
		
		DiscordServerDTO discordServer = super.getServerOwnerInfo(server.getIdLong());
		String department = discordServer.getDepartment();
		Color color = Color.decode("#" + discordServer.getColor());
		
		File teamMade = new File("assistant/images/WelcomeBanner_TEAM_MADE.png");
		File insociic = new File("assistant/images/WelcomeBanner_INSO_CIIC.png");
		FileUpload uploadTeamMade = FileUpload.fromData(teamMade);
		FileUpload uploadInsoCiic = FileUpload.fromData(insociic);
		
		String imageUrl_TeamMade = "attachment://WelcomeBanner_TEAM_MADE.png";
		String imageUrl_InsoCiic = "attachment://WelcomeBanner_INSO_CIIC.png";
		
		if ("ECE".equalsIgnoreCase(department)) {
			privateChannel.sendFiles(uploadTeamMade)
				.setEmbeds(embed.buildServerBanner(imageUrl_TeamMade, color)).queue();
		} else {
			privateChannel.sendFiles(uploadInsoCiic)
				.setEmbeds(embed.buildServerBanner(imageUrl_InsoCiic, color)).queue();
		}
		
		privateChannel.sendMessageEmbeds(embed.buildWelcomePrompt(server, department, color)).queue();
	}
}
