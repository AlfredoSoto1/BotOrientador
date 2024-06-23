/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.rest.dao.DiscordRegistrationDAO;
import assistant.rest.dto.RegisteredDiscordServerDTO;
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
	
	public List<RegisteredDiscordServerDTO> getAllRegisteredDiscordServers(int page, int size) {
		return dregistrationDAO.getAllRegisteredDiscordServers(page * size, size);
	}
	
	public Optional<RegisteredDiscordServerDTO> getRegistration(int id) {
		return dregistrationDAO.getDiscordServerRegistration(id);
	}
	
	public List<String> getEffectiveRoles() {
		return dregistrationDAO.getEffectiveRoles();
	}
	
	public List<DiscordRoleDTO> getAllRoles(int page, int size) {
		return dregistrationDAO.getAllRoles(page * size, size);
	}
	
	public Optional<DiscordRoleDTO> getRole(int id) {
		return dregistrationDAO.getRole(id);
	}
	
	public int registerDiscordServer(RegisteredDiscordServerDTO discordServer) {
		return dregistrationDAO.insertDiscordServer(discordServer);
	}
	
	public int registerRole(DiscordRoleDTO role) {
		return dregistrationDAO.insertRole(role);
	}
}
