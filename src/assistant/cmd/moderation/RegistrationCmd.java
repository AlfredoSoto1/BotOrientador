/**
 * 
 */
package assistant.cmd.moderation;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.interactions.CommandI;
import services.bot.interactions.InteractionModel;

/**
 * @author Alfredo
 */
public class RegistrationCmd extends InteractionModel implements CommandI {

	private boolean isGlobal;
	
	public RegistrationCmd() {

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
		return List.of(
			new OptionData(OptionType.STRING, "department", "adapt bot to server", true)
				.addChoice("ECE", "ECE")
				.addChoice("CSE", "CSE"),
				
			new OptionData(OptionType.STRING, "log-channel", "send server logs", true));
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
		
		Optional<TextChannel> textChannel = Optional.ofNullable(event.getGuild().getTextChannelById(logChannel));
		
		// Register and validate the server here
		
		// Check if the channel is in server
		if(textChannel.isPresent()) {
			event.reply("Assistant Registration Done").setEphemeral(true).queue();
			sendRegistrationEmbed(departmentOption, textChannel.get());
		} else {
			event.reply("Channel not found").setEphemeral(true).queue();
		}
	}
	
	private void sendRegistrationEmbed(String department, TextChannel textChannel) {
		
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
			Status: <STATUS HERE>
			""";
		
		String log_registration_title =
			"""
			**Log-Channel Registration**
			""";
		String log_registration_description =
			"""
			Channel: <Channel HERE>
			Status: <STATUS HERE>
			""";

		department_registration_description = String.format(department_registration_description, department);
		
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(40, 130, 138));
		embedBuider.setTitle(registration_title);
		
		embedBuider.addField(department_registration_title, department_registration_description, false);
		embedBuider.addField(log_registration_title, log_registration_description, false);
		
		textChannel.sendMessageEmbeds(embedBuider.build()).queue();
	}
}
