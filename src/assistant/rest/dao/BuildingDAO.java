/**
 * 
 */
package assistant.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.rest.dto.BuildingDTO;

/**
 * @author Alfredo
 */
@Repository
public class BuildingDAO {
	
	public BuildingDAO() {
		
	}
	
	public List<BuildingDTO> getAll(int offset, int limit) {
		final String SQL = 
			"""
			select buildid, code, name, gpin
				from building
			order by buildid
			offset ?
			limit  ?;
			""";
		List<BuildingDTO> buildings = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, offset);
			stmt.setInt(2, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				BuildingDTO building = new BuildingDTO();
				building.setId(result.getInt("buildid"));
				building.setCode(result.getString("code"));
				building.setName(result.getString("name"));
				building.setGpin(result.getString("gpin"));
				buildings.add(building);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return buildings;
	}
	
	public Optional<BuildingDTO> findBuilding(int id) {
		final String SQL = 
			"""
			select buildid, code, name, gpin
					from building
				where buildid = ?;
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		BuildingDTO building = new BuildingDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, id);

			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				building.setId(result.getInt("buildid"));
				building.setCode(result.getString("code"));
				building.setName(result.getString("name"));
				building.setGpin(result.getString("gpin"));
				found.set(true);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(building) : Optional.empty();
	}
	
	public int insertBuilding(BuildingDTO building) {
		final String SQL = 
			"""
			insert into building (code, name, gpin)
				values(?, ?, ?)
			returning buildid;
			""";
		AtomicInteger inserted = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, building.getCode());
			stmt.setString(2, building.getName());
			stmt.setString(3, building.getGpin());
			
			ResultSet result = stmt.executeQuery();
			while(result.next())
				inserted.set(result.getInt("buildid"));
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return inserted.get();
	}
	
	public void updateBuilding(int id, BuildingDTO buildingDTO) {
		final String SQL_UPDATE = 
			"""
			update building
				set
					code = ?,
					name = ?,
					gpin = ?
				where
					buildid = ?;
			""";
		RunnableSQL rq = connection -> {
			PreparedStatement update_stmt = connection.prepareStatement(SQL_UPDATE);
			update_stmt.setString(1, buildingDTO.getCode());
			update_stmt.setString(2, buildingDTO.getName());
			update_stmt.setString(3, buildingDTO.getGpin());
			update_stmt.setInt(4, id);
			
			update_stmt.executeUpdate();
			update_stmt.close();
		};
		Application.instance().getDatabaseConnection().establishConnection(rq);
	}
	
	public void deleteBuilding(int id) {
		final String SQL = 
			"""
			delete from building 
				where buildid = ?
			""";
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, id);
			stmt.executeUpdate();
			stmt.close();
		};
		Application.instance().getDatabaseConnection().establishConnection(rq);
	}
}
