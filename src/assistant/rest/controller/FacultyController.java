/**
 * 
 */
package assistant.rest.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alfredo
 */
@RestController
@RequestMapping("/assistant/faculty")
public class FacultyController {
	
	@GetMapping
	public String getFaculty() {
		return "getAllFaculty";
	}

	@GetMapping("/{id}")
    public String getProfessor(@PathVariable Integer id) {
        return "professor";
    }
	
    @PostMapping
    public String addProfessor(@RequestBody String professor) {
    	return "";
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
