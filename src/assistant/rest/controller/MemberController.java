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
import assistant.app.settings.TokenType;
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
	
	/**
	 * Requires REST-Token to access data
	 * 
	 * @param page
	 * @param size
	 * @param server
	 * @param ret
	 * @param email
	 * @param token
	 * @return Response entity that contains a list of Members 
	 */
	@GetMapping
	public ResponseEntity<?> getMembers(
			@RequestParam(defaultValue = "0")        Integer page,
			@RequestParam(defaultValue = "5")        Integer size,
			@RequestParam(defaultValue = "-1")       Long server,
			@RequestParam(defaultValue = "EVERYONE") String ret,
			@RequestBody(required = false)           EmailDTO email,
			@RequestHeader("Authorization")          String token) {
		
		TokenHolder restToken = tokenHolders.stream()
				.filter(tkholder -> tkholder.getType() == TokenType.REST_TOKEN)
				.findFirst().get();
		
		// Validate token
		if(token == null || !restToken.is(token))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		// Find the member with the given email
		if(email != null)
			return ResponseEntity.of(service.getMember(email.getEmail()));
		
		// If none of the above, return all members corresponding
		// to the retrievement type entered in the url
		MemberRetrievement mr = MemberRetrievement.EVERYONE;
		try {
			mr = MemberRetrievement.valueOf(ret);
			return ResponseEntity.ok(service.getAllMembers(page, size, mr, server));
		} catch (IllegalArgumentException ile) {
			return ResponseEntity.badRequest().body("Invalid return argument");
		}
	}
	
	/**
	 * Requires REST-Token to access data
	 * 
	 * @param page
	 * @param size
	 * @param server
	 * @param token
	 * @return Response entity that contains a list of Emails
	 */
	@GetMapping("/email")
	public ResponseEntity<?> getEmails(
			@RequestParam(defaultValue = "0")        Integer page,
			@RequestParam(defaultValue = "5")        Integer size,
			@RequestParam(defaultValue = "-1")       Long server,
			@RequestHeader("Authorization")          String token) {
		
		TokenHolder restToken = tokenHolders.stream()
				.filter(tkholder -> tkholder.getType() == TokenType.REST_TOKEN)
				.findFirst().get();
		
		// Validate token
		if(token == null || !restToken.is(token))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		// If no given email, then just return the emails of the members
		return ResponseEntity.ok(service.getEmails(page, size, server));
	}
	
	/**
	 * 
	 * @param email
	 * @param token
	 * @return Response entity that displays the team to which the member participates
	 */
	@GetMapping("/team")
	public ResponseEntity<?> getMemberTeam(
			@RequestBody(required = true)   EmailDTO email,
			@RequestHeader("Authorization") String token) {
		
		TokenHolder restToken = tokenHolders.stream()
				.filter(tkholder -> tkholder.getType() == TokenType.REST_TOKEN)
				.findFirst().get();
		
		// Validate token
		if(token == null || !restToken.is(token))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		return ResponseEntity.of(service.getMemberTeam(email.getEmail()));
	}
	
	/**
	 * Requires REST-Token to access data
	 * 
	 * @param member
	 * @param server
	 * @param teamname
	 * @param position
	 * @param token
	 * @return Response entity with the ID of the member added
	 */
	@PostMapping
	public ResponseEntity<?> addMember(
			@RequestBody(required = true)   MemberDTO member,
			@RequestParam(required = true)  Long server,
			@RequestParam(required = true)  String teamname,
			@RequestParam(required = true)  String position,
			@RequestHeader("Authorization") String token) {
		
		TokenHolder restToken = tokenHolders.stream()
				.filter(tkholder -> tkholder.getType() == TokenType.REST_TOKEN)
				.findFirst().get();
		
		// Validate token
		if(token == null || !restToken.is(token))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		MemberPosition mp = MemberPosition.ESTUDIANTE_ORIENTADOR;
		try {
			mp = MemberPosition.valueOf(position);
		} catch (IllegalArgumentException ile) {
			return ResponseEntity.badRequest().body("Invalid return argument");
		}
		
		int idResult = service.addMember(member, mp, server, teamname);
		
		if(idResult > 0)
			return ResponseEntity.status(HttpStatus.CREATED).body(idResult);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
	}	
	
	/**
	 * 
	 * @param members
	 * @param server
	 * @param teamname
	 * @param position
	 * @param token
	 * @return Response entity with the number of members added
	 */
	@PostMapping("/group")
	public ResponseEntity<?> addMemberCollection(
			@RequestBody(required = true)   List<MemberDTO> members,
			@RequestParam(required = true)  Long server,
			@RequestParam(required = true)  String teamname,
			@RequestParam(required = true)  String position,
			@RequestHeader("Authorization") String token) {
		
		TokenHolder restToken = tokenHolders.stream()
				.filter(tkholder -> tkholder.getType() == TokenType.REST_TOKEN)
				.findFirst().get();
		
		// Validate token
		if(token == null || !restToken.is(token))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		MemberPosition mp = MemberPosition.ESTUDIANTE_ORIENTADOR;
		try {
			mp = MemberPosition.valueOf(position);
		} catch (IllegalArgumentException ile) {
			return ResponseEntity.badRequest().body("Invalid return argument");
		}
		
		int idResult = service.addMembers(members, mp, server, teamname).size();
		
		if(idResult > 0)
			return ResponseEntity.status(HttpStatus.CREATED).body(idResult);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
	}	
	
    /**
     * 
     * @param memberVerificationIDs
     * @return Response entity with the count of members deleted
     */
    @DeleteMapping
    public ResponseEntity<?> deleteMembers(
    		@RequestBody List<Integer> memberVerificationIDs, 
    		@RequestHeader("Authorization") String token) {
    	
		TokenHolder restToken = tokenHolders.stream()
				.filter(tkholder -> tkholder.getType() == TokenType.REST_TOKEN)
				.findFirst().get();
		
		// Validate token
		if(token == null || !restToken.is(token))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
    	
    	int result = service.deleteMembers(memberVerificationIDs);
		
		if(result > 0) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete");
		}
    }
}
