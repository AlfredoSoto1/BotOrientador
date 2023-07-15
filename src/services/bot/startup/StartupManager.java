/**
 * 
 */
package services.bot.startup;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import services.bot.adapters.ProgrammableAdapter;

/**
 * @author Alfredo
 *
 */
public class StartupManager extends ListenerAdapter implements ProgrammableAdapter {
	
	private List<StartupI> startupPrograms;
	
	public StartupManager() {
		
	}

	/**
	 * Adds a StartupI program for when the discord
	 * bot startup for the first time
	 * 
	 * @param command
	 */
	public void add(StartupI program) {
		startupPrograms.add(program);
	}
	
	@Override
	public void init() {
		this.startupPrograms = new ArrayList<>();
	}
	
	@Override
	public void dispose() {
		for(StartupI startupProgram : startupPrograms)
			startupProgram.dispose();
		
		startupPrograms.clear();
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		for(StartupI startupProgram : startupPrograms)
			startupProgram.onStartup(event);
	}
}
