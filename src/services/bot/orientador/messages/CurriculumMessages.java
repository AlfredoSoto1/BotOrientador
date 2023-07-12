/**
 * 
 */
package services.bot.orientador.messages;

/**
 * @author Alfredo
 *
 */
public interface CurriculumMessages {

	public static final String GIVE_INEL_CURRICULUM = 
			"""
			Here is the Electrical Engineering Curriculum
			""";
	
	public static final String GIVE_ICOM_CURRICULUM = 
			"""
			Here is the Computer Engineering Curriculum
			""";

	public static final String GIVE_INSO_CURRICULUM = 
			"""
			Here is the Software Engineering Curriculum
			""";
	public static final String GIVE_CIIC_CURRICULUM = 
			"""
			Here is the Computer Science & Engineering Curriculum
			""";
	
	public static final String NOT_FOUND_CURRICULUM = 
			"""
			El curriculo que me pediste no está en mi base de datos.
			Trata una de las opciones que ofrece el ``/curriculo``.
			""";
}
