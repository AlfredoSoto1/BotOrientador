/**
 * 
 */
package assistant.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Function;

import assistant.app.core.Application;

/**
 * @author Alfredo
 */
public class BatchTransaction implements AutoCloseable {

	private DatabaseConnection connection;
	
	private List<TransactionError> errors;
	private Stack<SubTransactionResult> results;
	
	private Queue<List<?>> batches;
	private Queue<PreparedStatement> statements;
	private Queue<TransactionStatement> sqlTransactions;
	
	public BatchTransaction() {
		this.batches = new LinkedList<>();
		this.errors = new ArrayList<>();
		this.results = new Stack<>();
		this.statements = new LinkedList<>();
		this.sqlTransactions = new LinkedList<>();
		this.connection = Application.instance().getDatabaseConnection();
		
		this.connection.connect();
	}
	
	/**
	 * @return Last result from query
	 */
	public SubTransactionResult getLatestResult() {
		return results.peek();
	}
	
	/**
	 * @return true if transaction was done successfully
	 */
	public boolean isCompleted() {
		return errors.isEmpty();
	}
	
	/**
	 * @return List of transaction errors
	 */
	public List<TransactionError> catchErrors() {
		return errors;
	}
	
	/**
	 * Loads the batch to run in queue
	 * @param <T>
	 * @param batch
	 * @return BatchTransaction
	 */
	public <T> BatchTransaction loadBatch(List<T> batch) {
		if(!errors.isEmpty())
			return this;
		this.batches.add(batch);
		return this;
	}
	
	/**
	 * Submit the SQL to run as batch during transaction
	 * @param SQL
	 * @param batchedParameters
	 * @return BatchTransaction
	 */
	public <T> BatchTransaction batchSQL(String SQL, Function<T, List<?>> batchedParameters) {
		if(!errors.isEmpty())
			return this;
		sqlTransactions.add(new TransactionStatement(SQL, batchedParameters));
		return this;
	}
	
	/**
	 * Prepares the transaction for every SQL
	 * statement checking syntax and other parameters.
	 * This starts the transaction once preparation is done.
	 * @return BatchTransaction
	 */
	public BatchTransaction prepare() {
		if(!errors.isEmpty())
			return this;
		
		// Disables auto commit, meaning transaction starts here
		try {
			connection.getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			errors.add(new TransactionError(e.getMessage()));
			return this;
		}
		
		// For every transaction, prepare the query
		for(TransactionStatement statement : sqlTransactions) {
			try {
				PreparedStatement stmt = connection.getConnection().prepareStatement(statement.SQL());
				statements.add(stmt);
			} catch (SQLException e) {
				errors.add(new TransactionError(e.getMessage()));
				cancelTransaction();
				return this;
			}
		}
		return this;
	}
	
	/**
	 * Executes the batch
	 * @param type
	 * @return BatchTransaction
	 */
	public BatchTransaction executeThen(TransactionStatementType type) {
		// If there are errors, stop transaction here
		if (!errors.isEmpty())
			return this;
		
		// Obtain the statements required to execute the query
		PreparedStatement pstmt = statements.poll();
		TransactionStatement statement = sqlTransactions.poll();
		List<?> batch = batches.poll();
		
		// Process one batch at the time
		// Batches for this transaction implementation do NOT
		// depend on the result of the previous batch like a normal transaction
		processBatchExecution(type, pstmt, batch, statement.batchedParameters());
		
		try {
			pstmt.close();
		} catch (SQLException e) {
			errors.add(new TransactionError(e.getMessage()));
		}
		return this;
	}
	
	/**
	 * Ends closes the transaction connection
	 * This must be called always after object creation.
	 */
	@Override
	public void close() throws Exception {
		this.connection.disconnect();
	}
	
	/**
	 * This forces the closing of the transaction.
	 * It prevents from giving the user access to 
	 * custom exceptions.
	 */
	public void forceClose() {
		try {
			// Cancel any transaction that was not completed
			if(!connection.getConnection().getAutoCommit())
				cancelTransaction();
			close();
		} catch (Exception e) {
			errors.add(new TransactionError(e.getMessage()));
		}
	}
	
	/**
	 * Commits all changes that the transaction is
	 * about to do. Once it runs, it cannot be undone!
	 * @return BatchTransaction
	 */
	public BatchTransaction commit() {
		if (!errors.isEmpty())
			return this;
		try {
			connection.getConnection().commit();
			connection.getConnection().setAutoCommit(true);
		} catch (SQLException e) {
			errors.add(new TransactionError(e.getMessage()));
		}
		return this;
	}
	
	private void cancelTransaction() {
		try {
			connection.getConnection().rollback();
			connection.getConnection().setAutoCommit(true);
		} catch (SQLException e) {
			errors.add(new TransactionError(e.getMessage()));
		}
	}
	
	private <T> void processBatchExecution(TransactionStatementType type, PreparedStatement pstmt, List<T> batch, Function<T, List<?>> batchedParameters) {
		try {
			// Set all the parameters before running the query
			for(T element : batch) {
				int position = 1;
				for (Object parameter : batchedParameters.apply(element))
					setParameter(pstmt, position++, parameter);
				pstmt.addBatch();
			}

	        // Determine the type of query and execute accordingly
	        if (type == TransactionStatementType.MIXED_QUERY) {
	            pstmt.executeBatch();
	            
        		ResultSet result = pstmt.getGeneratedKeys();
	            SubTransactionResult rowResult = new SubTransactionResult(result);

	            // Save all results from query into table
	            while (result.next())
	                rowResult.addRow(result);

	            results.add(rowResult);
	            result.close();
	        } else if (type == TransactionStatementType.UPDATE_QUERY) {
	        	pstmt.executeBatch();
	        } else {
	        	throw new IllegalArgumentException("Cannot batch a SELECT statement");
	        }

		} catch (SQLException e) {
			errors.add(new TransactionError(e.getMessage()));
			cancelTransaction();
		}
	}
	
	private void setParameter(PreparedStatement pstmt, int position, Object obj) throws SQLException {
        if (obj instanceof String string) {
            pstmt.setString(position, string);
        } else if (obj instanceof Long bigint) {
            pstmt.setLong(position, bigint);
        } else if (obj instanceof Integer integer) {
            pstmt.setInt(position, integer);
        } else if (obj instanceof Boolean bool) {
            pstmt.setBoolean(position, bool);
        } else if (obj instanceof Double decimal) {
            pstmt.setDouble(position, decimal);
        } else {
            pstmt.setObject(position, obj);
        }
    }
}
