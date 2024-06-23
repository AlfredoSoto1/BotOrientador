/**
 * 
 */
package assistant.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.RegisteredDiscordServerDTO;
import assistant.rest.service.DiscordRegistrationService;

/**
 * @author Alfredo
 */
@Controller
@RequestMapping("/assistant/registration")
public class DiscordRegistrationController {
	
	private final DiscordRegistrationService service;
	
	@Autowired
	public DiscordRegistrationController(DiscordRegistrationService service) {
		this.service = service;
	}
	
	@GetMapping("/server")
	public ResponseEntity<?> getAllDiscordServers(
			@RequestParam(defaultValue = "-1") Integer id,
			@RequestParam(defaultValue = "0")  Integer page,
			@RequestParam(defaultValue = "5")  Integer size) {
		
		if(id == -1)
			return ResponseEntity.ok(service.getAllRegisteredDiscordServers(page, size));
		else
			return ResponseEntity.of(service.getRegistration(id));
	}
	
	@GetMapping("/role")
	public ResponseEntity<?> getAllDiscordRoles(
			@RequestParam(required = false, defaultValue = "false") Boolean effectiverole,
			@RequestParam(defaultValue = "-1") Integer id,
			@RequestParam(defaultValue = "0")  Integer page,
			@RequestParam(defaultValue = "5")  Integer size) {
		
		if(id != -1)
			return ResponseEntity.of(service.getRole(id));
		
		if(effectiverole) {
			return ResponseEntity.ok(service.getEffectiveRoles());
		} else {
			return ResponseEntity.ok(service.getAllRoles(page, size));
		}
	}
	
	@PostMapping("/server")
	public ResponseEntity<?> registerDiscordServer(@RequestBody RegisteredDiscordServerDTO discordServer) {
		int recordID = service.registerDiscordServer(discordServer);
		
		if (recordID > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(recordID);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
		}
	}
	
    @PostMapping("/role")
    public ResponseEntity<?> registerDiscordRole(@RequestBody DiscordRoleDTO role) {
        int recordID = service.registerRole(role);
        
        if (recordID > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(recordID);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
        }
    }
}
