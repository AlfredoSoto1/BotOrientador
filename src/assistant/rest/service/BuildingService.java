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
	
	public List<BuildingDTO> getAll(int page, int size) {
		return buildingDAO.findAll(page * size, size);
	}
	
    public Optional<BuildingDTO> getByID(Integer id) {
        return buildingDAO.findByID(id);
    }
    
    public int insertBuilding(BuildingDTO building) {
    	return buildingDAO.insert(building);
    }
    
    public Optional<BuildingDTO> updateBuilding(int id, BuildingDTO building) {
    	return buildingDAO.update(id, building);
    }
    
    public Optional<BuildingDTO> deleteBuilding(int id) {
    	return buildingDAO.delete(id);
    }
}
