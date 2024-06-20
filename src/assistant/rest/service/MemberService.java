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
import assistant.rest.dao.MemberDAO;
import assistant.rest.dto.MemberDTO;
import assistant.rest.dto.MemberProgramDTO;
import assistant.rest.dto.MemberRoleDTO;
import assistant.rest.dto.MemberTeamDTO;

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
	
	public List<MemberDTO> getAll(int page, int size) {
		return memberDAO.getAll(page * size, size);
	}
	
	public Optional<MemberDTO> getMember(int verid) {
		return memberDAO.getMember(verid);
	}
	
	public Optional<MemberTeamDTO> getMemberTeam(int verid) {
		return memberDAO.getMemberTeam(verid);
	}
	
	public Optional<MemberRoleDTO> getMemberRole(int verid) {
		return memberDAO.getMemberRole(verid);
	}
	
	public Optional<MemberProgramDTO> getMemberProgram(int verid) {
		return memberDAO.getMemberProgram(verid);
	}
	
	public int addMember(MemberDTO memberDTO, String program, String team) {
		if(!MemberProgram.isProgram(program))
			return -1;
		return memberDAO.insertOrientadorMember(memberDTO, program, team);
	}
	
	public List<Integer> addMembers(List<MemberDTO> members, String program, String team) {
		List<Integer> results = new ArrayList<>();
		for(MemberDTO member : members)
			results.add(memberDAO.insertOrientadorMember(member, program, team));
		return results;
	}
	
	public int deleteMembers(List<Integer> memberVerificationIDs) {
		return memberDAO.deleteMembers(memberVerificationIDs);
	}
}
