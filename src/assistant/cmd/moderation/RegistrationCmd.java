/**
 * 
 */
package assistant.cmd.moderation;

import java.util.ArrayList;
import java.util.List;

import application.core.Configs;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.interactions.CommandI;

/**
 * @author Alfredo
 */
public class RegistrationCmd implements CommandI {

	private boolean isGlobal;
	private List<OptionData> options;
	
	public RegistrationCmd() {
		this.options = new ArrayList<>();
		
		this.options.add(new OptionData(OptionType.STRING, "register", "register server", true)
			.addChoice("INEL/ICOM", "INEL/ICOM")
			.addChoice("INSO/CIIC", "INSO/CIIC")
		);
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
		return "assistant-registration";
	}

	@Override
	public String getDescription() {
		return "Register the server";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		event.reply("Verfication").setEphemeral(true).queue();
		
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
	
	private boolean validateUser(Guild server, Member member) {
		Role requiredRole = server.getRolesByName(Configs.get().assistantConfigs().developer_role, true).get(0);
		return member.getRoles().contains(requiredRole);
	}
}
