/**
 * 
 */
package assistant.rest.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
	public Map<Integer, List<StudentDTO>> getPrepaListFrom(List<StudentDTO> students, int teamCount, int groupSize, int femalesPerGroup) {
		
		Map<Integer, List<StudentDTO>> resultTable = new HashMap<>();
		
		List<StudentDTO> females = students.stream()
                .filter(s -> "F".equals(s.getSex()))
                .collect(Collectors.toList());

        List<StudentDTO> males = students.stream()
                .filter(s -> !"F".equals(s.getSex()))
                .collect(Collectors.toList());
        
        for(int i = 0;i < teamCount;i++)
        	resultTable.put(i, new ArrayList<>());
        
        // Distribute females into the teams
        int femaleIndex = 0;
        for (int i = 0; i < teamCount && femaleIndex < females.size(); i++) {
            for (int j = 0; j < femalesPerGroup && femaleIndex < females.size(); j++) {
                resultTable.get(i).add(females.get(femaleIndex++));
            }
        }
        
        // Distribute males into the teams
        int maleIndex = 0;
        for (int i = 0; i < teamCount; i++) {
            while (resultTable.get(i).size() < groupSize && maleIndex < males.size()) {
                resultTable.get(i).add(males.get(maleIndex++));
            }
        }
        
        return resultTable;
	}
	
	public void exportStudentsTo(List<StudentDTO> students, String pathToExcel) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Students");

		// Create header row
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("First Last Name");
		headerRow.createCell(1).setCellValue("Second Last Name");
		headerRow.createCell(2).setCellValue("First Name");
		headerRow.createCell(3).setCellValue("Initial");
		headerRow.createCell(4).setCellValue("Email");
		headerRow.createCell(5).setCellValue("Sex");
		headerRow.createCell(6).setCellValue("Program");

		// Populate sheet with student data
		int rowNum = 1;
		for (StudentDTO student : students) {
			Row row = sheet.createRow(rowNum++);

			String[] lastNameParts = student.getLastname().split(" ");
			String firstLastName = lastNameParts.length > 0 ? lastNameParts[0] : "";
			String secondLastName = lastNameParts.length > 1 ? lastNameParts[1] : "";

			row.createCell(0).setCellValue(firstLastName); // First Last Name
			row.createCell(1).setCellValue(secondLastName); // Second Last Name
			row.createCell(2).setCellValue(student.getFirstname());
			row.createCell(3).setCellValue(student.getInitial());
			row.createCell(4).setCellValue(student.getEmail());
			row.createCell(5).setCellValue(student.getSex());
			row.createCell(6).setCellValue(student.getProgram());
		}

		// Write the workbook to a file
		try (FileOutputStream fos = new FileOutputStream(pathToExcel)) {
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
