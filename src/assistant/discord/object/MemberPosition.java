/**
 * 
 */
package assistant.discord.object;

import java.util.Arrays;

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
	
	// Combination - level
	STAFF_CONSEJERO("StaffConsejero", STAFF, CONSEJERO_PROFESIONAL),

	STAFF_GRADUADO("StaffGraduado", STAFF, ESTUDIANTE_GRADUADO),
	STAFF_ORIENTADOR("StaffOrientador", STAFF, ESTUDIANTE_ORIENTADOR),

	MODERATOR_GRADUADO("ModeratorOrientador", MODERATOR, ESTUDIANTE_GRADUADO),
	MODERATOR_ORIENTADOR("ModeratorOrientador", MODERATOR, ESTUDIANTE_ORIENTADOR),

	BOT_DEVELOPER_GRADUADO("BotDeveloperOrientador", BOT_DEVELOPER, ESTUDIANTE_GRADUADO),
	BOT_DEVELOPER_ORIENTADOR("BotDeveloperOrientador", BOT_DEVELOPER, ESTUDIANTE_ORIENTADOR),
	
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
	private final MemberPosition[] positions;
	
	/**
	 * 
	 * @param literalName
	 */
	private MemberPosition(String literalName) {
		this.positions = null;
		this.effectiveName = literalName;
	}
	
	/**
	 * 
	 * @param literalName
	 */
	private MemberPosition(String literalName, MemberPosition... positions) {
		this.positions = positions;
		this.effectiveName = literalName;
	}
	
	/**
	 * @return literal name of the member role
	 */
	public String getEffectiveName() {
		return effectiveName;
	}
	
	/**
	 * @return array of positions
	 */
	public MemberPosition[] getPositions() {
		return positions;
	}
	
	/**
	 * @return array of positions
	 */
	public String[] getEffectiveNamePositions() {
		if (positions == null) {
			return new String[] { this.getEffectiveName() };
		} else {
			return Arrays.stream(positions)
					.map(MemberPosition::getEffectiveName)
					.toArray(String[]::new);
		}
	}
	
	public static MemberPosition asPosition(String program) {
		for(MemberPosition p : MemberPosition.values())
			if(p.effectiveName.equalsIgnoreCase(program))
				return p;
		return null;
	}
}
