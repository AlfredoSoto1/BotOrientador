/**
 * 
 */
package assistant.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.rest.dto.VerificationReport;
import assistant.rest.dto.VerificationReport.AtomicVerificationReport;
import assistant.rest.dto.VerificationReport.VerificationReportBuilder;
import net.dv8tion.jda.api.entities.Guild;

/**
 * @author Alfredo
 */
@Deprecated
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
		final String SQL =
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
			        longroleid            as team_role
			    from verification
			        inner join member          on verid   = member.fverid
			        inner join team            on teamid  = member.fteamid
			        inner join program         on progid  = fprogid
			        inner join all_people      on verid   = all_people.fverid
			        inner join discordrole     on droleid = fdroleid
			        inner join serverownership on seoid   = fseoid
			    where
			        discserid = ? and email = ?
			""";
		
		AtomicVerificationReport verificationResult = new AtomicVerificationReport(null);
		
		RunnableSQL rq = connection -> {
			// Prepare a new statement with the proper SQL statement
			// to obtain the user's data for verification
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setLong(1, server.getIdLong());
			stmt.setString(2, email);
			
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
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);

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
	public List<Long> getServerUserRoles(Guild server, String email) {
		final String SQL =
			"""
			select longroleid
			    from verification
			        inner join member          on fverid = verid
			        inner join memberrole      on fmemid = memid
			        inner join discordrole     on fdroleid = droleid
			        inner join serverownership on fseoid = seoid
			    where 
			        discserid = ? and email = ?
			""";
		
		List<Long> classificationRoles = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setLong(1, server.getIdLong());
			stmt.setString(2, email);
			
			// Execute the query and obtain the results
			ResultSet result = stmt.executeQuery();
			while(result.next())
				classificationRoles.add(result.getLong("longroleid"));
			
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);

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
		final String SQL = 
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
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, email);
			stmt.setString(2, funfact);
			
			stmt.executeUpdate();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
	}
}
