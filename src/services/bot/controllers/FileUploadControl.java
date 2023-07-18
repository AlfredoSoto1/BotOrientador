/**
 * 
 */
package services.bot.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import botOrientador.commands.fileuploads.CurriculumCmd;
import botOrientador.commands.fileuploads.FreshmanGuideCmd;
import services.bot.adapters.ComponentAdapter;

/**
 * @author Alfredo
 *
 */
public class FileUploadControl {
	
	private CurriculumCmd curriculum;
	private FreshmanGuideCmd freshmanGuide;
	
	private List<ComponentAdapter> componentAdapters;

	public FileUploadControl() {
		this.componentAdapters = new ArrayList<>();
		
		this.curriculum = new CurriculumCmd();
		this.freshmanGuide = new FreshmanGuideCmd();
		
		componentAdapters.add(curriculum);
		componentAdapters.add(freshmanGuide);
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
