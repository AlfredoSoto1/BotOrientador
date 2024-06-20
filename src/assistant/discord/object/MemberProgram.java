/**
 * 
 */
package assistant.discord.object;

/**
 * @author Alfredo
 */
public enum MemberProgram {
	// Department - level
	INEL("INEL"),
	ICOM("ICOM"),
	INSO("INSO"),
	CIIC("CIIC"),
	ININ("ININ"),
	FISI("FISI"),
	ECON("ECON"),
	GERH("GERH");
	
	/**
	 * Establish literal name that is used
	 * in database for reference. For more details
	 * look for the MemberRole table in schema.
	 */
	private final String literalName;
	
	/**
	 * 
	 * @param literalName
	 */
	private MemberProgram(String literalName) {
		this.literalName = literalName;
	}
	
	/**
	 * @return literal name of the member role
	 */
	public String getLiteral() {
		return literalName;
	}
	
	public static MemberProgram asProgram(String program) {
		for(MemberProgram p : MemberProgram.values())
			if(p.literalName.equalsIgnoreCase(program))
				return p;
		return null;
	}	
	
	public static boolean isProgram(String program) {
		for(MemberProgram p : MemberProgram.values())
			if(p.literalName.equalsIgnoreCase(program))
				return true;
		return false;
	}	
}
