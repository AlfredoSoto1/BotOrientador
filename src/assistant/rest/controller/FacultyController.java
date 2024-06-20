/**
 * 
 */
package assistant.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import assistant.rest.dto.FacultyDTO;
import assistant.rest.service.FacultyService;

/**
 * @author Alfredo
 */
@RestController
@RequestMapping("/assistant/faculty")
public class FacultyController {
	
	private final FacultyService service;
	
	@Autowired
	public FacultyController(FacultyService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<?> getFaculty(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		return ResponseEntity.ok(service.getAll(page, size));
	}

	@GetMapping("/{id}")
    public ResponseEntity<?> getProfessor(@RequestParam(required = false, defaultValue = "") List<String> contacts, @PathVariable Integer id) {
		if(contacts.size() > 5)
			return ResponseEntity.badRequest().body("too many contact arguments");
		
		for(String contact : contacts) {
			switch(contact.toLowerCase()) {
			case "projects":
			case "organizations":
			case "extensions":
			case "webpages":
			case "socialmedias":
				
			}
		}
		
		
		return ResponseEntity.of(service.getProfessor(id));
    }
	
    @PostMapping
    public ResponseEntity<?> addProfessor(@RequestBody FacultyDTO professor) {
    	return ResponseEntity.ok(null);
    }
    
    @PutMapping("/{id}")
    public String updateProfessor(@PathVariable Integer id, @RequestBody String professor) {
    	return "";
    }
    
    @DeleteMapping("/{id}")
    public String deleteProfessor(@PathVariable Integer id) {
    	return "";
    }
}
