/**
 * 
 */
package assistant.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assistant.rest.service.AdministrationService;

/**
 * @author Alfredo
 */
@Controller
@RequestMapping("/assistant/administration")
public class AdministrationController {

	private final AdministrationService service;
	
	@Autowired
	public AdministrationController(AdministrationService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<?> getAdministration(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		return ResponseEntity.ok(service.getAll(page, size));
	}

	@GetMapping("/{id}")
    public ResponseEntity<?> getAdministrationStaff(@PathVariable Integer id) {
        return ResponseEntity.of(service.getStaff(id));
    }
}
