/**
 * 
 */
package assistant.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		
		DatabaseConnectionManager.instance()
			.getConnection(Configs.DB_CONNECTION).get().establishConnection(connection -> {
				
				// Prepare a new statement with the proper SQL statement
				// to obtain the user's data for verification
				PreparedStatement stmt = connection.prepareStatement(prepareSQLCheckVerification());
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
							.build();
					
					verificationResult.set(report);
				}
				result.close();
				stmt.close();
			});

		return Optional.ofNullable(verificationResult.get());
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
		select  email                 as email,
		        fname || ' ' || lname as fullname,
		        funfact               as funfact,
		        program.name          as program_name,
		        is_verified           as is_verified,
		        team.name             as team_name,
		        orgname               as orgname
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
