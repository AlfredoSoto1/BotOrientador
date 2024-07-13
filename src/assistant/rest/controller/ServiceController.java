/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package assistant.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assistant.app.settings.TokenHolder;
import assistant.rest.service.ServicesService;

/**
 * @author Alfredo
 */
@Controller
@RequestMapping("/assistant/service")
public class ServiceController {
	
	private final ServicesService service;
	private final List<TokenHolder> tokenHolders;
	
	@Autowired
	public ServiceController(List<TokenHolder> tokenHolders, ServicesService service) {
		this.service = service;
		this.tokenHolders = tokenHolders;
	}
	
	@GetMapping
	public ResponseEntity<?> getAllServices(
			@RequestHeader("Authorization")   String token,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestParam(defaultValue = "None") String name) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		if("None".equalsIgnoreCase(name))
			return ResponseEntity.ok(service.getAllServices(page, size));
		else
			return ResponseEntity.of(service.getService(name));
	}
	
	@GetMapping("/name")
	public ResponseEntity<?> getServiceNames(@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		 return ResponseEntity.ok(service.getServiceNames());
	}
}
