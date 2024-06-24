/**
 * 
 */
package assistant.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assistant.app.settings.TokenHolder;
import assistant.database.DatabaseEntity;
import assistant.rest.dto.TeamDTO;
import assistant.rest.service.DeveloperService;

/**
 * @author Alfredo
 */
@Controller
@RequestMapping("/assistant/developer")
public class DeveloperController {

	private final List<TokenHolder> tokenHolders;
	private final DeveloperService service;
	
	@Autowired
	public DeveloperController(List<TokenHolder> tokenHolders, DeveloperService service) {
		this.service = service;
		this.tokenHolders = tokenHolders;
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getEntityContent(
			@RequestHeader("Authorization")   String token,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestParam(defaultValue = "BUILDING") DatabaseEntity entity) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		return ResponseEntity.noContent().build();
	}
	
    @PostMapping
    public ResponseEntity<?> addTeam(@RequestBody TeamDTO team) {
    	return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping
    public ResponseEntity<?> deleteTeam(@RequestParam(defaultValue = "-1") Integer id) {
    	return ResponseEntity.noContent().build();
    }
}
