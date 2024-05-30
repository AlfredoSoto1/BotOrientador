/**
 * 
 */
package assistant.cmd.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import assistant.cmd.files.CurriculumCmd;
import assistant.cmd.files.FreshmanGuideCmd;
import services.bot.interactions.InteractableEvent;

/**
 * @author Alfredo
 *
 */
@Deprecated
public class FileCmdManager {
	
	private List<InteractableEvent> componentAdapters;

	public FileCmdManager() {
		this.componentAdapters = new ArrayList<>();
		
		CurriculumCmd curriculum = new CurriculumCmd();
		FreshmanGuideCmd freshmanGuide = new FreshmanGuideCmd();
		
		curriculum.setGlobal(true);
		freshmanGuide.setGlobal(true);
		
		componentAdapters.add(curriculum);
		componentAdapters.add(freshmanGuide);
	}
	
	/**
	 * @return Collection of component adapters
	 */
	public Collection<InteractableEvent> getComponents() {
		return componentAdapters;
	}
}
