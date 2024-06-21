/**
 * 
 */
package assistant.rest.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import assistant.rest.dto.AdministrationStaffDTO;

/**
 * @author Alfredo
 */
@Repository
public class AdministrationDAO {

	public AdministrationDAO() {
		
	}
	
	public List<AdministrationStaffDTO> getAll(int offset, int size) {
		
		return null;
	}
	
	public Optional<AdministrationStaffDTO> getAdministrationStaff(int id) {
		
		return null;
	}
}
