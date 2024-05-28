package ece.assistant.cmd.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ece.assistant.cmd.info.CalendarCmd;
import ece.assistant.cmd.info.EOInfoCmd;
import ece.assistant.cmd.info.FAQCmd;
import ece.assistant.cmd.info.FacultyCmd;
import ece.assistant.cmd.info.FindBuildingCmd;
import ece.assistant.cmd.info.HelpCmd;
import ece.assistant.cmd.info.OrgsCmd;
import ece.assistant.cmd.info.ProjectsCmd;
import ece.assistant.cmd.info.RulesCmd;
import ece.assistant.cmd.info.UprmMapCmd;
import services.bot.managers.BotEventHandler;

/**
 * 
 * @author Alfredo
 *
 */
public class InfoCmdManager {
	
	private FAQCmd faq;
	private FacultyCmd faculty;
	private UprmMapCmd uprmMap;
	private ProjectsCmd projects;
	private HelpCmd helpMenu;
	private CalendarCmd calendar;
	private EOInfoCmd eoInfo;
	private RulesCmd serverRules;
	private FindBuildingCmd findBuilding;
	private OrgsCmd organizations;
	
	private List<BotEventHandler> componentAdapters;

	public InfoCmdManager() {
		this.componentAdapters = new ArrayList<>();
		
		this.faq = new FAQCmd();
//		this.faculty = new FacultyCmd();
//		this.uprmMap = new UprmMapCmd();
//		this.projects = new ProjectsCmd();
//		this.helpMenu = new HelpCmd();
//		this.calendar = new CalendarCmd();
//		this.eoInfo = new EOInfoCmd();
//		this.serverRules = new RulesCmd();
//		this.findBuilding = new FindBuildingCmd();
//		this.organizations = new OrgsCmd();
		
		this.faq.setGlobal(true);
		this.faculty.setGlobal(true);
		this.uprmMap.setGlobal(true);
		this.helpMenu.setGlobal(true);
		this.calendar.setGlobal(true);
		this.findBuilding.setGlobal(true);
		
		componentAdapters.add(faq);
		componentAdapters.add(faculty);
		componentAdapters.add(uprmMap);
		componentAdapters.add(projects);
		componentAdapters.add(helpMenu);
		componentAdapters.add(calendar);
		componentAdapters.add(eoInfo);
		componentAdapters.add(serverRules);
		componentAdapters.add(findBuilding);
		componentAdapters.add(organizations);
	}
	
	/**
	 * @return Collection of component adapters
	 */
	public Collection<BotEventHandler> getComponents() {
		return componentAdapters;
	}
	
	/**
	 * 
	 */
	public void dispose() {
		componentAdapters.clear();
	}
}
