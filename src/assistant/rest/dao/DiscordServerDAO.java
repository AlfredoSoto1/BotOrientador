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
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.database.SubTransactionResult;
import assistant.database.Transaction;
import assistant.database.TransactionError;
import assistant.database.TransactionStatementType;
import assistant.discord.object.MemberPosition;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.DiscordServerDTO;

/**
 * @author Alfredo
 */
@Repository
public class DiscordServerDAO {

	public DiscordServerDAO() {

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
	
	public List<DiscordServerDTO> getAllRegisteredDiscordServers(int offset, int limit) {
		final String SQL = 
			"""
			SELECT  seoid,
					discserid,
					log_channel,
					joined_at,
					abreviation,
					color
				
				FROM serverownership
					INNER JOIN department ON fdepid = depid
					
			ORDER BY seoid
			OFFSET ?
			LIMIT  ?;
			""";
		List<DiscordServerDTO> discordServers = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, offset);
			stmt.setInt(2, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				DiscordServerDTO discordServer = new DiscordServerDTO();
				discordServer.setId(result.getInt("seoid"));
				discordServer.setServerId(result.getLong("discserid"));
				discordServer.setLogChannelId(result.getLong("log_channel"));
				discordServer.setJoinedAt(result.getString("joined_at"));
				discordServer.setDepartment(result.getString("abreviation"));
				discordServer.setColor(result.getString("color"));
				
				discordServers.add(discordServer);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return discordServers;
	}
	
	public SubTransactionResult getRegisteredDiscordServer(long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  seoid,
					discserid,
					log_channel,
					joined_at,
					abreviation,
					color
				
				FROM serverownership
					INNER JOIN department ON fdepid = depid
				
				WHERE
					discserid = ?
			""", List.of(server));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
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
	
	public SubTransactionResult getEffectivePositionRole(MemberPosition rolePosition, long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
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
			""", List.of(rolePosition.getEffectiveName(), server));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public int insertDiscordServer(DiscordServerDTO discordServer) {
		final String SQL = 
			"""
			WITH department_selected AS (
				SELECT depid 
					FROM department
					WHERE abreviation = ? 
				LIMIT 1
			)
			INSERT INTO serverownership (color, discserid, log_channel, fdepid)
			    SELECT ?, ?, ?, department_selected.depid
				    FROM department_selected
			RETURNING seoid
			""";
		AtomicInteger idResult = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			connection.setAutoCommit(false);
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, discordServer.getDepartment());
			stmt.setString(2, discordServer.getColor());
			stmt.setLong(3, discordServer.getServerId());
			stmt.setLong(4, discordServer.getLogChannelId());
			
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
