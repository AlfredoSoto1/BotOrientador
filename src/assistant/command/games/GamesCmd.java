/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package assistant.command.games;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.discord.interaction.KeywordResponder;
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
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
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
			"https://tenor.com/view/damn-shookt-shocked-gif-5580082",
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
	private KeywordResponder responder;
	
	private boolean smartResponseEnabled = true;
	
	public GamesCmd() {
		this.random = new Random(System.currentTimeMillis());
		this.responder = new KeywordResponder();
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
		return List.of(new OptionData(OptionType.STRING, "disable-smart-response", "disables/enables smart response", false)
			.addChoice("enable", "enable")
			.addChoice("disable", "disable"));
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		OptionMapping option = event.getOption("disable-smart-response");
		
		if (option == null) {
			// Do Nothing
			event.reply("lol").queue();
		} else if ("enable".equalsIgnoreCase(option.getAsString())) {
			if(!super.validateCommandUse(event))
				return;
			smartResponseEnabled = true;
			event.reply("enabled smart response").setEphemeral(true).queue();
		} else if ("disable".equalsIgnoreCase(option.getAsString())) {
			if(!super.validateCommandUse(event))
				return;
			smartResponseEnabled = false;
			event.reply("disabled smart response").setEphemeral(true).queue();
		} else {
			event.reply("lol").queue();
		}

		// Update the user points stats when he uses the command
		service.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
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
		
		// Handle predefined message responses
		handlePredefinedMessages(userMessage, channel, message);
		
		// Give one point of XP for every message sent
		processUserXP(message, channel, event.getGuild());
	}

	@Override
	public List<Long> getMessageID() {
		return List.of();
	}

	@Override
	public void onMessageReaction(GenericMessageReactionEvent event) {
		// Do Nothing
	}
	
	private void handlePredefinedMessages(String userMessage, TextChannel channel, Message message) {
		
		if (userMessage.equalsIgnoreCase("ping")) {
		    channel.sendMessage("pong").queue();
		} if (userMessage.equalsIgnoreCase("pong")) {
		    channel.sendMessage("ping").queue();
		} if (userMessage.matches(".*\\bparkour\\b.*")) {
		    channel.sendMessage(PARKOUR_GIFS[random.nextInt(PARKOUR_GIFS.length)]).queue();
		} if (userMessage.matches(".*\\b(good night|gn|noches)\\b.*")) {
		    channel.sendMessage(GN_GIFS[random.nextInt(GN_GIFS.length)]).queue();
		} if (userMessage.matches(".*\\b(sus|random|raro|que cosa)\\b.*")) {
		    channel.sendMessage(SUS_GIFS[random.nextInt(SUS_GIFS.length)]).queue();
		} if (userMessage.matches(".*\\b(bye|adios|me voy)\\b.*")) {
		    channel.sendMessage(SAD_GIFS[random.nextInt(SAD_GIFS.length)]).queue();
		}

		if (!smartResponseEnabled)
			return;
		
		String response = responder.generateResponse(userMessage);
		
		if (response.isEmpty() || response.isBlank())
			return;
		
	    channel.sendMessage(message.getAuthor().getAsMention() + " " + response).queue();
	}
	
	private void processUserXP(Message message, TextChannel channel, Guild server) {
		// Give one point of XP for every message they send
		// Obtain the previous user rank before updating if it exists
		Optional<UserRankDTO> userRank = service.giveXP(message.getAuthor().getName(), 1, server.getIdLong());

		userRank.ifPresent(rank -> {
			if (!rank.isHasLevelup())
				return;
			
			String levelUpMessage = String.format(
				LEVELING_MESSAGES[random.nextInt(0, LEVELING_MESSAGES.length)], 
				message.getAuthor().getAsMention(),
				userRank.get().getLevel());
			
			channel.sendMessageFormat(levelUpMessage).queue();
		});
	}
}
