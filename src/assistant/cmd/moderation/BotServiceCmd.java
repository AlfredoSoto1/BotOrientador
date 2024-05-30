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
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.core.BotApplication;
import services.bot.interactions.CommandI;

/**
 * @author Alfredo
 *
 */
public class BotServiceCmd implements CommandI {

	private static final String COMMAND_LABEL = "service";
	
	private static final String OPTION_DISCONNECT = "disconnect";
	private static final String OPTION_REGISTER_ECE = "register-ece-department";
	private static final String OPTION_REGISTER_CSE = "register-cse-department";
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	private BotApplication bot;
	
	public BotServiceCmd(BotApplication bot) {
		this.bot = bot;
		this.options = new ArrayList<>();
		
		this.options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "Choose a command", true)
			.addChoice("disconnect", OPTION_DISCONNECT)
			.addChoice("register-ece-department", OPTION_REGISTER_ECE)
			.addChoice("register-cse-department", OPTION_REGISTER_CSE)
		);
	}
	
	@Override
	@Deprecated
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
		return "server-management-service";
	}

	@Override
	public String getDescription() {
		return "Manage the bot service on server";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		if (!validateUser(event.getGuild(), event.getMember())) {
			event.reply("You dont have the permissions to run this command").setEphemeral(true).queue();
			return;
		}
		
		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		if (programOption.getAsString().equals(OPTION_DISCONNECT)) {
			event.reply("Shutting down...").setEphemeral(true).queue();
			bot.shutdown();
		} else {
			// skip this action if no reply was provided
			event.reply("Mmhh this command does nothing, try again with another one").setEphemeral(true).queue();
		}
	}
	
	private boolean validateUser(Guild server, Member member) {
		Role requiredRole = server.getRolesByName(Configs.get().assistantConfigs().developer_role, true).get(0);
		return member.getRoles().contains(requiredRole);
	}
}