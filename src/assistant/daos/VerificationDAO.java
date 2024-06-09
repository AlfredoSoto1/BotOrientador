/**
 * 
 */
package assistant.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicBoolean;

import application.core.Configs;
import services.database.connections.DatabaseConnectionManager;

/**
 * @author Alfredo
 */
public class VerificationDAO {
	
	public VerificationDAO() {
		
	}
	
	/**
	 * 
	 * @param email
	 * @param description
	 */
	public boolean verifyUser(String email, String description) {
		
		AtomicBoolean verificationResult = new AtomicBoolean(false);
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(connection -> {
				
				PreparedStatement stmt = connection.prepareStatement(prepareSQLCheckVerification());
				stmt.setString(1, email);
				
				// Execute the query and obtain the results
				ResultSet result = stmt.executeQuery();
				
				while(result.next())
					verificationResult.set(result.getBoolean("is_verified"));
				
				result.close();
				stmt.close();
			});

		return verificationResult.get();
	}
	
	private String prepareSQLCheckVerification() {
		return 
		"""
		with all_people as (
		    select  fname,
		            lname,
		            fverid
		        from orientador
		    union all
		    select  fname, 
		            flname || ' ' || mlname as lname, 
		            fverid 
		        from prepa
		)
		select  email,
		        fname || ' ' || lname as fullname,
		        funfact,
		        program.name as program_name,
		        is_verified,
		        team.name as team_name,
		        orgname
		    from verification
		        inner join all_people on verid = fverid
		        inner join member     on verid = member.fverid
		        inner join program    on progid = fprogid
		        inner join team       on teamid = fteamid
		    where
		        email = ?;
		""";
	}
}
