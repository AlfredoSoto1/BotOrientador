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
package assistant.discord.interaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import assistant.app.core.Application;
import assistant.discord.object.InteractionState;
import assistant.discord.object.MemberPosition;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.InteractionStateDTO;
import assistant.rest.service.DiscordService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.modals.Modal;

/**
 * 
 */
public abstract class InteractionModel {
	
	private Map<String, ModalActionEvent> modalEvents;
	private Map<String, ButtonActionEvent> buttonEvents;
	private Map<String, SelectMenuActionEvent> selectMenuEvents;

	private DiscordService service;
	
	protected InteractionModel() {
		this.modalEvents = new HashMap<>();
		this.buttonEvents = new HashMap<>();
		this.selectMenuEvents = new HashMap<>();
		
		this.service = Application.instance().getSpringContext().getBean(DiscordService.class);
	}

	/**
	 * @param event
	 */
	public void onJDAInit(ReadyEvent event) {}

	/**
	 * @param server
	 */
	public void onGuildInit(Guild server) {}
	
	/**
	 * On disposal
	 */
	public void onDispose() {}
	
	public Map<String, ButtonActionEvent> getRegisteredButtons() {
		return buttonEvents;
	}

	public Map<String, ModalActionEvent> getRegisteredModals() {
		return modalEvents;
	}
	
	public Map<String, SelectMenuActionEvent> getRegisteredSelectMenu() {
		return selectMenuEvents;
	}

	protected boolean validateCommandUse(SlashCommandInteractionEvent event) {
		
		// Obtain the administrator and developer role for further authentication
		Role administratorRole = event.getGuild().getRolesByName("administrator", true).get(0);
		Optional<Role> developerRole = this.getEffectiveRole(MemberPosition.BOT_DEVELOPER, event.getGuild());
		
		// Validate if the member has the required role to continue
		boolean hasRole = event.getMember().getRoles().contains(administratorRole);
		
		// Update flag to contain the developer role if present in database
		if(developerRole.isPresent())
			hasRole = hasRole || event.getMember().getRoles().contains(developerRole.get());
		
		// If it doesn't meet role criteria, send a message
		if(!hasRole)
			event.reply("You dont have the permissions to run this command").setEphemeral(true).queue();
		
		return hasRole;
	}
	
	protected Optional<Role> getEffectiveRole(MemberPosition position, Guild server) {
		// Obtain the required role to allow member to continue
		Optional<DiscordRoleDTO> drole = service.getEffectiveRole(position, server.getIdLong());
		
		if (drole.isEmpty())
			return Optional.empty();
		return Optional.ofNullable(server.getRoleById(drole.get().getRoleid()));
	}
	
	protected DiscordServerDTO getServerOwnerInfo(long server) {
		return service.getRegisteredDiscordServer(server).get();
	}
	
	protected boolean cacheUniqueState(InteractionState type, long state, long sever) {
		return service.cacheInteractionState(type, state, sever);
	}
	
	protected List<InteractionStateDTO> getCacheInteractionStates(InteractionState type, long sever) {
		return service.getCacheInteractionState(type, sever);
	}
	
	protected boolean deleteCacheInteractionStates(long state, long sever) {
		return service.deleteCacheInteractionState(state, sever);
	}
	
	protected Button registerButton(ButtonActionEvent action, Button nativeButton) {
		// Saves the button action event linked to one button id
		// This way it makes it easier for the listener adapter
		// to just handle this mapped button table.
		buttonEvents.put(nativeButton.getId(), action);
		
		// Return the native button. This gives the ability
		// to still work on the button right after being registered.
		return nativeButton;
	}
	
	protected Modal registerModal(ModalActionEvent action, Modal nativeModal) {
		// Saves the modal action event linked to one modal id
		// This way it makes it easier for the listener adapter
		// to just handle this mapped modal table.
		modalEvents.put(nativeModal.getId(), action);
		
		// Return the native modal. This gives the ability
		// to still work on the button right after being registered.
		return nativeModal;
	}

	protected StringSelectMenu registerSelectMenu(SelectMenuActionEvent action, StringSelectMenu nativeSelectMenu) {
		// Saves the select menu action event linked to one select menu id
		// This way it makes it easier for the listener adapter
		// to just handle this mapped select menu table.
		selectMenuEvents.put(nativeSelectMenu.getId(), action);
		
		// Return the native modal. This gives the ability
		// to still work on the button right after being registered.
		return nativeSelectMenu;
	}
}
