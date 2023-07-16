package services.bot.orientador.controllers.links;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import services.bot.adapters.ComponentAdapter;
import services.bot.orientador.commands.links.MadeWeb;
import services.bot.orientador.commands.links.UsefulLinks;

/**
 * 
 * @author Alfredo
 *
 */
public class LinksControl {
	
	private MadeWeb madeWeb;
	private UsefulLinks usefulLinks;
	
	private List<ComponentAdapter> componentAdapters;

	public LinksControl() {
		this.componentAdapters = new ArrayList<>();
		
		this.madeWeb = new MadeWeb();
		this.usefulLinks = new UsefulLinks();
		
		componentAdapters.add(madeWeb);
		componentAdapters.add(usefulLinks);
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
