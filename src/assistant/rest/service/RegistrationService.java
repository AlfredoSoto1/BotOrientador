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
import assistant.rest.dao.DiscordServerDAO;
import assistant.rest.dao.MemberDAO;
import assistant.rest.dao.TeamDAO;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.MemberDTO;
import assistant.rest.dto.TeamDTO;

/**
 * @author Alfredo
 */
@Service
public class RegistrationService {
	
	/**
	 * THIS SHOULD BE MERGED WITH THE DISCORD REGISTRATION
	 * 
	 * This class must contain the important functions
	 * that allow CRUD operations with the database.
	 * This class is in charged of interacting with each table-entity
	 * of the database.
	 * 
	 * This must be the only service that allows contact with the database
	 * for such entities. If there is a special behavior/thing that relates
	 * table CRUD operations that is not directly with the concept of entities,
	 * then it should have its own Service-Class.
	 * 
	 * There will still be a controller for each special behavior and each entity
	 * HTTP control system. Not all services are tide to a controller, this is
	 * because not all services need to be exposed to the client when using the API.
	 */
	
	private final TeamDAO teamDAO;
	private final MemberDAO memberDAO;
	private final DiscordServerDAO discordServerDAO;
	
	@Autowired
	public RegistrationService(TeamDAO teamDAO, MemberDAO memberDAO, DiscordServerDAO discordServerDAO) {
		this.teamDAO = teamDAO;
		this.memberDAO = memberDAO;
		this.discordServerDAO = discordServerDAO;
	}
	
	public List<String> getEffectiveRoleNames() {
		return discordServerDAO.getEffectiveRoleNames();
	}
	
	public List<DiscordServerDTO> getAllRegisteredDiscordServers(int offset, int limit) {
		return discordServerDAO.getAllRegisteredDiscordServers(offset, limit);
	}
	
	public Optional<DiscordServerDTO> getRegisteredDiscordServer(int id) {
		return discordServerDAO.getRegisteredDiscordServer(id);
	}
	
	public List<DiscordRoleDTO> getAllRoles(int offset, int limit, long server) {
		return discordServerDAO.getAllRoles(offset, limit, server);
	}
	
	public Optional<DiscordRoleDTO> getEffectivePositionRole(MemberPosition rolePosition, long server) {
		return discordServerDAO.getEffectivePositionRole(rolePosition, server);
	}
	
	public int insertDiscordServer(DiscordServerDTO discordServer) {
		return discordServerDAO.insertDiscordServer(discordServer);
	}
	
	public int insertRole(DiscordRoleDTO role) {
		return discordServerDAO.insertRole(role);
	}
	
	/**
	 * @param page
	 * @param size
	 * @param server
	 * @return list of teams in a server
	 */
	public List<TeamDTO> getAllTeams(int page, int size, long server) {
		return teamDAO.getAllTeams(page * size, size, server);
	}
	
	/**
	 * @param teamName
	 * @param server
	 * @return team inside of a given server
	 */
	public Optional<TeamDTO> getTeam(String teamName, long server) {
		return teamDAO.getTeam(teamName, server);
	}
	
	/**
	 * @param team
	 * @return teamid of the team added
	 */
	public int addTeam(TeamDTO team) {
		return teamDAO.insertTeam(team);
	}
	
	/**
	 * @param teamname
	 * @param server
	 * @return Team that gets deleted
	 */
	public Optional<TeamDTO> deleteTeam(String teamname, long server) {
		return teamDAO.deleteTeam(teamname, server);
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
	
	public int addMember(MemberDTO memberDTO, MemberPosition rolePosition, long server, String teamname) {
		return memberDAO.insertMember(memberDTO, rolePosition, server, teamname);
	}
	
	public int addEOrientador(MemberDTO memberDTO, long server, String teamname) {
		return memberDAO.insertMember(memberDTO, MemberPosition.ESTUDIANTE_ORIENTADOR, server, teamname);
	}

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
