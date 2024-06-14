/**
 * 
 */
package assistant.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import application.core.Configs;
import assistant.models.VerificationReport;
import assistant.models.VerificationReport.AtomicVerificationReport;
import assistant.models.VerificationReport.VerificationReportBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import services.database.connections.DatabaseConnectionManager;

/**
 * @author Alfredo
 */
public class VerificationDAO {
	
	private VerificationReportBuilder reportBuilder;
	
	public VerificationDAO() {
		this.reportBuilder = new VerificationReportBuilder();
	}
	
	/**
	 * Generates a user report as an optional.
	 * If the optional is empty, that means that there
	 * is no record in relation to the email provided.
	 * 
	 * @param server
	 * @param email
	 * @return Verification Role
	 */
	public Optional<VerificationReport> getUserReport(Guild server, String email) {
		AtomicVerificationReport verificationResult = new AtomicVerificationReport(null);
		
		final String SQL = prepareSQLVerificationReport();
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(connection -> {
				
				// Prepare a new statement with the proper SQL statement
				// to obtain the user's data for verification
				PreparedStatement stmt = connection.prepareStatement(SQL);
				stmt.setString(1, email);
				
				// Execute the query and obtain the results
				ResultSet result = stmt.executeQuery();
				
				while(result.next()) {
					// Using the verification report builder, build
					// a new instance of a verification report. The
					// data obtained from the selection query must 
					// be processed in the order shown below. Otherwise
					// it will not work.
					VerificationReport report = reportBuilder
							.email(result.getString("email"))
							.fullname(result.getString("fullname"))
							.funfact(result.getString("funfact"))
							.program(result.getString("program_name"))
							.verified(result.getBoolean("is_verified"))
							.team(result.getString("team_name"))
							.teamOrganization(result.getString("orgname"))
							.teamrole(result.getLong("team_role"))
							.build();
					
					verificationResult.set(report);
				}
				result.close();
				stmt.close();
			});

		return Optional.ofNullable(verificationResult.get());
	}
	
	/**
	 * Obtains all the roles that the member has
	 * in a server.
	 * 
	 * @param server
	 * @param email
	 * @return List of roles
	 */
	public List<Long> getUserClassificationRoles(Guild server, String email) {
		List<Long> classificationRoles = new ArrayList<>();
		
		final String SQL = prepareSQLClassificationRoles();
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(connection -> {
				
				PreparedStatement stmt = connection.prepareStatement(SQL);
				stmt.setString(1, email);
				
				// Execute the query and obtain the results
				ResultSet result = stmt.executeQuery();
				
				while(result.next())
					classificationRoles.add(result.getLong("classification_role"));
				
				result.close();
				stmt.close();
			});

		return classificationRoles;
	}
	
	/**
	 * Confirms the verification of the member.
	 * 
	 * @param server
	 * @param email
	 * @param funfact
	 */
	public void confirmVerification(Guild server, String email, String funfact) {
		
		final String SQL = prepareSQLConfirmVerification();
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(connection -> {
				
				PreparedStatement stmt = connection.prepareStatement(SQL);
				stmt.setString(1, email);
				stmt.setString(2, funfact);
				
				stmt.executeUpdate();
				stmt.close();
			});
	}
	
	/**
	 * Obtains the role of a member using the default
	 * naming roles of the EO team.
	 * The default naming of roles of the EO team is:
	 * - prepa
	 * - EstudianteOrientador
	 * - ...etc
	 * 
	 * If the optional is empty, that means that the email
	 * or role name is not in the database.
	 * 
	 * @param server
	 * @param rolename
	 * @param email
	 * @return Role
	 */
	public Optional<Role> getMemberRole(Guild server, String rolename, String email) {
		AtomicLong roleID = new AtomicLong(-1L);
		
		final String SQL = prepareSQLGetMemberRole();
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(connection -> {
				
				PreparedStatement stmt = connection.prepareStatement(SQL);
				stmt.setLong(1, server.getIdLong());
				stmt.setString(2, email);
				stmt.setString(3, rolename);
				
				// Execute the query and obtain the results
				ResultSet result = stmt.executeQuery();
				if(result.next())
					roleID.set(result.getLong("role_id"));
					
				result.close();
				stmt.close();
			});
		return Optional.ofNullable(server.getRoleById(roleID.get()));
	}
	
	private String prepareSQLVerificationReport() {
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
		select  email                 as email,
		        fname || ' ' || lname as fullname,
		        funfact               as funfact,
		        program.name          as program_name,
		        is_verified           as is_verified,
		        team.name             as team_name,
		        orgname               as orgname,
		        longid                as team_role
		    from verification
		        inner join member      on verid   = member.fverid
		        inner join team        on teamid  = member.fteamid
		        inner join program     on progid  = fprogid
		        inner join all_people  on verid   = all_people.fverid
		        inner join discordrole on droleid = fdroleid
		    where
		        email = ?
		""";
	}

	private String prepareSQLClassificationRoles() {
		return 
		"""
		select longid as classification_role
		    from memberrole
		        inner join member       on fmemid  = memid
		        inner join discordrole  on droleid = fdroleid
		        inner join verification on fverid  = verid
		    where 
		        email = ?
		""";
	}
	
	private String prepareSQLConfirmVerification() {
		return 
		"""
		with update_verification AS (
		    update verification
		            set 
		                is_verified = true,
		                verified_at = current_timestamp
		        where email = ?
		    returning verid
		)
		update member
		        set funfact = ?
		    from update_verification
		    where member.fverid = update_verification.verid;
		""";
	}
	
	private String prepareSQLGetMemberRole() {
		return 
		"""
		select longid as role_id
		    from orientador
		        inner join verification    on orientador.fverid = verid
		        inner join member          on member.fverid = verid
		        inner join memberrole      on fmemid = memid
		        inner join discordrole     on fdroleid = droleid
		        inner join serverownership on fseoid = seoid
		    where 
		        discserid = ? and email = ? and memberrole.name = ?
		""";
	}
}
