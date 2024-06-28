/**
 * 
 */
package assistant.discord.object;

/**
 * @author Alfredo
 */
public enum MemberProgram {
	// Department - level
	INEL("inel"),
	ICOM("icom"),
	INSO("inso"),
	CIIC("ciic"),
	ININ("inin"),
	FISI("fisi"),
	ECON("econ"),
	GERH("gerh");
	
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
		if(program.equalsIgnoreCase("0509 - BACHELOR OF SCIENCE IN SOFTWARE ENGINEERING"))
			return INSO;
		if(program.equalsIgnoreCase("0508 - BACHELOR OF SCIENCE IN COMPUTER SCIENCES AND ENGINEERING"))
			return CIIC;
		if(program.equalsIgnoreCase("BACHELOR OF SCIENCES IN ELECTRICAL ENGINEERING"))
			return INEL;
		if(program.equalsIgnoreCase("BACHELOR OF SCIENCES IN COMPUTER ENGINEERING"))
			return ICOM;
		return null;
	}	
	
	public static boolean isProgram(String program) {
		for(MemberProgram p : MemberProgram.values())
			if(p.literalName.equalsIgnoreCase(program))
				return true;
		return false;
	}	
}
