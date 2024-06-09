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

import application.core.Configs;
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
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(connection -> {
				
				PreparedStatement stmt = connection.prepareStatement(prepareSQLRegistration());
				
				// Set the pre-set values to the query
				stmt.setString(1, department);
				stmt.setLong(2, discordServerID);
				stmt.setLong(3, logChannelID);
				
				// Execute the query and obtain the results
				try {
					stmt.executeQuery();
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
}
