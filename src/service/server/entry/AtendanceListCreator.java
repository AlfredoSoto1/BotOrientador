///**
// * 
// */
//package application.launch;
//
//import java.awt.Color;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Queue;
//import java.util.Stack;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import application.core.ApplicationThread;
//import application.database.DatabaseConnections;
//import application.records.MemberRecord;
//import services.bot.orientador.roles.DepartmentRole;
//import services.bot.orientador.roles.TeamRole;
//
///**
// * @author alfredo
// *
// */
//public class AtendanceListCreator extends ApplicationThread {
//	
//	public AtendanceListCreator() {
//	}
//
//	@Override
//	public void init() {
//		
////		DatabaseConnections.instance()
////		.getTeamMadeConnection()
////		.joinConnection(()->{
////			PreparedStatement stmt = DatabaseConnections.instance()
////					.getTeamMadeConnection()
////					.getConnection()
////					.prepareStatement("DELETE FROM VerifiedMembers");
////			
////			stmt.executeUpdate();
////			
////			stmt.close();
////		});
////		
////		try {
////			loadNamesAndEmails();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//		
//		
////		createFullName();
//		
//		Map<String, String> groups = getGroups();
//		List<MemberRecord> members = getMembers();
//		
//		Map<String, List<MemberRecord>> finalteams = createTeamGroups(groups, members);
//		
//		pushTeamnamesForstudents(finalteams);
//		
//		try {
//			createExcel(new ArrayList<>(groups.keySet()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void dispose() {
//	}
//	
//	private void loadNamesAndEmails() throws IOException {
//		
//		Workbook workbook = new XSSFWorkbook(new FileInputStream("assets/attendance/All-Prepas-2023.xlsx"));
//		
//		
//		DatabaseConnections.instance()
//		.getTeamMadeConnection()
//		.joinConnection(()->{
//			PreparedStatement stmt = DatabaseConnections.instance()
//					.getTeamMadeConnection()
//					.getConnection()
//					.prepareStatement("INSERT INTO VerifiedMembers (FatherLastName, MotherLastName, FirstName, Initial, Email, Department) VALUES (?, ?, ?, ?, ?, ?)");
//			
//			// For each department
//			for(Sheet sheet : workbook) {
//				System.out.println("======" + sheet.getSheetName() + "=====");
//				for(int rowNum = 15; rowNum < sheet.getLastRowNum() + 1;rowNum++) {
//					Row row = sheet.getRow(rowNum);
//					
//					String father = row.getCell(0).getStringCellValue();
//					String mother = row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue();
//					String name = row.getCell(2).getStringCellValue();
//					String initial = row.getCell(3) == null ? "" : row.getCell(3).getStringCellValue();
//					String email = row.getCell(7).getStringCellValue();
//					
//					System.out.printf("Name: %-30s Email: %s%n", father + " " + mother + " " + name + " " + initial, email);
//					
//					stmt.setString(1, father);
//					stmt.setString(2, mother);
//					stmt.setString(3, name);
//					stmt.setString(4, initial);
//					stmt.setString(5, email);
//					stmt.setString(6, sheet.getSheetName());
//					
//					stmt.addBatch();
//				}
//			}
//			stmt.executeBatch();
//			stmt.close();
//		});
//		
//		workbook.close();
//	}
//	
//	private void createFullName() {
//		
//		Stack<MemberRecord> records = new Stack<>();
//		
//		DatabaseConnections.instance()
//		.getTeamMadeConnection()
//		.joinConnection(()->{
//			PreparedStatement stmt = DatabaseConnections.instance()
//					.getTeamMadeConnection()
//					.getConnection()
//					.prepareStatement("SELECT FatherLastName, MotherLastName, FirstName, Email FROM VerifiedMembers");
//			
//			
//			ResultSet result = stmt.executeQuery();
//			
//			while(result.next()) {
//				
//				String fullName = result.getString(3) + " " + result.getString(1) + (result.getString(2) != null ? " " + result.getString(2) : "");
//				
//				MemberRecord record = new MemberRecord();
//				record.setFullName(fullName);
//				record.setEmail(result.getString(4));
//				
//				records.add(record);
//			}
//			
//			result.close();
//			stmt.close();
//			
//			stmt = DatabaseConnections.instance()
//					.getTeamMadeConnection()
//					.getConnection()
//					.prepareStatement("UPDATE VerifiedMembers SET FullName = ? WHERE (Email = ?)");
//			
//			while(!records.isEmpty()) {
//				
//				MemberRecord record = records.pop();
//				
//				stmt.setString(1, record.getFullName());
//				stmt.setString(2, record.getEmail());
//				stmt.addBatch();
//			}
//			stmt.executeBatch();
//			stmt.close();
//		});
//		
//	}
//	
//	private void createExcel(List<String> teams) throws IOException {
//		
//		// Create a workbook instance that will later be
//		// dispatched as an excel file
//		Workbook workbook = new XSSFWorkbook();
//		
//		OutputStream generatedExcel = new FileOutputStream(new File("assets/attendance/GeneratedAttendance.xlsx"));
//
//		// Read from database the students with their teams, and create new excel
//		DatabaseConnections.instance()
//		.getTeamMadeConnection()
//		.joinConnection(()->{
//			PreparedStatement stmt = DatabaseConnections.instance()
//					.getTeamMadeConnection()
//					.getConnection()
//					.prepareStatement("SELECT FullName, Email, Department FROM VerifiedMembers WHERE TeamName = ?");
//			
//			int teamNumber = 1;
//			
//			for(String team : teams) {
//								
//				Sheet sheet = workbook.createSheet("# " + teamNumber + team);
//				
//				teamNumber++;
//				
//				// Create header of excel
//				Row headerRow = sheet.createRow(0);
//				Cell headerStudentName = headerRow.createCell(0);
//				Cell headerEmail = headerRow.createCell(1);
//				Cell headerDepartment = headerRow.createCell(2);
//
//				headerStudentName.setCellValue("Student Name");
//				headerEmail.setCellValue("Email");
//				headerDepartment.setCellValue("Department");
//				
//				stmt.setString(1, team);
//				
//				ResultSet result = stmt.executeQuery();
//				
//				int rowCount = 1;
//				
//				while(result.next()) {
//					Row dataRow = sheet.createRow(rowCount);
//					Cell studentName = dataRow.createCell(0);
//					Cell studentemail = dataRow.createCell(1);
//					Cell department = dataRow.createCell(2);
//					
//					studentName.setCellValue(result.getString(1));
//					studentemail.setCellValue(result.getString(2));
//					department.setCellValue(result.getString(3));
//					
//					rowCount++;
//				}
//				
//				result.close();
//			}
//			
//			stmt.close();
//		});
//		
//		workbook.write(generatedExcel);
//		workbook.close();
//	}
//	
//	private void pushTeamnamesForstudents(Map<String, List<MemberRecord>> teams) {
//		
//		DatabaseConnections.instance()
//		.getTeamMadeConnection()
//		.joinConnection(()->{
//			
//			PreparedStatement stmt = DatabaseConnections.instance()
//					.getTeamMadeConnection()
//					.getConnection()
//					.prepareStatement("UPDATE VerifiedMembers SET TeamName = ? WHERE Email = ?");
//			
//			for(Map.Entry<String, List<MemberRecord>> entry : teams.entrySet()) {
//				
//				for(MemberRecord record : entry.getValue()) {
//					stmt.setString(1, record.getTeam().getName());
//					stmt.setString(2, record.getEmail());
//					stmt.addBatch();
//				}
//			}
//			
//			stmt.executeBatch();
//			stmt.close();
//		});
//	}
//	
//	private Map<String, String> getGroups() {
//		
//		Map<String, String> groups = new HashMap<>();
//		
//		DatabaseConnections.instance()
//		.getTeamMadeConnection()
//		.joinConnection(()->{
//			
//			PreparedStatement stmt = DatabaseConnections.instance()
//					.getTeamMadeConnection()
//					.getConnection()
//					.prepareStatement("SELECT TeamName, DepartmentGroup FROM Teams");
//			
//			ResultSet result = stmt.executeQuery();
//			
//			while(result.next())
//				groups.put(result.getString(1), result.getString(2));
//			
//			result.close();
//			stmt.close();
//		});
//		
//		return groups;
//	}
//	
//	private List<MemberRecord> getMembers() {
//		
//		List<MemberRecord> members = new ArrayList<>();
//		
//		DatabaseConnections.instance()
//		.getTeamMadeConnection()
//		.joinConnection(()->{
//			
//			PreparedStatement stmt = DatabaseConnections.instance()
//					.getTeamMadeConnection()
//					.getConnection()
//					.prepareStatement("SELECT FullName, Department, Email FROM VerifiedMembers");
//			
//			ResultSet result = stmt.executeQuery();
//			
//			while(result.next()) {
//				MemberRecord record = new MemberRecord();
//				
//				record.setFullName(result.getString(1));
//				record.setDepartment(new DepartmentRole(result.getString(2), Color.black));
//				record.setEmail(result.getString(3));
//				
//				members.add(record);
//			}
//			
//			result.close();
//			stmt.close();
//		});
//		
//		return members;
//	}
//	
//	private Map<String, List<MemberRecord>> createTeamGroups(Map<String, String> groups, List<MemberRecord> members) {
//		
//		List<String> teams = new ArrayList<>(groups.keySet());
//		List<String> depts = new ArrayList<>(groups.values());
//		
//		// Create a team map
//		Map<String, List<MemberRecord>> memberTeams = new HashMap<>();
//		
//		// Iterate over all teams to prepare lists
//		for(String team : teams)
//			memberTeams.put(team, new ArrayList<>());
//		
//		Queue<MemberRecord> INEL_S = new LinkedList<>();
//		Queue<MemberRecord> ICOM_S = new LinkedList<>();
//		Queue<MemberRecord> INSO_S = new LinkedList<>();
//		Queue<MemberRecord> CIIC_S = new LinkedList<>();
//		
//		for(MemberRecord record : members) {
//			switch(record.getDepartment().getName()) {
//			case "INEL" : INEL_S.add(record); break;
//			case "ICOM" : ICOM_S.add(record); break;
//			case "INSO" : INSO_S.add(record); break;
//			case "CIIC" : CIIC_S.add(record); break;
//			}
//		}
//		
//		int counter = 0;
//		
//		while(!INEL_S.isEmpty()) {
//			String team = teams.get(counter % teams.size());
//			String dept = depts.get(counter % depts.size());
//			
//			if(dept.equals("INEL/ICOM")) {
//				MemberRecord record = INEL_S.poll();
//				record.setTeam(new TeamRole(team, Color.black));
//				memberTeams.get(team).add(record);
//			}
//			counter++;
//		}
//		
//		while (!ICOM_S.isEmpty()) {
//			String team = teams.get(counter % teams.size());
//			String dept = depts.get(counter % depts.size());
//
//			if (dept.equals("INEL/ICOM")) {
//				MemberRecord record = ICOM_S.poll();
//				record.setTeam(new TeamRole(team, Color.black));
//				memberTeams.get(team).add(record);
//			}
//			counter++;
//		}
//		
//		counter = 0;
//
//		while(!INSO_S.isEmpty()) {
//			String team = teams.get(counter % teams.size());
//			String dept = depts.get(counter % depts.size());
//			
//			if(dept.equals("INSO/CIIC")) {
//				MemberRecord record = INSO_S.poll();
//				record.setTeam(new TeamRole(team, Color.black));
//				memberTeams.get(team).add(record);
//			}
//			counter++;
//		}
//		
//		while (!CIIC_S.isEmpty()) {
//			String team = teams.get(counter % teams.size());
//			String dept = depts.get(counter % depts.size());
//
//			if (dept.equals("INSO/CIIC")) {
//				MemberRecord record = CIIC_S.poll();
//				record.setTeam(new TeamRole(team, Color.black));
//				memberTeams.get(team).add(record);
//			}
//			counter++;
//		}
//		
//		return memberTeams;
//	}
//}