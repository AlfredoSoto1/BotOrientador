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

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.embeds.games.LevelUpEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.UserRankDTO;
import assistant.rest.service.GameService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 */
public class LeaderboardCmd extends InteractionModel implements CommandI {

	private LevelUpEmbed embed;
	private GameService service;
	private GameService commandEventService;
	
	public LeaderboardCmd() {
		this.embed = new LevelUpEmbed();
		this.service = Application.instance().getSpringContext().getBean(GameService.class);
		this.commandEventService = Application.instance().getSpringContext().getBean(GameService.class);
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
		return "rank";
	}

	@Override
	public String getDescription() {
		return "user ranking";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of(
			new OptionData(OptionType.STRING, "insights", "insights of user data", true)
				.addChoice("level", "level")
				.addChoice("leaderboard", "leaderboard"));
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
		Color color = Color.decode("#" + discordServer.getColor());
		
		String option = event.getOption("insights").getAsString();
		
		if ("level".equals(option)) {
			Optional<UserRankDTO> userData = service.getUserLeaderboardPosition(event.getUser().getName(), event.getGuild().getIdLong());
			
			if (userData.isPresent()) {
				// Show the level of the user
				event.replyEmbeds(embed.buildLeaderboardPosition(color, userData.get(), event.getUser().getAvatarUrl())).queue();
			} else {
				event.reply("Hmm no te en cuentro en mi base de datos para poder enceñarte tu nivel :pensive:")
					.setEphemeral(true).queue();
			}
		} else if ("leaderboard".equals(option))  {
			List<UserRankDTO> leaderboard = service.getLeaderboard(event.getGuild().getIdLong());
			event.replyEmbeds(embed.buildLeaderboard(color, leaderboard)).queue();
		}
		
		// Update the user points stats when he uses the command
		commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
	}
}
