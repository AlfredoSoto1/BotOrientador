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

import assistant.rest.dto.BuildingDTO;
import assistant.rest.service.BuildingService;

/**
 * @author Alfredo
 */
@RestController
@RequestMapping("/assistant/building")
public class BuildingController {

	private final BuildingService service;
	
	@Autowired
	public BuildingController(BuildingService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<?> getBuildings(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		return ResponseEntity.ok(service.getAll(page, size));
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<?> getBuilding(@PathVariable Integer id) {
        return ResponseEntity.ofNullable(service.getByID(id));
    }
	
    @PostMapping
    public ResponseEntity<?> addBuilding(@RequestBody BuildingDTO building) {
        int recordID = service.insertBuilding(building);
        
        if (recordID > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(recordID);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBuilding(@PathVariable Integer id, @RequestBody BuildingDTO building) {
    	return ResponseEntity.ofNullable(service.updateBuilding(id, building));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuilding(@PathVariable Integer id) {
    	return ResponseEntity.ofNullable(service.deleteBuilding(id));
    }
}
