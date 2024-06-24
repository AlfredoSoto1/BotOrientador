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
public class DeveloperService {
	
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
	
	@Autowired
	public DeveloperService(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}
	
	public List<TeamDTO> getAllTeams(int page, int size, String teamName) {
		return teamDAO.getAllTeams(page * size, size, teamName);
	}
	
	public int addTeam(TeamDTO team) {
		return teamDAO.insertTeam(team);
	}
	
	public Optional<TeamDTO> deleteTeam(int id) {
		return teamDAO.deleteTeam(id);
	}
}
