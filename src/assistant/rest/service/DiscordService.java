/**
 * 
 */
package assistant.rest.service;

import java.io.File;
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
	
	public File findFromAssets(String path) {
		return new File(path);
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
	public Optional<DiscordServerDTO> getRegisteredDiscordServer(long server) {
		
		SubTransactionResult result = dregistrationDAO.getRegisteredDiscordServer(server);
		
		if(result.isEmpty())
			return Optional.empty();
		
		// Map all the results from DAO to DTO
		DiscordServerDTO discordServer = new DiscordServerDTO();
		discordServer.setId(result.getValue("seoid", 0));
		discordServer.setServerId(result.getValue("discserid", 0));
		discordServer.setLogChannelId(result.getValue("log_channel", 0));
		discordServer.setJoinedAt(result.getValue("joined_at", 0).toString());
		discordServer.setDepartment(result.getValue("abreviation", 0));
		discordServer.setColor(result.getValue("color", 0));
		
		return Optional.of(discordServer);
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
