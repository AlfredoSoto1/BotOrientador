/**
 * 
 */
package assistant.cmd.moderation;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import assistant.daos.RegistrationDAO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.interactions.CommandI;
import services.bot.interactions.InteractionModel;

/**
 * @author Alfredo
 */
public class RegistrationCmd extends InteractionModel implements CommandI {

	private RegistrationDAO registrationDAO;
	
	private boolean isGlobal;
	
	public RegistrationCmd() {
		this.registrationDAO = new RegistrationDAO();
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
		return "assistant-registration";
	}

	@Override
	public String getDescription() {
		return "Registers the server for bot";
	}

	@Override
	public List<OptionData> getOptions() {
		List<Choice> choices = new LinkedList<>();
		List<String> departments = registrationDAO.getAvailableDepartments();
		
		for(String department : departments)
			choices.add(new Choice(department, department));
		
		return List.of(
			new OptionData(OptionType.STRING, "department", "adapt bot to server", true)
				.addChoices(choices),
				
			new OptionData(OptionType.STRING, "log-channel", "send server logs", true),
			new OptionData(OptionType.ATTACHMENT, "role-mapping", "registers each role", true));
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		if(!super.validateCommandUse(event))
			return;
		
		String logChannel = event.getOption("log-channel").getAsString();
		String departmentOption = event.getOption("department").getAsString();
		
		try {
			Long.parseLong(logChannel);
		} catch (NumberFormatException nfe) {
			event.reply("The id provided for the log-channel is not a valid number").setEphemeral(true).queue();
			return;
		}
		
		Optional<TextChannel> logTextChannel = Optional.ofNullable(event.getGuild().getTextChannelById(logChannel));
		
		// Check if the channel is in server
		if(!logTextChannel.isPresent()) {
			event.reply("Channel not found").setEphemeral(true).queue();
			return;
		}
		// Register the server directly on the database
		// This request returns a status flag to be used as output
		// in the embed message.
		String registrationStatus = registrationDAO.registerServer(event.getGuild().getIdLong(), Long.parseLong(logChannel), departmentOption);
		
		// Register server roles
		String roleStatus = registrationDAO.registerServerRoles(registrationStatus, event.getGuild().getIdLong(), event.getGuild().getRoles());
		
		// Prepare the embed message to display on log channel
		sendRegistrationEmbed(departmentOption, logTextChannel.get(), registrationStatus, roleStatus);
		
		// Reply to the client to close the response
		event.reply("Assistant Registration Done").setEphemeral(true).queue();
	}
	
	private void sendRegistrationEmbed(String department, TextChannel textChannel, String registrationStatus, String roleStatus) {
		
		/*
		 * Embedded messages
		 */
		String registration_title = 
			"""
		    **Server registration report**
		    """;
		String department_registration_title =
			"""
			 **Department Registration**
			""";
		String department_registration_description =
			"""
			Department: **%s**
			""";
		
		String log_registration_title =
			"""
			**Log-Channel Registration**
			""";
		String log_registration_description =
			"""
			Channel: **%s**
			Status: **%s**
			""";
		String roles_registration_title =
			"""
			**Roles Registration**
			""";
		String roles_registration_description =
			"""
			Status: **%s**
			""";

		department_registration_description = String.format(
				department_registration_description,
				department
			);
		log_registration_description = String.format(
				log_registration_description,
				textChannel.getIdLong(),
				registrationStatus
			);
		roles_registration_description = String.format(
				roles_registration_description,
				roleStatus
			);
		
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(40, 130, 138));
		embedBuider.setTitle(registration_title);
		
		embedBuider.addField(department_registration_title, department_registration_description, false);
		embedBuider.addField(log_registration_title, log_registration_description, false);
		embedBuider.addField(roles_registration_title, roles_registration_description, false);
		
		textChannel.sendMessageEmbeds(embedBuider.build()).queue();
	}
}
