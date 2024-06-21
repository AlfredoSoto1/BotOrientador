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

	public List<TeamDTO> getAll(int offset, int limit) {
		final String SQL = 
			"""
			select  teamid,
					fdroleid,
					name,
					orgname
				from team
			order by teamid
			offset ?
			limit  ?;
			""";
		List<TeamDTO> teams = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, offset);
			stmt.setInt(2, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				TeamDTO team = new TeamDTO();
				team.setId(result.getInt("teamid"));
				team.setName(result.getString("name"));
				team.setOrgname(result.getString("orgname"));
				
				DiscordRoleDTO role = new DiscordRoleDTO();
				team.setTeamRole(role);
				
				teams.add(team);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return teams;
	}

	public Optional<TeamDTO> getTeam(int id) {
		final String SQL = 
			"""
			select  teamid,
					fdroleid,
					name,
					orgname
				from team
				where teamid = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		TeamDTO team = new TeamDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				team.setId(result.getInt("teamid"));
				team.setName(result.getString("name"));
				team.setOrgname(result.getString("orgname"));
				
				DiscordRoleDTO role = new DiscordRoleDTO();
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
			insert into team (name, orgname, fdroleid)
			    select ?, ?, droleid
			            from discordrole
			        where
			            effectivename = ?
			returning teamid
			""";
		AtomicInteger idResult = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, team.getName());
			stmt.setString(2, team.getOrgname());
			stmt.setString(3, team.getTeamRole().getEffectivename());
			
			ResultSet result = stmt.executeQuery();
			while(result.next())
				idResult.set(result.getInt("teamid"));
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return idResult.get();
	}
}
