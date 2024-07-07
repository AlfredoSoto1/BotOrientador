/**
 * 
 */
package assistant.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alfredo
 */
public class SubTransactionResult {
	
    private List<String> columnNames;
    private List<Object[]> rows;

    /**
     * Creates a new sub transaction result
     * 
     * @param result
     * @throws SQLException
     */
    public SubTransactionResult(ResultSet result) throws SQLException {
    	this();
    	ResultSetMetaData metaData = result.getMetaData();
		int columnCount = metaData.getColumnCount();

		for (int i = 1; i <= columnCount; i++)
            columnNames.add(metaData.getColumnName(i));
    }
    
    /**
     * Prepare a default constructor for default results
     */
    protected SubTransactionResult() {
    	this.rows = new ArrayList<>();
    	this.columnNames = new ArrayList<>();
    }
    
    public boolean isEmpty() {
    	return rows.isEmpty();
    }
    
    /**
     * @return number of rows
     */
    public int rowCount() {
    	return rows.size();
    }
    
    /**
     * Adds a row to the result table
     * @param result
     * @throws SQLException
     */
    public void addRow(ResultSet result) throws SQLException {
        Object[] row = new Object[columnNames.size()];
        for (int i = 0; i < columnNames.size(); i++)
            row[i] = getColumnValue(result, result.getMetaData().getColumnType(i + 1), columnNames.get(i));
        rows.add(row);
    }

    /**
     * @return list of column names
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * @param columnName
     * @return index position of column name
     */
    public int columnIndexOf(String columnName) {
    	return columnNames.indexOf(columnName);
    }
    
    /**
     * @return rows for the result produced
     */
    public List<Object[]> getRows() {
        return rows;
    }

    /**
     * @param columnName
     * @return true if column name is present in table result
     */
    public boolean contains(String columnName) {
    	return columnNames.contains(columnName);
    }
    
    /**
     * @param columnName
     * @param rowIndex
     * @return Object from table result
     */
    @SuppressWarnings("unchecked")
	public <T> T getValue(String columnName, int rowIndex) {
    	if (rows.isEmpty())
    		return null;
        int columnIndex = columnNames.indexOf(columnName);
        return columnIndex >= 0 ? (T) rows.get(rowIndex)[columnIndex] : null;
    }
    
    /**
     * @param <T>
     * @param columnName
     * @return Returns the first value of the result table
     */
	public <T> T getValue(String columnName) {
    	return this.getValue(columnName, 0);
    }
    
	private Object getColumnValue(ResultSet result, int columnType, String columnName) throws SQLException {
		// Retrieve value based on column type
        switch (columnType) {
        case java.sql.Types.INTEGER:
        	return result.getInt(columnName);
        case java.sql.Types.BIGINT:
            return result.getLong(columnName);
        case java.sql.Types.VARCHAR:
        	return result.getString(columnName);
        case java.sql.Types.DATE:
            return result.getDate(columnName);
        case java.sql.Types.BOOLEAN:
            return result.getBoolean(columnName);
        case java.sql.Types.OTHER:
            // Handling JSON type (assuming JSON data is stored in `json` or `jsonb` columns)
            if (isJsonType(result, columnName)) {
                return result.getString(columnName);
            } else {
                return result.getObject(columnName);
            }
        default:
        	return result.getObject(columnName);
        }
	}
	
    private boolean isJsonType(ResultSet result, String columnName) throws SQLException {
        try {
            // Assuming JSON data type for the column, try to parse it as a JSON object
            result.getString(columnName);
            return true;
        } catch (SQLException e) {
            // If parsing fails, it is not JSON type
            return false;
        }
    }
	
	@Override
	public String toString() {
		return "Stored results: " + rows.size();
	}
	
	/**
	 * @author Alfredo
	 */
	public static class ResultReference {
        private final String columnName;

        public ResultReference(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }
        
        public static ResultReference of(String columnName) {
        	return new ResultReference(columnName);
        }
    }
	
	/**
	 * @author Alfredo
	 */
	public static class Replacement<T> {
        private final T[] values;

        public Replacement(T[] values) {
            this.values = values;
        }
        
        public T[] getValues() {
        	return values;
        }

    	public String getReplacement() {
    		if (values.length < 1)
    			throw new IllegalArgumentException("Invalid count for placeholders: " + values.length);

    		StringBuilder sb = new StringBuilder();
    		for (int i = 0; i < values.length - 1; i++)
    			sb.append("?, ");
    		sb.append("?");

    		return sb.toString();
    	}
        
        @SafeVarargs
		public static <E> Replacement<E> of(E... values) {
        	return new Replacement<>(values);
        }
    }
}