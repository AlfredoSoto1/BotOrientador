/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.rest.dao.TeamDAO;
import assistant.rest.dto.TeamDTO;

/**
 * @author Alfredo
 */
@Service
public class TeamService {
	
	/**
	 * FIXME: remove comment
	 * 
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
	
	@Autowired
	public TeamService(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
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
}
