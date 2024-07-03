/**
 * 
 */
package assistant.rest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.discord.object.MemberProgram;
import assistant.rest.dao.TeamDAO;
import assistant.rest.dto.StudentDTO;
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
	
	/**
	 * @param program1
	 * @param program2
	 * @param students
	 * @param teamCount
	 * @param groupSize
	 * @param femalesPerGroup
	 * @return Map of team numbers containing student lists perfectly distributed
	 */
	public Map<Integer, List<StudentDTO>> getPrepaTeamDivisionFrom(MemberProgram program1, MemberProgram program2, List<StudentDTO> students, int teamCount, int groupSize, int femalesPerGroup) {
		Map<Integer, List<StudentDTO>> resultTable = new HashMap<>();
		
        Queue<StudentDTO> femalesInso = new LinkedList<>(students.stream()
                .filter(s -> "F".equals(s.getSex()) && program1 == s.getProgram())
                .collect(Collectors.toList()));

        Queue<StudentDTO> femalesCiic = new LinkedList<>(students.stream()
                .filter(s -> "F".equals(s.getSex()) && program2 == s.getProgram())
                .collect(Collectors.toList()));

        Queue<StudentDTO> malesInso = new LinkedList<>(students.stream()
                .filter(s -> !"F".equals(s.getSex()) && program1 == s.getProgram())
                .collect(Collectors.toList()));

        Queue<StudentDTO> malesCiic = new LinkedList<>(students.stream()
                .filter(s -> !"F".equals(s.getSex()) && program2 == s.getProgram())
                .collect(Collectors.toList()));
        
        for(int i = 0;i < teamCount;i++)
        	resultTable.put(i, new ArrayList<>());
        
        System.out.println();
        
        // Distribute females into the teams
        for (int i = 0; i < teamCount; i++) {
            for (int j = 0; j < femalesPerGroup / 2 && !femalesInso.isEmpty(); j++) {
                resultTable.get(i).add(femalesInso.poll());
            }
            for (int j = 0; j < femalesPerGroup / 2 && !femalesCiic.isEmpty(); j++) {
                resultTable.get(i).add(femalesCiic.poll());
            }
            
            // Fill up to max
            while(!femalesInso.isEmpty() && resultTable.get(i).size() < femalesPerGroup)
            	resultTable.get(i).add(femalesInso.poll());
            
            while(!femalesCiic.isEmpty() && resultTable.get(i).size() < femalesPerGroup)
            	resultTable.get(i).add(femalesCiic.poll());
            
            // Fill if less than max
            while(!femalesInso.isEmpty() && (femalesCiic.size() + femalesInso.size()) < femalesPerGroup)
            	resultTable.get(i).add(femalesInso.poll());
            
            while(!femalesCiic.isEmpty() && (femalesCiic.size() + femalesInso.size()) < femalesPerGroup)
            	resultTable.get(i).add(femalesCiic.poll());
        }
        
        // Distribute males into the teams
        for (int i = 0; i < teamCount; i++) {
        	int malesPerGroup = groupSize - resultTable.get(i).size();
        	
        	for (int j = 0; j < malesPerGroup / 2 && !malesInso.isEmpty(); j++) {
                resultTable.get(i).add(malesInso.poll());
            }
            for (int j = 0; j < malesPerGroup / 2 && !malesCiic.isEmpty(); j++) {
                resultTable.get(i).add(malesCiic.poll());
            }
            
            // Fill up to max
            while(!malesInso.isEmpty() && resultTable.get(i).size() < malesPerGroup)
            	resultTable.get(i).add(malesInso.poll());
            
            while(!malesCiic.isEmpty() && resultTable.get(i).size() < malesPerGroup)
            	resultTable.get(i).add(malesCiic.poll());
        }
        return resultTable;
	}
	
}
