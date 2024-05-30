/**
 * 
 */
package assistant.app;

import java.util.ArrayList;
import java.util.List;

import assistant.cmd.contacts.AcademicAdvisoryCmd;
import assistant.cmd.contacts.DCSPCmd;
import assistant.cmd.contacts.DeanOfStudentsCmd;
import assistant.cmd.contacts.DepartmentCmd;
import assistant.cmd.contacts.EconomicAssistanceCmd;
import assistant.cmd.contacts.UniversityGuardCmd;
import assistant.cmd.files.CurriculumCmd;
import assistant.cmd.files.FreshmanGuideCmd;
import assistant.cmd.info.FAQCmd;
import assistant.cmd.links.LinksCmd;
import assistant.cmd.links.MadeWebCmd;
import assistant.cmd.moderation.BotServiceCmd;
import services.bot.core.BotApplication;
import services.bot.core.ListenerAdapterManager;
import services.bot.interactions.InteractableEvent;

/**
 * 
 */
public class ECEAssistant extends BotApplication {

	private List<InteractableEvent> interactions;
	
	/**
	 * 
	 * @param token
	 */
	public ECEAssistant(String token) {
		super(token);
		this.interactions = new ArrayList<>();
	}

	@Override
	protected void prepareListeners(ListenerAdapterManager listener) {
		
		prepareInfoCommands();
		prepareModeratorCommands();
		
		// Load all the interactions to JDA once it starts
		listener.upsertInteractions(interactions);
	}

	@Override
	protected void dispose() {
		// DO NOT dispose() the interactions individually here
		// since its already done automatically inside the listener adapter manager
		interactions.clear();
	}
	
	private void prepareInfoCommands() {
		FAQCmd faq = new FAQCmd();
//		FacultyCmd faculty = new FacultyCmd();
//		UprmMapCmd uprmMap = new UprmMapCmd();
//		HelpCmd helpMenu = new HelpCmd();
//		ProjectsCmd projects = new ProjectsCmd();
//		CalendarCmd calendar = new CalendarCmd();
//		RulesCmd serverRules = new RulesCmd();
//		OrgsCmd organizations = new OrgsCmd();
		
//		EOInfoCmd eoInfo = new EOInfoCmd();
//		FindBuildingCmd findBuilding = new FindBuildingCmd();
		
		// TODO: Update global variable to commands that actually needs them
		faq.setGlobal(false);
//		faculty.setGlobal(false);
//		uprmMap.setGlobal(false);
//		helpMenu.setGlobal(false);
//		calendar.setGlobal(false);
//		findBuilding.setGlobal(false);
		
		interactions.add(faq);
//		interactions.add(faculty);
//		interactions.add(uprmMap);
//		interactions.add(helpMenu);
//		interactions.add(projects);
//		interactions.add(calendar);
//		interactions.add(eoInfo);
//		interactions.add(serverRules);
//		interactions.add(findBuilding);
//		interactions.add(organizations);
	}
	
	private void prepareLinksCommands() {
		LinksCmd usefulLinks = new LinksCmd();
		MadeWebCmd madeWeb = new MadeWebCmd();
		
		madeWeb.setGlobal(true);
		usefulLinks.setGlobal(true);

		interactions.add(madeWeb);
		interactions.add(usefulLinks);
	}
	
	private void prepareContactsCommands() {
		DCSPCmd dcsp = new DCSPCmd();
		DepartmentCmd department = new DepartmentCmd();
		DeanOfStudentsCmd deanOfStudents = new DeanOfStudentsCmd();
		UniversityGuardCmd universityGuard = new UniversityGuardCmd();
		AcademicAdvisoryCmd academicAdvisory = new AcademicAdvisoryCmd();
		EconomicAssistanceCmd economicAssistance = new EconomicAssistanceCmd();
		
		dcsp.setGlobal(true);
		department.setGlobal(true);
		deanOfStudents.setGlobal(true);
		universityGuard.setGlobal(true);
		academicAdvisory.setGlobal(true);
		economicAssistance.setGlobal(true);
		
		interactions.add(dcsp);
		interactions.add(department);
		interactions.add(academicAdvisory);
		interactions.add(economicAssistance);
		interactions.add(universityGuard);
		interactions.add(deanOfStudents);
	}
	
	private void prepareFilesCommands() {
		CurriculumCmd curriculum = new CurriculumCmd();
		FreshmanGuideCmd freshmanGuide = new FreshmanGuideCmd();
		
		curriculum.setGlobal(true);
		freshmanGuide.setGlobal(true);
		
		interactions.add(curriculum);
		interactions.add(freshmanGuide);
	}
	
	private void prepareModeratorCommands() {
		/*
		 * Create the commands, preferably load them
		 * directly from a json file and upsert them to the jda.
		 */
//		interactions.add(new LoginCmd());
		interactions.add(new BotServiceCmd(this));
		
		// Do a registration server command
	}
}
