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

    public SubTransactionResult(ResultSet result) throws SQLException {
    	this.rows = new ArrayList<>();
    	this.columnNames = new ArrayList<>();
    	
    	ResultSetMetaData metaData = result.getMetaData();
		int columnCount = metaData.getColumnCount();

		for (int i = 1; i <= columnCount; i++)
            columnNames.add(metaData.getColumnName(i));
    }
    
    public static ResultReference of(String columnName) {
    	return new ResultReference(columnName);
    }

    public void addRow(ResultSet result) throws SQLException {
        Object[] row = new Object[columnNames.size()];
        for (int i = 0; i < columnNames.size(); i++)
            row[i] = getColumnValue(result, result.getMetaData().getColumnType(i + 1), columnNames.get(i));
        rows.add(row);
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public int columnIndexOf(String columnName) {
    	return columnNames.indexOf(columnName);
    }
    
    public List<Object[]> getRows() {
        return rows;
    }

    public boolean contains(String columnName) {
    	return columnNames.contains(columnName);
    }
    
    public Object getResult(String columnName, int rowIndex) {
        int columnIndex = columnNames.indexOf(columnName);
        return columnIndex >= 0 ? rows.get(rowIndex)[columnIndex] : null;
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
        default:
        	return result.getObject(columnName);
        }
	}
	
	public static class ResultReference {
        private final String columnName;

        public ResultReference(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }
    }
}