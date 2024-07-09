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
public class GameDAO {
	
	public SubTransactionResult queryUpdateCommandUserCount(String commandName, String user, long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		// Add all transaction parameter fields
		transaction.submitSQL(
				"""
				UPDATE advancement
					SET
					    commands_used = commands_used + 1
					FROM joinedmember
					        INNER JOIN serverownership ON fseoid = seoid
					WHERE
					    fjmid     = jmid AND
					    username  = ?    AND
					    discserid = ?
				""", 
				List.of(user, server))
			.submitSQL(
				"""
				WITH server_command AS (
				    INSERT INTO commands (fseoid, name, usage)
				    SELECT seoid, ?, 1
				        FROM serverownership
				    WHERE
				        discserid = ? AND
				        NOT EXISTS (
				            SELECT 1 
				                FROM commands
				                    INNER JOIN serverownership ON fseoid = seoid
				                WHERE discserid = ? AND name = ?
				        )
				    RETURNING comid
				),
				needs_to_update AS (
				    SELECT CASE WHEN count(*) > 0 THEN true ELSE false END AS created 
				        FROM server_command
				)
				UPDATE commands
				    SET
				        usage = usage + 1
				    FROM serverownership
				    WHERE
				        commands.fseoid = seoid AND
				        commands.name   = ?     AND 
				        discserid       = ?     AND
				        (SELECT created FROM needs_to_update LIMIT 1) = false
				""", 
				List.of(commandName, server, server, commandName, commandName, server));
		
		// Prepare transaction and execute by parts
		transaction.prepare()
			.executeThen(TransactionStatementType.UPDATE_QUERY)
			.executeThen(TransactionStatementType.UPDATE_QUERY)
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

	public SubTransactionResult queryUpdateXP(String user, int quantity, long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		// Add all transaction parameter fields
		transaction.submitSQL(
			"""
			WITH old_level AS (
			    SELECT level
			        FROM advancement
			            INNER JOIN joinedmember ON fjmid = jmid
			            INNER JOIN serverownership ON fseoid = seoid
			        WHERE
			            username = ? AND discserid = ?
			    LIMIT 1
			), 
			set_XP AS (
			    UPDATE advancement
			        SET
			            message_xp   = message_xp + ?,
			            xp_milestone = CASE WHEN message_xp + ? >= xp_milestone THEN xp_milestone * 2 ELSE xp_milestone END,
			            level        = CASE WHEN message_xp + ? >= xp_milestone THEN level + 1        ELSE level END
			        FROM joinedmember
			            INNER JOIN serverownership ON fseoid = seoid
			        WHERE 
			            fjmid     = jmid AND
			            username  = ?    AND
			            discserid = ?
			    RETURNING   message_xp, 
			                username, 
			                xp_milestone, 
			                level, 
			                commands_used, 
			                level > (SELECT old_level.level FROM old_level) AS level_increased
			)
			SELECT  message_xp, 
			        username, 
			        xp_milestone, 
			        level, 
			        commands_used, 
			        level_increased
			    FROM set_XP
			""", 
			List.of(user, server, quantity, quantity, quantity, user, server));
		
		// Prepare transaction and execute by parts
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

	public SubTransactionResult queryLeaderboard(long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		// Add all transaction parameter fields
		transaction.submitSQL(
			"""
			SELECT message_xp, username, xp_milestone, level, commands_used,
			        ROW_NUMBER() OVER (ORDER BY message_xp DESC) AS rank
			    FROM joinedmember
			        INNER JOIN advancement     ON fjmid = jmid
			        INNER JOIN serverownership ON fseoid = seoid
			    WHERE
				    discserid = ?
			ORDER BY message_xp DESC
			LIMIT 5
			""", 
			List.of(server));
		
		// Prepare transaction and execute by parts
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
	
	public SubTransactionResult queryLeaderboardUserPosition(String user, long server) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		// Add all transaction parameter fields
		transaction.submitSQL(
			"""
			WITH ranked_users AS (
			    SELECT message_xp, username, xp_milestone, level, commands_used, discserid,
			            ROW_NUMBER() OVER (ORDER BY message_xp DESC) AS rank
			        FROM joinedmember
			            INNER JOIN advancement     ON fjmid = jmid
			            INNER JOIN serverownership ON fseoid = seoid
			)
			SELECT *
			    FROM ranked_users
			    WHERE
			        username = ? AND discserid = ?
			""", 
			List.of(user, server));
		
		// Prepare transaction and execute by parts
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
