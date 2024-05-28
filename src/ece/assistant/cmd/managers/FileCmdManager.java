/**
 * 
 */
package ece.assistant.cmd.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ece.assistant.cmd.files.CurriculumCmd;
import ece.assistant.cmd.files.FreshmanGuideCmd;
import services.bot.managers.BotEventHandler;

/**
 * @author Alfredo
 *
 */
public class FileCmdManager {
	
	private CurriculumCmd curriculum;
	private FreshmanGuideCmd freshmanGuide;
	
	private List<BotEventHandler> componentAdapters;

	public FileCmdManager() {
		this.componentAdapters = new ArrayList<>();
		
		this.curriculum = new CurriculumCmd();
		this.freshmanGuide = new FreshmanGuideCmd();
		
		this.curriculum.setGlobal(true);
		this.freshmanGuide.setGlobal(true);
		
		componentAdapters.add(curriculum);
		componentAdapters.add(freshmanGuide);
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
