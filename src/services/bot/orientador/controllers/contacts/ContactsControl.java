/**
 * 
 */
package services.bot.orientador.controllers.contacts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import services.bot.adapters.ComponentAdapter;
import services.bot.orientador.commands.contacts.AcademicAdvisoryContact;
import services.bot.orientador.commands.contacts.EconomicAssistanceContact;
import services.bot.orientador.commands.contacts.DCSPContact;
import services.bot.orientador.commands.contacts.DeanOfStudentsContact;
import services.bot.orientador.commands.contacts.DepartmentContact;
import services.bot.orientador.commands.contacts.UniversityGuardContact;

/**
 * @author Alfredo
 *
 */
public class ContactsControl {
	
	private DCSPContact dcsp;
	private DepartmentContact department;
	private DeanOfStudentsContact deanOfStudents;
	private UniversityGuardContact universityGuard;
	private AcademicAdvisoryContact academicAdvisory;
	private EconomicAssistanceContact economicAssistance;
	
	private List<ComponentAdapter> componentAdapters;
	
	/**
	 * 
	 */
	public ContactsControl() {
		this.componentAdapters = new ArrayList<>();
		
		this.dcsp = new DCSPContact();
		this.department = new DepartmentContact();
		this.deanOfStudents = new DeanOfStudentsContact();
		this.universityGuard = new UniversityGuardContact();
		this.academicAdvisory = new AcademicAdvisoryContact();
		this.economicAssistance = new EconomicAssistanceContact();
		
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
	public Collection<ComponentAdapter> getComponents() {
		return componentAdapters;
	}
	
	/**
	 * Dispose all contents of this instance
	 */
	public void dispose() {
		componentAdapters.clear();
	}
}
