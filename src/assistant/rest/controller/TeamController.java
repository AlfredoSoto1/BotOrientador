/**
 * 
 */
package assistant.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assistant.rest.dto.TeamDTO;
import assistant.rest.service.TeamService;

/**
 * @author Alfredo
 */
@Controller
@RequestMapping("/assistant/team")
public class TeamController {

	private final TeamService service;
	
	@Autowired
	public TeamController(TeamService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<?> getAllTeams(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		return ResponseEntity.ok(service.getAll(page, size));
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<?> getTeam(@PathVariable Integer id) {
        return ResponseEntity.of(service.getTeam(id));
    }
	
    @PostMapping
    public ResponseEntity<?> addTeam(@RequestBody TeamDTO team) {
        int recordID = service.addTeam(team);
        
        if (recordID > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(recordID);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Integer id) {
    	return ResponseEntity.of(service.deleteTeam(id));
    }
}
