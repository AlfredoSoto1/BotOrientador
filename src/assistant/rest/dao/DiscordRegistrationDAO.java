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
import assistant.rest.dto.DiscordRegistrationDTO;
import assistant.rest.dto.DiscordRoleDTO;

/**
 * @author Alfredo
 */
@Repository
public class DiscordRegistrationDAO {

	public DiscordRegistrationDAO() {

	}
	
	public List<String> getEffectiveRoles() {
		final String SQL = 
			"""
			select effectivename
				from discordrole
			group by effectivename
			order by effectivename asc
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
	
	public List<DiscordRegistrationDTO> getAllRegistrations(int offset, int limit) {
		final String SQL = 
			"""
			select  seoid,
					discserid,
					log_channel,
					joined_at,
					abreviation
				from serverownership
					inner join department on fdepid = depid
					
			order by seoid
			offset ?
			limit  ?;
			""";
		List<DiscordRegistrationDTO> discordServers = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, offset);
			stmt.setInt(2, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				DiscordRegistrationDTO discordServer = new DiscordRegistrationDTO();
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
	
	public Optional<DiscordRegistrationDTO> getRegistration(int id) {
		final String SQL = 
			"""
			select  seoid,
					discserid,
					log_channel,
					joined_at,
					abreviation
				from serverownership
					inner join department on fdepid = depid
				where
					seoid = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		DiscordRegistrationDTO discordServer = new DiscordRegistrationDTO();
		
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
	
	public List<DiscordRoleDTO> getAllRoles(int offset, int limit) {
		final String SQL = 
			"""
			select  droleid,
					name,
					effectivename,
					longroleid,
					discserid
				from discordrole
					inner join serverownership on fseoid = seoid
					
			order by droleid
			offset ?
			limit  ?;
			""";
		List<DiscordRoleDTO> roles = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, offset);
			stmt.setInt(2, limit);
			
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
	
	public Optional<DiscordRoleDTO> getRole(int id) {
		final String SQL = 
			"""
			select  droleid,
					name,
					effectivename,
					longroleid,
					discserid
				from discordrole
					inner join serverownership on fseoid = seoid
				where
					droleid = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		DiscordRoleDTO role = new DiscordRoleDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, id);
			
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
	
	public int insertDiscordServer(DiscordRegistrationDTO discordServer) {
		final String SQL = 
			"""
			with department_selected as (
				select distinct depid 
						from department
					where abreviation = ? 
				limit 1
			)
			insert into serverownership (discserid, log_channel, fdepid)
			    select ?, ?, department_selected.depid
				    from department_selected
			returning seoid
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
			with discord_server as (
				select distinct seoid 
						from serverownership 
					where discserid = ? 
				limit 1
			)
			insert into discordrole (name, effectivename, longroleid, fseoid)
			    select ?, ?, ?, discord_server.seoid
				    from discord_server
			returning droleid
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
