/**
 * 
 */
package assistant.cmd.moderation;

import java.awt.Color;
import java.util.ArrayList;
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
import services.bot.interactions.ButtonI;
import services.bot.interactions.CommandI;

/**
 * @author Alfredo
 */
public class VerificationCmd implements CommandI, ButtonI {
	
	private static final String COMMAND_LABEL = "channel";
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	public VerificationCmd() {
		this.options = new ArrayList<>();
		
		this.options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "select channel", true));
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
		// TODO Auto-generated method stub
	}
	
	private boolean validateUser(Guild server, Member member) {
		Role requiredRole = server.getRolesByName(Configs.get().assistantConfigs().developer_role, true).get(0);
		return member.getRoles().contains(requiredRole);
	}
	
	private void sendVerificationEmbed(TextChannel textChannel) {
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(40, 130, 138));
		embedBuider.setTitle("Server Verification");

		embedBuider.addField("Click on the button to verify", "", false);

		textChannel.sendMessageEmbeds(embedBuider.build()).queue();
	}

}
