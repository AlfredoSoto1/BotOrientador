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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assistant.app.settings.TokenHolder;
import assistant.rest.dto.EmailDTO;
import assistant.rest.service.ServicesService;

/**
 * @author Alfredo
 */
@Controller
@RequestMapping("/assistant/service")
public class ServiceController {
	
	private final ServicesService service;
	private final List<TokenHolder> tokenHolders;
	
	@Autowired
	public ServiceController(List<TokenHolder> tokenHolders, ServicesService service) {
		this.service = service;
		this.tokenHolders = tokenHolders;
	}
	
	@GetMapping
	public ResponseEntity<?> getAllServices(
			@RequestBody EmailDTO email,
			@RequestHeader("Authorization")   String token,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		if(email.getEmail() == null)
			return ResponseEntity.ok(service.getAllServices(page, size));
		else
			return ResponseEntity.of(service.getService(email));
	}
	
	@GetMapping("/email")
	public ResponseEntity<?> getServiceEmails(@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		 return ResponseEntity.ok(service.getServiceEmails());
	}
}
