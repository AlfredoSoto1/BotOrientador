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
import java.util.MissingFormatArgumentException;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Predicate;

import assistant.app.core.Application;

/**
 * @author Alfredo
 */
public class Transaction implements AutoCloseable {
	
	private DatabaseConnection connection;
	
	private List<TransactionError> errors;
	private Stack<SubTransactionResult> results;
	
	private Queue<PreparedStatement> statements;
	private Queue<TransactionStatement> sqlTransactions;
	
	public Transaction() {
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
	 * Submits the SQL to run in transaction
	 * @param SQL
	 * @param parameters
	 * @return Transaction
	 */
	public Transaction submitSQL(String SQL, List<?> parameters) {
		if(!errors.isEmpty())
			return this;
		
		sqlTransactions.add(new TransactionStatement(SQL, parameters));
		return this;
	}
	
	/**
	 * Prepares the transaction for every SQL
	 * statement checking syntax and other parameters.
	 * This starts the transaction once preparation is done.
	 * @return Transaction
	 */
	public Transaction prepare() {
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
	 * Execute SQL query from statement queue
	 * @return Transaction
	 */
	public Transaction executeThen(TransactionStatementType type) {
		// If there are errors, stop transaction here
		if (!errors.isEmpty())
			return this;
		
		// Obtain the statements required to execute the query
		PreparedStatement pstmt = statements.poll();
		TransactionStatement statement = sqlTransactions.poll();
		
		// Check for previous query results to fill parameters with
		Optional<SubTransactionResult> previousResults = getIfAnyLastRowResults(statement);

		if (previousResults.isPresent()) {
			for (int row = 0;row < previousResults.get().getRows().size();row++)
				processExecution(type, pstmt, buildParameters(statement.parameters(), previousResults.get(), row));
		} else {
			processExecution(type, pstmt, statement.parameters());
		}
		
		try {
			pstmt.close();
		} catch (SQLException e) {
			errors.add(new TransactionError(e.getMessage()));
		}
		return this;
	}
	
	/**
	 * Execute SQL query from statement queue
	 * @return Transaction
	 */
	public Transaction executeThen(TransactionStatementType type, Predicate<SubTransactionResult> processResult) {
		// If there are errors, stop transaction here
		if (!errors.isEmpty())
			return this;
		
		// Obtain the statements required to execute the query
		PreparedStatement pstmt = statements.poll();
		TransactionStatement statement = sqlTransactions.poll();
		
		// Check for previous query results to fill parameters with
		Optional<SubTransactionResult> previousResults = getIfAnyLastRowResults(statement);
		
		if (previousResults.isPresent()) {
			for (int row = 0;row < previousResults.get().getRows().size();row++)
				processExecution(type, pstmt, buildParameters(statement.parameters(), previousResults.get(), row));
		} else {
			processExecution(type, pstmt, statement.parameters());
		}
		
		try {
			// Check for the type of transaction that we are doing
			// This is to test the results to confirm if transaction should continue
			if((type == TransactionStatementType.SELECT_QUERY ||
				type == TransactionStatementType.MIXED_QUERY) && !results.isEmpty()) {
				if (!processResult.test(results.peek()))
					throw new SQLException("Failed result filter test at: " + results.peek());
			}
			
			pstmt.close();
		} catch (SQLException e) {
			errors.add(new TransactionError(e.getMessage()));
		}
		return this;
	}
	
	/**
	 * Commits all changes that the transaction is
	 * about to do. Once it runs, it cannot be undone!
	 * @return
	 */
	public Transaction commit() {
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
	
	private Optional<SubTransactionResult> getIfAnyLastRowResults(TransactionStatement statement) {
		SubTransactionResult previousResults = null;
		Stack<SubTransactionResult> onHandSwapTransactions = new Stack<>();
		
		// Check all parameters, if one of them contains a result reference
		// process it differently.
		for (Object obj : statement.parameters()) {
			if (!(obj instanceof SubTransactionResult.ResultReference reference))
				continue;
			
			while (!results.empty()) {
				// Get the last result of the transaction and check if the
				// current result reference contains a column from previous result
				SubTransactionResult latestResult = results.peek();
				if (latestResult.contains(reference.getColumnName())) {
					previousResults = latestResult;
					// Stop looking since we found the latest column
					break;
				} else {
					// Save the latest result in hand to later
					// swap it back to the results stack
					onHandSwapTransactions.add(results.pop());
				}
			}
			
			// Throw an exception if couldn't find the 
			// column with its latest results
			if (previousResults == null)
				throw new MissingFormatArgumentException(
						"Column is not present in previous transaction result: " + reference.getColumnName());
			
			// End iteration over result reference. This will only
			// handle one and only one latest transaction column. (At the moment)
			break;
		}
		
		// Swap all the transactions back to the original stack
		while (!onHandSwapTransactions.empty())
			results.add(onHandSwapTransactions.pop());
		
		return Optional.ofNullable(previousResults);
	}
	
    private List<Object> buildParameters(List<?> parameters, SubTransactionResult latestResult, int row) {
        List<Object> params = new ArrayList<>();
        for (Object obj : parameters) {
            if (obj instanceof SubTransactionResult.ResultReference reference) {
            	params.add(latestResult.getResult(reference.getColumnName(), row));
            } else {
                params.add(obj);
            }
        }
        return params;
    }
	
	private void processExecution(TransactionStatementType type, PreparedStatement pstmt, List<?> parameters) {
		try {
			// Set all the parameters before running the query
			int position = 1;
			for (Object parameter : parameters)
				setParameter(pstmt, position++, parameter);

	        // Determine the type of query and execute accordingly
	        if (type == TransactionStatementType.SELECT_QUERY || type == TransactionStatementType.MIXED_QUERY) {
	            ResultSet result = pstmt.executeQuery();
	            SubTransactionResult rowResult = new SubTransactionResult(result);

	            // Save all results from query into table
	            while (result.next())
	                rowResult.addRow(result);

	            results.add(rowResult);
	            result.close();
	        } else if (type == TransactionStatementType.UPDATE_QUERY) {
	        	pstmt.executeUpdate();
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
