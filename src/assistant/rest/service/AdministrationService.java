/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.rest.dao.AdministrationDAO;
import assistant.rest.dto.AdministrationStaffDTO;

/**
 * @author Alfredo
 */
@Service
public class AdministrationService {

	private final AdministrationDAO administrationDAO;
	
	@Autowired
	public AdministrationService(AdministrationDAO administrationDAO) {
		this.administrationDAO = administrationDAO;
	}
	
	public List<AdministrationStaffDTO> getAll(int page, int size) {
		return administrationDAO.getAll(page * size, size);
	}
	
	public Optional<AdministrationStaffDTO> getStaff(int id) {
		return administrationDAO.getAdministrationStaff(id);
	}
}
