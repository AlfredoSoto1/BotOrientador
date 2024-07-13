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

import java.util.List;

import org.springframework.stereotype.Repository;

import assistant.database.SubTransactionResult;
import assistant.database.Transaction;
import assistant.database.TransactionError;
import assistant.database.TransactionStatementType;

/**
 * @author Alfredo
 */
@Repository
public class ProjectsDAO {
	
	public SubTransactionResult queryProjectNames(int offset, int limit) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  name
			    FROM project
			        INNER JOIN contact on fcontid = contid
			ORDER BY name
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
	
	public SubTransactionResult queryAllProjects(int offset, int limit) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  projecid,
			        name,
			        project.description,
			        email,
			        COALESCE(webpage.url, 'No web') AS url,
			        socialmedia.platform,
			        socialmedia.urlhandle
			    FROM project
			        INNER JOIN contact     ON project.fcontid = contid
			        LEFT  JOIN webpage     ON webpage.fcontid = contid
			        LEFT  JOIN socialmedia ON socialmedia.fcontid = contid
			ORDER BY name
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
	
	public SubTransactionResult queryProject(String name) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  projecid,
			        name,
			        project.description,
			        email,
			        COALESCE(webpage.url, 'No web') AS url,
			        socialmedia.platform,
			        socialmedia.urlhandle
			    FROM project
			        INNER JOIN contact     ON project.fcontid = contid
			        LEFT  JOIN webpage     ON webpage.fcontid = contid
			        LEFT  JOIN socialmedia ON socialmedia.fcontid = contid
			    WHERE
			        name = ?
			""", List.of(name));
		
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
}
