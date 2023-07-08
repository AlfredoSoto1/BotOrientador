/**
 * 
 */
package application.admin.accounts;

import application.admin.discord.roles.DepartmentRole;
import application.admin.discord.roles.ExtraRoles;
import application.admin.discord.roles.TeamRole;

/**
 * @author Alfredo
 *
 */
public class MemberRecord {
	
	private String email;
	private String fullName;
	private String discordUser;

	private TeamRole team;
	private ExtraRoles roles;
	private DepartmentRole department;
	
	boolean isLogged;
	
	public MemberRecord() {
		
	}

	public MemberRecord(String email, String fullName, String discordUser, TeamRole team, ExtraRoles roles, DepartmentRole department) {
		this.email = email;
		this.fullName = fullName;
		this.discordUser = discordUser;
		this.team = team;
		this.roles = roles;
		this.department = department;
	}
	
	public void setLoggedIn(boolean isLogged) {
		this.isLogged = isLogged;
	}
	
	public boolean isLogged() {
		return isLogged;
	}

	public void setEmail(String email) {
		 this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDiscordUser() {
		return discordUser;
	}

	public void setDiscordUser(String discordUser) {
		this.discordUser = discordUser;
	}

	public TeamRole getTeam() {
		return team;
	}

	public void setTeam(TeamRole team) {
		this.team = team;
	}

	public ExtraRoles getRoles() {
		return roles;
	}

	public void setRoles(ExtraRoles roles) {
		this.roles = roles;
	}

	public DepartmentRole getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentRole department) {
		this.department = department;
	}
}
