/**
 * 
 */
package assistant.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import application.core.Configs;
import assistant.models.RegistrationStatus;
import assistant.models.RegistrationStatus.AtomicRegistrationStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import services.database.connections.DatabaseConnection.RunnableQueries;
import services.database.connections.DatabaseConnectionManager;

/**
 * @author Alfredo
 */
public class RegistrationDAO {
	
	public RegistrationDAO() {
		
	}
	
	/**
	 * Registers the server to the database with
	 * a given department.
	 * 
	 * @param server
	 * @param logChannelID
	 * @param department
	 * @return RegistrationStatus
	 */
	public RegistrationStatus registerServer(Guild server, long logChannelID, String department) {
		final String SQL =
			"""
			with department_selected as (
			    select depid
			            from department
			        where 
			            abreviation = ?
			)
			insert into serverownership (discserid, fdepid, log_channel)
			    select ?, depid, ?
			            from department_selected;
			""";
		AtomicRegistrationStatus status = new AtomicRegistrationStatus(RegistrationStatus.SUCCESS);
		
		RunnableQueries rq = connection -> {
			// Set the pre-set values to the query
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, department);
			stmt.setLong(2,   server.getIdLong());
			stmt.setLong(3,   logChannelID);
			
			// Execute the query and obtain the results
			try {
				stmt.executeUpdate();
			} catch (SQLException sqle) {
				if (!"23505".equals(sqle.getSQLState())) {
					status.set(RegistrationStatus.ERROR);
					throw sqle;
				} else {
					status.set(RegistrationStatus.FAILED);
				}
			} finally {
				stmt.close();
			}
		};
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(rq);
		
		return status.get();
	}
	
	/**
	 * Registers the given roles to the database
	 * 
	 * @param registrationStatus
	 * @param server
	 * @param serverRoles
	 * @return RegistrationStatus
	 */
	public RegistrationStatus registerServerRoles(RegistrationStatus registrationStatus, Guild server, List<Role> serverRoles) {
		if (registrationStatus != RegistrationStatus.SUCCESS)
			return RegistrationStatus.CANCEL;
		final String SQL_insert =
			"""
			with role_ids (name, effectivename, id) as (
			    values %s
			)
			insert into discordrole (name, longroleid, effectivename, fseoid)
			    select  role_ids.name, 
					    role_ids.id,
					    role_ids.effectivename,
					    seoid 
					from role_ids, serverownership
		            where
		                discserid = ?
			""";
		final String SQL = String.format(SQL_insert, getRolePlaceHolder(serverRoles.size()));
		AtomicRegistrationStatus status = new AtomicRegistrationStatus(RegistrationStatus.SUCCESS);
		
		RunnableQueries rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			
			AtomicInteger valuePosition = new AtomicInteger();
			setRoleValue(stmt, 1, valuePosition, serverRoles);
			stmt.setLong(valuePosition.get(), server.getIdLong());
			
			try {
				stmt.executeUpdate();
			} catch(SQLException sqle) {
				status.set(RegistrationStatus.FAILED);
			} finally {
				stmt.close();
			}
		};
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(rq);
		
		return status.get();
	}
	
	/**
	 * @return department list
	 */
	public List<String> getAvailableDepartments() {
		final String SQL = 
			"""
			select abreviation
			        from department
			            inner join program on depid = fdepid
			group by abreviation
			order by abreviation asc;
			""";
		// Create a list containing the departments
		List<String> departments = new LinkedList<>();
		
		RunnableQueries rq = connection -> {
			// Create a new statement and prepare
			// a result set to contain all results from query
			Statement stmt = connection.createStatement();
			ResultSet results = stmt.executeQuery(SQL);
			while(results.next())
				departments.add(results.getString("abreviation"));
			
			results.close();
			stmt.close();
		};
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(rq);
		
		return departments;
	}
	
	private String getRolePlaceHolder(int rowCount) {
	    StringBuilder placeHolder = new StringBuilder();

	    for (int i = 0; i < rowCount; i++) {
	        placeHolder.append("(?, ?)");
	        if (i < rowCount - 1) 
	            placeHolder.append(", ");
	    }
	    return placeHolder.toString();
	}
	
	private void setRoleValue(PreparedStatement pstmt, int start, AtomicInteger end, List<Role> roles) throws SQLException {
		for(Role role : roles) {
			pstmt.setString(start++, role.getName());
			pstmt.setString(start++, role.getName()); // Effective name FIXME
			pstmt.setLong(start++, role.getIdLong());
		}
		end.set(start);
	}
}
