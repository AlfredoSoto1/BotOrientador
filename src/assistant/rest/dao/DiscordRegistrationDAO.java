/**
 * 
 */
package assistant.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.discord.object.MemberPosition;
import assistant.rest.dto.RegisteredDiscordServerDTO;
import assistant.rest.dto.DiscordRoleDTO;

/**
 * @author Alfredo
 */
@Repository
public class DiscordRegistrationDAO {

	public DiscordRegistrationDAO() {

	}
	
	public List<String> getEffectiveRoleNames() {
		final String SQL = 
			"""
			SELECT effectivename
				FROM discordrole
				
			GROUP BY effectivename
			ORDER BY effectivename ASC
			""";
		List<String> effectiveRoles = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			Statement stmt = connection.createStatement();
			
			ResultSet result = stmt.executeQuery(SQL);
			while(result.next())
				effectiveRoles.add(result.getString("effectivename"));
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return effectiveRoles;
	}
	
	public List<RegisteredDiscordServerDTO> getAllRegisteredDiscordServers(int offset, int limit) {
		final String SQL = 
			"""
			SELECT  seoid,
					discserid,
					log_channel,
					joined_at,
					abreviation
				
				FROM serverownership
					INNER JOIN department ON fdepid = depid
					
			ORDER BY seoid
			OFFSET ?
			LIMIT  ?;
			""";
		List<RegisteredDiscordServerDTO> discordServers = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, offset);
			stmt.setInt(2, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				RegisteredDiscordServerDTO discordServer = new RegisteredDiscordServerDTO();
				discordServer.setId(result.getInt("seoid"));
				discordServer.setServerid(result.getLong("discserid"));
				discordServer.setLogChannelId(result.getLong("log_channel"));
				discordServer.setJoinedAt(result.getString("joined_at"));
				discordServer.setDepartment(result.getString("abreviation"));
				
				discordServers.add(discordServer);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return discordServers;
	}
	
	public Optional<RegisteredDiscordServerDTO> getRegisteredDiscordServer(int id) {
		final String SQL = 
			"""
			SELECT  seoid,
					discserid,
					log_channel,
					joined_at,
					abreviation
				
				FROM serverownership
					INNER JOIN department ON fdepid = depid
				
				WHERE
					seoid = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		RegisteredDiscordServerDTO discordServer = new RegisteredDiscordServerDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				discordServer.setId(result.getInt("seoid"));
				discordServer.setServerid(result.getLong("discserid"));
				discordServer.setLogChannelId(result.getLong("log_channel"));
				discordServer.setJoinedAt(result.getString("joined_at"));
				discordServer.setDepartment(result.getString("abreviation"));
				
				found.set(true);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(discordServer) : Optional.empty();
	}
	
	public List<DiscordRoleDTO> getAllRoles(int offset, int limit, long server) {
		final String SQL = 
			"""
			SELECT  droleid,
					name,
					effectivename,
					longroleid,
					discserid
				
				FROM discordrole
					INNER JOIN serverownership ON fseoid = seoid
					
				WHERE
					discserid = ?
					
			ORDER BY droleid
			OFFSET ?
			LIMIT  ?;
			""";
		List<DiscordRoleDTO> roles = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setLong(1, server);
			stmt.setInt(2, offset);
			stmt.setInt(3, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				DiscordRoleDTO role = new DiscordRoleDTO();
				role.setId(result.getInt("droleid"));
				role.setName(result.getString("name"));
				role.setEffectivename(result.getString("effectivename"));
				role.setRoleid(result.getLong("longroleid"));
				role.setServerid(result.getLong("discserid"));
				
				roles.add(role);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return roles;
	}
	
	public Optional<DiscordRoleDTO> getEffectiveRole(MemberPosition rolePosition, long server) {
		final String SQL = 
			"""
			SELECT  droleid,
					name,
					effectivename,
					longroleid,
					discserid
				
				FROM discordrole
					INNER JOIN serverownership ON fseoid = seoid
				
				WHERE
					effectivename = ? AND discserid = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		DiscordRoleDTO role = new DiscordRoleDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, rolePosition.getEffectiveName());
			stmt.setLong(2, server);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				role.setId(result.getInt("droleid"));
				role.setName(result.getString("name"));
				role.setEffectivename(result.getString("effectivename"));
				role.setRoleid(result.getLong("longroleid"));
				role.setServerid(result.getLong("discserid"));
				
				found.set(true);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(role) : Optional.empty();
	}
	
	public int insertDiscordServer(RegisteredDiscordServerDTO discordServer) {
		final String SQL = 
			"""
			WITH department_selected AS (
				SELECT depid 
					FROM department
					
					WHERE abreviation = ? 
				LIMIT 1
			)
			INSERT INTO serverownership (discserid, log_channel, fdepid)
			    SELECT ?, ?, department_selected.depid
				    FROM department_selected
			RETURNING seoid
			""";
		AtomicInteger idResult = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			connection.setAutoCommit(false);
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, discordServer.getDepartment());
			stmt.setLong(2, discordServer.getServerid());
			stmt.setLong(3, discordServer.getLogChannelId());
			
			try {
				ResultSet result = stmt.executeQuery();
				while(result.next())
					idResult.set(result.getInt("seoid"));
				result.close();
				stmt.close();
				connection.commit();
			} catch(SQLException sqle) {
				connection.rollback();
			} finally {
				connection.setAutoCommit(true);
			}
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return idResult.get();
	}
	
	public int insertRole(DiscordRoleDTO role) {
		final String SQL = 
			"""
			WITH discord_server AS (
				SELECT distinct seoid 
						FROM serverownership 
					WHERE discserid = ? 
				LIMIT 1
			)
			INSERT INTO discordrole (name, effectivename, longroleid, fseoid)
			    SELECT ?, ?, ?, discord_server.seoid
				    FROM discord_server
			RETURNING droleid
			""";
		AtomicInteger idResult = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			connection.setAutoCommit(false);
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setLong(1, role.getServerid());
			stmt.setString(2, role.getName());
			stmt.setString(3, role.getEffectivename());
			stmt.setLong(4, role.getRoleid());
			
			try {
				ResultSet result = stmt.executeQuery();
				while(result.next())
					idResult.set(result.getInt("droleid"));
				result.close();
				stmt.close();
				connection.commit();
			} catch(SQLException sqle) {
				connection.rollback();
			} finally {
				connection.setAutoCommit(true);
			}
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return idResult.get();
	}
}
