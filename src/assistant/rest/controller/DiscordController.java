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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assistant.app.settings.TokenHolder;
import assistant.discord.object.MemberPosition;
import assistant.rest.dto.DiscordRoleDTO;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.service.DiscordService;

/**
 * @author Alfredo
 */
@Controller
@RequestMapping("/assistant/discord")
public class DiscordController {
	
	private final DiscordService service;
	private final List<TokenHolder> tokenHolders;
	
	@Autowired
	public DiscordController(List<TokenHolder> tokenHolders, DiscordService service) {
		this.service = service;
		this.tokenHolders = tokenHolders;
	}
	
	@GetMapping("/server")
	public ResponseEntity<?> getAllDiscordServers(
			@RequestHeader("Authorization")    String token,
			@RequestParam(defaultValue = "-1") Integer id,
			@RequestParam(defaultValue = "0")  Integer page,
			@RequestParam(defaultValue = "5")  Integer size) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		// If the id is not -1, then return all discord servers
		// else, return the server with the id given as parameter
		if(id == -1)
			return ResponseEntity.ok(service.getAllRegisteredDiscordServers(page, size));
		else
			return ResponseEntity.of(service.getRegisteredDiscordServer(id));
	}
	
	@GetMapping("/role")
	public ResponseEntity<?> getAllDiscordRoles(
			@RequestHeader("Authorization")   String token,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestParam(required = true)    Long server,
			@RequestParam(required = false, defaultValue = "NONE") MemberPosition position) {

		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		// Obtain the Discord Role from effective role position
		if(position != MemberPosition.NONE)
			return ResponseEntity.of(service.getEffectiveRole(position, server));
		
		// By default return all roles that are in a Discord Server
		return ResponseEntity.ok(service.getAllRoles(page, size, server));
	}
	
	@GetMapping("/role-names")
	public ResponseEntity<?> getAllDiscordRoles(@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		// Obtain the effective role names allowed to
		// use for accessing Discord role data
		return ResponseEntity.ok(service.getEffectiveRoleNames());
	}
	
	@PostMapping("/server")
	public ResponseEntity<?> registerDiscordServer(
			@RequestHeader("Authorization") String token,
			@RequestBody DiscordServerDTO discordServer) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		int recordID = service.registerDiscordServer(discordServer);
		if (recordID > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(recordID);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
		}
	}
	
    @PostMapping("/role")
    public ResponseEntity<?> registerDiscordRole(
    		@RequestHeader("Authorization") String token,
    		@RequestBody DiscordRoleDTO role) {
    	
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
        int recordID = service.registerRole(role);
        if (recordID > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(recordID);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert");
        }
    }
}
