package services.bot.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import botOrientador.commands.info.CalendarCmd;
import botOrientador.commands.info.EOInfoCmd;
import botOrientador.commands.info.FAQCmd;
import botOrientador.commands.info.FacultyCmd;
import botOrientador.commands.info.FindBuildingCmd;
import botOrientador.commands.info.HelpCmd;
import botOrientador.commands.info.OrgsCmd;
import botOrientador.commands.info.ProjectsCmd;
import botOrientador.commands.info.RulesCmd;
import botOrientador.commands.info.UprmMapCmd;
import services.bot.adapters.ComponentAdapter;

/**
 * 
 * @author Alfredo
 *
 */
public class InfoControl {
	
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
	
	private List<ComponentAdapter> componentAdapters;

	public InfoControl() {
		this.componentAdapters = new ArrayList<>();
		
		this.faq = new FAQCmd();
		this.faculty = new FacultyCmd();
		this.uprmMap = new UprmMapCmd();
		this.projects = new ProjectsCmd();
		this.helpMenu = new HelpCmd();
		this.calendar = new CalendarCmd();
		this.eoInfo = new EOInfoCmd();
		this.serverRules = new RulesCmd();
		this.findBuilding = new FindBuildingCmd();
		this.organizations = new OrgsCmd();
		
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
	public Collection<ComponentAdapter> getComponents() {
		return componentAdapters;
	}
	
	/**
	 * 
	 */
	public void dispose() {
		componentAdapters.clear();
	}
}
