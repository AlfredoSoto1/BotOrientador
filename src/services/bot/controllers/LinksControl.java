package services.bot.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import botOrientador.commands.links.LinksCmd;
import botOrientador.commands.links.MadeWebCmd;
import services.bot.managers.BotEventHandler;

/**
 * 
 * @author Alfredo
 *
 */
public class LinksControl {
	
	private MadeWebCmd madeWeb;
	private LinksCmd usefulLinks;
	
	private List<BotEventHandler> componentAdapters;

	public LinksControl() {
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
