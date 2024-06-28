/**
 * 
 */
package assistant.app.entry;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import assistant.app.core.Application;
import assistant.rest.service.MemberService;
import assistant.rest.service.TeamService;

/**
 * @author Alfredo
 * 
 * TODO: 
 * 
 * - Create WebApplication that can gather the necessary 
 *   data to insert into the database.
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
		MemberService memberService = super.getSpringContext().getBean(MemberService.class);
		TeamService teamService = super.getSpringContext().getBean(TeamService.class);
		
		var students = memberService.loadStudentsFrom("assets/attendance/Copy of Admitidos INSO-CIIC 05 15 2024.xlsx");
		
		var teamsTable = teamService.getPrepaListFrom(students, 6, 30, 6);
		
		for(var team : teamsTable.entrySet())
			teamService.exportStudentsTo(team.getValue(), "assets/attendance/CSE-Equipos/Equipo-" + team.getKey() + ".xlsx");
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
