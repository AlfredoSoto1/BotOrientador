/**
 * 
 */
package assistant.discord.object;

/**
 * @author Alfredo
 */
public enum MemberPosition {
	// Moderation - level
	BOTS                   ("Bots"),
	STAFF                  ("Staff"),
	ASSISTANT              ("Assistant"),
	MODERATOR              ("Moderator"),
	BOT_DEVELOPER          ("BotDeveloper"),
	ADMINISTRATOR          ("Administrator"),
	CONSEJERO_PROFESIONAL  ("ConsejeroProfesional"),

	// Member     - level
	PREPA                  ("Prepa"),
	NOT_VERIFIED           ("Not-Verified"),
	ESTUDIANTE_GRADUADO    ("EstudianteGraduado"),
	ESTUDIANTE_ORIENTADOR  ("EstudianteOrientador");
	
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
	private MemberPosition(String literalName) {
		this.literalName = literalName;
	}
	
	/**
	 * @return literal name of the member role
	 */
	public String getLiteral() {
		return literalName;
	}
	
	public static MemberPosition asPosition(String program) {
		for(MemberPosition p : MemberPosition.values())
			if(p.literalName.equalsIgnoreCase(program))
				return p;
		return null;
	}
}
