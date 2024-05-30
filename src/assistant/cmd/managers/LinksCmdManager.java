package assistant.cmd.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import assistant.cmd.links.LinksCmd;
import assistant.cmd.links.MadeWebCmd;
import services.bot.interactions.InteractableEvent;

/**
 * 
 * @author Alfredo
 *
 */
@Deprecated
public class LinksCmdManager {
	
	private MadeWebCmd madeWeb;
	private LinksCmd usefulLinks;
	
	private List<InteractableEvent> componentAdapters;

	public LinksCmdManager() {
		this.componentAdapters = new ArrayList<>();
		
		this.madeWeb = new MadeWebCmd();
		this.usefulLinks = new LinksCmd();
		
		this.madeWeb.setGlobal(true);
		this.usefulLinks.setGlobal(true);

		componentAdapters.add(madeWeb);
		componentAdapters.add(usefulLinks);
	}
	
	/**
	 * @return Collection of component adapters
	 */
	public Collection<InteractableEvent> getComponents() {
		return componentAdapters;
	}
}
