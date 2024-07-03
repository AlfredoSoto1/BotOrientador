/**
 * 
 */
package assistant.rest.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import assistant.discord.object.MemberPosition;
import assistant.discord.object.MemberProgram;
import assistant.rest.dto.MemberDTO;
import assistant.rest.dto.StudentDTO;
import assistant.rest.dto.TeamDTO;

/**
 * @author Alfredo
 */
@Service
public class TeamGroupCreatorService {
	
	private final TeamService teamService;
	private final MemberService memberService;
	
	public TeamGroupCreatorService(MemberService memberService, TeamService teamService) {
		this.teamService = teamService;
		this.memberService = memberService;
	}
	
	/**
	 * 
	 * @param program1
	 * @param program2
	 * @param server
	 * @param excelFilePath
	 */
	public void distributeAndExportStudentsToTeams(int groupSize, int femaleCount, MemberProgram program1, MemberProgram program2, long server, String excelFilePath, String outputFolder) {
		// Obtain all teams available in a server
		// as of max values that can receive is 20 starting at page 0
		List<TeamDTO> teams = teamService.getAllTeams(0, 20, server);
		
		// Load students from excel file to a
		// Student Data transfer object to manipulate the data
		List<StudentDTO> loadedStudents = this.loadStudentsFrom(excelFilePath);
		
		// Obtain the Prepa division teams from student list
		var teamsTable = teamService.getPrepaTeamDivisionFrom(
			program1,
			program2,
			loadedStudents,
			teams.size(),
			groupSize,
			femaleCount
		);
		
		this.debugLogTeamDistribution(program1, program2, teamsTable);
		
		for (var groupTeam : teamsTable.entrySet()) {
			// Transform the set of students of a team
			// to members, this way the service can handle them as new members
			List<MemberDTO> members = memberService.toMember(groupTeam.getValue());
			
			// Tell the member service to add all the new members from 
			// the selected team according to the id generated by the teams table.
			// This will allocate the new members into the Prepa entity table
			// as part of a team within a server.
			memberService.addMembers(members, MemberPosition.PREPA, server, teams.get(groupTeam.getKey()).getName());
			
			// Obtain the team name from the team list using the id from group division
			String teamname = teams.get(groupTeam.getKey()).getName();
			
			// Obtain a team from the list and export the student
			// group to an excel sheet where it will contain all new student group data
			this.exportStudentsTo(teamname, groupTeam.getValue(), outputFolder + "Equipo-" + teamname + ".xlsx");
		}
	}
	
	/**
	 * Exports the student list to an excel
	 * @param students
	 * @param pathToExcel
	 */
	public void exportStudentsTo(String teamname, List<StudentDTO> students, String pathToExcel) {
		
		// Sort the students so that they appear by name
		students.sort(Comparator.comparing(StudentDTO::getFirstname)
                .thenComparing(StudentDTO::getLastname));
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(teamname);

		// Create header row
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("First Name");
		headerRow.createCell(1).setCellValue("Last Name");
		headerRow.createCell(2).setCellValue("Initial");
		headerRow.createCell(3).setCellValue("Email");
		headerRow.createCell(4).setCellValue("Program");

		// Populate sheet with student data
		int rowNum = 1;
		for (StudentDTO student : students) {
			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(student.getFirstname());
			row.createCell(1).setCellValue(student.getLastname());
			row.createCell(2).setCellValue(student.getInitial());
			row.createCell(3).setCellValue(student.getEmail());
			row.createCell(4).setCellValue(student.getProgram().getLiteral());
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
	 * Loads students from excel
	 * @param pathToExcel
	 * @return List of students
	 */
	public List<StudentDTO> loadStudentsFrom(String pathToExcel) {
		List<StudentDTO> students = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(pathToExcel); 
				Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = (Sheet) workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			// Skip header row
			if (rowIterator.hasNext())
				rowIterator.next();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

                String first_lastName  = getCellValue(row.getCell(0));
                String second_lastName = getCellValue(row.getCell(1));
                String firstname = getCellValue(row.getCell(2));
                String initial   = getCellValue(row.getCell(3));
                String email     = getCellValue(row.getCell(4));
                String sex       = getCellValue(row.getCell(5));
                String program   = getCellValue(row.getCell(6));
                
                if (first_lastName.isBlank())
                	// If no first last name is found, provide empty string character
                	first_lastName = "";

                if (second_lastName.isBlank())
                	// If no second last name is found, provide empty string character
                	second_lastName = "";
                
                if(initial.isBlank())
                	// If no initial is found, provide empty string character
                	initial = "-";
                
                if (sex.isBlank())
                	// If no sex is implied, provide empty string character
                	sex = "-";
                
                if(email.isBlank())
                	// Check for the personal gmail account instead
                	// This condition only gets triggered if and only if
                	// the student doesn't have an institutional email given by mistake
                	email = getCellValue(row.getCell(5));
                
                // Add the student DTO
				students.add(new StudentDTO(firstname, first_lastName + " " + second_lastName, initial, sex, email, MemberProgram.asProgram(program)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return students;
	}
	
	
	/**
	 * Prints to the console the distribution of the teams
	 * @param program1
	 * @param program2
	 * @param teamsTable
	 */
	public void debugLogTeamDistribution(MemberProgram program1, MemberProgram program2, Map<Integer, List<StudentDTO>> teamsTable) {
		System.out.println("==========================================");
		// Print teams for verification
		int total = 0;
        for (Map.Entry<Integer, List<StudentDTO>> entry : teamsTable.entrySet()) {
            int teamNumber = entry.getKey();
            List<StudentDTO> team = entry.getValue();

            long femaleCount = team.stream().filter(s -> "F".equals(s.getSex())).count();
            long maleCount = team.stream().filter(s -> !"F".equals(s.getSex())).count();

            long femalesInsoCount = team.stream().filter(s -> "F".equals(s.getSex()) && program1 == s.getProgram()).count();
            long femalesCiicCount = team.stream().filter(s -> "F".equals(s.getSex()) && program2 == s.getProgram()).count();

            long malesInsoCount = team.stream().filter(s -> !"F".equals(s.getSex()) && program1 == s.getProgram()).count();
            long malesCiicCount = team.stream().filter(s -> !"F".equals(s.getSex()) && program2 == s.getProgram()).count();

            System.out.println("Team " + teamNumber + ":");
            System.out.println("Total students: " + team.size());
            System.out.println("Females: " + femaleCount + " (" + program1 + ": " + femalesInsoCount + ", " + program2 + ": " + femalesCiicCount + ")");
            System.out.println("Males: " + maleCount + " (" + program1 + ": " + malesInsoCount + ", " + program2 + ": " + malesCiicCount + ")");
            System.out.println();

            total += team.size();
        }
        System.out.println("Total students processed in distribution: " + total);
	}
	
    private String getCellValue(Cell cell) {
        if (cell == null)
            return "";
        
        switch (cell.getCellType()) {
        case STRING:
            return cell.getStringCellValue();
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                return String.valueOf(cell.getNumericCellValue());
            }
        case BOOLEAN:
            return String.valueOf(cell.getBooleanCellValue());
        case FORMULA:
            return cell.getCellFormula();
        default:
            return "";
        }
    }
    
	public List<StudentDTO> lookForDifference(List<StudentDTO> group1, List<StudentDTO> group2) {
		Set<StudentDTO> set1 = new HashSet<>(group1);
	    Set<StudentDTO> set2 = new HashSet<>(group2);

	    // Find students that are only in group1
	    Set<StudentDTO> onlyInGroup1 = set1.stream()
	                                       .filter(student -> !set2.contains(student))
	                                       .collect(Collectors.toSet());

	    // Find students that are only in group2
	    Set<StudentDTO> onlyInGroup2 = set2.stream()
	                                       .filter(student -> !set1.contains(student))
	                                       .collect(Collectors.toSet());

	    // Combine the two sets
	    onlyInGroup1.addAll(onlyInGroup2);

	    return new ArrayList<>(onlyInGroup1);
	}
}
