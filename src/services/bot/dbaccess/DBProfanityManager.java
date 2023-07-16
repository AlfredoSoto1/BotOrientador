/**
 * 
 */
package services.bot.dbaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import application.database.DatabaseConnections;

/**
 * @author Alfredo
 *
 */
public class DBProfanityManager {

	private static final String INSERT_WORD = "INSERT INTO Profanities (ProfanityWord) VALUES (?)";
	private static final String SELECT_ALL_WORDS = "SELECT ProfanityWord FROM Profanities";
	
	/**
	 * 
	 */
	public DBProfanityManager() {
		
	}
	
	/**
	 * Dispose all content from 'this' instance
	 */
	public void dispose() {
		
	}
	
	/**
	 * Stores in database a word
	 * 
	 * @param word
	 */
	public void pushWord(String word) {
		DatabaseConnections.instance()
		.getTeamMadeConnection()
		.joinConnection(()->{
			
			PreparedStatement stmt = DatabaseConnections.instance()
					.getTeamMadeConnection()
					.getConnection()
					.prepareStatement(INSERT_WORD);
			
			stmt.setString(1, word);
			
			stmt.executeUpdate();
			
			stmt.close();
		});
	}
	
	/**
	 * Pull from database all the bad words
	 * 
	 * @return set of words
	 */
	public Set<String> pullProfanities(){
		
		Set<String> pulledProfanities = new HashSet<>();
		
		DatabaseConnections.instance()
		.getTeamMadeConnection()
		.joinConnection(()->{
			
			PreparedStatement stmt = DatabaseConnections.instance()
					.getTeamMadeConnection()
					.getConnection()
					.prepareStatement(SELECT_ALL_WORDS);
			
			ResultSet result = stmt.executeQuery();
			
			while(result.next())
				pulledProfanities.add(result.getString(1));
			
			result.close();
			stmt.close();
		});
		
		return pulledProfanities;
	}
}
