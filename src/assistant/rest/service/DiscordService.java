/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.database.SubTransactionResult;
import assistant.discord.object.MemberPosition;
import assistant.rest.dao.DiscordServerDAO;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.DiscordServerDTO;

/**
 * @author Alfredo
 */
@Service
public class DiscordService {

	private final DiscordServerDAO dregistrationDAO;
	
	@Autowired
	public DiscordService(DiscordServerDAO dregistrationDAO) {
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
	
	/**
	 * @param page
	 * @param size
	 * @param server
	 * @return List of discord roles from a server
	 */
	public List<DiscordRoleDTO> getAllRoles(int page, int size, long server) {
		return dregistrationDAO.getAllRoles(page * size, size, server);
	}
	
	/**
	 * @param rolePosition
	 * @param server
	 * @return Discord role from an effective name position in a server
	 */
	public Optional<DiscordRoleDTO> getEffectiveRole(MemberPosition rolePosition, long server) {
		// Get the effective role position role from
		// access object's transaction result
		SubTransactionResult result = dregistrationDAO.getEffectivePositionRole(rolePosition, server);
		
		if(result.isEmpty())
			return Optional.empty();
		
		DiscordRoleDTO role = new DiscordRoleDTO();
		role.setId(result.getValue("droleid", 0));
		role.setName(result.getValue("name", 0));
		role.setEffectivename(result.getValue("effectivename", 0));
		role.setRoleid(result.getValue("longroleid", 0));
		role.setServerid(result.getValue("discserid", 0));
		
		return Optional.of(role);
	}
	
	/**
	 * Registers Discord server
	 * @param discordServer
	 * @return id of the server registered
	 */
	public int registerDiscordServer(DiscordServerDTO discordServer) {
		return dregistrationDAO.insertDiscordServer(discordServer);
	}
	
	/**
	 * Registers Discord role
	 * @param role
	 * @return id of the role registered
	 */
	public int registerRole(DiscordRoleDTO role) {
		return dregistrationDAO.insertRole(role);
	}
	
}
