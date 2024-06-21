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

import assistant.rest.service.ServiceService;

/**
 * @author Alfredo
 */
@Controller
@RequestMapping("/assistant/service")
public class ServiceController {
	
	private final ServiceService service;
	
	@Autowired
	public ServiceController(ServiceService service) {
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
        return ResponseEntity.of(service.getService(id));
    }
}
