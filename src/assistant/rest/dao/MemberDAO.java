/**
 * 
 */
package assistant.rest.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import assistant.database.BatchTransaction;
import assistant.database.SubTransactionResult;
import assistant.database.SubTransactionResult.Replacement;
import assistant.database.Transaction;
import assistant.database.TransactionError;
import assistant.database.TransactionStatementType;
import assistant.discord.object.MemberPosition;
import assistant.discord.object.MemberRetrievement;
import assistant.rest.dto.MemberDTO;

/**
 * @author Alfredo
 */
@Repository
public class MemberDAO {
	
	public MemberDAO() {
		
	}
	
	public SubTransactionResult queryEmails(int offset, int limit, long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT memid, email 
				FROM member
					INNER JOIN program         AS pro ON fprogid    = progid
					INNER JOIN department      AS dep ON pro.fdepid = depid
					INNER JOIN serverownership AS seo ON seo.fdepid = depid
				
				WHERE
					discserid = ? OR ? = -1
			
			ORDER BY memid
			OFFSET ?
			LIMIT  ?
			""", List.of(server, server, offset, limit));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public SubTransactionResult queryAllMembers(int offset, int limit, MemberRetrievement retrievement, long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
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
			""", List.of(
					server, server,
					retrievement.name(),
					retrievement.name(),
					retrievement.name(),
					retrievement.name(),
					retrievement.name(),
					retrievement.name(),
					retrievement.name(),
					retrievement.name(),
					retrievement.name(),
					offset, limit));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public SubTransactionResult queryMember(String email) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
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
			""", List.of(email));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public SubTransactionResult queryMemberTeam(String email, long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
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
			""", List.of(email, server));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public SubTransactionResult queryMemberRoles(String email, long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  droleid           AS droleid,
			        longroleid        AS longroleid,
			        discserid         AS discserid,
			        dir.name          AS role_name,
			        dir.effectivename AS effectivename
			    FROM member
			        INNER JOIN assignedrole    AS asr ON asr.fmemid = memid
			        INNER JOIN discordrole     AS dir ON dir.droleid = asr.fdroleid
			        INNER JOIN serverownership AS seo ON seo.seoid   = dir.fseoid
			    
			    WHERE
			        email = ? AND discserid = ?
			""", List.of(email, server));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public SubTransactionResult insertAndVerifyMember(MemberDTO member, long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		// Add all transaction parameter fields
		transaction.submitSQL(
			"""
			WITH selected_member AS (
			    SELECT memid FROM member WHERE email = ? LIMIT 1
			),
			join_member AS (
			    INSERT INTO joinedmember(funfact, username, fmemid, fseoid, member_since)
			        SELECT ?, ?, (SELECT memid FROM selected_member), seoid, CURRENT_TIMESTAMP
			            FROM serverownership
			            WHERE 
			                discserid = ?
			    RETURNING jmid
			),
			set_advancement AS (
			    INSERT INTO advancement(name, fjmid)
			        SELECT 'participation', jmid FROM join_member
			    RETURNING fjmid as jmid
			)
			SELECT jmid FROM set_advancement
			""", 
			List.of(member.getEmail(), member.getFunfact(), member.getUsername(), server));
		
		// Prepare transaction and execute by parts
		transaction.prepare()
			.executeThen(TransactionStatementType.MIXED_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public int insertMember(MemberDTO member, MemberPosition positionRole, long server, String teamname) {
		
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		String[] lastNameParts = member.getLastname().split(" ");
	    String firstLastName = lastNameParts.length > 1 ? lastNameParts[0] : member.getLastname();
	    String secondLastName = lastNameParts.length > 1 ? lastNameParts[1] : "_";
		
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
			                effectivename IN (%s) AND fseoid = (SELECT seoid FROM chosen_server)
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
			            WHERE ? != 'Prepa'
			    RETURNING fmemid
			), 
			insert_prepa AS (
			    INSERT INTO prepa (fname, flname, mlname, initial, sex, fmemid)
			        SELECT ?, ?, ?, ?, ?, (SELECT memid FROM new_member)
			            WHERE ? = 'Prepa'
			    RETURNING fmemid
			)
			SELECT 
			    (SELECT memid FROM new_member)                             AS memid,
			    (SELECT arid FROM assigned_program_role)                   AS pro_arid,
			    (SELECT DISTINCT arid FROM assigned_position_role LIMIT 1) AS pos_arid,
			    COALESCE((SELECT atid FROM assigned_team),       0)        AS atid,
			    COALESCE((SELECT fmemid FROM insert_orientador), 0)        AS orientador_memid,
			    COALESCE((SELECT fmemid FROM insert_prepa),      0)        AS prepa_memid;
			""",
			List.of(
					server,
					member.getProgram().getLiteral(),
					member.getEmail(),
					teamname,
					Replacement.of(positionRole.getEffectiveNamePositions()),
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
					return ((int)result.getValue("memid", 0) != 0) &&
//						   ((int)result.getValue("atid",  0) != 0) &&
						   ((int)result.getValue("pos_arid", 0) != 0) &&
						   ((int)result.getValue("pro_arid", 0) != 0) &&
						   ((int)result.getValue("prepa_memid",      0) != 0 || positionRole != MemberPosition.PREPA) &&
						   ((int)result.getValue("orientador_memid", 0) != 0 || positionRole == MemberPosition.PREPA);
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
			return (int)transaction.getLatestResult().getValue("memid", 0);
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
			                effectivename IN (%s) AND fseoid = (SELECT seoid FROM chosen_server)
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
			            WHERE ? != 'Prepa'
			    RETURNING fmemid
			), 
			insert_prepa AS (
			    INSERT INTO prepa (fname, flname, mlname, initial, sex, fmemid)
			        SELECT ?, ?, ?, ?, ?, (SELECT memid FROM new_member)
			            WHERE ? = 'Prepa'
			    RETURNING fmemid
			)
			SELECT 
			    (SELECT memid FROM new_member)                             AS memid,
			    (SELECT arid FROM assigned_program_role)                   AS pro_arid,
			    (SELECT DISTINCT arid FROM assigned_position_role LIMIT 1) AS pos_arid,
			    COALESCE((SELECT atid FROM assigned_team),       0)        AS atid,
			    COALESCE((SELECT fmemid FROM insert_orientador), 0)        AS orientador_memid,
			    COALESCE((SELECT fmemid FROM insert_prepa),      0)        AS prepa_memid;
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
						Replacement.of(positionRole.getEffectiveNamePositions()),
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