/**
 * 
 */
package assistant.command.moderation;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

/**
 * @author Alfredo
 */
public class RoleSelectionCmd extends InteractionModel implements CommandI {

	private boolean isGlobal;
	private StringSelectMenu roleSelection;
	
	public RoleSelectionCmd() {
		// Create the role selection menu
		super.registerSelectMenu(this::onRoleSelection, 		
			roleSelection = StringSelectMenu.create("role-selection")
	            .setPlaceholder("Select your roles")
	            .setMinValues(0) 
	            .setMaxValues(3)
	            .addOptions(
	                SelectOption.of("Role 1", "role1"),
	                SelectOption.of("Role 2", "role2"),
	                SelectOption.of("Role 3", "role3")
	            )
            .build()
           );
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
		return "assistant-role-selection";
	}

	@Override
	public String getDescription() {
		return "Assigns embed for displaying custom role selection";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of(
			new OptionData(OptionType.STRING, "log-channel", "send server logs", true),
			new OptionData(OptionType.STRING, "role-selection-channel", "send role selection", true));
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		if(!super.validateCommandUse(event))
			return;
		
		String logChannel = event.getOption("log-channel").getAsString();
		String roleChanel = event.getOption("role-selection-channel").getAsString();
		
		try {
			Long.parseLong(logChannel);
		} catch (NumberFormatException nfe) {
			event.reply("The id provided for the log-channel is not a valid number").setEphemeral(true).queue();
			return;
		}
		
		try {
			Long.parseLong(roleChanel);
		} catch (NumberFormatException nfe) {
			event.reply("The id provided for the role-selection-channel is not a valid number").setEphemeral(true).queue();
			return;
		}
		
		Optional<TextChannel> logTextChannel = Optional.ofNullable(event.getGuild().getTextChannelById(logChannel));
		Optional<TextChannel> roleTextChannel = Optional.ofNullable(event.getGuild().getTextChannelById(roleChanel));
		
		// Register and validate the log channel here
		
		// Check if the channel is in server
		if(logTextChannel.isPresent()) {
			event.reply("Log channel set-up completed").setEphemeral(true).queue();
		} else {
			event.reply("Log channel not found").setEphemeral(true).queue();
			return;
		}
		
		if(roleTextChannel.isPresent()) {
			event.reply("Role selection embed sent to channel").setEphemeral(true).queue();
			sendRoleSelectionEmbed(roleTextChannel.get());
		} else {
			event.reply("Role selection channel not found").setEphemeral(true).queue();
			return;
		}
	}
	
	private void sendRoleSelectionEmbed(TextChannel textChannel) {
		/*
		 * Embedded messages
		 */
		String role_selection_title = 
			"""
		    **Role selection**
		    """;
		String role_gaming_title =
			"""
			 **Gaming roles**
			""";
		String role_gaming_description =
			"""
			@Fortnite
			""";
		
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(40, 130, 138));
		embedBuider.setTitle(role_selection_title);
		
		embedBuider.addField(role_gaming_title, role_gaming_description, false);
		
		textChannel.sendMessageEmbeds(embedBuider.build())
			.addActionRow(roleSelection)
			.queue();
	}
	
	private void onRoleSelection(StringSelectInteractionEvent event) {
		event.reply("Selected").setEphemeral(true).queue();
	}
}
