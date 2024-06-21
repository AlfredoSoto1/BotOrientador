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

import assistant.rest.dto.TeamDTO;
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
	
	@GetMapping
	public ResponseEntity<?> getAllDiscordServers(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		return ResponseEntity.ok(service.getAllRegistrations(page, size));
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<?> getRegisteredDiscordServer(@PathVariable Integer id) {
        return ResponseEntity.ofNullable(service.getRegistration(id));
    }
	
//    @PostMapping
//    public ResponseEntity<?> addBuilding(@RequestBody TeamDTO team) {
//        int recordID = service.addTeam(team);
//        
//        if (recordID > 0) {
//            return ResponseEntity.status(HttpStatus.CREATED).body(recordID);
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
//        }
//    }
}
