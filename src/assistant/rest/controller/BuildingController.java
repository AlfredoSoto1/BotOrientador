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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import assistant.app.settings.TokenHolder;
import assistant.rest.dto.BuildingDTO;
import assistant.rest.dto.LabDTO;
import assistant.rest.service.BuildingService;

/**
 * @author Alfredo
 */
@RestController
@RequestMapping("/assistant/building")
public class BuildingController {

	private final BuildingService service;
	private final List<TokenHolder> tokenHolders;
	
	@Autowired
	public BuildingController(List<TokenHolder> tokenHolders, BuildingService service) {
		this.service = service;
		this.tokenHolders = tokenHolders;
	}
	
	@GetMapping
	public ResponseEntity<?> getBuildings(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestHeader("Authorization")   String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		return ResponseEntity.ok(service.getAllBuilding(page, size));
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<?> getBuilding(
    		@PathVariable String code,
    		@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
        return ResponseEntity.of(service.findBuilding(code));
    }
	
    @PostMapping
    public ResponseEntity<?> addBuilding(
    		@RequestBody BuildingDTO building,
    		@RequestHeader("Authorization") String token) {
    	
    	if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
    	
        int recordID = service.insertBuilding(building);
        if (recordID > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(recordID);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
        }
    }
    
    @PostMapping("/lab")
    public ResponseEntity<?> addLab(
    		@RequestBody LabDTO lab,
    		@RequestHeader("Authorization") String token) {
    	
    	if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
    	
        if (service.insertLab(lab)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Lab Inserted");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBuilding(
    		@PathVariable Integer id, 
    		@RequestBody BuildingDTO building,
    		@RequestHeader("Authorization") String token) {
    	
    	if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
    	
    	return ResponseEntity.of(service.updateBuilding(id, building));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuilding(
    		@PathVariable Integer id,
    		@RequestHeader("Authorization") String token) {
    	
    	if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
    	
    	return ResponseEntity.of(service.deleteBuilding(id));
    }
}
