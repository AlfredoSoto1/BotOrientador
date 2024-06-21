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
	
	private final TeamDAO teamDAO;
	
	@Autowired
	public TeamService(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}
	
	public List<TeamDTO> getAll(int page, int size) {
		return teamDAO.getAll(page * size, size);
	}
	
	public Optional<TeamDTO> getTeam(int id) {
		return teamDAO.getTeam(id);
	}
	
	public int addTeam(TeamDTO team) {
		return teamDAO.insertTeam(team);
	}
}
