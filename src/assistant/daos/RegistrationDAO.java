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
import net.dv8tion.jda.api.entities.Role;
import services.database.connections.DatabaseConnectionManager;

/**
 * @author Alfredo
 */
public class RegistrationDAO {
	
	public RegistrationDAO() {
		
	}
	
	/**
	 * 
	 * @param discordServerID
	 * @param logChannelID
	 * @param department
	 * @return status message
	 */
	public String registerServer(long discordServerID, long logChannelID, String department) {
		// Insert into database these parameters, for success
		// operation as output, return the id of the server ownership record inserted
		StringBuilder status = new StringBuilder();
		
		final String SQL = prepareSQLRegistration();
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(connection -> {
				
				PreparedStatement stmt = connection.prepareStatement(SQL);
				
				// Set the pre-set values to the query
				stmt.setString(1, department);
				stmt.setLong(2,   discordServerID);
				stmt.setLong(3,   logChannelID);
				
				// Execute the query and obtain the results
				try {
					stmt.executeUpdate();
				} catch (SQLException sqle) {
					if (!"23505".equals(sqle.getSQLState())) {
						status.append("ERROR");
						throw sqle;
					}
					status.append("FAILED: Already exists");
					return;
				}
				status.append("SUCCESS");
				stmt.close();
			});
		
		return status.toString();
	}
	
	/**
	 * 
	 * @param serverRegistrationStatus
	 * @param discordServerID
	 * @param serverRoles
	 * @return status message
	 */
	public String registerServerRoles(String serverRegistrationStatus, long discordServerID, List<Role> serverRoles) {
		if (!serverRegistrationStatus.contains("SUCCESS"))
			return "CANCELED";
		
		StringBuilder status = new StringBuilder();
		
		final String SQL = prepareSQLRoleRegistration(serverRoles.size());
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(connection -> {
				
				PreparedStatement stmt = connection.prepareStatement(SQL);
				
				AtomicInteger valuePosition = new AtomicInteger();
				setRoleValue(stmt, 1, valuePosition, serverRoles);
				stmt.setLong(valuePosition.get(), discordServerID);
				
				try {
					stmt.executeUpdate();
				} catch(SQLException sqle) {
					stmt.close();
					status.append("FAILED");
					return;
				}
				
				stmt.close();
				status.append("SUCCESS");
			});
		
		return status.toString();
	}
	
	/**
	 * 
	 * @return department list
	 */
	public List<String> getAvailableDepartments() {
		// Create a list containing the departments
		List<String> departments = new LinkedList<>();
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(connection -> {
				
				// Create a new statement and prepare
				// a result set to contain all results from query
				Statement stmt = connection.createStatement();
				ResultSet results = stmt.executeQuery(prepareSQLAvailableDepartments());
				
				// Store the results
				while(results.next())
					departments.add(results.getString("abreviation"));
				
				results.close();
				stmt.close();
			});
		
		return departments;
	}
	
	private String prepareSQLAvailableDepartments() {
		return 
		"""
		select abreviation
		        from department
		            inner join program on depid = fdepid
		group by abreviation
		order by abreviation asc;
		""";
	}
	
	private String prepareSQLRegistration() {
		return 
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
	}
	
	private String prepareSQLRoleRegistration(int rowCount) {
		String SQL = 
		"""
		with role_ids (name, id) as (
		    values %s
		)
		insert into discordrole (name, longid, fseoid)
		    select  role_ids.name, 
				    role_ids.id, 
				    seoid 
				from role_ids, serverownership
	            where
	                discserid = ?
		""";
		return String.format(SQL, getRolePlaceHolder(rowCount));
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
			pstmt.setLong(start++, role.getIdLong());
		}
		end.set(start);
	}
}
