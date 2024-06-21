/**
 * 
 */
package assistant.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getProfessor(@PathVariable Integer id) {
		return ResponseEntity.of(service.getProfessor(id));
    }
	
    @PostMapping("/{department}")
    public ResponseEntity<?> addProfessor(@PathVariable String department, @RequestBody FacultyDTO professor) {
    	int idResult = service.addProfessor(professor, department);
		
		if(idResult > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(idResult);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
		}
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
