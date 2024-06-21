/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.rest.dao.ServiceDAO;
import assistant.rest.dto.ServiceDTO;

/**
 * @author Alfredo
 */
@Service
public class ServiceService {
	
	private final ServiceDAO serviceDAO;
	
	@Autowired
	public ServiceService(ServiceDAO serviceDAO) {
		this.serviceDAO = serviceDAO;
	}
	
	public List<ServiceDTO> getAll(int page, int size) {
		return serviceDAO.getAll(page * size, size);
	}
	
	public Optional<ServiceDTO> getService(int id) {
		return serviceDAO.getService(id);
	}
}
