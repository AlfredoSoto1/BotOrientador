/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.rest.dao.ServicesDAO;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.ServiceDTO;

/**
 * @author Alfredo
 */
@Service
public class ServicesService {
	
	private final ServicesDAO servicesDAO;
	
	@Autowired
	public ServicesService(ServicesDAO serviceDAO) {
		this.servicesDAO = serviceDAO;
	}
	
	public List<EmailDTO> getServiceEmails() {
		return servicesDAO.getServiceEmails();
	}
	
	public List<ServiceDTO> getAllServices(int page, int size) {
		return servicesDAO.getAllServices(page * size, size);
	}
	
	public Optional<ServiceDTO> getService(EmailDTO email) {
		return servicesDAO.getService(email);
	}
}
