/**
 * 
 */
package assistant.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assistant.rest.dto.DiscordRegistrationDTO;
import assistant.rest.dto.DiscordRoleDTO;
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
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		return ResponseEntity.ok(service.getAllRegistrations(page, size));
	}
	
	@GetMapping("/role")
	public ResponseEntity<?> getAllDiscordRoles(
			@RequestParam(required = false, defaultValue = "false") Boolean effectiverole,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		
		if(effectiverole) {
			return ResponseEntity.ok(service.getEffectiveRoles());
		} else {
			return ResponseEntity.ok(service.getAllRoles(page, size));
		}
	}
	
	@GetMapping("/server/{id}")
    public ResponseEntity<?> getRegisteredDiscordServer(@PathVariable Integer id) {
        return ResponseEntity.of(service.getRegistration(id));
    }
	
	@GetMapping("/role/{id}")
    public ResponseEntity<?> getRegisteredRole(@PathVariable Integer id) {
        return ResponseEntity.of(service.getRole(id));
    }
	
	@PostMapping("/server")
	public ResponseEntity<?> registerDiscordServer(@RequestBody DiscordRegistrationDTO discordServer) {
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
