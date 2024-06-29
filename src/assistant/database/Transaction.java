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

import assistant.app.core.Application;

/**
 * @author Alfredo
 */
public class Transaction implements AutoCloseable {
	
	private DatabaseConnection connection;
	
	private List<TransactionError> errors;
	private List<SubTransactionResult> results;
	
	private Queue<PreparedStatement> statements;
	private Queue<TransactionStatement> sqlTransactions;
	
	public Transaction() {
		this.errors = new ArrayList<>();
		this.results = new ArrayList<>();
		this.statements = new LinkedList<>();
		this.sqlTransactions = new LinkedList<>();
		this.connection = Application.instance().getDatabaseConnection();
		
		this.connection.connect();
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
		Optional<List<Object[]>> previousResults = getIfAnyLastRowResults(statement);
		
		if (previousResults.isPresent()) {
			for (var row : previousResults.get())
				processExecution(type, pstmt, buildParameters(statement.parameters(), row));
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
			close();
		} catch (Exception e) {
			errors.add(new TransactionError(e.getMessage()));
		}
	}
	
	private Optional<List<Object[]>> getIfAnyLastRowResults(TransactionStatement statement) {
		List<Object[]> previousRowResults = null;
		
		// Check all parameters, if one of them contains a result reference
		// process it differently.
		for (Object obj : statement.parameters()) {
			if (!(obj instanceof SubTransactionResult.ResultReference reference))
				continue;
			
			// Get the last result of the transaction and check if the
			// current result reference contains a column from previous result
			SubTransactionResult previousResult = results.getLast();
			if (previousResult.contains(reference.getColumnName()))
				previousRowResults = previousResult.getRows();
			else
				throw new MissingFormatArgumentException(
						"Column is not present in previous transaction result: " + reference.getColumnName());
		}
		return Optional.ofNullable(previousRowResults);
	}
	
    private List<Object> buildParameters(List<?> parameters, Object[] row) {
        List<Object> params = new ArrayList<>();
        for (Object obj : parameters) {
            if (obj instanceof SubTransactionResult.ResultReference reference) {
                SubTransactionResult previousResult = results.getLast();
                params.add(row[previousResult.columnIndexOf(reference.getColumnName())]);
            } else {
                params.add(obj);
            }
        }
        return params;
    }
	
	private void processExecution(TransactionStatementType type, PreparedStatement pstmt, List<?> parameters) {
		try {
			int position = 1;
			for (Object parameter : parameters)
				setParameter(pstmt, position++, parameter);

	        // Determine the type of query and execute accordingly
	        if (type == TransactionStatementType.SELECT_QUERY || type == TransactionStatementType.MIXED_QUERY) {
	            ResultSet result = pstmt.executeQuery();
	            SubTransactionResult rowResult = new SubTransactionResult(result);

	            // Save all results from query into table
	            while (result.next()) {
	                rowResult.addRow(result);
	            }

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
