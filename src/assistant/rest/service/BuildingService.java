/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.rest.dao.BuildingDAO;
import assistant.rest.dto.BuildingDTO;
import assistant.rest.entity.BuildingEntity;

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
		return buildingDAO.findAll(page * size, size).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
	}
	
    public BuildingDTO getByID(Integer id) {
        return buildingDAO.findByID(id)
        		.map(this::convertToDTO).orElse(null);
    }
    
    public int insertBuilding(BuildingDTO building) {
    	return buildingDAO.insert(convertToEntity(building));
    }
    
    public BuildingDTO updateBuilding(int id, BuildingDTO building) {
    	return buildingDAO.update(id, convertToEntity(building))
    			.map(this::convertToDTO).orElse(null);
    }
    
    public BuildingDTO deleteBuilding(int id) {
    	return buildingDAO.delete(id)
    			.map(this::convertToDTO).orElse(null);
    }
	
	private BuildingDTO convertToDTO(BuildingEntity entity) {
		return new BuildingDTO(entity.getCode(), entity.getName(), entity.getGpin());
	}
	
	private BuildingEntity convertToEntity(BuildingDTO dto) {
		return new BuildingEntity(0, dto.getCode(), dto.getName(), dto.getGpin());
	}
}
