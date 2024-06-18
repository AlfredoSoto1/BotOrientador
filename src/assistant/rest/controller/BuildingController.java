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
@RequestMapping("/assistant/building")
public class BuildingController {

	@GetMapping
	public String getBuilding() {
		return "getAllFaculty";
	}

	@GetMapping("/{id}")
    public String getBuilding(@PathVariable Integer id) {
        return "professor";
    }
	
    @PostMapping
    public String addBuilding(@RequestBody String professor) {
    	return ""; // return the added record
    }
    
    @PutMapping("/{id}")
    public String updateBuilding(@PathVariable Integer id, @RequestBody String professor) {
    	return ""; // return the updated record
    }
    
    @DeleteMapping("/{id}")
    public String deleteBuilding(@PathVariable Integer id) {
    	return ""; // Return the record
    }
}
