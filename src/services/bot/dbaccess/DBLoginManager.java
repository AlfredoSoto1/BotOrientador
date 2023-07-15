/**
 * 
 */
package services.bot.dbaccess;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import application.database.DatabaseConnections;
import application.records.MemberRecord;
import services.bot.orientador.roles.DepartmentRole;
import services.bot.orientador.roles.ExtraRoles;
import services.bot.orientador.roles.TeamRole;

/**
 * @author Alfredo
 *
 */
public class DBLoginManager {
	
	private static final String SELECT_PREPA = 
			"SELECT FirstName, FatherLastName, MotherLastName, Department, TeamName, LoggedIn FROM VerifiedMembers WHERE (Email = ?)";

	private static final String SELECT_EO = 
			"SELECT FullName, Department, TeamName, EstudianteGraduado, EstudianteOrientador, BotDeveloper, ConsejeroProfecional, LoggedIn FROM StaffMembers WHERE (Email = ?)";

	private static final String LOGIN_UPDATE_STATUS = 
			"UPDATE TABLE SET LoggedIn = ?, DiscordUser = ?, BriefInfo = ? WHERE (Email = ?)";
	
	private static final String LOGIN_UPDATE_STATUS_PER_USER = 
			"UPDATE TABLE SET LoggedIn = ?, DiscordUser = ?, BriefInfo = ? WHERE (DiscordUser = ?)";

	private static final String LOGIN_UPDATE_FLAG = 
			"UPDATE TABLE SET LoggedIn = ?";
	
	private static final String SELECT_TEAMS = 
			"SELECT TeamName, TeamColor FROM Teams";

	private static final String SELECT_DEPARTMENTS = 
			"SELECT DepartmentName, DepartmentColor FROM Departments";
	
	private Map<String, TeamRole> loadedTeams;
	private Map<String, DepartmentRole> loadedDepartments;
	
	/**
	 * 
	 */
	public DBLoginManager() {
		this.loadedTeams = new HashMap<>();
		this.loadedDepartments = new HashMap<>();
	}
	
	public void dispose() {
		loadedTeams.clear();
		loadedDepartments.clear();
	}
	
	public Optional<MemberRecord> getStudent(String enteredEmail) {
		
		// Create a mutable optional container of a member record
		// this makes the member record mutable, meaning that the lambda
		// expression below  can modify the optional<>. The optional is
		// just used here to avoid null pointer exceptions. Basically
		// is just to handle these in a more elegant way the possibility 
		// of the MemberRecord just not exist.
		AtomicReference<Optional<MemberRecord>> student = new AtomicReference<>(Optional.empty());
		
		// Join a new connection to the TM-Database
		DatabaseConnections.instance()
		.getTeamMadeConnection()
		.joinConnection(
		()-> {
			// Get the student record that matches the entered email
			// if the email is not valid, then it will return empty optional
			student.set(pullStudentRecord(enteredEmail));
			
			// If empty check if its an EO trying to log in
			if(student.get().isEmpty())
				student.set(pullEORecord(enteredEmail));
		});
		return student.get();
	}
	
	public void logInMember(MemberRecord record, String discordUser) {
		// Join a new connection to the TM-Database
		DatabaseConnections.instance()
		.getTeamMadeConnection()
		.joinConnection(()->updateDatabaseLoginStatus(record, discordUser));
	}
	
	public boolean logOffMember(String discordUser) {
		AtomicInteger exitStatus = new AtomicInteger(0);
		// Join a new connection to the TM-Database
		DatabaseConnections.instance()
		.getTeamMadeConnection()
		.joinConnection(
		()->{
			exitStatus.set(updateLogMemberStatus(discordUser, "VerifiedMembers"));
			
			if(exitStatus.get() < 1)
				exitStatus.set(updateLogMemberStatus(discordUser, "StaffMembers"));
		});
		
		return exitStatus.get() > 0;
	}
	
	public void logOffAllMemberRecords() {
		// Join a new connection to the TM-Database
		DatabaseConnections.instance()
		.getTeamMadeConnection()
		.joinConnection(
		()->{
			// Update login status
			PreparedStatement stmt = DatabaseConnections.instance()
					.getTeamMadeConnection()
					.getConnection()
					.prepareStatement(LOGIN_UPDATE_FLAG.replace("TABLE", "StaffMembers"));
			
			// Set the unknown value to SQL statement
			stmt.setBoolean(1, false);
			// Run SQL
			stmt.executeUpdate();
			stmt.close();

			// Update login status
			stmt = DatabaseConnections.instance()
					.getTeamMadeConnection()
					.getConnection()
					.prepareStatement(LOGIN_UPDATE_FLAG.replace("TABLE", "VerifiedMembers"));

			stmt.setBoolean(1, false);
			// Run SQL
			stmt.executeUpdate();
			stmt.close();
		});
	}
	
	public void prepareRolesFromDatabase() {
		
		// Join a new connection to the TM-Database
		DatabaseConnections.instance()
		.getTeamMadeConnection()
		.joinConnection(
		// Define lambda to join the database connection
		// always keeping in mind that this can throw an SQLException
		() -> {
			// Pull from database all role related
			// records for ease data manipulation later
			pullTeamsFromDB();
			pullDepartmentsFromDB();
		});
	}
	
	private int updateLogMemberStatus(String discordUser, String targetTable) throws SQLException {
		String logInMember_SQL = LOGIN_UPDATE_STATUS_PER_USER.replace("TABLE", targetTable);
		
		// Update login status
		PreparedStatement stmt = DatabaseConnections.instance()
				.getTeamMadeConnection()
				.getConnection()
				.prepareStatement(logInMember_SQL);
		
		// Set the loggedIn flag to false
		stmt.setBoolean(1, false);
		stmt.setString(2, "");
		stmt.setString(3, "");
		// Set the email to look up for member in database
		stmt.setString(4, discordUser);
		// Run SQL
		int resultStatus = stmt.executeUpdate();
		
		// Free resources
		stmt.close();
		
		return resultStatus;
	}
	
	private void updateDatabaseLoginStatus(MemberRecord record, String discordUser) throws SQLException {
		
		String logInMember_SQL = LOGIN_UPDATE_STATUS.replace("TABLE", record.getRoles().isPrepa() ? "VerifiedMembers" : "StaffMembers");
		
		PreparedStatement stmt = DatabaseConnections.instance()
				.getTeamMadeConnection()
				.getConnection()
				.prepareStatement(logInMember_SQL);
		
		// update log-in flag
		record.setLoggedIn(true);
		// update discord user
		record.setDiscordUser(discordUser);

		// Set the unknown value to SQL statement
		stmt.setBoolean(1, record.isLogged());
		stmt.setString(2, discordUser);
		stmt.setString(3, record.getBriefInfo());
		stmt.setString(4, record.getEmail());
		
		// Run SQL
		stmt.executeUpdate();
		
		// Free resources
		stmt.close();
	}
	
	private Optional<MemberRecord> pullEORecord(String enteredEmail) throws SQLException {

		// Create a prepared statement to access the database
		PreparedStatement stmt = DatabaseConnections.instance()
				.getTeamMadeConnection()
				.getConnection()
				.prepareStatement(SELECT_EO);
		
		// Set the first value of the SQL statement
		stmt.setString(1, enteredEmail);
		
		// Execute and process results to a result set
		ResultSet result = stmt.executeQuery();
		
		// Declare a member record to store data if 
		// such record exists in database
		MemberRecord student = null;
		
		while(result.next()) {
			student = new MemberRecord();

			student.setEmail(enteredEmail);

			student.setFullName(result.getString(1));
			student.setDepartment(loadedDepartments.get(result.getString(2)));
			student.setTeam(loadedTeams.get(result.getString(3)));

			student.setRoles(
				new ExtraRoles(
					false, // False, since we are working with staff members
					result.getBoolean(4),
					result.getBoolean(5),
					result.getBoolean(6), 
					result.getBoolean(7)
				)
			);
			
			student.setLoggedIn(result.getBoolean(8));
		}
		
		result.close();
		stmt.close();
		
		return student == null ? Optional.empty() : Optional.of(student);
	}
	
	private Optional<MemberRecord> pullStudentRecord(String enteredEmail) throws SQLException {

		// Create a prepared statement to access the database
		PreparedStatement stmt = DatabaseConnections.instance()
				.getTeamMadeConnection()
				.getConnection()
				.prepareStatement(SELECT_PREPA);
		
		// Set the first value of the SQL statement
		stmt.setString(1, enteredEmail);
		
		// Execute and process results to a result set
		ResultSet result = stmt.executeQuery();
		
		StringBuilder fullnameBuilder = new StringBuilder();
		
		// Declare a member record to store data if 
		// such record exists in database
		MemberRecord student = null;
		
		while(result.next()) {
			student = new MemberRecord();
			
			fullnameBuilder.append(result.getString(1) + " ");
			fullnameBuilder.append(result.getString(2) + " ");
			fullnameBuilder.append(result.getString(3));
			
			student.setEmail(enteredEmail);

			student.setFullName(fullnameBuilder.toString());
			student.setDepartment(loadedDepartments.get(result.getString(4)));

			student.setTeam(loadedTeams.get(result.getString(5)));
			
			student.setRoles(
				new ExtraRoles(
					true, // True since we are working with the prepa-record
					false,
					false,
					false,
					false
				)
			);
			
			student.setLoggedIn(result.getBoolean(6));
			
			// Reset string buffer
			fullnameBuilder.setLength(0);
		}
		
		result.close();
		stmt.close();
		
		return student == null ? Optional.empty() : Optional.of(student);
	}
	
	private void pullTeamsFromDB() throws SQLException {
		// Create an SQL statement to select
		// the teams from database
		PreparedStatement stmt = DatabaseConnections.instance()
				.getTeamMadeConnection()
				.getConnection()
				.prepareStatement(SELECT_TEAMS);
		
		// Execute and process results to a result set
		ResultSet result = stmt.executeQuery();
		
		// Pull the teams out to a team role struct
		while(result.next()) {
			String name = result.getString(1);
			String colorHex = result.getString(2);
			
			loadedTeams.put(name, new TeamRole(name, Color.decode(colorHex)));
		}
		
		// free resources
		result.close();
		stmt.close();
	}
	
	private void pullDepartmentsFromDB() throws SQLException {
		// Create an SQL statement to select
		// the departments from database
		PreparedStatement stmt = DatabaseConnections.instance()
				.getTeamMadeConnection()
				.getConnection()
				.prepareStatement(SELECT_DEPARTMENTS);
		
		// Execute and process results to a result set
		ResultSet result = stmt.executeQuery();
		
		// Pull out the departments out to a department role struct
		while(result.next()) {
			String name = result.getString(1);
			String colorHex = result.getString(2);
			
			loadedDepartments.put(name, new DepartmentRole(name, Color.decode(colorHex)));
		}
		
		// free resources
		result.close();
		stmt.close();
	}
}
