/**
 * 
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
public class OrganizationsDAO {
	
	public SubTransactionResult queryOrganizationNames(int offset, int limit) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT name
			    FROM organization
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
	
	public SubTransactionResult queryAllOrganizations(int offset, int limit) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  orgid,
			        name,
			        organization.description,
			        email,
			        COALESCE(webpage.url, 'No web') AS url,
			        COALESCE(socialmedia.platform, 'No socialmedia') AS platform,
			        COALESCE(socialmedia.urlhandle, 'No socialmedia') AS urlhandle
			    FROM organization
			        INNER JOIN contact     ON organization.fcontid = contid
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
	
	public SubTransactionResult queryOrganization(String name) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  orgid,
			        name,
			        organization.description,
			        email,
			        COALESCE(webpage.url, 'No web') AS url,
			        COALESCE(socialmedia.platform,  'No socialmedia') AS platform,
			        COALESCE(socialmedia.urlhandle, 'No socialmedia') AS urlhandle
			    FROM organization
			        INNER JOIN contact     ON organization.fcontid = contid
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
