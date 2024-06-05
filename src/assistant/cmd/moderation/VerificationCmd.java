/**
 * 
 */
package assistant.cmd.moderation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import application.core.Configs;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import services.bot.interactions.ButtonI;
import services.bot.interactions.CommandI;

/**
 * @author Alfredo
 */
public class VerificationCmd implements CommandI, ButtonI {
	
	private static final String COMMAND_LABEL = "channel";
	private static final String VERIFY_BUTTON = "Verify";
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	private Button verifyButton;
	
	public VerificationCmd() {
		this.options = new ArrayList<>();
		
		this.options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "select channel", true));
		
		this.verifyButton = Button.success(VERIFY_BUTTON + VERIFY_BUTTON.hashCode(), VERIFY_BUTTON);
	}
	
	@Override
	public void init(ReadyEvent event) {

	}
	
	@Override
	public void dispose() {
		options.clear();
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
		return options;
	}

	@Override
	public Set<String> getButtonIDs() {
		return null;
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		Optional<String> enteredChannel = Optional.ofNullable(event.getOption(COMMAND_LABEL).getAsString());
		
		// Check if input was given successfully
		if (enteredChannel.isPresent()) {
			Optional<TextChannel> textChannel = Optional.ofNullable(event.getGuild().getTextChannelById(enteredChannel.get()));
			
			// Check if the channel is in server
			if (textChannel.isPresent()) {
				event.reply("Verification embed sent to " + textChannel.get().getName()).setEphemeral(true).queue();
				sendVerificationEmbed(textChannel.get());
			}
			else
				event.reply("Channel not found").setEphemeral(true).queue();
		} else {
			event.reply("Invalid entry").setEphemeral(true).queue();
		}
		
		
//		if (!validateUser(event.getGuild(), event.getMember())) {
//			event.reply("You dont have the permissions to run this command").setEphemeral(true).queue();
//			return;
//		}
//		
//		OptionMapping programOption = event.getOption(COMMAND_LABEL);
//		
//		if (programOption.getAsString().equals(OPTION_DISCONNECT)) {
//			event.reply("Shutting down...").setEphemeral(true).queue();
//			bot.shutdown();
//		} else {
//			// skip this action if no reply was provided
//			event.reply("Mmhh this command does nothing, try again with another one").setEphemeral(true).queue();
//		}
	}
	
	@Override
	public void onButtonEvent(String buttonID, ButtonInteractionEvent event) {
		System.out.println("Interacted with button");
	}
	
	private boolean validateUser(Guild server, Member member) {
		Role requiredRole = server.getRolesByName(Configs.get().assistantConfigs().developer_role, true).get(0);
		return member.getRoles().contains(requiredRole);
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

}
