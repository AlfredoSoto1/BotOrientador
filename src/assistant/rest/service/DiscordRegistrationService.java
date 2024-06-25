/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.discord.object.MemberPosition;
import assistant.rest.dao.DiscordServerDAO;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.DiscordServerDTO;

/**
 * @author Alfredo
 */
@Deprecated
@Service
public class DiscordRegistrationService {

	private final DiscordServerDAO dregistrationDAO;
	
	@Autowired
	public DiscordRegistrationService(DiscordServerDAO dregistrationDAO) {
		this.dregistrationDAO = dregistrationDAO;
	}
	
	/**
	 * @param page
	 * @param size
	 * @return List of registered Discord Servers
	 */
	public List<DiscordServerDTO> getAllRegisteredDiscordServers(int page, int size) {
		return dregistrationDAO.getAllRegisteredDiscordServers(page * size, size);
	}
	
	/**
	 * @param id
	 * @return Registered Discord Server
	 */
	public Optional<DiscordServerDTO> getRegisteredDiscordServer(int id) {
		return dregistrationDAO.getRegisteredDiscordServer(id);
	}
	
	/**
	 * @return List of the effective name roles
	 */
	public List<String> getEffectiveRoleNames() {
		return dregistrationDAO.getEffectiveRoleNames();
	}
	
	public List<DiscordRoleDTO> getAllRoles(int page, int size, long server) {
		return dregistrationDAO.getAllRoles(page * size, size, server);
	}
	
	public Optional<DiscordRoleDTO> getEffectiveRole(MemberPosition rolePosition, long server) {
		return dregistrationDAO.getEffectivePositionRole(rolePosition, server);
	}

	public int registerDiscordServer(DiscordServerDTO discordServer) {
		return dregistrationDAO.insertDiscordServer(discordServer);
	}
	
	public int registerRole(DiscordRoleDTO role) {
		return dregistrationDAO.insertRole(role);
	}
}
