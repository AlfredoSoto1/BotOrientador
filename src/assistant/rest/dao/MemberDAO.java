/**
 * 
 */
package assistant.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
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
	
	public int insertAndVerifyMember(MemberDTO member) {
		final String SQL_INSERT_JOINED_MEMBER =
			"""
			INSERT INTO joinedmember(funfact, username, fmemid, fseoid)
			    SELECT ?, ?, ?, seoid
			        FROM serverownership
			        WHERE 
			            discserid = ?
			RETURNING jmid
			""";
		final String SQL_INSERT_ADVANCEMENT =
			"""
			INSERT INTO advancement(name, fjmid)
			    VALUES('attendance', ?)
			""";
		final String SQL_SELECT_MEMBER =
			"""
			SELECT memid FROM member WHERE email = ?
			""";
		
		RunnableSQL rq = connection -> {
			connection.setAutoCommit(false);
			
			PreparedStatement stmt_select_member = connection.prepareStatement(SQL_SELECT_MEMBER);
			stmt_select_member.setString(1, member.getEmail());
			
			int memid = -1;
			ResultSet result = stmt_select_member.executeQuery();
			while(result.next())
				memid = result.getInt("memid");
			result.close();
			stmt_select_member.close();
			if(memid == -1)
				return;
			
			PreparedStatement stmt_insert_joinedmember = connection.prepareStatement(SQL_INSERT_JOINED_MEMBER);
			PreparedStatement stmt_insert_advancement  = connection.prepareStatement(SQL_INSERT_ADVANCEMENT);
			stmt_insert_joinedmember.setString(1, member.getFunfact());
			stmt_insert_joinedmember.setString(2, member.getUsername());
			stmt_insert_joinedmember.setInt(3, memid);
			connection.commit();
			connection.setAutoCommit(true);
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		
		return 0;
	}
	
	public int insertMember(MemberDTO member, MemberPosition positionRole, long server, String teamname) {
		final String SQL =
			"""
			SELECT insert_member(
				?, -- Program name
			    ?, -- Email
			    ?, -- Position role
			    ?, -- Server id
			    ?, -- Team name
			    ?, -- Role effective name
			    ?, -- First name
			    ?, -- Last Name
			    ?, -- Initial
			    ?  -- Sex
			) AS memid;
			""";
		AtomicInteger idResult = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			connection.setAutoCommit(false);
			
			try {
				PreparedStatement stmt = connection.prepareStatement(SQL);
				// Member insertion fields
				stmt.setString(1, member.getProgram().getLiteral());
				stmt.setString(2, member.getEmail());
				stmt.setString(3, positionRole.getEffectiveName());
				stmt.setLong(4,   server);
				stmt.setString(5, teamname);
				stmt.setString(6, positionRole.getEffectiveName());
				stmt.setString(7, member.getFirstname());
				stmt.setString(8, member.getLastname());
				stmt.setString(9, member.getInitial());
				stmt.setString(10, member.getSex());
				
				ResultSet result = stmt.executeQuery();
				while(result.next())
					idResult.set(result.getInt("memid"));
				
				result.close();
				stmt.close();
				connection.commit();
			} catch(SQLException sqle) {
				sqle.printStackTrace();
				connection.rollback();
			} finally {
				connection.setAutoCommit(true);
			}
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return idResult.get();
	}
	
	public int deleteMembers(List<Integer> memberIds) {
		final String SQL_DELETE = 
			"""
			SELECT delete_member(?) AS has_deleted
			""";
		AtomicInteger deletedCount = new AtomicInteger(0);
		
		RunnableSQL rq = connection -> {
			connection.setAutoCommit(false);
			PreparedStatement stmt = connection.prepareStatement(SQL_DELETE);
			
			try {
				for(int memid : memberIds) {
					stmt.setInt(1, memid);
					ResultSet result = stmt.executeQuery();
					if (result.next()) {
						if(result.getBoolean("has_deleted"))
							deletedCount.incrementAndGet();
					}
					result.close();
				}
				
				connection.commit();
			} catch(SQLException sqle) {
				connection.rollback();
			} finally {
				stmt.close();
				connection.setAutoCommit(true);
			}
		};
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return deletedCount.get();
	}
}