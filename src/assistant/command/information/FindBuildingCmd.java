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

import java.util.List;
import java.util.Optional;

import assistant.app.core.Application;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.rest.dto.BuildingDTO;
import assistant.rest.service.BuildingService;
import assistant.rest.service.GameService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class FindBuildingCmd extends InteractionModel implements CommandI {

	private static final String COMMAND_LABEL = "location";
	
	private BuildingService service;
	private GameService commandEventService;

	private boolean isGlobal;
	
	public FindBuildingCmd() {
		this.service = Application.instance().getSpringContext().getBean(BuildingService.class);
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
		return "salon";
	}

	@Override
	public String getDescription() {
		return "Obten información acerca de donde queda un salón en el recinto";
	}

	@Override
	public List<OptionData> getOptions(Guild server) {
		return List.of(
			new OptionData(OptionType.STRING, COMMAND_LABEL, "Dispone la localización del edificio en el que se encuentra", true));
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		// Obtain building codification from command
		String roomCode = event.getOption(COMMAND_LABEL).getAsString();
		
		// Look for the building associated to the room code provided
		Optional<BuildingDTO> building = service.findBuilding(roomCode);
		
		if (building.isPresent()) {
			event.replyFormat(				
				"""
				> ## Salon %s
				> El salon que me diste se puede encontrar en el edificio %s.
				> %s
				""", roomCode, building.get().getName(), building.get().getGpin())
				// Set to be ephemeral if the command was
				// called from the server.
				.setEphemeral(event.isFromGuild())
				.queue();
		} else {
			event.replyFormat(
				"""
				> ## No se pudo encontrar!
				> El salon %s que me diste no lo encuentro en mi base de datos :pensive:
				""", roomCode)
				// Set to be ephemeral if the command was
				// called from the server.
				.setEphemeral(event.isFromGuild())
				.queue();
		}
		
		if (event.isFromGuild()) {
			// Update the user points stats when he uses the command
			commandEventService.updateCommandUserCount(this.getCommandName(), event.getUser().getName(), event.getGuild().getIdLong());
		}
	}
}
