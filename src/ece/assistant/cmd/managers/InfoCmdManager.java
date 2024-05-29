package ece.assistant.cmd.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ece.assistant.cmd.info.FAQCmd;
import ece.assistant.cmd.info.FacultyCmd;
import services.bot.managers.BotEventHandler;

/**
 * 
 * @author Alfredo
 *
 */
public class InfoCmdManager {
	
	private List<BotEventHandler> componentAdapters;

	public InfoCmdManager() {
		this.componentAdapters = new ArrayList<>();
		
		FAQCmd faq = new FAQCmd();
		FacultyCmd faculty = new FacultyCmd();
//		this.uprmMap = new UprmMapCmd();
//		this.projects = new ProjectsCmd();
//		this.helpMenu = new HelpCmd();
//		this.calendar = new CalendarCmd();
//		this.eoInfo = new EOInfoCmd();
//		this.serverRules = new RulesCmd();
//		this.findBuilding = new FindBuildingCmd();
//		this.organizations = new OrgsCmd();
		
		faq.setGlobal(false);
		faculty.setGlobal(false);
//		uprmMap.setGlobal(true);
//		helpMenu.setGlobal(true);
//		calendar.setGlobal(true);
//		findBuilding.setGlobal(true);
		
		componentAdapters.add(faq);
		componentAdapters.add(faculty);
//		componentAdapters.add(uprmMap);
//		componentAdapters.add(projects);
//		componentAdapters.add(helpMenu);
//		componentAdapters.add(calendar);
//		componentAdapters.add(eoInfo);
//		componentAdapters.add(serverRules);
//		componentAdapters.add(findBuilding);
//		componentAdapters.add(organizations);
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
		for(BotEventHandler handler : componentAdapters)
			handler.dispose();
		componentAdapters.clear();
	}
}
