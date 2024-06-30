/**
 * 
 */
package assistant.database;

import java.util.List;
import java.util.function.Function;

/**
 * @author Alfredo
 */
public class TransactionStatement {
	
	private String SQL;
	private List<?> parameters;
	private Function<?, List<?>> batchedParameters;
	
	/**
	 * This is only for normal SQL transactions
	 * @param SQL
	 * @param parameters
	 */
	public TransactionStatement(String SQL, List<?> parameters) {
		this.SQL = SQL;
		this.parameters = parameters;
	}
	
	/**
	 * This is only for batched SQL transactions
	 * @param SQL
	 * @param batchedParameters
	 */
	public <T> TransactionStatement(String SQL, Function<T, List<?>> batchedParameters) {
		this.SQL = SQL;
		this.batchedParameters = batchedParameters;
	}
	
	public String SQL() {
		return SQL;
	}
	
	public List<?> parameters() {
		if (parameters == null)
			throw new IllegalAccessError("Cannot read parameters for a batch statement");
		return parameters;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Function<T, List<?>> batchedParameters() {
		if (batchedParameters == null)
			throw new IllegalAccessError("Cannot read batched parameters for a regular statement");
		return (Function<T, List<?>>) batchedParameters;
	}
}
