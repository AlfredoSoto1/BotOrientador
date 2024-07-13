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
package assistant.rest.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.database.SubTransactionResult;
import assistant.rest.dao.BuildingDAO;
import assistant.rest.dto.BuildingDTO;
import assistant.rest.dto.LabDTO;

/**
 * @author Alfredo
 */
@Service
public class BuildingService {
	
	private final BuildingDAO buildingDAO;
	
	@Autowired
	public BuildingService(BuildingDAO buildingDAO) {
		this.buildingDAO = buildingDAO;
	}
	
	/**
	 * @param page
	 * @param size
	 * @return List of buildings
	 */
	public List<BuildingDTO> getAllBuilding(int page, int size) {
		SubTransactionResult result = buildingDAO.queryAllBuildings(page, size);
		
		List<BuildingDTO> buildings = new ArrayList<>();
		for (int i = 0; i < result.rowCount();i++) {
			BuildingDTO building = new BuildingDTO();
			building.setId(result.getValue("buildid", i));
			building.setCode(result.getValue("code", i));
			building.setName(result.getValue("name", i));
			building.setGpin(result.getValue("gpin", i));
			buildings.add(building);
		}
		return buildings;
	}
	
	public List<LabDTO> getLabsFrom(String buildingCode) {
		// Find the building and validate its existance
		Optional<BuildingDTO> building = this.findBuilding(buildingCode);
		
		// If no matching building found, return empty
		if (building.isEmpty())
			return List.of();
		
		// For the building found, look for all
		// the labs in that building. We use the code from
		// the previous result because that will give the code
		// of the building that matches the ones from the database.
		// This is done so that we dont have to validate the building code again.
		SubTransactionResult result = buildingDAO.queryBuildingLab(building.get().getCode());
		
		List<LabDTO> labs = new ArrayList<>();
		for (int i = 0; i < result.rowCount();i++) {
			LabDTO lab = new LabDTO();
			lab.setId(result.getValue("labid",  i));
			lab.setName(result.getValue("name", i));
			lab.setCode(result.getValue("code", i));
			lab.setBuildingName(result.getValue("building_name", i));
			labs.add(lab);
		}
		return labs;
	}
	
	/**
	 * @param code
	 * @return Finds a building with its respective code
	 */
    public Optional<BuildingDTO> findBuilding(String roomCode) {
    	
    	// Make the code be lower case by default
    	roomCode = roomCode.toLowerCase();
    	
    	// Separate letters and numbers
    	String letters = roomCode.replaceAll("^([A-Za-z]+).*", "$1");
    	String numbers = roomCode.substring(letters.length());

        SubTransactionResult result = buildingDAO.queryBuilding(roomCode, letters);
        
        List<BuildingDTO> matchingBuildings = new LinkedList<>();
        for (int i = 0; i < result.rowCount(); i++) {
			BuildingDTO building = new BuildingDTO();
			building.setId(result.getValue("buildid", i));
			building.setCode(result.getValue("code",  i));
			building.setName(result.getValue("name",  i));
			building.setGpin(result.getValue("gpin",  i));
			matchingBuildings.add(building);
		}
        
        // Filter the results to select only the matching building
        for (BuildingDTO building : matchingBuildings) {
        	if (building.getCode().equals(roomCode))
        		return Optional.of(building);
        	if (roomCode.equals(building.getCode() + numbers))
        		return Optional.of(building);
        }
		return Optional.empty();
    }
    
    public int insertBuilding(BuildingDTO building) {
    	return buildingDAO.insertBuilding(building);
    }
    
    public Optional<BuildingDTO> updateBuilding(int id, BuildingDTO newBuilding) {
//    	Optional<BuildingDTO> building = buildingDAO.findBuilding(id);
//    	
//    	if(building.isPresent()) {
//    		buildingDAO.updateBuilding(id, newBuilding);
//    		return building;
//    	} else {
//    		return Optional.empty();
//    	}
    	return Optional.empty();
    }
    
    public Optional<BuildingDTO> deleteBuilding(int id) {
//    	Optional<BuildingDTO> building = buildingDAO.findBuilding(id);
//    	
//    	if(building.isPresent()) {
//    		buildingDAO.deleteBuilding(id);
//    		return building;
//    	} else {
//    		return Optional.empty();
//    	}
    	return Optional.empty();
    }

	public boolean insertLab(LabDTO lab) {
		return !buildingDAO.insertLab(lab).isEmpty();
	}
}
