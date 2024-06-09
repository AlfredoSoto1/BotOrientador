/**
 * 
 */
package assistant.app;

import java.util.HashSet;
import java.util.Set;

import assistant.cmd.contacts.AcademicAdvisoryCmd;
import assistant.cmd.contacts.DCSPCmd;
import assistant.cmd.contacts.DeanOfStudentsCmd;
import assistant.cmd.contacts.DepartmentCmd;
import assistant.cmd.contacts.EconomicAssistanceCmd;
import assistant.cmd.contacts.UniversityGuardCmd;
import assistant.cmd.files.CurriculumCmd;
import assistant.cmd.files.FreshmanGuideCmd;
import assistant.cmd.games.GamesCmd;
import assistant.cmd.info.CalendarCmd;
import assistant.cmd.info.EOInfoCmd;
import assistant.cmd.info.FAQCmd;
import assistant.cmd.info.FacultyCmd;
import assistant.cmd.info.FindBuildingCmd;
import assistant.cmd.info.HelpCmd;
import assistant.cmd.info.OrgsCmd;
import assistant.cmd.info.ProjectsCmd;
import assistant.cmd.info.RulesCmd;
import assistant.cmd.info.UprmMapCmd;
import assistant.cmd.links.LinksCmd;
import assistant.cmd.links.MadeWebCmd;
import assistant.cmd.moderation.AssistantCmd;
import assistant.cmd.moderation.RegistrationCmd;
import assistant.cmd.moderation.RoleSelectionCmd;
import assistant.cmd.moderation.VerificationCmd;
import services.bot.core.BotApplication;
import services.bot.core.ListenerAdapterManager;
import services.bot.interactions.InteractionModel;

/**
 * 
 */
public class ECEAssistant extends BotApplication {

	private Set<InteractionModel> interactions;
	
	/**
	 * 
	 * @param token
	 */
	public ECEAssistant(String token) {
		super(token);
		this.interactions = new HashSet<>();
	}

	@Override
	protected void prepareListeners(ListenerAdapterManager listener) {
		
		prepareInfoCommands();
		prepareLinksCommands();
		prepareContactsCommands();
		prepareFilesCommands();
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
		/*
		 * TODO:
		 * - Verification command and behavior
		 * - Role selection command and behavior
		 * - Role implanting when joining server
		 * - DAO and model implementation for command
		 * - Command with DAOs implementation
		 */

		/*
		 * Require manual update to work with database
		 */
		FacultyCmd faculty = new FacultyCmd();
		ProjectsCmd projects = new ProjectsCmd();
		OrgsCmd organizations = new OrgsCmd();
		EOInfoCmd eoInfo = new EOInfoCmd();
		FindBuildingCmd findBuilding = new FindBuildingCmd();
		
		/*
		 * Require update the hard coded data
		 */
		FAQCmd faq = new FAQCmd();
		HelpCmd helpMenu = new HelpCmd();
		UprmMapCmd uprmMap = new UprmMapCmd();
		RulesCmd serverRules = new RulesCmd();
		CalendarCmd calendar = new CalendarCmd();
		
		
		// TODO: Update global variable to commands that actually needs them
		faq.setGlobal(false);
//		faculty.setGlobal(false);
//		uprmMap.setGlobal(false);
		helpMenu.setGlobal(false);
//		calendar.setGlobal(false);
//		findBuilding.setGlobal(false);
		
		interactions.add(faq);
//		interactions.add(faculty);
//		interactions.add(uprmMap);
		interactions.add(helpMenu);
//		interactions.add(projects);
//		interactions.add(calendar);
//		interactions.add(eoInfo);
		interactions.add(serverRules);
//		interactions.add(findBuilding);
//		interactions.add(organizations);
	}
	
	private void prepareLinksCommands() {
		/*
		 * Require manual update to work with database
		 */
		LinksCmd usefulLinks = new LinksCmd();
		MadeWebCmd madeWeb = new MadeWebCmd();
		
		madeWeb.setGlobal(false);
		usefulLinks.setGlobal(false);

		interactions.add(madeWeb);
		interactions.add(usefulLinks);
	}
	
	private void prepareContactsCommands() {
		/*
		 * Require manual update to work with database
		 */
		DCSPCmd dcsp = new DCSPCmd();
		DepartmentCmd department = new DepartmentCmd();
		DeanOfStudentsCmd deanOfStudents = new DeanOfStudentsCmd();
		UniversityGuardCmd universityGuard = new UniversityGuardCmd();
		AcademicAdvisoryCmd academicAdvisory = new AcademicAdvisoryCmd();
		EconomicAssistanceCmd economicAssistance = new EconomicAssistanceCmd();
		
		
		dcsp.setGlobal(false);
		department.setGlobal(false);
		deanOfStudents.setGlobal(false);
		universityGuard.setGlobal(false);
		academicAdvisory.setGlobal(false);
		economicAssistance.setGlobal(false);
		
		interactions.add(dcsp);
		interactions.add(department);
		interactions.add(academicAdvisory);
		interactions.add(economicAssistance);
		interactions.add(universityGuard);
		interactions.add(deanOfStudents);
	}
	
	private void prepareFilesCommands() {
		/*
		 * Require manual update to work with database
		 */
		CurriculumCmd curriculum = new CurriculumCmd();
		FreshmanGuideCmd freshmanGuide = new FreshmanGuideCmd();
		
//		curriculum.setGlobal(true);
//		freshmanGuide.setGlobal(true);
//		
//		interactions.add(curriculum);
//		interactions.add(freshmanGuide);
	}
	
	private void prepareModeratorCommands() {
		/*
		 * Create the commands, preferably load them
		 * directly from a json file and upsert them to the jda.
		 */
//		interactions.add(new LoginCmd());
		interactions.add(new AssistantCmd(this));
		interactions.add(new VerificationCmd());
		interactions.add(new RegistrationCmd());
		interactions.add(new RoleSelectionCmd());
		interactions.add(new GamesCmd());
		
		// Do a registration server command
	}
}
