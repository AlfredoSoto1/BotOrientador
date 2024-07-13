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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assistant.app.settings.TokenHolder;
import assistant.rest.dto.TeamDTO;
import assistant.rest.service.TeamService;

/**
 * @author Alfredo
 */
@Controller
@RequestMapping("/assistant/team")
public class TeamController {

	private final List<TokenHolder> tokenHolders;
	private final TeamService service;
	
	@Autowired
	public TeamController(List<TokenHolder> tokenHolders, TeamService service) {
		this.service = service;
		this.tokenHolders = tokenHolders;
	}
	
	@GetMapping
	public ResponseEntity<?> getTeam(
			@RequestHeader("Authorization") String token,
			@RequestParam(required = true)  Long server,
			@RequestParam(defaultValue = "None") String teamname,
			@RequestParam(defaultValue = "0")  Integer page,
			@RequestParam(defaultValue = "5")  Integer size) {

		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		if("None".equalsIgnoreCase(teamname))
			return ResponseEntity.ok(service.getAllTeams(page, size, server));
		else
			return ResponseEntity.of(service.getTeam(teamname, server));
	}
	
	@PostMapping
	public ResponseEntity<?> addTeam(
			@RequestBody TeamDTO team,
			@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		int result = service.addTeam(team);
		if(result > 0)
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert");
	}

    @DeleteMapping
    public ResponseEntity<?> deleteTeam(
			@RequestParam(required = true)  Long server,
			@RequestParam(required = true)  String teamname,
    		@RequestHeader("Authorization") String token) {
		
		if (TokenHolder.authenticateREST(token, tokenHolders))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect token");
		
		return ResponseEntity.of(service.deleteTeam(teamname, server));
    }
}
