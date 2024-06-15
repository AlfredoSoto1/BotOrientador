/**
 * 
 */
package java.assistant.command.games;

import java.service.discord.interaction.CommandI;
import java.service.discord.interaction.InteractionModel;
import java.service.discord.interaction.MessengerI;
import java.util.List;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * 
 */
public class GamesCmd extends InteractionModel implements CommandI, MessengerI {

	private boolean isGlobal;
	
	public GamesCmd() {
		
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
		return "games";
	}

	@Override
	public String getDescription() {
		return "Provides cool games to have fun";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageReceived(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
