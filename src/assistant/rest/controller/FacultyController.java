/**
 * 
 */
package assistant.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import assistant.app.settings.TokenHolder;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.FacultyDTO;
import assistant.rest.service.FacultyService;

/**
 * @author Alfredo
 */
@RestController
@RequestMapping("/assistant/faculty")
public class FacultyController {
	
	private final FacultyService service;
	private final List<TokenHolder> tokenHolders;
	
	@Autowired
	public FacultyController(List<TokenHolder> tokenHolders, FacultyService service) {
		this.service = service;
		this.tokenHolders = tokenHolders;
	}
	
	@GetMapping
	public ResponseEntity<?> getFaculty(
			@RequestHeader("Authorization") String token,
			@RequestParam(required = true)  String department,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		return ResponseEntity.ok(service.getFaculty(page, size, department));
	}

	@GetMapping("/professor")
    public ResponseEntity<?> getProfessor(
    		@RequestBody(required = true)   EmailDTO email,
    		@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		return ResponseEntity.ok(service.getProfessor(email));
    }
	
	@GetMapping("/professor/email")
    public ResponseEntity<?> getEmails(
    		@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		return ResponseEntity.ok(service.getFacultyEmails());
    }
	
    @PostMapping("/professor")
    public ResponseEntity<?> addProfessor(
    		@RequestBody  FacultyDTO professor,
    		@RequestHeader("Authorization") String token) {
    	
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
    	int idResult = service.addProfessor(professor);
		if(idResult > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(idResult);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
		}
    }
}
