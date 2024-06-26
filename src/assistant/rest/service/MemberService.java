/**
 * 
 */
package assistant.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.discord.object.MemberPosition;
import assistant.discord.object.MemberRetrievement;
import assistant.rest.dao.MemberDAO;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.MemberDTO;
import assistant.rest.dto.TeamDTO;

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
	
	/**
	 * @param member
	 * @return true if verification succeeded
	 */
	public boolean verifyMember(MemberDTO member) {
		// If greater than zero, then it got
		// verified and inserted successfully
		return memberDAO.insertAndVerifyMember(member) > 0;
	}
	
	/**
	 * @param page
	 * @param size
	 * @param server
	 * @return List of DTO containing all emails of members
	 */
	public List<EmailDTO> getEmails(int page, int size, long server) {
		return memberDAO.getEmails(page * size, size, server);
	}
	
	/**
	 * @param page
	 * @param size
	 * @param retrievement
	 * @param server
	 * @return List of DTO containing all members
	 */
	public List<MemberDTO> getAllMembers(int page, int size, MemberRetrievement retrievement, long server) {
		return memberDAO.getMembers(page * size, size, retrievement, server);
	}
	
	/**
	 * @param email
	 * @param server
	 * @return Member that matches the given email
	 */
	public Optional<MemberDTO> getMember(String email) {
		return memberDAO.getMember(email);
	}

	/**
	 * @param email
	 * @return Team of the member that has the given email
	 */
	public Optional<TeamDTO> getMemberTeam(String email, long server) {
		return memberDAO.getMemberTeam(email, server);
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
	public List<Integer> addMembers(List<MemberDTO> members, MemberPosition rolePosition, long server, String teamname) {
		List<Integer> results = new ArrayList<>();
		for(MemberDTO member : members)
			results.add(memberDAO.insertMember(member, rolePosition, server, teamname));
		return results;
	}
	
	/**
	 * Deletes the members by group with the given list of verification IDs
	 * 
	 * @param memberVerificationIDs
	 * @return number of rows deleted
	 */
	public int deleteMembers(List<Integer> memberVerificationIDs) {
		return memberDAO.deleteMembers(memberVerificationIDs);
	}
}
