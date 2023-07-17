package services.bot.orientador.controllers.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import services.bot.adapters.ComponentAdapter;
import services.bot.dbaccess.DBBuildingManager;
import services.bot.orientador.commands.info.Calendar;
import services.bot.orientador.commands.info.EOInformation;
import services.bot.orientador.commands.info.FAQ;
import services.bot.orientador.commands.info.Faculty;
import services.bot.orientador.commands.info.FindBuilding;
import services.bot.orientador.commands.info.HelpMenu;
import services.bot.orientador.commands.info.Organizations;
import services.bot.orientador.commands.info.Projects;
import services.bot.orientador.commands.info.ServerRules;
import services.bot.orientador.commands.info.UprmMap;

/**
 * 
 * @author Alfredo
 *
 */
public class InfoControl {
	
	private FAQ faq;
	private Faculty faculty;
	private UprmMap uprmMap;
	private Projects projects;
	private HelpMenu helpMenu;
	private Calendar calendar;
	private EOInformation eoInfo;
	private ServerRules serverRules;
	private FindBuilding findBuilding;
	private Organizations organizations;
	
	private DBBuildingManager dbBuildingManager;
	
	private List<ComponentAdapter> componentAdapters;

	public InfoControl() {
		this.componentAdapters = new ArrayList<>();
		
		this.dbBuildingManager = new DBBuildingManager();
		
		this.faq = new FAQ();
		this.faculty = new Faculty();
		this.uprmMap = new UprmMap();
		this.projects = new Projects();
		this.helpMenu = new HelpMenu();
		this.calendar = new Calendar();
		this.eoInfo = new EOInformation();
		this.serverRules = new ServerRules();
		this.findBuilding = new FindBuilding(dbBuildingManager);
		this.organizations = new Organizations();
		
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
		dbBuildingManager.dispose();
		componentAdapters.clear();
	}
}
