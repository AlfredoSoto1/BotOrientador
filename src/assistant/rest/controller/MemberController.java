/**
 * 
 */
package assistant.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import assistant.app.settings.TokenHolder;
import assistant.discord.object.MemberPosition;
import assistant.discord.object.MemberRetrievement;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.MemberDTO;
import assistant.rest.service.MemberService;

/**
 * @author Alfredo
 */
@RestController
@RequestMapping("/assistant/member")
public class MemberController {
	
	private final MemberService service;
	private final List<TokenHolder> tokenHolders;
	
	@Autowired
	public MemberController(List<TokenHolder> tokenHolders, MemberService service) {
		this.service = service;
		this.tokenHolders = tokenHolders;
	}
	
	@GetMapping
	public ResponseEntity<?> getMembers(
			@RequestParam(defaultValue = "0")        Integer page,
			@RequestParam(defaultValue = "5")        Integer size,
			@RequestParam(defaultValue = "EVERYONE") MemberRetrievement ret,
			@RequestParam(defaultValue = "-1")       Long server,
			@RequestBody(required = false)           EmailDTO email,
			@RequestHeader("Authorization")          String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		// Find the member with the given email
		if(email != null)
			return ResponseEntity.of(service.getMember(email.getEmail()));
		
		// If none of the above, return all members corresponding
		// to the retrievement type entered in the url
		return ResponseEntity.ok(service.getAllMembers(page, size, ret, server));
	}
	
	@GetMapping("/email")
	public ResponseEntity<?> getEmails(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestParam(required = true)    Long server,
			@RequestHeader("Authorization")   String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		// If no given email, then just return the emails of the members
		return ResponseEntity.ok(service.getEmails(page, size, server));
	}

	@GetMapping("/team")
	public ResponseEntity<?> getMemberTeam(
			@RequestBody(required = true)   EmailDTO email,
			@RequestParam(required = true)  Long server,
			@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		return ResponseEntity.of(service.getMemberTeam(email.getEmail(), server));
	}
	
	@PostMapping
	public ResponseEntity<?> addMember(
			@RequestBody(required = true)   MemberDTO member,
			@RequestParam(required = true)  Long server,
			@RequestParam(required = true)  String teamname,
			@RequestParam(required = true)  MemberPosition position,
			@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		int result = service.addMember(member, position, server, teamname);
		if(result > 0)
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
	}	
	
	@PostMapping("/group")
	public ResponseEntity<?> addMemberCollection(
			@RequestBody(required = true)   List<MemberDTO> members,
			@RequestParam(required = true)  Long server,
			@RequestParam(required = true)  String teamname,
			@RequestParam(required = true)  MemberPosition position,
			@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		if(service.addMembers(members, position, server, teamname))
			return ResponseEntity.status(HttpStatus.CREATED).body("Added all members");
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
	}	
	
    @DeleteMapping
    public ResponseEntity<?> deleteMembers(
    		@RequestBody List<Integer> memberVerificationIDs, 
    		@RequestHeader("Authorization") String token) {
    	
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
    	
		if(service.deleteMembers(memberVerificationIDs)) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("SUCCESS ON DELETION");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete");
		}
    }
    
	@PostMapping("/verify")
	public ResponseEntity<?> verifyMember(
			@RequestBody(required = true)   MemberDTO member,
			@RequestParam(required = true)  Long server,
			@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		if(service.verifyMember(member, server))
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("VERIFICATION SUCCESS");
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
	}
}
