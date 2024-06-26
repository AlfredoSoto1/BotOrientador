/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public List<BuildingDTO> getAllBuilding(int page, int size) {
		return buildingDAO.getAll(page * size, size);
	}
	
    public Optional<BuildingDTO> findBuilding(Integer id) {
        return buildingDAO.findBuilding(id);
    }
    
    public int insertBuilding(BuildingDTO building) {
    	return buildingDAO.insertBuilding(building);
    }
    
    public Optional<BuildingDTO> updateBuilding(int id, BuildingDTO newBuilding) {
    	Optional<BuildingDTO> building = buildingDAO.findBuilding(id);
    	
    	if(building.isPresent()) {
    		buildingDAO.updateBuilding(id, newBuilding);
    		return building;
    	} else {
    		return Optional.empty();
    	}
    }
    
    public Optional<BuildingDTO> deleteBuilding(int id) {
    	Optional<BuildingDTO> building = buildingDAO.findBuilding(id);
    	
    	if(building.isPresent()) {
    		buildingDAO.deleteBuilding(id);
    		return building;
    	} else {
    		return Optional.empty();
    	}
    }
}
