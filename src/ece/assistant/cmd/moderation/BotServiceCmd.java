/**
 * 
 */
package ece.assistant.cmd.moderation;

import java.util.ArrayList;
import java.util.List;

import application.launch.AssistantAppEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.core.BotApplication;
import services.bot.managers.BotEventHandler;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class BotServiceCmd extends BotEventHandler implements CommandI {

	private static final String COMMAND_LABEL = "service";
	private static final String OPTION_DISCONNECT = "disconnect";
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	private BotApplication bot;
	
	public BotServiceCmd(BotApplication bot) {
		this.bot = bot;
		this.options = new ArrayList<>();
		
		this.options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "Choose a command", true)
			.addChoice("disconnect", OPTION_DISCONNECT)
		);
	}
	
	@Override
	@Deprecated
	public void init(ReadyEvent event) {

	}
	
	@Override
	public void dispose() {
		if(!BotEventHandler.validateEventDispose(this.getClass()))
			return;
		options.clear();
		
		BotEventHandler.registerDisposeEvent(this);
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
			event.reply("You dont have the permissions to run this command")
			.setEphemeral(true)
			.queue();
			return;
		}
		
		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		if (programOption.getAsString().equals(OPTION_DISCONNECT)) {
			event.reply("Shutting down...").setEphemeral(true).queue();
			bot.shutdown();
		} else {
			// skip this action if no reply was provided
			event.deferReply().queue();
		}
	}
	
	private boolean validateUser(Guild server, Member member) {
		// TODO: Read from local config file the assigned roles that can work with specific commands.
		Role requiredRole = server.getRolesByName(AssistantAppEntry.DEVELOPER_ROLE_NAME, true).get(0);
		return member.getRoles().contains(requiredRole);
	}
}
