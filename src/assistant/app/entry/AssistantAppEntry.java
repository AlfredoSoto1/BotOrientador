/**
 * 
 */
package assistant.app.entry;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import assistant.app.core.Application;
import assistant.app.core.DebugConfiguration;

/**
 * @author Alfredo
 * 
 * TODO: 
 *  
 *  Tomorrow
 *  - Add food location search and listing command
 *  
 *  - Merge to master and test
 *  - Complete the Jar file and run in PI
 */
@SpringBootApplication
@ComponentScan("assistant")
public class AssistantAppEntry extends Application {
	
	public static void main(String[] args) {
		// Create a new assistant application to start running
		Application.run(new AssistantAppEntry(), args, DebugConfiguration.BOT_ENABLED);
    }

	@Override
	public void onBotStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRestStart() {
		
		// DO NOT UNCOMMENT THIS CODE UNLESS YOU KNOW WHAT YOU ARE DOING
		
//		// Obtain the service
//		TeamGroupCreatorService tmCreatorService = super.getSpringContext().getBean(TeamGroupCreatorService.class);
//		
//		// Load the students to database and generate lists
//		tmCreatorService.distributeAndExportStudentsToTeams(
//			30, // Students per group
//			6,  // Females per group
//			MemberProgram.INSO,
//			MemberProgram.CIIC,
//			1251583768863445142L,
//			"assets/attendance/prepas_2024/CSE_PREPAS_2024.xlsx",
//			"assets/attendance/CSE-equipos/"
//			);
//		// Load the students to database and generate lists
//		tmCreatorService.distributeAndExportStudentsToTeams(
//			27, // Students per group
//			6,  // Females per group
//			MemberProgram.INEL,
//			MemberProgram.ICOM,
//			1243392516351590451L,
//			"assets/attendance/prepas_2024/ECE_PREPAS_2024.xlsx",
//			"assets/attendance/ECE-equipos/"
//		);
	}

	@Override
	public void onDatabaseStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBotShutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRestShutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDatabaseShutdown() {
		// TODO Auto-generated method stub
		
	}
}
