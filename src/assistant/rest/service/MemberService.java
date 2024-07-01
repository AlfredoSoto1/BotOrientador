/**
 * 
 */
package assistant.rest.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.database.SubTransactionResult;
import assistant.discord.core.AsyncTaskQueue;
import assistant.discord.object.MemberPosition;
import assistant.discord.object.MemberProgram;
import assistant.discord.object.MemberRetrievement;
import assistant.rest.dao.MemberDAO;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.MemberDTO;
import assistant.rest.dto.StudentDTO;
import assistant.rest.dto.TeamDTO;

/**
 * @author Alfredo
 */
@Service
public class MemberService {
	
	private final MemberDAO memberDAO;
	private AsyncTaskQueue verificationQueue;
	
	@Autowired
	public MemberService(MemberDAO memberDAO) {
		this.memberDAO = memberDAO;
	}
	
	/**
	 * 
	 */
	public void shutdownVerificationQueueService() {
		verificationQueue.shutdown();
	}
	
	public boolean verifyMember(MemberDTO member, long server, Runnable method) {
//		// Do this asynchronously
//		verificationQueue.addTask(() -> {
//			try {
//				// Try assigning the roles and appropriate nickname
//				// to the member. Catch any exceptions that might happen during run-time.
//				assignRoleAndChangeNickname(event.getHook(), event.getGuild(), event.getMember(), report.get());
//			} catch (InterruptedException ie) {
//				ie.printStackTrace();
//			} catch (InsufficientPermissionException ipe) {
//				super.feedbackDev("Insufficient permissions to assign role or change nickname: " + ipe.getMessage());
//			}
//			
//			// Confirm and commit verification
//			verificationDAO.confirmVerification(event.getGuild(), email, funfacts);
//			
//			// TODO Send welcome message through DMs
//		});
		
		return false;
	}
	
	/**
	 * @param member
	 * @return true if verification succeeded
	 */
	public boolean verifyMember(MemberDTO member, long server) {
		return memberDAO.insertAndVerifyMember(member, server);
	}
	
	/**
	 * @param page
	 * @param size
	 * @param server
	 * @return List of DTO containing all emails of members
	 */
	public List<EmailDTO> getEmails(int page, int size, long server) {
		// Obtain the results and pass them to the DTO list
		List<EmailDTO> emails = new ArrayList<>();
		SubTransactionResult result = memberDAO.queryEmails(page * size, size, server);
		
		// Map all the results from DAO to DTO
		for (int i = 0; i < result.rowCount(); i++) {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setId((int)result.getValue("memid", i));
            emailDTO.setEmail((String) result.getValue("email", i));
			emails.add(emailDTO);
		}
		return emails;
	}
	
	/**
	 * @param page
	 * @param size
	 * @param retrievement
	 * @param server
	 * @return List of DTO containing all members
	 */
	public List<MemberDTO> getAllMembers(int page, int size, MemberRetrievement retrievement, long server) {
		
		// Obtain the results and pass them to the DTO list
		List<MemberDTO> members = new ArrayList<>();
		SubTransactionResult result = memberDAO.queryAllMembers(page * size, size, retrievement, server);
		
		// Map all the results from DAO to DTO
		for (int i = 0; i < result.rowCount(); i++) {
			MemberDTO member = new MemberDTO();
			member.setId(result.getValue("memid", i));
			member.setUserId(result.getValue("identifier", i));
			
			member.setFirstname(result.getValue("firstname", i));
			member.setLastname(result.getValue("lastname", i));
			member.setInitial(result.getValue("initial", i));
			member.setSex(result.getValue("sex", i));
			
			member.setEmail(result.getValue("email", i));
			member.setProgram(MemberProgram.asProgram(result.getValue("program_name", i)));
			member.setFunfact(result.getValue("funfact", i));
			member.setUsername(result.getValue("username", i));

			member.setVerified(result.getValue("is_verified", i));
			
			members.add(member);
		}
		return members;
	}
	
	/**
	 * @param email
	 * @param server
	 * @return Member that matches the given email
	 */
	public Optional<MemberDTO> getMember(String email) {
		SubTransactionResult result = memberDAO.queryMember(email);
		
		if(result.isEmpty())
			return Optional.empty();
		
		// Map all the results from DAO to DTO
		MemberDTO member = new MemberDTO();
		member.setId(result.getValue("memid", 0));
		member.setUserId(result.getValue("identifier", 0));
		
		member.setFirstname(result.getValue("firstname", 0));
		member.setLastname(result.getValue("lastname", 0));
		member.setInitial(result.getValue("initial", 0));
		member.setSex(result.getValue("sex", 0));
		
		member.setEmail(result.getValue("email", 0));
		member.setProgram(MemberProgram.asProgram(result.getValue("program_name", 0)));
		member.setFunfact(result.getValue("funfact", 0));
		member.setUsername(result.getValue("username", 0));

		member.setVerified(result.getValue("is_verified", 0));
		
		return Optional.of(member);
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
	public boolean addMembers(List<MemberDTO> members, MemberPosition rolePosition, long server, String teamname) {
		return memberDAO.insertMemberGroup(members, rolePosition, server, teamname);
	}
	
	/**
	 * Deletes the members by group with the given list of verification IDs
	 * 
	 * @param memberIDs
	 * @return number of rows deleted
	 */
	public boolean deleteMembers(List<Integer> memberIDs) {
		return memberDAO.deleteMembers(memberIDs);
	}
	
	/**
	 * Converts from students to members
	 * @param students
	 * @return List of members
	 */
	public List<MemberDTO> toMember(List<StudentDTO> students) {
		return students.stream().map(student -> {
            MemberDTO member = new MemberDTO();
            member.setFirstname(student.getFirstname());
            member.setLastname(student.getLastname());
            member.setInitial(student.getInitial());
            member.setSex(student.getSex());
            member.setEmail(student.getEmail());
            member.setProgram(student.getProgram());
            return member;
        }).collect(Collectors.toList());
	}
	
	/**
     * Converts from members to students
     * @param members List of MemberDTO objects
     * @return List of StudentDTO objects
     */
    public List<StudentDTO> toStudent(List<MemberDTO> members) {
        return members.stream().map(member -> {
            StudentDTO student = new StudentDTO();
            student.setFirstname(member.getFirstname());
            student.setLastname(member.getLastname());
            student.setInitial(member.getInitial());
            student.setSex(member.getSex());
            student.setEmail(member.getEmail());
            student.setProgram(member.getProgram());
            return student;
        }).collect(Collectors.toList());
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
                String email     = getCellValue(row.getCell(6));
                String sex       = getCellValue(row.getCell(9));
                String program   = getCellValue(row.getCell(10));
                
                if (first_lastName.isBlank())
                	// If no first last name is found, provide empty string character
                	first_lastName = "_";

                if (second_lastName.isBlank())
                	// If no second last name is found, provide empty string character
                	second_lastName = "_";
                
                if(initial.isBlank())
                	// If no initial is found, provide empty string character
                	initial = "_";
                
                if (sex.isBlank())
                	// If no sex is implied, provide empty string character
                	sex = "_";
                
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
}
