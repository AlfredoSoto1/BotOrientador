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
package assistant.embeds.games;

import java.awt.Color;
import java.util.List;

import assistant.rest.dto.UserRankDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Alfredo
 */
public class LevelUpEmbed {
	
	public MessageEmbed buildUserLevelUpCongrats(Color color, String pfpUrl) {
		return new EmbedBuilder()
			.setImage(pfpUrl)
			.setColor(color)
			.build();
	}
	
	public MessageEmbed buildLeaderboard(Color color, List<UserRankDTO> leaderboard) {
		EmbedBuilder embed = new EmbedBuilder()
			.setColor(color)
			.setTitle("Leaderboard");
		
		for (UserRankDTO rank : leaderboard) {
			embed.addField(rank.getUsername(),String.format(
				"""
				> Rank: %s
				> Level: %s
				> Milestone: %s
				""",
				rank.getRank(),
				rank.getLevel(),
				rank.getMilestone()), false);
		}
		
		return embed.build();
	}
	
	public MessageEmbed buildLeaderboardPosition(Color color, UserRankDTO userRank, String pfpUrl) {
		
		int progress = (int)(100.0 * userRank.getUserXP() / (double)userRank.getMilestone());
		
		return new EmbedBuilder()
			.setImage(pfpUrl)
			.setColor(color)
			.setTitle("Your rank position")
			.addField("User info", String.format(
				"""
				> Name: %s
				> Level: %s
				> Commands used: %s
				""",
				userRank.getUsername(),
				userRank.getLevel(),
				userRank.getCommandsUsed()), true)
			.addField("User Rank", String.format(
				"""
				> Rank: %s
				> Next milestone: %s
				""",
				userRank.getRank(),
				userRank.getMilestone()), true)
			
			.addField("Progress", generateProgressBar(progress), false)
			.setFooter("Current Progress: " + progress + "% for next milestone")
			.build();
	}
	
    private String generateProgressBar(int percent) {
        int length = 20; // Length of the progress bar
        int progressChars = (int) Math.ceil(length * (percent / 100.0)); // Number of progress characters
        
        StringBuilder progressBar = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i < progressChars) {
                progressBar.append("▰"); // Unicode block character for filled part
            } else {
                progressBar.append("▱"); // Unicode block character for unfilled part
            }
        }
        
        return progressBar.toString();
    }
}
