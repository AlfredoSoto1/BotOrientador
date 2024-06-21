/**
 * 
 */
package assistant.rest.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import assistant.rest.dto.DiscordRegistrationDTO;
import assistant.rest.dto.DiscordRoleDTO;

/**
 * @author Alfredo
 */
@Repository
public class DiscordRegistrationDAO {

	public DiscordRegistrationDAO() {

	}
	
	public List<DiscordRegistrationDTO> getAllRegistrations(int offset, int limit) {
		return null;
	}
	
	public Optional<DiscordRegistrationDTO> getRegistration(int id) {
		return Optional.empty();
	}
	
	public List<DiscordRoleDTO> getAllRoles(int offset, int limit) {
		return null;
	}
	
	public Optional<DiscordRoleDTO> getRole(int id) {
		return Optional.empty();
	}
	
	public int insertDiscordServer(DiscordRegistrationDTO discordServer) {
		return 0;
	}
	
	public int insertRole(DiscordRoleDTO role) {
		return 0;
	}
}
