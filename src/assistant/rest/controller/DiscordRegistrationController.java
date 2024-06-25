/**
 * 
 */
package assistant.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assistant.app.settings.TokenHolder;
import assistant.discord.object.MemberPosition;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.service.DiscordRegistrationService;

/**
 * @author Alfredo
 */
@Controller
@RequestMapping("/assistant/registration")
public class DiscordRegistrationController {
	
	private final List<TokenHolder> tokenHolders;
	private final DiscordRegistrationService service;
	
	@Autowired
	public DiscordRegistrationController(List<TokenHolder> tokenHolders, DiscordRegistrationService service) {
		this.service = service;
		this.tokenHolders = tokenHolders;
	}
	
	/**
	 * @param token
	 * @param id
	 * @param page
	 * @param size
	 * @return Either a single or a collection of all registered servers
	 */
	@GetMapping("/server")
	public ResponseEntity<?> getAllDiscordServers(
			@RequestHeader("Authorization")    String token,
			@RequestParam(defaultValue = "-1") Integer id,
			@RequestParam(defaultValue = "0")  Integer page,
			@RequestParam(defaultValue = "5")  Integer size) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		if(id == -1)
			return ResponseEntity.ok(service.getAllRegisteredDiscordServers(page, size));
		else
			return ResponseEntity.of(service.getRegisteredDiscordServer(id));
	}
	
	/**
	 * @param token
	 * @param page
	 * @param size
	 * @param server
	 * @param position
	 * @return Effective role or all roles in a server
	 */
	@GetMapping("/role")
	public ResponseEntity<?> getAllDiscordRoles(
			@RequestHeader("Authorization")   String token,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestParam(required = true)    Long server,
			@RequestParam(required = false, defaultValue = "NONE") MemberPosition position) {

		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		// Obtain the Discord Role from effective role position
		if(position != MemberPosition.NONE)
			return ResponseEntity.of(service.getEffectiveRole(position, server));
		
		// By default return all roles that are in a Discord Server
		return ResponseEntity.ok(service.getAllRoles(page, size, server));
	}
	
	/**
	 * @param token
	 * @return List of role names
	 */
	@GetMapping("/role-names")
	public ResponseEntity<?> getAllDiscordRoles(@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		// Obtain the effective role names allowed to
		// use for accessing Discord role data
		return ResponseEntity.ok(service.getEffectiveRoleNames());
	}
	
	/**
	 * @param token
	 * @param discordServer
	 * @return Id of the registered Discord Server
	 */
	@PostMapping("/server")
	public ResponseEntity<?> registerDiscordServer(
			@RequestHeader("Authorization") String token,
			@RequestBody DiscordServerDTO discordServer) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		int recordID = service.registerDiscordServer(discordServer);
		
		if (recordID > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(recordID);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
		}
	}
	
	/**
	 * @param token
	 * @param role
	 * @return Id of the registered Discord Role
	 */
    @PostMapping("/role")
    public ResponseEntity<?> registerDiscordRole(
    		@RequestHeader("Authorization") String token,
    		@RequestBody DiscordRoleDTO role) {
    	
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
        int recordID = service.registerRole(role);
        
        if (recordID > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(recordID);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
        }
    }
}
