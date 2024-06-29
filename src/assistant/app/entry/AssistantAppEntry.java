/**
 * 
 */
package assistant.app.entry;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import assistant.app.core.Application;
import assistant.discord.object.MemberProgram;
import assistant.rest.service.TeamGroupCreatorService;

/**
 * @author Alfredo
 * 
 * TODO: 
 * 
 * - finish the other commands that require data from db
 * 
 * - Complete the role selection display
 * - make the streamlit application for data insertion into the db
 * 
 */
@SpringBootApplication
@ComponentScan("assistant")
public class AssistantAppEntry extends Application {
	
	public static void main(String[] args) {
		// Create a new assistant application to start running
		Application.run(new AssistantAppEntry(), args);
    }

	@Override
	public void onBotStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRestStart() {
//		// Obtain the service
//		TeamGroupCreatorService tmCreatorService = super.getSpringContext().getBean(TeamGroupCreatorService.class);
//		
//		// Load the students to database and generate lists
//		tmCreatorService.distributeAndExportStudentsToTeams(
//			MemberProgram.INSO,
//			MemberProgram.CIIC,
//			1251583768863445142L,
//			"assets/attendance/Copy of Admitidos INSO-CIIC 05 15 2024.xlsx"
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
