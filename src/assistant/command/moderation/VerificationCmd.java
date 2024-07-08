/**
 * 
 */
package assistant.command.moderation;

import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import assistant.app.core.Application;
import assistant.app.core.Logger;
import assistant.app.core.Logger.LogFeedback;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.discord.object.MemberPosition;
import assistant.embeds.moderation.VerificationEmbed;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.MemberDTO;
import assistant.rest.dto.PrepaOrientadorDTO;
import assistant.rest.dto.TeamDTO;
import assistant.rest.service.MemberService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

/**
 * @author Alfredo
 */
public class VerificationCmd extends InteractionModel implements CommandI {
	
	private static final String COMMAND_LABEL = "channel";
	
	private Modal verifyPrompt;
	private Button verifyButton;
	
	private MemberService service;
	private VerificationEmbed verificationEmbed;
	
	public VerificationCmd() {
		this.verificationEmbed = new VerificationEmbed();
		this.service = Application.instance().getSpringContext().getBean(MemberService.class);
		
		// Create an Email field to be displayed inside the modal
		TextInput email = TextInput.create("email-id", "Email", TextInputStyle.SHORT)
				.setMinLength(9)
				.setMaxLength(100)
				.setRequired(true)
				.setPlaceholder("your.email@upr.edu")
				.build();
		
		// Create a FunFacts field to be displayed inside the modal
		TextInput funfacts = TextInput.create("funfact-id", "Fun facts about you :)", TextInputStyle.PARAGRAPH)
				.setMinLength(1)
				.setMaxLength(255)
				.setRequired(false)
				.setPlaceholder("Tell us more about you!")
				.build();
		
		// Create a simple modal containing two text fields
		// in which the user will enter his email to log-in and
		// a fun fact about them
		super.registerModal(this::onModalVerificationRespond, 
			verifyPrompt = Modal.create("mem-verification", "Member Verification")
				.addComponents(
					ActionRow.of(email), 
					ActionRow.of(funfacts))
				.build()
			);

		super.registerButton(this::onVerificationEvent, verifyButton = Button.success("verification-button", "verify"));
	}
	
	@Override
	public void onDispose() {
		service.shutdownVerificationQueueService();
	} 
	
	@Override
	public boolean isGlobal() {
		return false;
	}
	
	@Override
	@Deprecated
	public void setGlobal(boolean isGlobal) {
		// This is a server only command
	}

	@Override
	public String getCommandName() {
		return "assistant-verification";
	}

	@Override
	public String getDescription() {
		return "Sends an embed to verify user to a channel of choice";
	}
	
	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of(
			new OptionData(OptionType.STRING, COMMAND_LABEL, "select channel to send verification message", true));
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		if(!super.validateCommandUse(event))
			return;
		
		String logChannel = event.getOption(COMMAND_LABEL).getAsString();
		
		try {
			Long.parseLong(logChannel);
		} catch (NumberFormatException nfe) {
			event.reply("The id provided for the verification-channel is not a valid number").setEphemeral(true).queue();
			return;
		}
		
		// Check if input was given successfully
		Optional<TextChannel> textChannel = Optional.ofNullable(event.getGuild().getTextChannelById(logChannel));
		
		// Check if the channel is in server
		if (textChannel.isPresent()) {
			event.reply("Verification embed sent to: " + textChannel.get().getName()).setEphemeral(true).queue();
			
			// Mentioned Roles in embedded message
			Optional<Role> modRole = super.getEffectiveRole(MemberPosition.MODERATOR, textChannel.get().getGuild());
			Optional<Role> bdeRole = super.getEffectiveRole(MemberPosition.BOT_DEVELOPER, textChannel.get().getGuild());
			
			DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
			Color color = Color.decode("#" + discordServer.getColor());
			
			textChannel.get().sendMessageEmbeds(verificationEmbed.buildVerificationPrompt(modRole.get(), bdeRole.get(), color))
				.setActionRow(verifyButton)
				.queue();
		}
		else
			event.reply("Channel not found").setEphemeral(true).queue();
	}
	
	private void onVerificationEvent(ButtonInteractionEvent event) {
		// Show the verification modal to the user
		event.replyModal(verifyPrompt).queue();
	}
	
	private void onModalVerificationRespond(ModalInteractionEvent event) {
        // Respond to the user (ephemeral response)
        event.reply("Gracias por unirte al server, en cualquier momento recibiras tus roles.").setEphemeral(true).queue();
        
        // Do this asynchronously
        service.queueVerificationTask(event, this::verifyUser);
    }
	
	private void verifyUser(ModalInteractionEvent event) {
		
		// Retrieve the values entered by the user
        String email = event.getValue("email-id").getAsString();
        String funfacts = event.getValue("funfact-id").getAsString();
		
		// Obtain the roles of the EOs in charge of Discord
		Optional<Role> modRole = super.getEffectiveRole(MemberPosition.MODERATOR, event.getGuild());
		Optional<Role> bdeRole = super.getEffectiveRole(MemberPosition.BOT_DEVELOPER, event.getGuild());
		
		// Check if the roles exists in database
		if (modRole.isEmpty() || bdeRole.isEmpty()) {
			event.reply("No se puede asignar los roles, contacta a un bot developer").setEphemeral(true).queue();
			Logger.instance().logFile(LogFeedback.ERROR, "Failed looking for effective roles in database");
			return;
		}
		
        // Obtain member from database
        Optional<MemberDTO> memberDTO = service.getMember(email);
        
        // Safety check if the member doesn't exist in database
        if (!memberDTO.isPresent()) {
        	String hookMessage = 
    			"""
    			Hmm parece que el email que entraste *__%s__* no está en nuestra base de datos :confused:
    			Por favor trata de nuevo. Si no puedo encontrar tu información, contacta a %s or %s.
    			
    			Asegurate que estas usando **__@upr.edu__** ya que se necesita el email institucional para poder ser verificado.
    			""";
        	
        	// Reply user privately
        	event.getHook()
	        	.sendMessageFormat(hookMessage, email, bdeRole.get().getAsMention(), modRole.get().getAsMention())
	        	.setEphemeral(true).queue();
        	
        	Logger.instance().logFile(LogFeedback.INFO, "Failed looking for user: %s", event.getMember().getEffectiveName());
        	return;
        }
		
        // Update the member to later submit the member presence
        memberDTO.ifPresent(member -> {
        	// Set the funfacts and the username
        	member.setFunfact(funfacts);
        	member.setUsername(event.getMember().getUser().getName());
        });
        
		// Check member verification
		if(memberDTO.get().isVerified()) {
			// Set the roles and nickname of the member.
			applyTeam(event.getHook(), event.getGuild(), event.getMember(), memberDTO.get().getEmail());
			applyRoles(event.getHook(), event.getGuild(), event.getMember(), memberDTO.get().getEmail());
			applyNickname(event.getHook(), event.getGuild(), event.getMember(), memberDTO.get());
		} else {
			// Stamp the presence of the member and assign the roles
			service.stampMemberPresence(memberDTO.get(), event.getGuild().getIdLong());
			
			// Set the roles and nickname of the member.
			applyTeam(event.getHook(), event.getGuild(), event.getMember(), memberDTO.get().getEmail());
			applyRoles(event.getHook(), event.getGuild(), event.getMember(), memberDTO.get().getEmail());
			applyNickname(event.getHook(), event.getGuild(), event.getMember(), memberDTO.get());

			// From the user, open a private channel to send DMs
			event.getMember().getUser()
				.openPrivateChannel().queue(privateMessage -> showWelcomeMessage(privateMessage, memberDTO.get(), event.getGuild()));
		}
	}
	
	private void showWelcomeMessage(PrivateChannel privateChannel, MemberDTO member, Guild server) {
		
		// Check if the member is an orientador
		if(service.isOrientador(member.getEmail(), server.getIdLong())) {
			// Send custom message to orientador is joining the server
			sendWelcomeMessageToOrientador(privateChannel, server, member);
			return;
		}

		// Obtain the team of the member
		Optional<TeamDTO> team = service.getMemberTeam(member.getEmail(), server.getIdLong());
		
		// If team not found, no need to print or display any message
		// since its handled when the team is applied to the member
		if(team.isEmpty())
			return;
		
		// Send welcome message to member if its a prepa
		sendWelcomeMessageToPrepa(privateChannel, server, member, team.get());
	}
	
	private void sendWelcomeMessageToPrepa(PrivateChannel privateChannel, Guild server, MemberDTO member, TeamDTO team) {
		// Obtain discord server information
		DiscordServerDTO discordServer = super.getServerOwnerInfo(server.getIdLong());
		
		// Obtain a list of all the orientadores that form part of
		// the same group as the prepa member who is joining
		List<PrepaOrientadorDTO> prepaOrientadors = service.getPrepaOrientadores(member.getEmail(), server.getIdLong());
		
		// Obtain the names of the orientadores assuming
		// that the member joining is a prepa and is already a verified member
		String orientadorNames = prepaOrientadors.stream()
				.map(PrepaOrientadorDTO::getFirstname)
				.collect(Collectors.joining(", "));
		
		privateChannel.sendMessage(String.format(
			"""
			¡Increíble **%s**! Ahora eres un %s **COLEGIAL** %s :tada::tada::raised_hands_tone3::raised_hands_tone3:
					
			¿Fácil, no?
			Bueno, ahora sí me presento formalmente.
			
			Hola %s **%s** %s,
			
			Me alegra mucho que estés aquí en el Colegio.
			Yo soy el **Smart Assistant** del servidor y formo parte del equipo de **%s**, 
			donde estamos organizados en sub-equipos para ayudarte mejor.
			
			Permíteme presentarte al equipo **_%s_**, uno de nuestros sub-equipos más destacados durante esta semana de orientación!. 
			Juntos, nos esforzamos para ofrecerte el mejor soporte y resolver cualquier duda que tengas.
			Tus estudiantes orientadores de tu equipo son: %s
			
			Para que tengas una idea, te puedo ayudar a:
			
			> ### :mag_right:Búsqueda de lugares y edificios
			> - Encontrar edificios
			> - Encontrar sitios de comer
			> - Salones de estudio
			
			> ### :bulb:Información de contactos
			> - Oficinas importantes
			> - Departamentos y facultades 
			> - Administración y servicios
			
			> ### :link:Links
			> - Guía prepística
			> - Proyectos y organizaciones
			> - Enlaces para complementar información
			
			Espero ser de gran ayuda para tí, recuerda aquí siempre a la orden!!
			
			Si quieres, puedes empezar por utilizando los *slash commands*.
			Para empezar, puedes intentar ``/help`` y veras como te sale un menú donde
			podrás ver varios de mis comandos que tengo.
			""",
				member.getFirstname(),
				server.getEmojisByName("Huella", true).get(0).getAsMention(),
				server.getEmojisByName("Huella", true).get(0).getAsMention(),
				server.getEmojisByName("Huella", true).get(0).getAsMention(),
				member.getFirstname(),
				server.getEmojisByName("Huella", true).get(0).getAsMention(),
				"ECE".equalsIgnoreCase(discordServer.getDepartment()) ? "TEAM-MADE" : "INSO/CIIC",
				team.getName(), orientadorNames))
			.queue();		
	}
	
	private void sendWelcomeMessageToOrientador(PrivateChannel privateChannel, Guild server, MemberDTO member) {
		// Obtain discord server information
		DiscordServerDTO discordServer = super.getServerOwnerInfo(server.getIdLong());
		
		privateChannel.sendMessage(String.format(
			"""
			Bienvenido %s **%s** %s al **%s** Discord Server. 
			Recuerda, avisar a los Bot Developers de cualquier problema con el bot.
			De tener alguna idea respecto al bot o del server como tal, puedes decirle
			a los Administradores o a los Bot Developers!!
			""",
				server.getEmojisByName("Huella", true).get(0).getAsMention(),
				member.getFirstname(),
				server.getEmojisByName("Huella", true).get(0).getAsMention(),
				"ECE".equalsIgnoreCase(discordServer.getDepartment()) ? "TEAM-MADE" : "INSO/CIIC"))
			.queue();		
	}
	
	private void applyRoles(InteractionHook hook, Guild server, Member member, String email) {
		// Obtain the roles that the user has associated to the email
		List<DiscordRoleDTO> classificationRoles = service.getMemberRoles(email, server.getIdLong());
		
		// Set the roles to the member
		for (DiscordRoleDTO roleDTO : classificationRoles) {
			Role role = server.getRoleById(roleDTO.getRoleid());
			
			try {
				server.addRoleToMember(member, role).queue(
					success -> Logger.instance().logFile(LogFeedback.SUCCESS, "Given Role [%s] to [%s]", role.getName(), member.getEffectiveName()));
			} catch (HierarchyException he) {
				hook.sendMessage("No se pudo otorgar los roles, por favor notifique a un Bot developer").setEphemeral(true).queue();
				Logger.instance().logFile(LogFeedback.WARNING, "Failed to add role: %s to member %s", he.getMessage(), member.getEffectiveName());
			}
		}
	}
	
	private void applyTeam(InteractionHook hook, Guild server, Member member, String email) {
		// Obtain the team that the user has associated to the email
		Optional<TeamDTO> team = service.getMemberTeam(email, server.getIdLong());
		
		if(team.isEmpty()) {
			hook.sendMessage("No se pudo otorgar el equipo, por favor notifique a un Bot developer").setEphemeral(true).queue();
			Logger.instance().logFile(LogFeedback.ERROR, "Failed to get team role for: %s", member.getEffectiveName());
			return;
		}
		
		Role role = server.getRoleById(team.get().getTeamRole().getRoleid());
		
		try {
			server.addRoleToMember(member, role).queue(
					success -> Logger.instance().logFile(LogFeedback.SUCCESS, "Given Role [%s] to [%s]", role.getName(), member.getEffectiveName()));
		} catch (HierarchyException he) {
			hook.sendMessage("No se pudo otorgar los roles, por favor notifique a un Bot developer").setEphemeral(true).queue();
			Logger.instance().logFile(LogFeedback.WARNING, "Failed to add role: %s to member %s", he.getMessage(), member.getEffectiveName());
		}
	}
	
	private void applyNickname(InteractionHook hook, Guild server, Member member, MemberDTO memberDTO) {
		String nickname = memberDTO.getFirstname() + " " + memberDTO.getInitial() + " " + memberDTO.getLastname();
    	
		try {
			server.modifyNickname(member, nickname).queue(
				success -> Logger.instance().logFile(LogFeedback.SUCCESS, "Successfully changed nickname from [%s] to [%s]", member.getEffectiveName(), nickname));
		} catch (HierarchyException he) {
			hook.sendMessage("No se pudo otorgar el nickname, por favor notifique a un Bot developer").setEphemeral(true).queue();
			Logger.instance().logFile(LogFeedback.WARNING, "Failed to change nickname: %s", he.getMessage());
		}
	}
}
