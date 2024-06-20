/**
 * 
 */
package assistant.discord.interaction;

import java.util.HashMap;
import java.util.Map;

import assistant.discord.object.MemberRole;
import assistant.rest.dao.InteractionModelDAO;
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
	
	/**
	 * This should be protected so that all sub classes
	 * of this interaction model can access the default CRUD operations
	 */
	protected InteractionModelDAO interactionModelDAO;
	
	protected InteractionModel() {
		this.modalEvents = new HashMap<>();
		this.buttonEvents = new HashMap<>();
		this.selectMenuEvents = new HashMap<>();
		this.interactionModelDAO = new InteractionModelDAO();
	}
	
	public void onInit(ReadyEvent event) {
		
	}
	
	public void onDispose() {
		
	}
	
	public Map<String, ButtonActionEvent> getRegisteredButtons() {
		return buttonEvents;
	}

	public Map<String, ModalActionEvent> getRegisteredModals() {
		return modalEvents;
	}
	
	public Map<String, SelectMenuActionEvent> getRegisteredSelectMenu() {
		return selectMenuEvents;
	}

	public void upsertCommand(ReadyEvent event) {
		if (!(this instanceof CommandI command))
			return;
		
		for(Guild server : event.getJDA().getGuilds()) {
			/*
			 * Commands that are global, are commands
			 * that are accessible throughout all servers
			 * and private channels that the bot can interact with the user.
			 */
			if(command.isGlobal()) {
				// For global commands, they get inserted
				// directly into the JDA when it loads.
				// This action can take a long time to see
				// the command fully activated in the server.
				event.getJDA().upsertCommand(
					command.getCommandName(), 
					command.getDescription()
				)
				.addOptions(command.getOptions())
				.queue();
			} else {
				// For non global commands, they get
				// inserted directly into the server.
				// This has no delay when looking for the
				// command in the server where the bot is.
				server.upsertCommand(
					command.getCommandName(),
					command.getDescription()
				)
				.addOptions(command.getOptions())
				.queue();
			}
		}
	}
	
	protected boolean validateCommandUse(SlashCommandInteractionEvent event) {
		// Obtain the required role to allow member to continue
		Role requiredRole = interactionModelDAO.getServerMemberRole(event.getGuild(), MemberRole.BOT_DEVELOPER);
		
		// If the role is not found, force the required role to be administrator
		if (requiredRole == null)
			requiredRole = event.getGuild().getRolesByName("administrator", true).get(0);
		
		// Validate if the member has the required role to continue
		boolean hasRole = event.getMember().getRoles().contains(requiredRole);
		
		// If it doesn't meet role criteria, send a message
		if(!hasRole)
			event.reply("You dont have the permissions to run this command").setEphemeral(true).queue();
		
		return hasRole;
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
	
	public void feedbackDev(String feedback, Object... parameters) {
		
	}
}
