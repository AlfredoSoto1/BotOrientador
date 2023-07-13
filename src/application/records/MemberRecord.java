/**
 * 
 */
package application.records;

import services.bot.orientador.roles.DepartmentRole;
import services.bot.orientador.roles.ExtraRoles;
import services.bot.orientador.roles.TeamRole;

/**
 * @author Alfredo
 *
 */
public class MemberRecord {
	
	private String email;
	private String fullName;
	private String discordUser;

	private String briefInfo;

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
		
		this.briefInfo = "";
	}
	
	public void setLoggedIn(boolean isLogged) {
		this.isLogged = isLogged;
	}
	
	public boolean isLogged() {
		return isLogged;
	}

	public void setBriefInfo(String brief) {
		this.briefInfo = brief;
	}
	
	public String getBriefInfo() {
		return briefInfo;
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
