/**
 * 
 */
package assistant.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.BatchTransaction;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.database.SubTransactionResult.ResultReference;
import assistant.database.Transaction;
import assistant.database.TransactionError;
import assistant.database.TransactionStatementType;
import assistant.discord.object.MemberPosition;
import assistant.discord.object.MemberProgram;
import assistant.discord.object.MemberRetrievement;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.MemberDTO;
import assistant.rest.dto.TeamDTO;

/**
 * @author Alfredo
 */
@Repository
public class MemberDAO {
	
	public MemberDAO() {
		
	}
	
	public List<EmailDTO> getEmails(int offset, int limit, long server) {
		final String SQL_SELECT =
			"""
			SELECT memid, email 
				FROM member
					INNER JOIN program         AS pro ON fprogid    = progid
					INNER JOIN department      AS dep ON pro.fdepid = depid
					INNER JOIN serverownership AS seo ON seo.fdepid = depid
				
				WHERE
					discserid = ? OR ? = -1
			
			ORDER BY memid
			""";
		List<EmailDTO> emails = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT);
			stmt.setLong(1, server);
			stmt.setLong(2, server);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				EmailDTO member = new EmailDTO();
				member.setId(result.getInt("memid"));
				member.setEmail(result.getString("email"));
				emails.add(member);
			}
			result.close();
			stmt.close();
		};
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return emails;
	}
	
	public List<MemberDTO> getMembers(int offset, int limit, MemberRetrievement retrievement, long server) {
		final String SQL_SELECT =
			"""
			WITH all_people AS (
			    SELECT  orid         AS identifier,
			            fname        AS firstname,
			            lname        AS lastname,
			            '-'          AS initial,
			            '-'          AS sex,
			            'orientador' AS type,
			            fmemid
			        FROM orientador
			    
			    UNION ALL
			    SELECT  prepaid                 AS identifier,
			            fname                   AS firstname, 
			            flname || ' ' || mlname AS lastname,
			            initial                 AS initial, 
			            sex                     AS sex,
			            'prepa'                 AS type, 
			            fmemid
			        FROM prepa
			),
			people_with_verification AS (
			    SELECT  mb.memid,
			            identifier,
			            firstname,
			            lastname,
			            initial,
			            sex,
			            mb.email,
			            pr.name                              AS program_name,
			            COALESCE(jm.username, 'No username') AS username,
			            COALESCE(jm.funfact,  'No fun fact') AS funfact,
			            CASE WHEN jm.jmid IS NULL THEN FALSE ELSE TRUE END AS is_verified,
			            type,
			            discserid
			        FROM all_people
			            INNER JOIN member          AS mb ON fmemid     = memid
			            INNER JOIN program         AS pr ON mb.fprogid = progid
			            LEFT  JOIN joinedmember    AS jm ON jm.fmemid  = memid
			            
			            INNER JOIN department      AS dp ON dp.depid   = pr.fdepid 
			            INNER JOIN serverownership AS so ON dp.depid   = so.fdepid
			)
			SELECT *
			    FROM people_with_verification
			    WHERE
			        (discserid = ? OR ? = -1) AND 
			        (
			            (? = 'EVERYONE')                                 OR 
			            (? = 'ALL_PREPA'        AND type = 'prepa')      OR
			            (? = 'ALL_ORIENTADOR'   AND type = 'orientador') OR
			
			            (? = 'ALL_VERIFIED'     AND is_verified = TRUE)  OR
			            (? = 'ALL_NON_VERIFIED' AND is_verified = FALSE) OR
			            
			            (? = 'VERIFIED_PREPA'      AND is_verified = TRUE AND type = 'prepa')      OR
			            (? = 'VERIFIED_ORIENTADOR' AND is_verified = TRUE AND type = 'orientador') OR
			
			            (? = 'NON_VERIFIED_PREPA'      AND is_verified = FALSE AND type = 'prepa')      OR
			            (? = 'NON_VERIFIED_ORIENTADOR' AND is_verified = FALSE AND type = 'orientador')
			        )
			ORDER BY memid
			OFFSET ?
			LIMIT  ?
			""";
		List<MemberDTO> members = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT);
			stmt.setLong(1, server);
			stmt.setLong(2, server);
			stmt.setString(3, retrievement.name());
			stmt.setString(4, retrievement.name());
			stmt.setString(5, retrievement.name());
			stmt.setString(6, retrievement.name());
			stmt.setString(7, retrievement.name());
			stmt.setString(8, retrievement.name());
			stmt.setString(9, retrievement.name());
			stmt.setString(10, retrievement.name());
			stmt.setString(11, retrievement.name());
			stmt.setInt(12, offset);
			stmt.setInt(13, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				MemberDTO member = new MemberDTO();
				member.setId(result.getInt("memid"));
				member.setUserId(result.getInt("identifier"));
				
				member.setFirstname(result.getString("firstname"));
				member.setLastname(result.getString("lastname"));
				member.setInitial(result.getString("initial"));
				member.setSex(result.getString("sex"));
				
				member.setEmail(result.getString("email"));
				member.setProgram(MemberProgram.asProgram(result.getString("program_name")));
				member.setFunfact(result.getString("funfact"));
				member.setUsername(result.getString("username"));

				member.setVerified(result.getBoolean("is_verified"));
				
				members.add(member);
			}
			result.close();
			stmt.close();
		};
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return members;
	}
	
	public Optional<MemberDTO> getMember(String email) {
		final String SQL_SELECT =
			"""
			WITH all_people AS (
			    SELECT  orid         AS identifier,
			            fname        AS firstname,
			            lname        AS lastname,
			            '-'          AS initial,
			            '-'          AS sex,
			            'orientador' AS type,
			            fmemid
			        FROM orientador
			    
			    UNION ALL
			    SELECT  prepaid                 AS identifier,
			            fname                   AS firstname, 
			            flname || ' ' || mlname AS lastname,
			            initial                 AS initial, 
			            sex                     AS sex,
			            'prepa'                 AS type, 
			            fmemid
			        FROM prepa
			),
			people_with_verification AS (
			    SELECT  mb.memid,
			            identifier,
			            firstname,
			            lastname,
			            initial,
			            sex,
			            mb.email,
			            pr.name                              AS program_name,
			            COALESCE(jm.username, 'No username') AS username,
			            COALESCE(jm.funfact,  'No fun fact') AS funfact,
			            CASE WHEN jm.jmid IS NULL THEN FALSE ELSE TRUE END AS is_verified,
			            type,
			            discserid
			        FROM all_people
			            INNER JOIN member          AS mb ON fmemid     = memid
			            INNER JOIN program         AS pr ON mb.fprogid = progid
			            LEFT  JOIN joinedmember    AS jm ON jm.fmemid  = memid
			            
			            INNER JOIN department      AS dp ON dp.depid   = pr.fdepid 
			            INNER JOIN serverownership AS so ON dp.depid   = so.fdepid
			)
			SELECT *
			    FROM people_with_verification
			    WHERE
				    email = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		MemberDTO member = new MemberDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT);
			stmt.setString(1, email);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				member.setId(result.getInt("memid"));
				member.setUserId(result.getInt("identifier"));
				
				member.setFirstname(result.getString("firstname"));
				member.setLastname(result.getString("lastname"));
				member.setInitial(result.getString("initial"));
				member.setSex(result.getString("sex"));
				
				member.setEmail(result.getString("email"));
				member.setProgram(MemberProgram.asProgram(result.getString("program_name")));
				member.setFunfact(result.getString("funfact"));
				member.setUsername(result.getString("username"));

				member.setVerified(result.getBoolean("is_verified"));
				
				found.set(true);
			}
			
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(member) : Optional.empty();
	}
	
	public Optional<TeamDTO> getMemberTeam(String email, long server) {
		final String SQL =
			"""
			SELECT  teamid            AS teamid,
			        team.name         AS team_name,
			        team.orgname      AS team_orgname,
			        droleid           AS droleid,
			        longroleid        AS longroleid,
			        discserid         AS discserid,
			        dir.name          AS role_name,
			        dir.effectivename AS effectivename
			    
                FROM team
			        INNER JOIN assignedteam    AS ast ON team.teamid = ast.fteamid
			        INNER JOIN member          AS mem ON mem.memid   = ast.fmemid
			        INNER JOIN discordrole     AS dir ON dir.droleid = team.fdroleid
			        INNER JOIN serverownership AS seo ON seo.seoid   = dir.fseoid
			    
                WHERE
			        email = ? AND seo.discserid = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		TeamDTO team = new TeamDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, email);
			stmt.setLong(2, server);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				team.setId(result.getInt("teamid"));
				team.setName(result.getString("team_name"));
				team.setOrgname(result.getString("team_orgname"));
				
				DiscordRoleDTO role = new DiscordRoleDTO();
				role.setId(result.getInt("droleid"));
				role.setRoleid(result.getLong("longroleid"));
				role.setServerid(result.getLong("discserid"));
				role.setName(result.getString("role_name"));
				role.setEffectivename(result.getString("effectivename"));
				team.setTeamRole(role);
				
				found.set(true);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(team) : Optional.empty();
	}
	
	public boolean insertAndVerifyMember(MemberDTO member, long server) {
		
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		// Add all transaction parameter fields
		transaction.submitSQL(
				"""
				SELECT memid FROM member WHERE email = ?
				""", 
				List.of(member.getEmail()))
			.submitSQL(
				"""
				INSERT INTO joinedmember(funfact, username, fmemid, fseoid, member_since)
				    SELECT ?, ?, ?, seoid, CURRENT_TIMESTAMP
				        FROM serverownership
				        WHERE 
				            discserid = ?
				RETURNING jmid
				""", 
				List.of(member.getFunfact(), member.getUsername(), ResultReference.of("memid"), server))
			.submitSQL(
				"""
				INSERT INTO advancement(name, fjmid)
				    VALUES('attendance', ?)
				""", 
				List.of(ResultReference.of("jmid")));
		
		// Prepare transaction and execute by parts
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.executeThen(TransactionStatementType.MIXED_QUERY)
			.executeThen(TransactionStatementType.UPDATE_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.isCompleted();
	}
	
	public int insertMember(MemberDTO member, MemberPosition positionRole, long server, String teamname) {
		
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		String firstLastName  = member.getLastname().split(" ")[0];
		String secondLastName = member.getLastname().split(" ")[1];
		
		// Add all transaction parameter fields
		transaction.submitSQL(
			"""
			WITH chosen_server AS (
			    SELECT seoid FROM serverownership
			        WHERE discserid = ?
			    LIMIT 1
			), 
			chosen_program AS (
			    SELECT progid FROM program 
			        WHERE name = ?
			    LIMIT 1
			), 
			new_member AS (
			    INSERT INTO member (email, fprogid) 
			        SELECT ?, progid FROM chosen_program
			    RETURNING memid
			), 
			assigned_team AS (
			    INSERT INTO assignedteam (fmemid, fteamid)
			        SELECT (SELECT memid FROM new_member), teamid
			            FROM team
			                INNER JOIN discordrole     ON fdroleid = droleid
			                INNER JOIN serverownership ON fseoid   = seoid
			            WHERE 
			                team.name = ? AND fseoid = (SELECT seoid FROM chosen_server)
			    RETURNING atid
			), 
			assigned_position_role AS (
			    INSERT INTO assignedrole (fmemid, fdroleid)
			        SELECT (SELECT memid FROM new_member), droleid
			            FROM discordrole
			                INNER JOIN serverownership ON fseoid = seoid
			            WHERE
			                effectivename = ? AND fseoid = (SELECT seoid FROM chosen_server)
			    RETURNING arid
			), 
			assigned_program_role AS (
			    INSERT INTO assignedrole (fmemid, fdroleid)
			        SELECT (SELECT memid FROM new_member), droleid
			            FROM discordrole
			                INNER JOIN serverownership ON fseoid = seoid
			            WHERE
			                UPPER(effectivename) = UPPER(?) AND fseoid = (SELECT seoid FROM chosen_server)
			    RETURNING arid
			), 
			insert_orientador AS (
			    INSERT INTO orientador (fname, lname, fmemid)
			        SELECT ?, ?, (SELECT memid FROM new_member)
			            WHERE ? IN ('EstudianteGraduado', 'ConsejeroProfesional', 'EstudianteOrientador')
			    RETURNING fmemid
			), 
			insert_prepa AS (
			    INSERT INTO prepa (fname, flname, mlname, initial, sex, fmemid)
			        SELECT ?, ?, ?, ?, ?, (SELECT memid FROM new_member)
			            WHERE ? = 'Prepa'
			    RETURNING fmemid
			)
			SELECT 
			    (SELECT memid FROM new_member)   AS memid,
			    (SELECT atid FROM assigned_team) AS atid,
			    (SELECT arid FROM assigned_position_role) AS pos_arid,
			    (SELECT arid FROM assigned_program_role)  AS pro_arid,
			    COALESCE((SELECT fmemid FROM insert_orientador), 0) AS orientador_memid,
			    COALESCE((SELECT fmemid FROM insert_prepa), 0)      AS prepa_memid;
			""",
			List.of(
					server,
					member.getProgram().getLiteral(),
					member.getEmail(),
					teamname,
					positionRole.getEffectiveName(),
					member.getProgram().getLiteral(),
					
					member.getFirstname(),
					member.getLastname(),
					positionRole.getEffectiveName(),
					
					member.getFirstname(), 
					firstLastName, 
					secondLastName, 
					member.getInitial(), 
					member.getSex(),
					positionRole.getEffectiveName()));

		// Prepare transaction and execute by parts
		transaction.prepare()
			.executeThen(TransactionStatementType.MIXED_QUERY,
				result -> {
					return ((int)result.getResult("memid", 0) != 0) &&
						   ((int)result.getResult("atid",  0) != 0) &&
						   ((int)result.getResult("pos_arid", 0) != 0) &&
						   ((int)result.getResult("pro_arid", 0) != 0) &&
						   ((int)result.getResult("prepa_memid",      0) != 0 || positionRole != MemberPosition.PREPA) &&
						   ((int)result.getResult("orientador_memid", 0) != 0 || positionRole == MemberPosition.PREPA);
				})
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		if(transaction.isCompleted())
			return (int)transaction.getLatestResult().getResult("memid", 0);
		else
			return -1;
	}
	
	public boolean insertMemberGroup(List<MemberDTO> members, MemberPosition positionRole, long server, String teamname) {
		@SuppressWarnings("resource")
		BatchTransaction transaction = new BatchTransaction();
		
		transaction.loadBatch(members)
			.batchSQL(
				"""
				WITH chosen_server AS (
				    SELECT seoid FROM serverownership
				        WHERE discserid = ?
				    LIMIT 1
				), 
				chosen_program AS (
				    SELECT progid FROM program 
				        WHERE name = ?
				    LIMIT 1
				), 
				new_member AS (
				    INSERT INTO member (email, fprogid) 
				        SELECT ?, progid FROM chosen_program
				    RETURNING memid
				), 
				assigned_team AS (
				    INSERT INTO assignedteam (fmemid, fteamid)
				        SELECT (SELECT memid FROM new_member), teamid
				            FROM team
				                INNER JOIN discordrole     ON fdroleid = droleid
				                INNER JOIN serverownership ON fseoid   = seoid
				            WHERE 
				                team.name = ? AND fseoid = (SELECT seoid FROM chosen_server)
				    RETURNING atid
				), 
				assigned_position_role AS (
				    INSERT INTO assignedrole (fmemid, fdroleid)
				        SELECT (SELECT memid FROM new_member), droleid
				            FROM discordrole
				                INNER JOIN serverownership ON fseoid = seoid
				            WHERE
				                effectivename = ? AND fseoid = (SELECT seoid FROM chosen_server)
				    RETURNING arid
				), 
				assigned_program_role AS (
				    INSERT INTO assignedrole (fmemid, fdroleid)
				        SELECT (SELECT memid FROM new_member), droleid
				            FROM discordrole
				                INNER JOIN serverownership ON fseoid = seoid
				            WHERE
				                UPPER(effectivename) = UPPER(?) AND fseoid = (SELECT seoid FROM chosen_server)
				    RETURNING arid
				), 
				insert_orientador AS (
				    INSERT INTO orientador (fname, lname, fmemid)
				        SELECT ?, ?, (SELECT memid FROM new_member)
				            WHERE ? IN ('EstudianteGraduado', 'ConsejeroProfesional', 'EstudianteOrientador')
				    RETURNING fmemid
				), 
				insert_prepa AS (
				    INSERT INTO prepa (fname, flname, mlname, initial, sex, fmemid)
				        SELECT ?, ?, ?, ?, ?, (SELECT memid FROM new_member)
				            WHERE ? = 'Prepa'
				    RETURNING fmemid
				)
				SELECT 
				    (SELECT memid FROM new_member)   AS memid,
				    (SELECT atid FROM assigned_team) AS atid,
				    (SELECT arid FROM assigned_position_role) AS pos_arid,
				    (SELECT arid FROM assigned_program_role)  AS pro_arid,
				    COALESCE((SELECT fmemid FROM insert_orientador), 0) AS orientador_memid,
				    COALESCE((SELECT fmemid FROM insert_prepa), 0)      AS prepa_memid;
				""",
				(MemberDTO member) -> {
					String[] lastNameParts = member.getLastname().split(" ");
				    String firstLastName = lastNameParts.length > 1 ? lastNameParts[0] : member.getLastname();
				    String secondLastName = lastNameParts.length > 1 ? lastNameParts[1] : "_";
					return List.of(
							server,
							member.getProgram().getLiteral(),
							member.getEmail(),
							teamname,
							positionRole.getEffectiveName(),
							member.getProgram().getLiteral(),
							
							member.getFirstname(),
							member.getLastname(),
							positionRole.getEffectiveName(),
							
							member.getFirstname(), 
							firstLastName, 
							secondLastName, 
							member.getInitial(), 
							member.getSex(),
							positionRole.getEffectiveName());
				});
		
		
		// Prepare transaction and execute by parts
		transaction.prepare()
			.executeThen(TransactionStatementType.UPDATE_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.isCompleted();
	}
	
	public boolean deleteMembers(List<Integer> memberIds) {
		@SuppressWarnings("resource")
		BatchTransaction transaction = new BatchTransaction();
		
		transaction.loadBatch(memberIds)
			.batchSQL(
				"""
				WITH select_member AS (
					SELECT memid FROM member WHERE memid = ? LIMIT 1
				),
				delete_orientador AS (
				    DELETE FROM orientador 
					    WHERE fmemid = (SELECT memid FROM select_member)
				    RETURNING fmemid
				), 
				delete_prepa AS (
				    DELETE FROM prepa 
					    WHERE fmemid = (SELECT memid FROM select_member)
				    RETURNING fmemid
				), 
				delete_assignedteam AS (
				    DELETE FROM assignedteam 
					    WHERE fmemid = (SELECT memid FROM select_member)
				    RETURNING fmemid
				), 
				delete_assignedrole AS (
				    DELETE FROM assignedrole 
					    WHERE fmemid = (SELECT memid FROM select_member)
				    RETURNING fmemid
				), 
				delete_joinedmember AS (
				    DELETE FROM joinedmember 
					    WHERE fmemid = (SELECT memid FROM select_member)
				    RETURNING fmemid
				)
				DELETE FROM member 
					WHERE memid = (SELECT memid FROM select_member)
			    RETURNING memid;
				""",
				(Integer memberid) -> List.of(memberid));
		
		// Prepare transaction and execute by parts
		transaction.prepare()
			.executeThen(TransactionStatementType.UPDATE_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.isCompleted();
	}
}