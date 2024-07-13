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
package assistant.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.database.SubTransactionResult;
import assistant.database.Transaction;
import assistant.database.TransactionError;
import assistant.database.TransactionStatementType;
import assistant.rest.dto.BuildingDTO;
import assistant.rest.dto.LabDTO;

/**
 * @author Alfredo
 */
@Repository
public class BuildingDAO {
	
	public BuildingDAO() {
		
	}
	
	public SubTransactionResult queryBuildingLab(String buildingCode) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  labid,
			        lab.name,
			        lab.code,
			        building.name AS building_name
			    FROM lab
			        INNER JOIN building ON fbuildid = buildid
			    WHERE
			        building.code = ?
			""", List.of(buildingCode));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public SubTransactionResult queryAllBuildings(int offset, int limit) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT buildid, code, name, gpin
				FROM building
			ORDER BY buildid
			OFFSET ?
			LIMIT  ?
			""", List.of(offset, limit));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public SubTransactionResult queryBuilding(String code, String possibleMatch) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  buildid,
			        code,
			        name, 
			        gpin
			    FROM building
			    WHERE
			        code = ?
			UNION ALL
			SELECT  buildid,
			        code,
			        name, 
			        gpin
			    FROM building
			    WHERE
			        code like ? AND code != ? AND
			        NOT EXISTS (SELECT 1 FROM building WHERE code = ?)
			""", List.of(code, possibleMatch + "%", code, code));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
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
	
	public void deleteBuilding(String code) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			DELETE FROM building 
				WHERE code = ?
			""", List.of(code));
		
		transaction.prepare()
		.executeThen(TransactionStatementType.UPDATE_QUERY)
		.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
	}
	
	public SubTransactionResult insertLab(LabDTO lab) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			INSERT INTO lab (name, code, fbuildid)
			    SELECT ?, ?, buildid
			    FROM building
			        WHERE
			            building.code = ?
			RETURNING labid
			""", List.of(lab.getName(), lab.getCode(), lab.getBuildingCode()));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.MIXED_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		return transaction.getLatestResult();
	}
}
