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

import java.io.File;
import java.util.List;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.rest.service.GameService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

/**
 * @author Alfredo
 *
 */
public class CalendarCmd extends InteractionModel implements CommandI {

	private GameService commandEventService;
	
	private boolean isGlobal;
	
	public CalendarCmd() {
		this.commandEventService = Application.instance().getSpringContext().getBean(GameService.class);
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
		return "calendario";
	}

	@Override
	public String getDescription() {
		return "Provee un enlace rápido al Calendario Académico de UPRM";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of();
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.reply(String.format(
			"""
			Hola %s
			Aquí adjunto el calendario académico de UPRM.
			**Calendario Académico:** https://www.uprm.edu/decestu/calendario/
			También puedes añadir este calendario a tu calendario personal.
			Presta atención a la esquina inferior derecha del calendario.
			se ve asi:
			""", event.getUser().getAsMention()))
		.addFiles(FileUpload.fromData(new File("assistant/images/google_add_calendar.png")))
			.setEphemeral(event.isFromGuild()).queue();
		
		if (event.isFromGuild()) {
			// Update the user points stats when he uses the command
			commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
		}
	}
}
