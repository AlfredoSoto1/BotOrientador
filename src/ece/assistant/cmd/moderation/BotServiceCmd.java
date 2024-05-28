/**
 * 
 */
package ece.assistant.cmd.moderation;

import java.util.ArrayList;
import java.util.List;

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
//	private DBRoleManager roleManager;
	
	public BotServiceCmd(BotApplication bot) {
		this.bot = bot;
		this.options = new ArrayList<>();
//		this.roleManager = new DBRoleManager();
		
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
		
		// Validate for user role before executing command
//		if(!roleManager.roleValdiation(AssistantAppEntry.DEVELOPER_ROLE_NAME, event.getMember().getRoles())) {
//			ReplyCallbackAction reply = event.reply("You dont have the permissions to run this command");
//			reply.setEphemeral(true);
//			reply.queue();
//			return;
//		}
		
		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		switch(programOption.getAsString()) {
		case OPTION_DISCONNECT:
			event.reply("Shutting down...").setEphemeral(true).queue();
			bot.shutdown();
			return;
		}
		
		// skip this action if no reply was provided
		event.deferReply().queue();
	}
}
