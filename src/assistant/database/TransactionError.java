/**
 * 
 */
package assistant.database;

/**
 * @author Alfredo
 */
public class TransactionError {

	private String message;
	
	public TransactionError(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
