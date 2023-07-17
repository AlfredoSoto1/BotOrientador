/**
 * 
 */
package services.bot.dbaccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.records.MemberRecord;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import services.bot.entry.BotConfigs;

/**
 * @author Alfredo
 *
 */
public class DBRoleManager {

	private Map<String, Role> loadedRoles;
	
	public DBRoleManager() {
		this.loadedRoles = new HashMap<>();
	}

	public void dispose() {
		loadedRoles.clear();
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
	
	public boolean roleValdiation(String targetRoleName, List<Role> memberRoles) {
		// No roles to test
		if(memberRoles.isEmpty())
			return false;
		
		for (Role role : memberRoles) {
			if (role.getName().equals(targetRoleName))
				// If parameter role matches target, return true
				return true;
		}
		
		// No matching roles to the target
		return false;
	}
	
	public boolean applyRoles(Guild server, MemberRecord record) {
		// Obtain the server from the name
		Member serverMember = server.getMembersByName(record.getDiscordUser(), true)
				.get(0);
		
		// For this version of the bot, the roles are going to be hard-coded
		
		if(record.getRoles().isPrepa())
			try {
				server.addRoleToMember(serverMember, loadedRoles.get("prepa")).queue();
			} catch(HierarchyException e) {
				consoleLog(record, "prepa");
			}
		
		if(record.getRoles().isEstudianteOrientador())
			try {
				server.addRoleToMember(serverMember, loadedRoles.get("EstudianteOrientador")).queue();
			} catch(HierarchyException e) {
				consoleLog(record, "EstudianteOrientador");
			}

		if(record.getRoles().isEstudianteGraduado())
			try {
				server.addRoleToMember(serverMember, loadedRoles.get("EstudianteGraduado")).queue();
			} catch(HierarchyException e) {
				consoleLog(record, "EstudianteGraduado");
			}
		
		if(record.getRoles().isConsejeroProfesional())
			try {
				server.addRoleToMember(serverMember, loadedRoles.get("ConsejeraProfesional")).queue();
			} catch(HierarchyException e) {
				consoleLog(record, "ConsejeraProfesional");
			}
		
		if(loadedRoles.containsKey(record.getTeam().getName()))
			try {
				server.addRoleToMember(serverMember, loadedRoles.get(record.getTeam().getName())).queue();
			} catch(HierarchyException e) {
				consoleLog(record, record.getTeam().getName());
			}
		
		if(loadedRoles.containsKey(record.getDepartment().getName()))
			try {
				server.addRoleToMember(serverMember, loadedRoles.get(record.getDepartment().getName())).queue();
			} catch(HierarchyException e) {
				consoleLog(record, record.getDepartment().getName());
			}
		

		try {
			server.modifyNickname(serverMember, record.getFullName()).queue();
		} catch(HierarchyException e) {
			consoleLog(record, record.getFullName());
			return false;
		}
		
		return true;
	}
	
	public boolean removeRoles(Guild server, String discordUser) {
		// Obtain the server from the name
		Member serverMember = server.getMembersByName(discordUser, true).get(0);
		
		for(Role role : serverMember.getRoles()) {
			if(!role.getName().equalsIgnoreCase(BotConfigs.DEVELOPER_ROLE_NAME))
				server.removeRoleFromMember(serverMember, role).queue();
		}
		
		try {
			server.modifyNickname(serverMember, null).queue();
			return true;
		} catch(HierarchyException e) {
			return false;
		}
	}
	
	private void consoleLog(MemberRecord record, String roleName) {
		System.out.println("[WARNING]: " + record.getFullName() + ", cannot assign role -> " + roleName);
	}
}
