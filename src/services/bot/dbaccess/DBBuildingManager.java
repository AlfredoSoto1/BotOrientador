///**
// * 
// */
//package services.bot.dbaccess;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Set;
//import application.database.DatabaseConnections;
//import application.records.GoogleMapPin;
//
///**
// * @author Alfredo
// *
// */
//public class DBBuildingManager {
//	
//	private static final String SELECT_BUILDING_PIN = "SELECT Code, BuildingName, Links FROM GoogleMapPins";
//	
//	private Map<String, GoogleMapPin> buildings;
//	
//	private Set<String> justLetterBuildingCode;
//	private Set<String> letterWithDashBuildingCode;
//	
//	public DBBuildingManager() {
//		this.buildings = new HashMap<>();
//		this.justLetterBuildingCode = new HashSet<>();
//		this.letterWithDashBuildingCode = new HashSet<>();
//	}
//	
//	/**
//	 * 
//	 */
//	public void dispose() {
//		buildings.clear();
//		justLetterBuildingCode.clear();
//		letterWithDashBuildingCode.clear();
//	}
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public Set<String> buildingCodes() {
//		return buildings.keySet();
//	}
//	
//	/**
//	 * 
//	 * @param buildingCode
//	 * @return Google map pin of a building with a code
//	 */
//	public Optional<GoogleMapPin> getBuilding(String buildingCode) {
//		
//		// Extract the code once formatted
//		GoogleMapPin buildingPin = buildings.get(buildingCode);
//		
//		// If the ping is not in the map, return empty
//		// optional. This is to avoid null pointer exceptions
//		return buildingPin == null ? Optional.empty() : Optional.of(buildingPin);
//	}
//	
//	/**
//	 * 
//	 * @param code
//	 * @return
//	 */
//    public Optional<String> formatCode(String code) {
//    	
//    	code = code.toLowerCase();
//    	
//    	if(code.contains("-")) {
//    		// Check if the code already exists
//    		if(letterWithDashBuildingCode.contains(code))
//    			return Optional.of(code);
//    	}
//    	// Remove everything that comes after the dash
//    	code = code.split("-")[0];
//    	
//    	// Check if the code already exists
//    	if(justLetterBuildingCode.contains(code))
//    		return Optional.of(code);
//
//        List<String> similarCodes = new ArrayList<>();
//        
//        // Look up for similar codes
//        for (String establishedCode : this.buildingCodes())
//            if (establishedCode.startsWith(code.toCharArray()[0] + ""))
//                similarCodes.add(establishedCode);
//        
//        // If there are similar codes from the code given
//        // as parameter, return a string containing all codes separated
//        // by commas to later be handled. If there are no similar
//        // codes from the given one as parameter, return empty optional.
//        return similarCodes.isEmpty() ? Optional.empty() : Optional.of(String.join(", ", similarCodes));
//    }
//	
//	/**
//	 * 
//	 */
//	public void loadBuildingPins() {
//		
//		DatabaseConnections.instance()
//		.getTeamMadeConnection()
//		.joinConnection(()->{
//			
//			PreparedStatement stmt = DatabaseConnections.instance()
//					.getTeamMadeConnection()
//					.getConnection()
//					.prepareStatement(SELECT_BUILDING_PIN);
//			
//			ResultSet result = stmt.executeQuery();
//			
//			while(result.next()) {
//				String code = result.getString(1);
//				String name = result.getString(2);
//				String link = result.getString(3);
//				
//				// Insert in map a new Google pin map
//				// from selected database fields
//				buildings.put(code, new GoogleMapPin(code, name, link));
//				
//				// Separate the codes that already have dashes
//				// this is to simplify classification
//				if(!code.contains("-"))
//					justLetterBuildingCode.add(code);
//				else
//					letterWithDashBuildingCode.add(code);
//			}
//			
//			// Free statement resources
//			result.close();
//			stmt.close();
//		});
//	}
//}
