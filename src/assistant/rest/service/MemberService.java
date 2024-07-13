/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package assistant.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.database.SubTransactionResult;
import assistant.discord.core.AsyncTaskQueue;
import assistant.discord.object.MemberPosition;
import assistant.discord.object.MemberProgram;
import assistant.discord.object.MemberRetrievement;
import assistant.rest.dao.MemberDAO;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.MemberDTO;
import assistant.rest.dto.PrepaOrientadorDTO;
import assistant.rest.dto.StudentDTO;
import assistant.rest.dto.TeamDTO;

/**
 * @author Alfredo
 */
@Service
public class MemberService {
	
	private final MemberDAO memberDAO;
	private AsyncTaskQueue verificationQueue;
	
	@Autowired
	public MemberService(MemberDAO memberDAO) {
		this.memberDAO = memberDAO;
		this.verificationQueue = new AsyncTaskQueue();
	}
	
	/**
	 * 
	 */
	public void shutdownVerificationQueueService() {
		verificationQueue.shutdown();
	}
	
	public <T> void queueVerificationTask(T t, Consumer<T> runTask) {
		verificationQueue.addTask(() -> runTask.accept(t));
	}
	
	public boolean stampMemberPresence(MemberDTO member, long server) {
		return !memberDAO.insertAndVerifyMember(member, server).isEmpty();
	}
	
	/**
	 * @param page
	 * @param size
	 * @param server
	 * @return List of DTO containing all emails of members
	 */
	public List<EmailDTO> getEmails(int page, int size, long server) {
		// Obtain the results and pass them to the DTO list
		List<EmailDTO> emails = new ArrayList<>();
		SubTransactionResult result = memberDAO.queryEmails(page * size, size, server);
		
		// Map all the results from DAO to DTO
		for (int i = 0; i < result.rowCount(); i++) {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setId((int)result.getValue("memid", i));
            emailDTO.setEmail((String) result.getValue("email", i));
			emails.add(emailDTO);
		}
		return emails;
	}
	
	/**
	 * @param retrievement
	 * @param server
	 * @return count of members related to the retrievement in a server
	 */
	public long memberCount(MemberRetrievement retrievement, long server) {
		SubTransactionResult result = memberDAO.queryMemberCount(retrievement, server);
		if (result.isEmpty())
			return 0;
		return result.getValue("count");
	}
	
	/**
	 * @param page
	 * @param size
	 * @param retrievement
	 * @param server
	 * @return List of DTO containing all members
	 */
	public List<MemberDTO> getAllMembers(int page, int size, MemberRetrievement retrievement, long server) {
		
		// Obtain the results and pass them to the DTO list
		List<MemberDTO> members = new ArrayList<>();
		SubTransactionResult result = memberDAO.queryAllMembers(page * size, size, retrievement, server);
		
		// Map all the results from DAO to DTO
		for (int i = 0; i < result.rowCount(); i++) {
			MemberDTO member = new MemberDTO();
			member.setId(result.getValue("memid", i));
			member.setUserId(result.getValue("identifier", i));
			
			member.setFirstname(result.getValue("firstname", i));
			member.setLastname(result.getValue("lastname", i));
			member.setInitial(result.getValue("initial", i));
			member.setSex(result.getValue("sex", i));
			
			member.setEmail(result.getValue("email", i));
			member.setProgram(MemberProgram.asProgram(result.getValue("program_name", i)));
			member.setFunfact(result.getValue("funfact", i));
			member.setUsername(result.getValue("username", i));

			member.setVerified(result.getValue("is_verified", i));
			
			members.add(member);
		}
		return members;
	}
	
	/**
	 * @param email
	 * @param server
	 * @return Member that matches the given email
	 */
	public Optional<MemberDTO> getMember(String email) {
		SubTransactionResult result = memberDAO.queryMember(email);
		
		if(result.isEmpty())
			return Optional.empty();
		
		// Map all the results from DAO to DTO
		MemberDTO member = new MemberDTO();
		member.setId(result.getValue("memid"));
		member.setUserId(result.getValue("identifier"));
		
		member.setFirstname(result.getValue("firstname"));
		member.setLastname(result.getValue("lastname"));
		member.setInitial(result.getValue("initial"));
		member.setSex(result.getValue("sex"));
		
		member.setEmail(result.getValue("email"));
		member.setProgram(MemberProgram.asProgram(result.getValue("program_name")));
		member.setFunfact(result.getValue("funfact"));
		member.setUsername(result.getValue("username"));

		member.setVerified(result.getValue("is_verified"));
		
		return Optional.of(member);
	}

	/**
	 * @param email
	 * @return Team of the member that has the given email
	 */
	public Optional<TeamDTO> getMemberTeam(String email, long server) {
		SubTransactionResult result = memberDAO.queryMemberTeam(email, server);
		
		if(result.isEmpty())
			return Optional.empty();
		
		// Map all the results from DAO to DTO
		TeamDTO team = new TeamDTO();
		team.setId(result.getValue("teamid"));
		team.setName(result.getValue("team_name"));
		team.setOrgname(result.getValue("team_orgname"));
		
		DiscordRoleDTO role = new DiscordRoleDTO();
		role.setId(result.getValue("droleid"));
		role.setRoleid(result.getValue("longroleid"));
		role.setServerid(result.getValue("discserid"));
		role.setName(result.getValue("role_name"));
		role.setEffectivename(result.getValue("effectivename"));
		team.setTeamRole(role);
		
		return Optional.of(team);
	}
	
	/**
	 * @param email
	 * @return Team of the member that has the given email
	 */
	public List<DiscordRoleDTO> getMemberRoles(String email, long server) {
		
		List<DiscordRoleDTO> roles = new ArrayList<>();
		SubTransactionResult result = memberDAO.queryMemberRoles(email, server);
		
		// Map all the results from DAO to DTO
		for (int i = 0; i < result.rowCount(); i++) {
			DiscordRoleDTO role = new DiscordRoleDTO();
			role.setId(result.getValue("droleid", i));
			role.setRoleid(result.getValue("longroleid", i));
			role.setServerid(result.getValue("discserid", i));
			role.setName(result.getValue("role_name", i));
			role.setEffectivename(result.getValue("effectivename", i));
			
			roles.add(role);
		}
		return roles;
	}
	
	/**
	 * Prepa MUST be verified in order to provide the orientador data
	 * 
	 * @param email of the prepa
	 * @param server
	 * @return List of the orientadores that are part of the group that the prepa is.
	 */
	public List<PrepaOrientadorDTO> getPrepaOrientadores(String email, long server) {
		
		List<PrepaOrientadorDTO> orientadores = new ArrayList<>();
		SubTransactionResult result = memberDAO.queryMemberRoles(email, server);
		
		// Map all the results from DAO to DTO
		for (int i = 0; i < result.rowCount(); i++) {
			PrepaOrientadorDTO orientador = new PrepaOrientadorDTO();
			orientador.setFirstname(result.getValue("fname", i));
			orientador.setLastname(result.getValue("lname", i));
			orientador.setTeamname(result.getValue("name", i));
			orientador.setOrganization(result.getValue("orgname", i));
			
			orientadores.add(orientador);
		}
		// If list is empty, that means that the prepa is not verified.
		return orientadores;
	}
	
	/**
	 * @param email
	 * @param server
	 * @return true if he given email with the server turns out to be an orientador
	 */
	public boolean isOrientador(String email, long server) {
		return !memberDAO.queryIsOrientador(email, server).isEmpty();
	}
	
	/**
	 * @param memberDTO
	 * @param rolePosition
	 * @param server
	 * @param teamname
	 * @return ID of the Member added
	 */
	public int addMember(MemberDTO memberDTO, MemberPosition rolePosition, long server, String teamname) {
		return memberDAO.insertMember(memberDTO, rolePosition, server, teamname);
	}
	
	/**
	 * @param memberDTO
	 * @param server
	 * @param teamname
	 * @return ID of the EO added
	 */
	public int addEOrientador(MemberDTO memberDTO, long server, String teamname) {
		return memberDAO.insertMember(memberDTO, MemberPosition.ESTUDIANTE_ORIENTADOR, server, teamname);
	}

	/**
	 * @param memberDTO
	 * @param server
	 * @param teamname
	 * @return ID of the Prepa added
	 */
	public int addPrepa(MemberDTO memberDTO, long server, String teamname) {
		return memberDAO.insertMember(memberDTO, MemberPosition.PREPA, server, teamname);
	}
	
	/**
	 * Adds a group of members to a server and assigns 
	 * a team for it to participate
	 * 
	 * @param members
	 * @param rolePosition
	 * @param server
	 * @param teamname
	 * @return List of all the member IDs that got added
	 */
	public boolean addMembers(List<MemberDTO> members, MemberPosition rolePosition, long server, String teamname) {
		return memberDAO.insertMemberGroup(members, rolePosition, server, teamname);
	}
	
	/**
	 * Deletes the members by group with the given list of verification IDs
	 * 
	 * @param memberIDs
	 * @return number of rows deleted
	 */
	public boolean deleteMembers(List<Integer> memberIDs) {
		return memberDAO.deleteMembers(memberIDs);
	}
	
	/**
	 * Converts from students to members
	 * @param students
	 * @return List of members
	 */
	public List<MemberDTO> toMember(List<StudentDTO> students) {
		return students.stream().map(student -> {
            MemberDTO member = new MemberDTO();
            member.setFirstname(student.getFirstname());
            member.setLastname(student.getLastname());
            member.setInitial(student.getInitial());
            member.setSex(student.getSex());
            member.setEmail(student.getEmail());
            member.setProgram(student.getProgram());
            return member;
        }).collect(Collectors.toList());
	}
	
	/**
     * Converts from members to students
     * @param members List of MemberDTO objects
     * @return List of StudentDTO objects
     */
    public List<StudentDTO> toStudent(List<MemberDTO> members) {
        return members.stream().map(member -> {
            StudentDTO student = new StudentDTO();
            student.setFirstname(member.getFirstname());
            student.setLastname(member.getLastname());
            student.setInitial(member.getInitial());
            student.setSex(member.getSex());
            student.setEmail(member.getEmail());
            student.setProgram(member.getProgram());
            return student;
        }).collect(Collectors.toList());
    }
}
