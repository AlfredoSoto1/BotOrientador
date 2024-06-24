/**
 * 
 */
package assistant.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.TeamDTO;

/**
 * @author Alfredo
 */
@Repository
public class TeamDAO {

	public TeamDAO() {
		
	}

	public List<TeamDTO> getAllTeams(int offset, int limit, String teamName) {
		final String SQL = 
			"""
			SELECT  teamid,
			        fdroleid,
			        team.name        AS team_name,
			        team.orgname     AS orgname,
			        dr.name          AS role_name,
			        dr.effectivename AS effectivename,
			        dr.longroleid    AS longroleid,
			        so.discserid     AS discserid
			    
			    FROM team
			        INNER JOIN discordrole     AS dr ON fdroleid = droleid
			        INNER JOIN serverownership AS so ON fseoid   = seoid
				       
				WHERE
					team.name = ? OR ? = 'None'

			ORDER BY teamid
			OFFSET ?
			LIMIT  ?;
			""";
		List<TeamDTO> teams = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, teamName);
			stmt.setString(2, teamName);
			stmt.setInt(3, offset);
			stmt.setInt(4, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				TeamDTO team = new TeamDTO();
				team.setId(result.getInt("teamid"));
				team.setName(result.getString("team_name"));
				team.setOrgname(result.getString("orgname"));
				
				DiscordRoleDTO role = new DiscordRoleDTO();
				role.setId(result.getInt("fdroleid"));
				role.setRoleid(result.getLong("longroleid"));
				role.setServerid(result.getLong("discserid"));
				role.setName(result.getString("role_name"));
				role.setEffectivename(result.getString("effectivename"));
				team.setTeamRole(role);
				
				teams.add(team);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return teams;
	}

	public int insertTeam(TeamDTO team) {
		final String SQL = 
			"""
			INSERT INTO team (name, orgname, fdroleid)
			    SELECT ?, ?, droleid
		            FROM discordrole
			            INNER JOIN serverownership ON fseoid = seoid
			        WHERE
			            effectivename = ? AND discserid = ?
			RETURNING teamid
			""";
		AtomicInteger idResult = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, team.getName());
			stmt.setString(2, team.getOrgname());
			stmt.setString(3, team.getTeamRole().getEffectivename());
			stmt.setLong(4, team.getTeamRole().getServerid());
			
			ResultSet result = stmt.executeQuery();
			while(result.next())
				idResult.set(result.getInt("teamid"));
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return idResult.get();
	}
	
	public Optional<TeamDTO> deleteTeam(int id) {
		final String SQL_DELETE = 
			"""
			WITH deleted_team AS (
			    DELETE FROM team
			        WHERE teamid = ?
			    RETURNING *
			)
			SELECT  team.teamid      AS teamid,
			        team.fdroleid    AS fdroleid,
			        team.name        AS team_name,
			        team.orgname     AS orgname,
			        dr.name          AS role_name,
			        dr.effectivename AS effectivename,
			        dr.longroleid    AS longroleid,
			        so.discserid     AS discserid
			    
			    FROM deleted_team    AS team
			        INNER JOIN discordrole     AS dr ON fdroleid = droleid
			        INNER JOIN serverownership AS so ON fseoid   = seoid
			""";
		AtomicBoolean deleted = new AtomicBoolean(false);
		TeamDTO team = new TeamDTO();

		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL_DELETE);
			stmt.setInt(1, id);
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				team.setId(result.getInt("teamid"));
				team.setName(result.getString("team_name"));
				team.setOrgname(result.getString("orgname"));
				
				DiscordRoleDTO role = new DiscordRoleDTO();
				role.setId(result.getInt("fdroleid"));
				role.setRoleid(result.getLong("longroleid"));
				role.setServerid(result.getLong("discserid"));
				role.setName(result.getString("role_name"));
				role.setEffectivename(result.getString("effectivename"));
				team.setTeamRole(role);
				
				deleted.set(true);
			}
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return deleted.get() ? Optional.of(team) : Optional.empty();
	}
}
