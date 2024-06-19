/**
 * 
 */
package assistant.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import assistant.rest.dto.Building;
import assistant.rest.dto.PostStatement;
import assistant.rest.dto.PutStatement;

/**
 * @author Alfredo
 */
@RestController
@RequestMapping("/assistant/building")
public class BuildingController {

	@GetMapping
	public List<Building> getBuilding() {
		return new ArrayList<>();
	}

	@GetMapping("/{id}")
    public Building getBuilding(@PathVariable Integer id) {
        return null;
    }
	
    @PostMapping
    public PostStatement addBuilding(@RequestBody Building building) {
    	return null; // return the added record
    }
    
    @PutMapping("/{id}")
    public PutStatement updateBuilding(@PathVariable Integer id, @RequestBody Building building) {
    	return null; // return the updated record
    }
    
    @DeleteMapping("/{id}")
    public Building deleteBuilding(@PathVariable Integer id) {
    	return null; // Return the record
    }
}
