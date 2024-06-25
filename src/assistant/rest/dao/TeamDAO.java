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

	public List<TeamDTO> getAllTeams(int offset, int limit, long server) {
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
					discserid = ?

			ORDER BY teamid
			OFFSET ?
			LIMIT  ?;
			""";
		List<TeamDTO> teams = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setLong(1, server);
			stmt.setInt(2, offset);
			stmt.setInt(3, limit);
			
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

	public Optional<TeamDTO> getTeam(String teamName, long server) {
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
					team.name = ? AND discserid = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		TeamDTO team = new TeamDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, teamName);
			stmt.setLong(2, server);
			
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
				
				found.set(true);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(team) : Optional.empty();
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
	
	public Optional<TeamDTO> deleteTeam(String teamname, long server) {
		final String SQL_DELETE = 
			"""
			WITH team_record AS (
                SELECT teamid 
                    FROM team
                        INNER JOIN discordrole     ON fdroleid = droleid
                        INNER JOIN serverownership ON fseoid   = seoid
                    WHERE
                        discserid = ? AND team.name = ?
                LIMIT 1
            ), 
            deleted_team AS (
			    DELETE FROM team
			        WHERE teamid = (SELECT team_record.teamid FROM team_record)
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
			stmt.setLong(1, server);
			stmt.setString(2, teamname);
			
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
