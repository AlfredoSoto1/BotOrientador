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
	
	
	/**
	 * @param program1
	 * @param program2
	 * @param students
	 * @param teamCount
	 * @param groupSize
	 * @param femalesPerGroup
	 * @return Map of team numbers containing student lists perfectly distributed
	 */
	public Map<Integer, List<StudentDTO>> getPrepaListFrom(String program1, String program2, List<StudentDTO> students, int teamCount, int groupSize, int femalesPerGroup) {
		
		Map<Integer, List<StudentDTO>> resultTable = new HashMap<>();
		
        List<StudentDTO> femalesInso = students.stream()
                .filter(s -> "F".equals(s.getSex()) && program1.equals(s.getProgram()))
                .collect(Collectors.toList());

        List<StudentDTO> femalesCiic = students.stream()
                .filter(s -> "F".equals(s.getSex()) && program2.equals(s.getProgram()))
                .collect(Collectors.toList());

        List<StudentDTO> malesInso = students.stream()
                .filter(s -> !"F".equals(s.getSex()) && program1.equals(s.getProgram()))
                .collect(Collectors.toList());

        List<StudentDTO> malesCiic = students.stream()
                .filter(s -> !"F".equals(s.getSex()) && program2.equals(s.getProgram()))
                .collect(Collectors.toList());
        
        for(int i = 0;i < teamCount;i++)
        	resultTable.put(i, new ArrayList<>());
        
        // Distribute females into the teams
        // Distribute females into the teams
        int femaleInsoIndex = 0;
        int femaleCiicIndex = 0;
        for (int i = 0; i < teamCount; i++) {
            for (int j = 0; j < femalesPerGroup / 2 && femaleInsoIndex < femalesInso.size(); j++) {
                resultTable.get(i).add(femalesInso.get(femaleInsoIndex++));
            }
            for (int j = 0; j < femalesPerGroup / 2 && femaleCiicIndex < femalesCiic.size(); j++) {
                resultTable.get(i).add(femalesCiic.get(femaleCiicIndex++));
            }
        }
        
        // Distribute males into the teams
        int malesPerGroup = groupSize - femalesPerGroup;
        int maleInsoIndex = 0;
        int maleCiicIndex = 0;
        for (int i = 0; i < teamCount; i++) {
            for (int j = 0; j < malesPerGroup / 2 && maleInsoIndex < malesInso.size(); j++) {
                resultTable.get(i).add(malesInso.get(maleInsoIndex++));
            }
            for (int j = 0; j < malesPerGroup / 2 && maleCiicIndex < malesCiic.size(); j++) {
                resultTable.get(i).add(malesCiic.get(maleCiicIndex++));
            }
        }
        
        // If there are remaining females or males, distribute them to balance
        for (int i = 0; i < teamCount; i++) {
            while (resultTable.get(i).size() < groupSize) {
                if (femaleInsoIndex < femalesInso.size()) {
                    resultTable.get(i).add(femalesInso.get(femaleInsoIndex++));
                } else if (femaleCiicIndex < femalesCiic.size()) {
                    resultTable.get(i).add(femalesCiic.get(femaleCiicIndex++));
                } else if (maleInsoIndex < malesInso.size()) {
                    resultTable.get(i).add(malesInso.get(maleInsoIndex++));
                } else if (maleCiicIndex < malesCiic.size()) {
                    resultTable.get(i).add(malesCiic.get(maleCiicIndex++));
                } else {
                    break;
                }
            }
        }
        
        return resultTable;
	}
	
	/**
	 * Exports the student list to an excel
	 * @param students
	 * @param pathToExcel
	 */
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
	
	/**
	 * Prints to the console the distribution of the teams
	 * @param program1
	 * @param program2
	 * @param teamsTable
	 */
	public void debugLogTeamDistribution(String program1, String program2, Map<Integer, List<StudentDTO>> teamsTable) {
		// Print teams for verification
		int total = 0;
        for (Map.Entry<Integer, List<StudentDTO>> entry : teamsTable.entrySet()) {
            int teamNumber = entry.getKey();
            List<StudentDTO> team = entry.getValue();

            long femaleCount = team.stream().filter(s -> "F".equals(s.getSex())).count();
            long maleCount = team.stream().filter(s -> !"F".equals(s.getSex())).count();

            long femalesInsoCount = team.stream().filter(s -> "F".equals(s.getSex()) && program1.equals(s.getProgram())).count();
            long femalesCiicCount = team.stream().filter(s -> "F".equals(s.getSex()) && program2.equals(s.getProgram())).count();

            long malesInsoCount = team.stream().filter(s -> !"F".equals(s.getSex()) && program1.equals(s.getProgram())).count();
            long malesCiicCount = team.stream().filter(s -> !"F".equals(s.getSex()) && program2.equals(s.getProgram())).count();

            System.out.println("Team " + teamNumber + ":");
            System.out.println("Total students: " + team.size());
            System.out.println("Females: " + femaleCount + " (INSO: " + femalesInsoCount + ", CIIC: " + femalesCiicCount + ")");
            System.out.println("Males: " + maleCount + " (INSO: " + malesInsoCount + ", CIIC: " + malesCiicCount + ")");
            System.out.println();

            total += team.size();
        }
        System.out.println("Total students processed in distribution: " + total);
	}
}
