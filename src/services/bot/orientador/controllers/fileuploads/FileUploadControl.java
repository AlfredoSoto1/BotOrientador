/**
 * 
 */
package services.bot.orientador.controllers.fileuploads;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import services.bot.adapters.ComponentAdapter;
import services.bot.orientador.commands.fileuploads.Curriculum;
import services.bot.orientador.commands.fileuploads.FreshmanGuide;

/**
 * @author Alfredo
 *
 */
public class FileUploadControl {
	
	private Curriculum curriculum;
	private FreshmanGuide freshmanGuide;
	
	private List<ComponentAdapter> componentAdapters;

	public FileUploadControl() {
		this.componentAdapters = new ArrayList<>();
		
		this.curriculum = new Curriculum();
		this.freshmanGuide = new FreshmanGuide();
		
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
