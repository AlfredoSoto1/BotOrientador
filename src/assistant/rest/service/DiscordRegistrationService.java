/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.rest.dao.DiscordRegistrationDAO;
import assistant.rest.dto.DiscordRegistrationDTO;
import assistant.rest.dto.DiscordRoleDTO;

/**
 * @author Alfredo
 */
@Service
public class DiscordRegistrationService {

	private final DiscordRegistrationDAO dregistrationDAO;
	
	@Autowired
	public DiscordRegistrationService(DiscordRegistrationDAO dregistrationDAO) {
		this.dregistrationDAO = dregistrationDAO;
	}
	
	
	public List<DiscordRegistrationDTO> getAllRegistrations(int page, int size) {
		return dregistrationDAO.getAllRegistrations(page * size, size);
	}
	
	public Optional<DiscordRegistrationDTO> getRegistration(int id) {
		return dregistrationDAO.getRegistration(id);
	}
	
	public List<DiscordRoleDTO> getAllRoles(int page, int size) {
		return dregistrationDAO.getAllRoles(page * size, size);
	}
	
	public Optional<DiscordRoleDTO> getRole(int id) {
		return dregistrationDAO.getRole(id);
	}
	
	public int registerDiscordServer(DiscordRegistrationDTO discordServer) {
		return 0;
	}
	
	public int registerRole(DiscordRoleDTO role) {
		return 0;
	}
}
