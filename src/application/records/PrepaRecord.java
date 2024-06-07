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
@Deprecated
public class PrepaRecord extends UserRecord {
	
	private int serverLevel;

	private TeamRole team;
	private EORecord assignedEO;
	private DepartmentRole department;
	
	private List<Role> discordRoles;

	public PrepaRecord(UserRecord user) {
		super(user.getMemberID(), user.getEmail(), user.getActualName(), user.getDiscordUser());
	}

	public void setServerLevel(int serverLevel) {
		this.serverLevel = serverLevel;
	}

	public void setTeam(TeamRole team) {
		this.team = team;
	}

	public void setAssignedEO(EORecord assignedEO) {
		this.assignedEO = assignedEO;
	}

	public void setDepartment(DepartmentRole department) {
		this.department = department;
	}

	public void setDiscordRoles(List<Role> discordRoles) {
		this.discordRoles = discordRoles;
	}

	public int getServerLevel() {
		return serverLevel;
	}

	public TeamRole getTeam() {
		return team;
	}

	public EORecord getAssignedEO() {
		return assignedEO;
	}

	public DepartmentRole getDepartment() {
		return department;
	}

	public List<Role> getDiscordRoles() {
		return discordRoles;
	}
	
}
