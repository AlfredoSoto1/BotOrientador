/**
 * 
 */
package application.records;

import java.util.List;

import net.dv8tion.jda.api.entities.Role;
import services.bot.orientador.roles.DepartmentRole;
import services.bot.orientador.roles.TeamRole;

/**
 * @author Alfredo
 *
 */
public class EORecord extends UserRecord {

	private int serverLevel;

	private TeamRole team;
	private DepartmentRole department;
	
	private List<Role> discordRoles;

	public EORecord(UserRecord user) {
		super(user.getMemberID(), user.getEmail(), user.getActualName(), user.getDiscordUser());
	}

	public int getServerLevel() {
		return serverLevel;
	}

	public void setServerLevel(int serverLevel) {
		this.serverLevel = serverLevel;
	}

	public TeamRole getTeam() {
		return team;
	}

	public void setTeam(TeamRole team) {
		this.team = team;
	}

	public DepartmentRole getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentRole department) {
		this.department = department;
	}

	public List<Role> getDiscordRoles() {
		return discordRoles;
	}

	public void setDiscordRoles(List<Role> discordRoles) {
		this.discordRoles = discordRoles;
	}
}
