/**
 * 
 */
package application.services.botOrientador.validations;

import java.util.HashMap;
import java.util.Map;

import application.admin.accounts.MemberRecord;
import application.services.botOrientador.discord.BotServer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;

/**
 * @author Alfredo
 *
 */
public class RoleValidation {

	private Map<String, Role> loadedRoles;
	
	public RoleValidation() {
		this.loadedRoles = new HashMap<>();
	}

	/**
	 * This is a temporal method. This will be
	 * removed when the bot is capable of generating
	 * the roles and assign the proper permissions
	 */
	public void extractRolesFromDiscord(Guild server) {
		for(Role role : server.getRoles())
			loadedRoles.put(role.getName(), role);
	}
	
	
	public boolean applyRoles(Guild server, MemberRecord record) {
		// Obtain the server from the name
		Member serverMember = server.getMembersByName(record.getDiscordUser(), true)
				.get(0);
		
		// For this version of the bot, the roles are going to be hard-coded
		
		if(record.getRoles().isPrepa())
			server.addRoleToMember(serverMember, loadedRoles.get("prepa")).queue();
		
		if(record.getRoles().isEstudianteOrientador())
			server.addRoleToMember(serverMember, loadedRoles.get("EstudianteOrientador")).queue();

		if(record.getRoles().isEstudianteGraduado())
			server.addRoleToMember(serverMember, loadedRoles.get("EstudianteGraduado")).queue();
		
		if(record.getRoles().isConsejeroProfesional())
			server.addRoleToMember(serverMember, loadedRoles.get("ConsejeraProfesional")).queue();
		
		if(loadedRoles.containsKey(record.getTeam().getName()))
			server.addRoleToMember(serverMember, loadedRoles.get(record.getTeam().getName())).queue();
		
		if(loadedRoles.containsKey(record.getDepartment().getName()))
			server.addRoleToMember(serverMember, loadedRoles.get(record.getDepartment().getName())).queue();
		
		try {
			server.modifyNickname(serverMember, record.getFullName()).queue();
			return true;
		} catch(HierarchyException e) {
			return false;
		}
	}
	
	public boolean removeRoles(Guild server, String discordUser) {
		// Obtain the server from the name
		Member serverMember = server.getMembersByName(discordUser, true).get(0);
		
		for(Role role : serverMember.getRoles()) {
			if(!role.getName().equalsIgnoreCase(BotServer.DEVELOPER))
				server.removeRoleFromMember(serverMember, role).queue();
		}
		
		try {
			server.modifyNickname(serverMember, null).queue();
			return true;
		} catch(HierarchyException e) {
			return false;
		}
	}

	public void dispose() {
		loadedRoles.clear();
	}
}
