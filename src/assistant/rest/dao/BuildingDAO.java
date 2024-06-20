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
import assistant.rest.entity.BuildingEntity;

/**
 * @author Alfredo
 */
@Repository
public class BuildingDAO {
	
	public BuildingDAO() {
		
	}
	
	public List<BuildingEntity> findAll(int offset, int limit) {
		final String SQL = 
			"""
			select buildid, code, name, gpin
				from building
			order by buildid
			offset ?
			limit  ?;
			""";
		List<BuildingEntity> buildings = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, offset);
			stmt.setInt(2, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				BuildingEntity building = new BuildingEntity();
				building.setID(result.getInt("buildid"));
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
	
	public Optional<BuildingEntity> findByID(int id) {
		final String SQL = 
			"""
			select buildid, code, name, gpin
					from building
				where buildid = ?;
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		BuildingEntity building = new BuildingEntity();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, id);

			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				building.setID(result.getInt("buildid"));
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
	
	public int insert(BuildingEntity entity) {
		final String SQL = 
			"""
			insert into building (code, name, gpin)
				values(?, ?, ?)
			returning buildid;
			""";
		AtomicInteger inserted = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, entity.getCode());
			stmt.setString(2, entity.getName());
			stmt.setString(3, entity.getGpin());
			
			ResultSet result = stmt.executeQuery();
			while(result.next())
				inserted.set(result.getInt("buildid"));
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return inserted.get();
	}
	
	public Optional<BuildingEntity> update(int id, BuildingEntity entity) {
		final String SQL_select = 
			"""
			select buildid, code, name, gpin
				from building
			where buildid = ?;
			""";
		final String SQL_update = 
			"""
			update building
				set
					code = ?,
					name = ?,
					gpin = ?
				where
					buildid = ?;
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		BuildingEntity building = new BuildingEntity();
		
		RunnableSQL rq = connection -> {
			PreparedStatement select_stmt = connection.prepareStatement(SQL_select);
			select_stmt.setInt(1, id);
			
			ResultSet result = select_stmt.executeQuery();
			while(result.next()) {
				building.setID(result.getInt("buildid"));
				building.setCode(result.getString("code"));
				building.setName(result.getString("name"));
				building.setGpin(result.getString("gpin"));
				found.set(true);
			}
			result.close();
			select_stmt.close();
			
			if (!found.get())
				return;
			
			PreparedStatement update_stmt = connection.prepareStatement(SQL_update);
			update_stmt.setString(1, entity.getCode());
			update_stmt.setString(2, entity.getName());
			update_stmt.setString(3, entity.getGpin());
			update_stmt.setInt(4, id);
			
			update_stmt.executeUpdate();
			update_stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(building) : Optional.empty();
	}
	
	public Optional<BuildingEntity> delete(int id) {
		final String SQL = 
			"""
			delete from building 
				where buildid = ?
			returning name, code, gpin;
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		BuildingEntity building = new BuildingEntity();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, id);
			
			// Execute the query and obtain the results
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
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
}
