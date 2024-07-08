/**
 * 
 */
package assistant.command.games;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.discord.interaction.MessengerI;
import assistant.rest.dto.UserRankDTO;
import assistant.rest.service.GameService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 */
public class GamesCmd extends InteractionModel implements CommandI, MessengerI {

	private static final String[] PARKOUR_GIFS = {
		"https://tenor.com/view/parkour-theoffice-freerunning-gif-5128248",
		"https://tenor.com/view/parkour-the-office-andy-bernard-gif-11765843"
	};
	
	private static final String[] SUS_GIFS = {
			"https://tenor.com/view/hmm-suspect-gif-22611582",
			"https://tenor.com/view/side-eye-dog-suspicious-look-suspicious-doubt-dog-doubt-gif-23680990",
			"https://tenor.com/view/sus-cat-2-suspicious-cat-the-cat-looks-suspiciously-cat-sits-in-front-of-food-the-ginger-cat-is-watching-gif-14890167989997543813",
			"https://tenor.com/view/the-rock-rock-gif-21708339",
			"https://tenor.com/view/the-rock-sus-the-rock-meme-the-rock-sus-meme-gif-23972805",
	};
	
	private static final String[] YES_GIFS = {
			"https://tenor.com/view/yes-yay-shaqoneal-excited-dance-gif-8818184967646032189",
	};
	
	private static final String[] SAD_GIFS = {
			"https://tenor.com/view/stitch-sad-sad-stitch-gif-14364046974961747120",
	};
	
	private static final String[] GN_GIFS = {
			"https://tenor.com/view/sleepy-gif-25544536",
			"https://tenor.com/view/puppy-tired-tired-puppy-doggo-cute-puppy-gif-12230785304918330904",
	};
	
	private static final String[] LEVELING_MESSAGES = {
		    "%s ¡Felicidades! Has alcanzado el nivel %s :tada:",
		    "%s ¡Subiste de nivel a %s! ¡Sigue así! :rocket:",
		    "%s ¡Avanzaste al nivel %s! ¡Increíble progreso! :fire:",
		    "%s ¡Has ascendido al nivel %s! Continúa desafiándote a ti mismo. :star:",
		    "%s ¡Nivel %s alcanzado! ¡Bien hecho! :balloon:",
		    "%s ¡Has alcanzado el nivel %s! ¡Eres genial! :confetti_ball:",
		    "%s ¡Enhorabuena por alcanzar el nivel %s! :mechanical_arm:",
		    "%s ¡Has avanzado al nivel %s! ¡Sigue así, eres imparable! :stars:",
		    "%s ¡Alcanzaste el nivel %s! ¡Qué logro tan emocionante! :video_game:",
		    "%s ¡Subiste al nivel %s! ¡Estás en camino hacia la cima! :trophy:"
	};

	
	private Random random;
	private GameService service;
	
	public GamesCmd() {
		this.random = new Random(System.currentTimeMillis());
		this.service = Application.instance().getSpringContext().getBean(GameService.class);
	}
	
	@Override
	public boolean isGlobal() {
		return false;
	}

	@Override
	@Deprecated
	public void setGlobal(boolean isGlobal) {
		// Server only command
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
	public List<OptionData> getOptions(Guild server) {
		return List.of();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		// Do Nothing
	}
	
	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		// Do Nothing
	}

	@Override
	public void messageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		TextChannel channel = event.getChannel().asTextChannel();
        
		String userMessage = message.getContentRaw();
		
		if (userMessage.equalsIgnoreCase("ping")) {
			channel.sendMessage("pong").queue();
		} else if (userMessage.equalsIgnoreCase("pong")) {
			channel.sendMessage("ping").queue();
		} else if (userMessage.contains("parkour")) {
			channel.sendMessage(PARKOUR_GIFS[random.nextInt(0, PARKOUR_GIFS.length)]).queue();
		} else if (userMessage.contains("good night") || userMessage.contains("gn") || userMessage.contains("noches")) {
			channel.sendMessage(GN_GIFS[random.nextInt(0, GN_GIFS.length)]).queue();
		} else if (userMessage.contains("sus") || userMessage.contains("random") || userMessage.contains("raro") || userMessage.contains("que cosa")) {
			channel.sendMessage(SUS_GIFS[random.nextInt(0, SUS_GIFS.length)]).queue();
		} else if (userMessage.contains("bye") || userMessage.contains("adios") || userMessage.contains("me voy")) {
			channel.sendMessage(SAD_GIFS[random.nextInt(0, SAD_GIFS.length)]).queue();
		}
		
		// Give one point of XP for every message they send
		Optional<UserRankDTO> userRank = service.giveXP(message.getAuthor().getName(), 1, event.getGuild().getIdLong());
		
		if (userRank.isPresent()) {
			if (userRank.get().isHasLevelup()) {
				channel.sendMessageFormat(
					LEVELING_MESSAGES[random.nextInt(0, LEVELING_MESSAGES.length)], 
					message.getAuthor().getAsMention(),
					userRank.get().getLevel()).queue();
			}
		}
	}

	@Override
	public List<Long> getMessageID() {
		return List.of();
	}

	@Override
	public void onMessageReaction(GenericMessageReactionEvent event) {
		// Do Nothing
	}
}
