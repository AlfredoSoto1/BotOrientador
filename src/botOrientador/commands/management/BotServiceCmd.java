/**
 * 
 */
package botOrientador.commands.management;

import java.util.ArrayList;
import java.util.List;

import botOrientador.entry.BotConfigs;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.GenericBot;
import services.bot.dbaccess.DBRoleManager;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class BotServiceCmd implements CommandI {

	private static final String COMMAND_LABEL = "service";

	private static final String OPTION_DISCONNECT = "disconnect";
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	private GenericBot genericBot;
	private DBRoleManager roleManager;
	
	public BotServiceCmd(GenericBot genericBot) {
		this.options = new ArrayList<>();
		this.genericBot = genericBot;
		this.roleManager = new DBRoleManager();
		
		this.options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "Choose a command", true)
			.addChoice("disconnect", OPTION_DISCONNECT)
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
		
		if(!roleManager.roleValdiation(BotConfigs.DEVELOPER_ROLE_NAME, event.getMember().getRoles())) {
			event.reply("You dont have the permissions to run this command")
			.setEphemeral(true).queue();
			// Exit failure
			return;
		}
		
		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		switch(programOption.getAsString()) {
		case OPTION_DISCONNECT:
			// End bot
			genericBot.interrupt();
			// Display message
			event.reply("Bot Disconnected").setEphemeral(true).queue();
			return;
		}
		
		event.deferReply().queue();
	}

}
