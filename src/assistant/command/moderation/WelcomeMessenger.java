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
package assistant.command.moderation;

import java.awt.Color;
import java.io.File;
import java.util.List;

import assistant.discord.interaction.InteractionModel;
import assistant.discord.interaction.MessengerI;
import assistant.embeds.moderation.VerificationEmbed;
import assistant.rest.dto.DiscordServerDTO;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.utils.FileUpload;

/**
 * @author Alfredo
 */
public class WelcomeMessenger extends InteractionModel implements MessengerI {

	private VerificationEmbed embed;
	
	public WelcomeMessenger() {
		this.embed = new VerificationEmbed();
	}

	@Override
	public List<Long> getMessageID() {
		return List.of();
	}
	
	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		event.getMember()
			.getUser().openPrivateChannel().queue(privateChannel -> onPrivateChannel(event.getGuild(), privateChannel));
	}

	@Override
	public void messageReceived(MessageReceivedEvent event) {
//		event.getMember()
//			.getUser().openPrivateChannel().queue(privateChannel -> onPrivateChannel(event.getGuild(), privateChannel));
	}
	
	private void onPrivateChannel(Guild server, PrivateChannel privateChannel) {
		
		DiscordServerDTO discordServer = super.getServerOwnerInfo(server.getIdLong());
		String department = discordServer.getDepartment();
		Color color = Color.decode("#" + discordServer.getColor());
		
		String imageUrl_TeamMade = "attachment://WelcomeBanner_TEAM_MADE.png";
		String imageUrl_InsoCiic = "attachment://WelcomeBanner_INSO_CIIC.png";
		
		if ("ECE".equalsIgnoreCase(department)) {
			File teamMade = new File("assistant/images/WelcomeBanner_TEAM_MADE.png");
			privateChannel.sendFiles(FileUpload.fromData(teamMade))
				.setEmbeds(embed.buildServerBanner(imageUrl_TeamMade, color)).queue();
		} else {
			File insociic = new File("assistant/images/WelcomeBanner_INSO_CIIC.png");
			privateChannel.sendFiles(FileUpload.fromData(insociic))
				.setEmbeds(embed.buildServerBanner(imageUrl_InsoCiic, color)).queue();
		}
		
		privateChannel.sendMessageEmbeds(embed.buildWelcomePrompt(server, department, color)).queue();
	}

	@Override
	public void onMessageReaction(GenericMessageReactionEvent event) {
		// Do Nothing
	}
}
