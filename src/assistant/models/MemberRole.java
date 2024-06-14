/**
 * 
 */
package assistant.models;

/**
 * @author Alfredo
 */
public enum MemberRole {
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
	private MemberRole(String literalName) {
		this.literalName = literalName;
	}
	
	/**
	 * @return literal name of the member role
	 */
	public String getName() {
		return literalName;
	}
}
