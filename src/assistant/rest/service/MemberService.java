/**
 * 
 */
package assistant.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.discord.object.MemberProgram;
import assistant.discord.object.MemberRetrievement;
import assistant.rest.dao.MemberDAO;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.MemberDTO;

/**
 * @author Alfredo
 */
@Service
public class MemberService {
	
	private final MemberDAO memberDAO;
	
	@Autowired
	public MemberService(MemberDAO memberDAO) {
		this.memberDAO = memberDAO;
	}
	
	public List<EmailDTO> getEmails(int page, int size) {
		return memberDAO.getEmails(page * size, size);
	}
	
	public List<MemberDTO> getAllMembers(int page, int size, MemberRetrievement retrievement) {
		return memberDAO.getMembers(page * size, size, retrievement);
	}
	
	public Optional<MemberDTO> getPrepa(String email) {
		return memberDAO.getMember(email, MemberRetrievement.ALL_PREPA);
	}
	
	public Optional<MemberDTO> getOrientador(String email) {
		return memberDAO.getMember(email, MemberRetrievement.ALL_ORIENTADOR);
	}
	
	public Optional<MemberDTO> getMember(String email) {
		return memberDAO.getMember(email, MemberRetrievement.EVERYONE);
	}
	
	
//	public Optional<MemberTeamDTO> getMemberTeam(int verid) {
//		return memberDAO.getMemberTeam(verid);
//	}
//	
//	public Optional<MemberRoleDTO> getMemberRole(int verid) {
//		return memberDAO.getMemberRole(verid);
//	}
//	
//	public Optional<MemberProgramDTO> getMemberProgram(int verid) {
//		return memberDAO.getMemberProgram(verid);
//	}
	
	public int addEOrientador(MemberDTO memberDTO, String program, String team) {
		if(!MemberProgram.isProgram(program))
			return -1;
		return memberDAO.insertOrientadorMember(memberDTO, program, team);
	}

	public int addPrepa(MemberDTO memberDTO, String program, String team) {
		if(!MemberProgram.isProgram(program))
			return -1;
		return memberDAO.insertPrepaMember(memberDTO, program, team);
	}
	
	public List<Integer> addEOrientadores(List<MemberDTO> members, String program, String team) {
		List<Integer> results = new ArrayList<>();
		for(MemberDTO member : members)
			results.add(memberDAO.insertOrientadorMember(member, program, team));
		return results;
	}

	public List<Integer> addPrepas(List<MemberDTO> members, String program, String team) {
		List<Integer> results = new ArrayList<>();
		for(MemberDTO member : members)
			results.add(memberDAO.insertPrepaMember(member, program, team));
		return results;
	}
	
	public int deleteMembers(List<Integer> memberVerificationIDs) {
		return memberDAO.deleteMembers(memberVerificationIDs);
	}
}
