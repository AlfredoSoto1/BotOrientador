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
