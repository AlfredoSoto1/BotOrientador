package services.bot.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import botOrientador.commands.links.MadeWeb;
import botOrientador.commands.links.LinksCmd;
import services.bot.adapters.ComponentAdapter;

/**
 * 
 * @author Alfredo
 *
 */
public class LinksControl {
	
	private MadeWeb madeWeb;
	private LinksCmd usefulLinks;
	
	private List<ComponentAdapter> componentAdapters;

	public LinksControl() {
		this.componentAdapters = new ArrayList<>();
		
		this.madeWeb = new MadeWeb();
		this.usefulLinks = new LinksCmd();
		
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
