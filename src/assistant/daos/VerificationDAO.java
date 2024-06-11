/**
 * 
 */
package assistant.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.core.Configs;
import assistant.models.VerificationReport;
import assistant.models.VerificationReport.AtomicVerificationReport;
import assistant.models.VerificationReport.VerificationReportBuilder;
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
	 * 
	 * @param email
	 * @param funfact
	 * @return verification report
	 */
	public Optional<VerificationReport> getUserReport(String email) {
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
	
	public List<Long> getUserClassificationRoles(String email) {
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
	
	public void confirmVerification(String email, String funfact) {
		
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
}
