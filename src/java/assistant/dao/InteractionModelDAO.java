/**
 * 
 */
package java.assistant.dao;

import java.assistant.model.MemberRole;
import java.service.database.DatabaseConnection.RunnableQueries;
import java.service.database.DatabaseConnectionManager;
import java.service.server.core.Configs;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicLong;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

/**
 * @author Alfredo
 */
public class InteractionModelDAO {
	
	public InteractionModelDAO() {
		
	}
	
	/**
	 * Obtains the role of the specified member role enum
	 * provided as parameter of a server.
	 * 
	 * @param server
	 * @param memberRole
	 * @param email
	 * @return role
	 */
	public Role getServerMemberRole(Guild server, MemberRole memberRole) {
		final String SQL = 
			"""
			select distinct longroleid
			    from discordrole
			        inner join serverownership on seoid = fseoid
			    where 
			        effectivename = ? and discserid = ?
			limit 1
			""";
		
		// Set default value to -1L. Meaning that is invalid
		// In case that the query cannot find the proper role
		// it will just return null as default.
		AtomicLong roleID = new AtomicLong(-1L);
		
		RunnableQueries rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			
			stmt.setString(1, memberRole.getName());
			stmt.setLong(2, server.getIdLong());
			
			// Execute the query and obtain single role
			ResultSet result = stmt.executeQuery();
			if(result.next())
				roleID.set(result.getLong("longroleid"));
				
			result.close();
			stmt.close();
		};
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(rq);
		
		// Check JDA documentation for when it returns null for invalid IDs
		return server.getRoleById(roleID.get());
	}
}
