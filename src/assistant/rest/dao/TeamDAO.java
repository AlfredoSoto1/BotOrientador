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

	public List<TeamDTO> getAll(int offset, int limit, String teamName) {
		final String SQL = 
			"""
			select  teamid,
			        fdroleid,
			        team.name                 as team_name,
			        team.orgname              as orgname,
			        discordrole.name          as role_name,
			        discordrole.effectivename as effectivename,
			        discordrole.longroleid    as longroleid,
			        serverownership.discserid as discserid

			    from team
			        inner join discordrole     on fdroleid = droleid
			        inner join serverownership on fseoid   = seoid
				       
				where
					team.name = ? or ? = 'None'

			order by teamid
			offset ?
			limit  ?;
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
			insert into team (name, orgname, fdroleid)
			    select ?, ?, droleid
		            from discordrole
			            inner join serverownership on fseoid = seoid
			        where
			            effectivename = ? and discserid = ?
			returning teamid
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
			with deleted_team as (
			    delete from team
			        where
			            teamid = ?
			    returning *
			)
			select  team.teamid       as teamid,
			        team.fdroleid     as fdroleid,
			        team.name         as team_name,
			        team.orgname      as orgname,
			        dir.name          as role_name,
			        dir.effectivename as effectivename,
			        dir.longroleid    as longroleid,
			        seo.discserid     as discserid
			    
			    from deleted_team     as team
			        inner join discordrole     as dir on fdroleid = droleid
			        inner join serverownership as seo on fseoid   = seoid
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
