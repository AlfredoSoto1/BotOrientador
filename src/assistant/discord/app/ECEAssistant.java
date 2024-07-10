/**
 * 
 */
package assistant.discord.app;

import java.util.HashSet;
import java.util.Set;

import assistant.app.settings.TokenHolder;
import assistant.command.contacts.AcademicAdvisoryCmd;
import assistant.command.contacts.DCSPCmd;
import assistant.command.contacts.DeanOfStudentsCmd;
import assistant.command.contacts.DepartmentCmd;
import assistant.command.contacts.EconomicAssistanceCmd;
import assistant.command.contacts.UniversityGuardCmd;
import assistant.command.files.CurriculumCmd;
import assistant.command.files.FreshmanGuideCmd;
import assistant.command.games.GamesCmd;
import assistant.command.games.LeaderboardCmd;
import assistant.command.games.ProfanityMessenger;
import assistant.command.information.CalendarCmd;
import assistant.command.information.EOInfoCmd;
import assistant.command.information.FAQCmd;
import assistant.command.information.FacultyCmd;
import assistant.command.information.FindBuildingCmd;
import assistant.command.information.FindLabCmd;
import assistant.command.information.HelpCmd;
import assistant.command.information.OrgsCmd;
import assistant.command.information.ProjectsCmd;
import assistant.command.information.RulesCmd;
import assistant.command.information.UprmMapCmd;
import assistant.command.links.LinksCmd;
import assistant.command.links.MadeWebCmd;
import assistant.command.moderation.AssistantCmd;
import assistant.command.moderation.RoleSelectionCmd;
import assistant.command.moderation.VerificationCmd;
import assistant.command.moderation.WelcomeMessenger;
import assistant.discord.core.ListenerAdapterManager;
import assistant.discord.interaction.InteractionModel;

/**
 * 
 */
public class ECEAssistant extends BotApplication {

	private Set<InteractionModel> interactions;
	
	/**
	 * 
	 * @param token
	 */
	public ECEAssistant(TokenHolder botToken) {
		super(botToken);
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
		FacultyCmd faculty = new FacultyCmd();
		ProjectsCmd projects = new ProjectsCmd();
		OrgsCmd organizations = new OrgsCmd();
		EOInfoCmd eoInfo = new EOInfoCmd();
		FindBuildingCmd findBuilding = new FindBuildingCmd();
		FindLabCmd findLab = new FindLabCmd();
		
		/*
		 * Require to update since its hard coded data
		 */
		FAQCmd faq = new FAQCmd();
		HelpCmd helpMenu = new HelpCmd();
		UprmMapCmd uprmMap = new UprmMapCmd();
		RulesCmd serverRules = new RulesCmd();
		CalendarCmd calendar = new CalendarCmd();

		helpMenu.setGlobal(false);
		uprmMap.setGlobal(false);
		serverRules.setGlobal(false);
		calendar.setGlobal(false);

		projects.setGlobal(true);
		findBuilding.setGlobal(true);
		findLab.setGlobal(false);
		organizations.setGlobal(true);
		
		/*
		 * Add the commands that have hard-coded data
		 */
		interactions.add(faq);
		interactions.add(helpMenu);
		interactions.add(uprmMap);
		interactions.add(serverRules);
		interactions.add(calendar);
		
		/*
		 * Add the commands that require database to
		 * retrieve the data.
		 */
		interactions.add(faculty);
		interactions.add(projects);
		interactions.add(eoInfo);
		interactions.add(findBuilding);
		interactions.add(findLab);
		interactions.add(organizations);
	}
	
	private void prepareLinksCommands() {
		LinksCmd usefulLinks = new LinksCmd();
		MadeWebCmd madeWeb = new MadeWebCmd();
		
		usefulLinks.setGlobal(false);

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
		
		dcsp.setGlobal(false);
		deanOfStudents.setGlobal(false);
		universityGuard.setGlobal(false);
		economicAssistance.setGlobal(false);
		
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
		
		freshmanGuide.setGlobal(true);
		
		interactions.add(curriculum);
		interactions.add(freshmanGuide);
	}
	
	private void prepareModeratorCommands() {
		/*
		 * Create the commands, preferably load them
		 * directly from a json file and upsert them to the jda.
		 */
		interactions.add(new AssistantCmd(this));
		interactions.add(new VerificationCmd());
		interactions.add(new WelcomeMessenger());
		interactions.add(new RoleSelectionCmd());
		interactions.add(new GamesCmd());
		interactions.add(new LeaderboardCmd());
		interactions.add(new ProfanityMessenger());
	}
}
