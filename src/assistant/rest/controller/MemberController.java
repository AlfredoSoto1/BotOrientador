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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import assistant.rest.dto.MemberDTO;
import assistant.rest.service.MemberService;

/**
 * @author Alfredo
 */
@RestController
@RequestMapping("/assistant/member")
public class MemberController {
	
	private final MemberService service;
	
	@Autowired
	public MemberController(MemberService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<?> getMembers(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		return ResponseEntity.ok(service.getAll(page, size));
	}

	@GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable Integer id) {
        return ResponseEntity.of(service.getMember(id));
    }
	
	@GetMapping("/{id}-team")
	public ResponseEntity<?> getMemberTeam(@PathVariable Integer id) {
		return ResponseEntity.of(service.getMemberTeam(id));
	}
	
	@GetMapping("/{id}-role")
	public ResponseEntity<?> getMemberRole(@PathVariable Integer id) {
		return ResponseEntity.of(service.getMemberRole(id));
	}
	
	@GetMapping("/{id}-program")
    public ResponseEntity<?> getMemberProgram(@PathVariable Integer id) {
        return ResponseEntity.of(service.getMemberProgram(id));
    }
	
	@PostMapping("/{program}-{team}/eo")
	public ResponseEntity<?> addEOrientador(@PathVariable String program, @PathVariable String team, @RequestBody MemberDTO member) {
		int idResult = service.addEOrientador(member, program, team);
		
		if(idResult > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(idResult);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
		}
	}

	@PostMapping("/{program}-{team}/eo/group")
    public ResponseEntity<?> addEOrientadores(@PathVariable String program, @PathVariable String team, @RequestBody List<MemberDTO> member) {
		List<Integer> results = service.addEOrientadores(member, program, team);
		
		if(!results.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(results);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
		}
    }
	
	@PostMapping("/{program}-{team}/prepa")
	public ResponseEntity<?> addPrepa(@PathVariable String program, @PathVariable String team, @RequestBody MemberDTO member) {
		int idResult = service.addPrepa(member, program, team);
		
		if(idResult > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(idResult);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
		}
	}

	@PostMapping("/{program}-{team}/prepa/group")
    public ResponseEntity<?> addPrepas(@PathVariable String program, @PathVariable String team, @RequestBody List<MemberDTO> member) {
		List<Integer> results = service.addPrepas(member, program, team);
		
		if(!results.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(results);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
		}
    }
    
    @DeleteMapping
    public ResponseEntity<?> deleteMembers(@RequestBody List<Integer> memberVerificationIDs) {
    	int result = service.deleteMembers(memberVerificationIDs);
		
		if(result > 0) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete");
		}
    }
}
