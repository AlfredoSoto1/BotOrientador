/**
 * 
 */
package assistant.cmd.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import assistant.cmd.contacts.AcademicAdvisoryCmd;
import assistant.cmd.contacts.DCSPCmd;
import assistant.cmd.contacts.DeanOfStudentsCmd;
import assistant.cmd.contacts.DepartmentCmd;
import assistant.cmd.contacts.EconomicAssistanceCmd;
import assistant.cmd.contacts.UniversityGuardCmd;
import services.bot.interactions.InteractableEvent;

/**
 * @author Alfredo
 *
 */
@Deprecated
public class ContactsCmdManager {
	
	private List<InteractableEvent> componentAdapters;
	
	/**
	 * 
	 */
	public ContactsCmdManager() {
		this.componentAdapters = new ArrayList<>();
		
		DCSPCmd dcsp = new DCSPCmd();
		DepartmentCmd department = new DepartmentCmd();
		DeanOfStudentsCmd deanOfStudents = new DeanOfStudentsCmd();
		UniversityGuardCmd universityGuard = new UniversityGuardCmd();
		AcademicAdvisoryCmd academicAdvisory = new AcademicAdvisoryCmd();
		EconomicAssistanceCmd economicAssistance = new EconomicAssistanceCmd();
		
		dcsp.setGlobal(true);
		department.setGlobal(true);
		deanOfStudents.setGlobal(true);
		universityGuard.setGlobal(true);
		academicAdvisory.setGlobal(true);
		economicAssistance.setGlobal(true);
		
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
	public Collection<InteractableEvent> getComponents() {
		return componentAdapters;
	}
}
