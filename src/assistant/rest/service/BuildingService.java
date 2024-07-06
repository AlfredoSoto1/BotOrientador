/**
 * 
 */
package assistant.rest.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.database.SubTransactionResult;
import assistant.rest.dao.BuildingDAO;
import assistant.rest.dto.BuildingDTO;

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
		return buildingDAO.getAll(page * size, size);
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

        SubTransactionResult result = buildingDAO.findBuilding(roomCode, letters);
        
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
}
