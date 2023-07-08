///**
// * 
// */
//package application.hosts.bot.listeners;
//
//import java.util.List;
//
//import net.dv8tion.jda.api.entities.Guild;
//import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
//import net.dv8tion.jda.api.events.session.ReadyEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import net.dv8tion.jda.api.interactions.commands.OptionType;
//import net.dv8tion.jda.api.interactions.commands.build.OptionData;
//
///**
// * @author Alfredo
// *
// */
//public class SlashCommandListener extends ListenerAdapter {
//	
//	public SlashCommandListener() {
//		
//	}
//	
//	@Override
//	public void onReady(ReadyEvent event) {
//		List<Guild> guilds = event.getJDA().getGuilds();
//		
//		for(Guild server : guilds) {
//			server.upsertCommand("noises", "Just fart already!").queue();
//			
//			server.upsertCommand("name", "My name :)")
//			.addOptions(
//				new OptionData(OptionType.INTEGER, "number1", "Enter number", true)
//			).queue();
//		}
//	}
//	
//	@Override
//	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
//		
//		if(event.getName().equals("noises"))
//			event.reply("Noises done").queue();
//		if(event.getName().equals("name"))
//			event.reply(event.getUser().getName()).setEphemeral(true).queue();
//	}
//}
