/**
 * 
 */
package assistant.database;

import java.util.List;

/**
 * @author Alfredo
 */
public record TransactionStatement(String SQL, List<?> parameters) {
	
}
