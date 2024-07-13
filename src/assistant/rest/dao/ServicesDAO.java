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
public class ServicesDAO {

	public ServicesDAO() {
		
	}
	
	public SubTransactionResult getServiceNames() {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT name FROM service
			""", List.of());
		
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
	
	public SubTransactionResult getAllServices(int offset, int limit) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  servid,              service.name, 
			        service.description, offering,
			        availability,        additional,
			        abreviation,         email,
			        building.code,       building.name AS bname,
			        contid, webid, socialid,
			        COALESCE(webpage.url,           'No Webpage')     AS url,
			        COALESCE(webpage.description,   'No Webpage')     AS web_title,
			        COALESCE(socialmedia.platform,  'No Socialmedia') AS platform,
			        COALESCE(socialmedia.urlhandle, 'No Socialmedia') AS urlhandle,
			        CASE WHEN webpage.url          IS NULL THEN false ELSE true END AS has_webpage,
			        CASE WHEN socialmedia.platform IS NULL THEN false ELSE true END AS has_socialmedia
			    FROM service
			        INNER JOIN department  ON fdepid = depid
			        INNER JOIN building    ON department.fbuildid = buildid
			        INNER JOIN contact     ON service.fcontid = contid
			        LEFT  JOIN webpage     ON webpage.fcontid = contid
			        LEFT  JOIN socialmedia ON socialmedia.fcontid = contid
			
			ORDER BY servid ASC
			OFFSET ?
			LIMIT  ?;
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
	
	public SubTransactionResult getService(String servicename) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  servid,              service.name, 
			        service.description, offering,
			        availability,        additional,
			        abreviation,         email,
			        building.code,       building.name AS bname,
			        contid, webid, socialid,
			        COALESCE(webpage.url,           'No Webpage')     AS url,
			        COALESCE(webpage.description,   'No Webpage')     AS web_title,
			        COALESCE(socialmedia.platform,  'No Socialmedia') AS platform,
			        COALESCE(socialmedia.urlhandle, 'No Socialmedia') AS urlhandle,
			        CASE WHEN webpage.url          IS NULL THEN false ELSE true END AS has_webpage,
			        CASE WHEN socialmedia.platform IS NULL THEN false ELSE true END AS has_socialmedia
			    FROM service
			        INNER JOIN department  ON fdepid = depid
			        INNER JOIN building    ON department.fbuildid = buildid
			        INNER JOIN contact     ON service.fcontid = contid
			        LEFT  JOIN webpage     ON webpage.fcontid = contid
			        LEFT  JOIN socialmedia ON socialmedia.fcontid = contid
				WHERE
					service.name = ?
			
			""", List.of(servicename));
		
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
