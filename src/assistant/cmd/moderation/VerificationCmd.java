/**
 * 
 */
package assistant.cmd.moderation;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import assistant.daos.VerificationDAO;
import assistant.models.VerificationReport;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import services.bot.interactions.CommandI;
import services.bot.interactions.InteractionModel;

/**
 * @author Alfredo
 */
public class VerificationCmd extends InteractionModel implements CommandI {
	
	private static final String COMMAND_LABEL = "channel";
	
	private VerificationDAO verificationDAO;
	
	private boolean isGlobal;
	private Modal verifyPrompt;
	private Button verifyButton;
	
	public VerificationCmd() {
		this.verificationDAO = new VerificationDAO();
		
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
	public boolean isGlobal() {
		return isGlobal;
	}
	
	@Override
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
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
	public List<OptionData> getOptions() {
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
			sendVerificationEmbed(textChannel.get());
		}
		else
			event.reply("Channel not found").setEphemeral(true).queue();
	}
	
	private void sendVerificationEmbed(TextChannel textChannel) {
		
		// Mentioned Roles in embedded message
		Role modRole = textChannel.getGuild().getRolesByName("Moderator", true).get(0);
		Role bdeRole = textChannel.getGuild().getRolesByName("BotDeveloper", true).get(0);
		
		/*
		 * Embedded messages
		 */
		String verification_title = 
			"""
		    **¡Bienvenido al servidor!** :wave:
		    """;
		String verification_description = 
			"""
		    Para poder acceder a todas las áreas del servidor y participar en las conversaciones, necesitamos verificar que eres un estudiante de nuevo ingreso. 
		    Este proceso nos ayuda a mantener un ambiente seguro y exclusivo para los estudiantes.
		    """;
		String verification_step_title =
			"""
			 **Pasos para la verificación:**
			""";
		String verification_step_description_1 =
			"""
			1. **Presiona el botón "Verify"**:
			Al presionar el botón de verificación que se encuentra abajo, iniciarás el proceso de verificación.
			
			2. **Provee tu correo institucional**:
			Se te pedirá que ingreses tu correo electrónico institucional (el que termina en __@upr.edu__). 
			Este correo es utilizado únicamente para confirmar tu identidad como estudiante.
			""";
		
		String verification_problem_title =
			"""
			**¿Problemas con la verificación?**
			""";
		String verification_problem_description =
			"""
			- Asegúrate de haber ingresado correctamente tu correo institucional.
			- Si aún tienes problemas, comunícate con un %s o %s para obtener ayuda.
			
			¡Gracias por unirte y esperamos que disfrutes tu tiempo en el servidor! :blush:
			""";

		verification_problem_description = String.format(verification_problem_description, bdeRole.getAsMention(), modRole.getAsMention());
		
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(40, 130, 138));
		embedBuider.setTitle(verification_title);
		embedBuider.setDescription(verification_description);
		
		embedBuider.addField(verification_step_title, verification_step_description_1, false);
		embedBuider.addBlankField(false);
		embedBuider.addField(verification_problem_title, verification_problem_description, false);
		
		textChannel.sendMessageEmbeds(embedBuider.build())
			.setActionRow(verifyButton)
			.queue();
	}
	
	private void onVerificationEvent(ButtonInteractionEvent event) {
		// Show the verification modal to the user
		event.replyModal(verifyPrompt).queue();
	}
	
	private void onModalVerificationRespond(ModalInteractionEvent event) {
		
		// Obtain the roles of the EOs in charge of Discord
		Role modRole = event.getGuild().getRolesByName("Moderator", true).get(0);
		Role bdeRole = event.getGuild().getRolesByName("BotDeveloper", true).get(0);
		
        // Respond to the user (ephemeral response)
        event.reply("Thank you for verifying, any time soon you'll be able to have all the coresponding roles").setEphemeral(true).queue();
        
        // Retrieve the values entered by the user
        String email = event.getValue("email-id").getAsString();
        String funfacts = event.getValue("funfact-id").getAsString();
        
        Optional<VerificationReport> report = verificationDAO.getUserReport(email);
        
        if (!report.isPresent()) {
        	// Send message to member replying that he is not in the database
        	event.getHook()
	        	.sendMessageFormat(
        			"""
        			Hmm parece que el email que entraste *__%s__* no está en nuestra base de datos :confused:
        			Por favor trata de nuevo. Si no puedo encontrar tu información, contacta a %s or %s.
        			
        			Asegurate que estas usando **__@upr.edu__** ya que se necesita el email institucional para poder ser verificado.
        			""", email, bdeRole.getAsMention(), modRole.getAsMention())
	        	.setEphemeral(true)
	        	.queue();
        	return;
        }

        Member member = event.getMember();
        // TODO set role to member and change nickname
        // if the role was not assigned or changed nickname, try again with the member after 2 seconds
    	assignRoleAndChangeNickname(event.getGuild(), member, report.get());
    	
    	// Confirm and commit verification
    	verificationDAO.confirmVerification(email, funfacts);
    	
    	// TODO Send welcome message through DMs
    }
	
	private void assignRoleAndChangeNickname(Guild server, Member member, VerificationReport report) {
		
		boolean isPrepa = false;
		
		List<Long> classificationRoles = verificationDAO.getUserClassificationRoles(report.getEmail());
		
	    try {
	    	for (Long role_id : classificationRoles) {
	    		Role role = server.getRoleById(role_id);
	    		
	    		isPrepa = role.getName().equalsIgnoreCase("prepa");
	    		
	    		server.addRoleToMember(member, role).queue(
    				success -> super.feedbackDev(
    						String.format("Given Role [%s] to [%s]", role.getName(), member.getEffectiveName())),
    				error -> super.feedbackDev("Failed to add role: " + error.getMessage())
   				);
	    	}
	    	
	    	String nickname = isPrepa ? report.getFullname().toUpperCase() : report.getFullname();
	    	
	    	// Role successfully added, now changing the nickname
        	server.modifyNickname(member, nickname).queue(
                nicknameSuccess -> super.feedbackDev(
						String.format("Successfully changed nickname from [%s] to [%s]", member.getEffectiveName(), nickname)),
                nicknameError -> super.feedbackDev("Failed to change nickname: " + nicknameError.getMessage())
            );
	    } catch (InsufficientPermissionException ipe) {
	    	String error = "Insufficient permissions to assign role or change nickname: " + ipe.getMessage();
	    	super.feedbackDev(error);
	    }
	}
}