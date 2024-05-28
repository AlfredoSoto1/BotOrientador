/**
 * 
 */
package ece.assistant.cmd.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ece.assistant.cmd.contacts.AcademicAdvisoryCmd;
import ece.assistant.cmd.contacts.DCSPCmd;
import ece.assistant.cmd.contacts.DeanOfStudentsCmd;
import ece.assistant.cmd.contacts.DepartmentCmd;
import ece.assistant.cmd.contacts.EconomicAssistanceCmd;
import ece.assistant.cmd.contacts.UniversityGuardCmd;
import services.bot.managers.BotEventHandler;

/**
 * @author Alfredo
 *
 */
public class ContactsCmdManager {
	
	private DCSPCmd dcsp;
	private DepartmentCmd department;
	private DeanOfStudentsCmd deanOfStudents;
	private UniversityGuardCmd universityGuard;
	private AcademicAdvisoryCmd academicAdvisory;
	private EconomicAssistanceCmd economicAssistance;
	
	private List<BotEventHandler> componentAdapters;
	
	/**
	 * 
	 */
	public ContactsCmdManager() {
		this.componentAdapters = new ArrayList<>();
		
		this.dcsp = new DCSPCmd();
		this.department = new DepartmentCmd();
		this.deanOfStudents = new DeanOfStudentsCmd();
		this.universityGuard = new UniversityGuardCmd();
		this.academicAdvisory = new AcademicAdvisoryCmd();
		this.economicAssistance = new EconomicAssistanceCmd();
		
		this.dcsp.setGlobal(true);
		this.department.setGlobal(true);
		this.deanOfStudents.setGlobal(true);
		this.universityGuard.setGlobal(true);
		this.academicAdvisory.setGlobal(true);
		this.economicAssistance.setGlobal(true);
		
		componentAdapters.add(dcsp);
		componentAdapters.add(department);
		componentAdapters.add(academicAdvisory);
		componentAdapters.add(economicAssistance);
		componentAdapters.add(universityGuard);
		componentAdapters.add(deanOfStudents);
	}
	
	/**
	 * @return Collection of component adapters
	 */
	public Collection<BotEventHandler> getComponents() {
		return componentAdapters;
	}
	
	/**
	 * Dispose all contents of this instance
	 */
	public void dispose() {
		componentAdapters.clear();
	}
}
