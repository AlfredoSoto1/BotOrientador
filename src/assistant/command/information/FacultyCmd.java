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
package assistant.command.information;

import java.awt.Color;
import java.util.List;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.embeds.information.FacultyEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.FacultyDTO;
import assistant.rest.service.FacultyService;
import assistant.rest.service.GameService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class FacultyCmd extends InteractionModel implements CommandI {

	private FacultyEmbed embed;
	private FacultyService service;
	private GameService commandEventService;
	
	public FacultyCmd() {
		this.embed = new FacultyEmbed();
		this.service = Application.instance().getSpringContext().getBean(FacultyService.class);
		this.commandEventService = Application.instance().getSpringContext().getBean(GameService.class);
	}
	
	@Override
	public boolean isGlobal() {
		return false;
	}

	@Override
	@Deprecated
	public void setGlobal(boolean isGlobal) {
		// This is a server only command
	}
	
	@Override
	public String getCommandName() {
		return "facultad";
	}

	@Override
	public String getDescription() {
		return "Información acerca de nuestra facultad";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of(
			new OptionData(OptionType.INTEGER, "page", "select the page", true)
				.setRequired(true)
				.setMinValue(0));
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		int page = event.getOption("page").getAsInt();
		
		DiscordServerDTO discordServer = super.getServerOwnerInfo(event.getGuild().getIdLong());
		String department = discordServer.getDepartment();
		Color color = Color.decode("#" + discordServer.getColor());
		
		List<FacultyDTO> faculty = service.getFaculty(page, 3, department);
		long maxPages = service.getRecordCount(department) / 3;
		
		if (faculty.isEmpty()) {
			event.reply(String.format(
					"""
					Hmm no creo que hallan demasiados profesores en esta página,
					trata con un rango de páginas de [0-%s]
					""", maxPages))
				.setEphemeral(true).queue();
			return;
		}
		
		event.replyEmbeds(embed.buildFaculty(color, department, faculty, page, maxPages))
			.setEphemeral(true).queue();
		
		// Update the user points stats when he uses the command
		commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
	}
}
