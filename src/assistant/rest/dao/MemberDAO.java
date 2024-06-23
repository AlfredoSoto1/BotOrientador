/**
 * 
 */
package assistant.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.discord.object.MemberRetrievement;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.MemberDTO;

/**
 * @author Alfredo
 */
@Repository
public class MemberDAO {
	
	public MemberDAO() {
		
	}
	
	public List<EmailDTO> getEmails(int offset, int limit) {
		final String SQL_SELECT =
			"""
			select verid, email from verification
			""";
		List<EmailDTO> emails = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				EmailDTO member = new EmailDTO();
				member.setId(result.getInt("verid"));
				member.setEmail(result.getString("email"));
				emails.add(member);
			}
			result.close();
			stmt.close();
		};
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return emails;
	}
	
	public List<MemberDTO> getMembers(int offset, int limit, MemberRetrievement retrievement) {
		final String SQL_SELECT =
			"""
			with all_people as (
			    select  orid         as identifier,
			            fname        as firstname,
			            lname        as lastname,
			            '-'          as initial,
			            '-'          as sex,
			            'orientador' as type,
			            fverid
			        from orientador
			        
			    union all
			    select  prepaid                 as identifier,
			            fname                   as firstname, 
			            flname || ' ' || mlname as lastname,
			            initial, 
			            sex,
			            'prepa'                 as type, 
			            fverid
			        from prepa
			)
			select  verid,
			        identifier,
			        firstname,
			        lastname,
			        initial,
			        sex,
			        email,
			        is_verified,
			        verified_date,
			        program.name                         as program_name,
			        coalesce(jm.username, 'No username') as username,
			        coalesce(jm.funfact,  'No fun fact') as funfact
			    
			    from all_people
			        inner join verification       on fverid    = verid
			        inner join program            on fprogid   = progid
			        left  join joinedmember as jm on jm.fverid = verid
			    
			    where
			        (? = 'EVERYONE') or 
			        (? = 'ALL_PREPA'        and type = 'prepa')      or
			        (? = 'ALL_ORIENTADOR'   and type = 'orientador') or
			
			        (? = 'ALL_VERIFIED'     and is_verified = TRUE)  or
			        (? = 'ALL_NON_VERIFIED' and is_verified = FALSE) or
			        
			        (? = 'VERIFIED_PREPA'      and is_verified = TRUE and type = 'prepa')      or
			        (? = 'VERIFIED_ORIENTADOR' and is_verified = TRUE and type = 'orientador') or
			
			        (? = 'NON_VERIFIED_PREPA'      and is_verified = FALSE and type = 'prepa')      or
			        (? = 'NON_VERIFIED_ORIENTADOR' and is_verified = FALSE and type = 'orientador')
					
				offset ?
				limit  ?
			""";
		List<MemberDTO> members = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT);
			stmt.setString(1, retrievement.name());
			stmt.setString(2, retrievement.name());
			stmt.setString(3, retrievement.name());
			stmt.setString(4, retrievement.name());
			stmt.setString(5, retrievement.name());
			stmt.setString(6, retrievement.name());
			stmt.setString(7, retrievement.name());
			stmt.setString(8, retrievement.name());
			stmt.setString(9, retrievement.name());
			stmt.setInt(10, offset);
			stmt.setInt(11, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				MemberDTO member = new MemberDTO();
				member.setId(result.getInt("verid"));
				member.setUserId(result.getInt("identifier"));
				
				member.setFirstname(result.getString("firstname"));
				member.setLastname(result.getString("lastname"));
				member.setInitial(result.getString("initial"));
				member.setSex(result.getString("sex"));
				
				member.setEmail(result.getString("email"));
				member.setProgram(result.getString("program_name"));
				member.setFunfact(result.getString("funfact"));
				member.setUsername(result.getString("username"));

				member.setVerified(result.getBoolean("is_verified"));
				member.setVerificationDate(result.getDate("verified_date"));
				
				members.add(member);
			}
			result.close();
			stmt.close();
		};
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return members;
	}
	
	public Optional<MemberDTO> getMember(String email, MemberRetrievement retrievement) {
		final String SQL_SELECT =
			"""
			with all_people as (
			    select  orid         as identifier,
			            fname        as firstname,
			            lname        as lastname,
			            '-'          as initial,
			            '-'          as sex,
			            'orientador' as type,
			            fverid
			        from orientador
			        
			    union all
			    select  prepaid                 as identifier,
			            fname                   as firstname, 
			            flname || ' ' || mlname as lastname,
			            initial, 
			            sex,
			            'prepa'                 as type, 
			            fverid
			        from prepa
			)
			select  verid,
			        identifier,
			        firstname,
			        lastname,
			        initial,
			        sex,
			        email,
			        is_verified,
			        verified_date,
			        program.name                         as program_name,
			        coalesce(jm.username, 'No username') as username,
			        coalesce(jm.funfact,  'No fun fact') as funfact
			    
			    from all_people
			        inner join verification       on fverid    = verid
			        inner join program            on fprogid   = progid
			        left  join joinedmember as jm on jm.fverid = verid
			    
			    where
				    email = ?                                      and
			        (? = 'EVERYONE')                               or 
			        (? = 'ALL_PREPA'      and type = 'prepa')      or
			        (? = 'ALL_ORIENTADOR' and type = 'orientador')
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		MemberDTO member = new MemberDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT);
			stmt.setString(1, email);
			stmt.setString(2, retrievement.name());
			stmt.setString(3, retrievement.name());
			stmt.setString(4, retrievement.name());
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				member.setId(result.getInt("verid"));
				member.setUserId(result.getInt("identifier"));
				
				member.setFirstname(result.getString("firstname"));
				member.setLastname(result.getString("lastname"));
				member.setInitial(result.getString("initial"));
				member.setSex(result.getString("sex"));
				
				member.setEmail(result.getString("email"));
				member.setProgram(result.getString("program_name"));
				member.setFunfact(result.getString("funfact"));
				member.setUsername(result.getString("username"));

				member.setVerified(result.getBoolean("is_verified"));
				member.setVerificationDate(new Date(result.getDate("verified_date").getTime()));
				
				found.set(true);
			}
			
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(member) : Optional.empty();
	}
	
//	public Optional<MemberTeamDTO> getMemberTeam(int verid) {
//		final String SQL =
//			"""
//			select  teamid, fdroleid, name, orgname
//			    from verification
//			        inner join member on verid   = member.fverid
//			        inner join team   on teamid  = member.fteamid
//			    where 
//			        verid = ?
//			""";
//		AtomicBoolean found = new AtomicBoolean(false);
//		MemberTeamDTO memberTeam = new MemberTeamDTO();
//		
//		RunnableSQL rq = connection -> {
//			PreparedStatement stmt = connection.prepareStatement(SQL);
//			stmt.setInt(1, verid);
//			
//			ResultSet result = stmt.executeQuery();
//			while(result.next()) {
//				int teamid = result.getInt("teamid");
//				int droleid = result.getInt("fdroleid");
//				
//				String name = result.getString("name");
//				String orgname = result.getString("orgname");
//				
//				memberTeam.setTeamid(teamid);
//				memberTeam.setDroleid(droleid);
//				memberTeam.setName(name);
//				memberTeam.setOrgname(orgname);
//				found.set(true);
//			}
//			
//			result.close();
//			stmt.close();
//		};
//		
//		Application.instance().getDatabaseConnection().establishConnection(rq);
//		return found.get() ? Optional.of(memberTeam) : Optional.empty();
//	}
//	
//	public Optional<MemberRoleDTO> getMemberRole(int verid) {
//		final String SQL =
//			"""
//			select memid, fdroleid
//			    from verification
//			        inner join member     on verid = member.fverid
//			        inner join memberrole on memid = fmemid
//			    where 
//			        verid = ?
//			""";
//		AtomicBoolean found = new AtomicBoolean(false);
//		MemberRoleDTO memberTeam = new MemberRoleDTO();
//		
//		RunnableSQL rq = connection -> {
//			PreparedStatement stmt = connection.prepareStatement(SQL);
//			stmt.setInt(1, verid);
//			
//			ResultSet result = stmt.executeQuery();
//			while(result.next()) {
//				int memid = result.getInt("memid");
//				int droleid = result.getInt("fdroleid");
//				
//				memberTeam.setMemid(memid);
//				memberTeam.setFdroleid(droleid);
//				found.set(true);
//			}
//			
//			result.close();
//			stmt.close();
//		};
//		
//		Application.instance().getDatabaseConnection().establishConnection(rq);
//		return found.get() ? Optional.of(memberTeam) : Optional.empty();
//	}
//	
//	public Optional<MemberProgramDTO> getMemberProgram(int verid) {
//		final String SQL =
//			"""
//			select progid, fdepid, name
//			    from verification
//			        inner join program on progid = fprogid
//			    where 
//			        verid = ?
//			""";
//		AtomicBoolean found = new AtomicBoolean(false);
//		MemberProgramDTO memberTeam = new MemberProgramDTO();
//		
//		RunnableSQL rq = connection -> {
//			PreparedStatement stmt = connection.prepareStatement(SQL);
//			stmt.setInt(1, verid);
//			
//			ResultSet result = stmt.executeQuery();
//			while(result.next()) {
//				int progid = result.getInt("progid");
//				int depid = result.getInt("fdepid");
//				String name = result.getString("name");
//				
//				memberTeam.setProgid(progid);
//				memberTeam.setDepid(depid);
//				memberTeam.setName(name);
//				found.set(true);
//			}
//
//			result.close();
//			stmt.close();
//		};
//		
//		Application.instance().getDatabaseConnection().establishConnection(rq);
//		return found.get() ? Optional.of(memberTeam) : Optional.empty();
//	}

	public int insertOrientadorMember(MemberDTO member, String program, String teamname) {
		final String SQL_SELECT_PROGRAM = 
			"""
	        select progid from program
		        where 
		            name = ?
			""";
		final String SQL_INSERT_VERIFICATION = 
			"""
		    insert into verification(email, fprogid)
		        select ?, progid
		            from program
		        where 
		            name = ?
			returning verid
			""";
		final String SQL_INSERT_ORIENTADOR = 
			"""
		    insert into orientador (fname, lname, fverid)
		        values (?, ?, ?)
			""";
		final String SQL_SELECT_TEAM = 
			"""
		    select teamid from team
		        where 
		            team.name = ?
			""";
		final String SQL_INSERT_MEMBER = 
			"""
			insert into member (funfact, fverid, fteamid)
			    select ?, ?, teamid from team
			        where 
			            team.name = ?
			returning memid
			""";
		final String SQL_INSERT_ADVANCEMENT = 
			"""
			insert into advancement (name, fmemid) values('participation', ?)
			""";
		AtomicInteger idResult = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			connection.setAutoCommit(false);
			
			PreparedStatement stmt_program = connection.prepareStatement(SQL_SELECT_PROGRAM);
			stmt_program.setString(1, program);
			
			ResultSet result_program = stmt_program.executeQuery();
			if(!result_program.next()) {
				connection.rollback();
				connection.setAutoCommit(true);
				result_program.close();
				stmt_program.close();
				return;
			}
			
			PreparedStatement stmt_team = connection.prepareStatement(SQL_SELECT_TEAM);
			stmt_team.setString(1, teamname);
			
			ResultSet result_team = stmt_team.executeQuery();
			if(!result_team.next()) {
				result_team.close();
				stmt_program.close();
				connection.rollback();
				connection.setAutoCommit(true);
				return;
			}
			
			PreparedStatement stmt_verification = connection.prepareStatement(SQL_INSERT_VERIFICATION);
			PreparedStatement stmt_orientador   = connection.prepareStatement(SQL_INSERT_ORIENTADOR);
			PreparedStatement stmt_member       = connection.prepareStatement(SQL_INSERT_MEMBER);
			PreparedStatement stmt_advancement  = connection.prepareStatement(SQL_INSERT_ADVANCEMENT);
			
			try {
				stmt_verification.setString(1, member.getEmail());
				stmt_verification.setString(2, program);
				
				ResultSet result_verification = stmt_verification.executeQuery();
				if(!result_verification.next()) {
					result_verification.close();
					throw new SQLException();
				}
				int verid = result_verification.getInt("verid");
				result_verification.close();
				
				stmt_orientador.setString(1, member.getFirstname());
				stmt_orientador.setString(2, member.getLastname());
				stmt_orientador.setInt(3, verid);
				
				stmt_orientador.executeUpdate();
				
				stmt_member.setString(1, member.getFunfact());
				stmt_member.setInt(2, verid);
				stmt_member.setString(3, teamname);
				
				ResultSet result_member = stmt_member.executeQuery();
				if(!result_member.next()) {
					result_member.close();
					throw new SQLException();
				}
				int memid = result_member.getInt("memid");
				result_member.close();
				
				stmt_advancement.setInt(1, memid);
				stmt_advancement.executeUpdate();
				
				connection.commit();
				idResult.set(verid);
			} catch (SQLException sqle) {
				connection.rollback();
			} finally {
				stmt_verification.close();
				stmt_orientador.close();
				stmt_member.close();
				stmt_advancement.close();
				connection.setAutoCommit(true);
			}
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return idResult.get();
	}
	
	public int insertPrepaMember(MemberDTO member, String program, String teamname) {
		final String SQL_SELECT_PROGRAM = 
			"""
	        select progid from program
		        where 
		            name = ?
			""";
		final String SQL_INSERT_VERIFICATION = 
			"""
		    insert into verification(email, fprogid)
		        select ?, progid
		            from program
		        where 
		            name = ?
			returning verid
			""";
		final String SQL_INSERT_PREPA = 
			"""
		    insert into prepa (fname, flname, mlname, initial, sex, fverid)
		        values (?, ?, ?, ?, ?, ?)
			""";
		final String SQL_SELECT_TEAM = 
			"""
		    select teamid from team
		        where 
		            team.name = ?
			""";
		final String SQL_INSERT_MEMBER = 
			"""
			insert into member (funfact, fverid, fteamid)
			    select ?, ?, teamid from team
			        where 
			            team.name = ?
			returning memid
			""";
		final String SQL_INSERT_ADVANCEMENT = 
			"""
			insert into advancement (name, fmemid) values('participation', ?)
			""";
		AtomicInteger idResult = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			connection.setAutoCommit(false);
			
			PreparedStatement stmt_program = connection.prepareStatement(SQL_SELECT_PROGRAM);
			stmt_program.setString(1, program);
			
			ResultSet result_program = stmt_program.executeQuery();
			if(!result_program.next()) {
				connection.rollback();
				connection.setAutoCommit(true);
				result_program.close();
				stmt_program.close();
				return;
			}
			
			PreparedStatement stmt_team = connection.prepareStatement(SQL_SELECT_TEAM);
			stmt_team.setString(1, teamname);
			
			ResultSet result_team = stmt_team.executeQuery();
			if(!result_team.next()) {
				result_team.close();
				stmt_program.close();
				connection.rollback();
				connection.setAutoCommit(true);
				return;
			}
			
			PreparedStatement stmt_verification = connection.prepareStatement(SQL_INSERT_VERIFICATION);
			PreparedStatement stmt_prepa        = connection.prepareStatement(SQL_INSERT_PREPA);
			PreparedStatement stmt_member       = connection.prepareStatement(SQL_INSERT_MEMBER);
			PreparedStatement stmt_advancement  = connection.prepareStatement(SQL_INSERT_ADVANCEMENT);
			
			try {
				stmt_verification.setString(1, member.getEmail());
				stmt_verification.setString(2, program);
				
				ResultSet result_verification = stmt_verification.executeQuery();
				if(!result_verification.next()) {
					result_verification.close();
					throw new SQLException();
				}
				int verid = result_verification.getInt("verid");
				result_verification.close();
				
				stmt_prepa.setString(1, member.getFirstname());
				stmt_prepa.setString(2, member.getLastname().split(" ")[0]);
				stmt_prepa.setString(3, member.getLastname().split(" ")[1]);
				stmt_prepa.setString(4, ""+member.getInitial());
				stmt_prepa.setString(5, ""+member.getSex());
				stmt_prepa.setInt(6, verid);
				
				stmt_prepa.executeUpdate();

				stmt_member.setString(1, member.getFunfact());
				stmt_member.setInt(2, verid);
				stmt_member.setString(3, teamname);

				ResultSet result_member = stmt_member.executeQuery();
				if(!result_member.next()) {
					result_member.close();
					throw new SQLException();
				}
				int memid = result_member.getInt("memid");
				result_member.close();
				
				stmt_advancement.setInt(1, memid);
				stmt_advancement.executeUpdate();
				
				connection.commit();
				idResult.set(verid);
			} catch (SQLException sqle) {
				connection.rollback();
			} finally {
				stmt_verification.close();
				stmt_prepa.close();
				stmt_member.close();
				stmt_advancement.close();
				connection.setAutoCommit(true);
			}
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return idResult.get();
	}
	
	public int deleteMembers(List<Integer> memberVerificationIDs) {
		final String SQL_DELETE = 
				"""
				with delete_advancement as (
				    delete from advancement
				    where 
				        advid = (
				            select advid from advancement
				                inner join member on fmemid = memid
				            where
				                fverid = ?
				        )
				),
				delete_member_role as (
				    delete from memberrole
				    where 
				        roleid = (
				            select roleid from memberrole
				                inner join member on fmemid = memid
				            where 
				                fverid = ?
				        )
				),
				delete_member as (
				    delete from member
				    where 
				        memid = (
				            select memid from member
				            where 
				                fverid = ?
				        )
				),
				delete_orientador as (
				    delete from orientador
				    where 
				        orid = (
				            select orid from orientador
				            where 
				                fverid = ?
				        )
				),
				delete_prepa as (
				    delete from prepa
				    where 
				        prepaid = (
				            select prepaid from prepa
				            where
				                fverid = ?
				        )
				)
				delete from verification
				    where verid = ?
				""";
		AtomicInteger deletedCount = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			connection.setAutoCommit(false);
			
			PreparedStatement stmt = connection.prepareStatement(SQL_DELETE);
			
			for(int verid : memberVerificationIDs) {
				stmt.setInt(1, verid);
				stmt.setInt(2, verid);
				stmt.setInt(3, verid);
				stmt.setInt(4, verid);
				stmt.setInt(5, verid);
				stmt.setInt(6, verid);
				stmt.addBatch();
			}
			
			try {
				int[] rows = stmt.executeBatch();
				deletedCount.set(rows.length);
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
