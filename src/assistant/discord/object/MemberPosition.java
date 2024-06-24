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
	ESTUDIANTE_ORIENTADOR  ("EstudianteOrientador"),
	
	// This is for carrying no value in
	// response to the member position. Mostly used
	// in controllers.
	NONE("NONE");
	
	/**
	 * Establish literal name that is used
	 * in database for reference. For more details
	 * look for the MemberRole table in schema.
	 */
	private final String effectiveName;
	
	/**
	 * 
	 * @param literalName
	 */
	private MemberPosition(String literalName) {
		this.effectiveName = literalName;
	}
	
	/**
	 * @return literal name of the member role
	 */
	public String getEffectiveName() {
		return effectiveName;
	}
	
	public static MemberPosition asPosition(String program) {
		for(MemberPosition p : MemberPosition.values())
			if(p.effectiveName.equalsIgnoreCase(program))
				return p;
		return null;
	}
}
