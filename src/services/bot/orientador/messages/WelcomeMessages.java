/**
 * 
 */
package services.bot.orientador.messages;

/**
 * @author Alfredo
 *
 */
public interface WelcomeMessages {
	
	public static final String WELCOME_MESSAGE = 
		"""
		Bienvenido al **COLEGIO**, *UPRM* y al Discord de **TEAM MADE**, @Member.UserName:tada::tada::raised_hands_tone3: 
		**Por favor, haz log-in para poder asignarte al grupo que Made eligió para ti!**
		""";
	
	public static final String PRESENTATION_MESSAGE_PREPA = 
		"""
		Increíble @Member.UserName, ahora eres un **COLEGIAL**:tada::tada::tada::raised_hands_tone3::raised_hands_tone3:
		Que fácil no?
		Ahora si me presento formalmente,
		Hola @Member.FullName
		Me alegra mucho que estés aquí en el Colegio
		Yo soy El Bot Orientador del server y formo parte del TeamMade.
		Estoy aquí para ayudarte lo mejor que pueda con cualquier duda que tengas.
		Para que tengas una idea, te puedo ayudar a:
			•Encontrar edificios
			•Proveer información de contacto para oficinas importantes
			•Proveer *links* súper utiles para complementar información
			•Y muchas cosas más!!!
		
		Espero ser de gran ayuda para tí, recuerda aquí siempre a la orden!!
		
		Si quieres, puedes empezar por utilizando los *slash commands*
		Basicamente escribes ``/'tu-comando-aquí'`` y veras como te sale un menú donde
		podrás ver varios de mis comandos que tengo.
		Empieza escribiendo ``/help``, para poderte dar una lista de todos los comandos
		que conozco para poderte ayudar!!
		""";
	
	public static final String PRESENTATION_MESSAGE_EOS = 
		"""
		Bienvenido @Member.UserName al TeamMade Discord Server 2023. 
		Recuerda, avisar a los Bot Developers de cualquier problema con el bot.
		De tener alguna idea respecto al bot o del server como tal, puedes decirle
		a los Administradores o a los Bot Developers!!
		""";
	
	public static final String ALREADY_LOGGED_IN = 
		"""
		Hmmm, veo que ya estas Logged-in al servidor.:hushed:
		Si necesitas algun tipo de asistencia respecto al servidor o ayuda
		respecto al bot, puedes contactar a los Bot Developers.
		""";
	
	public final String ERROR_MESSAGE_RETRY = 
		"""
		Veo que tienes problema entrando tu email. Por favor trata de nuevo. 
		Usa de ejemplo este formato: bienvenido.velez@upr.edu
		""";
	
	public final String ERROR_MESSAGE_NOSPACES = 
		"""
		El Email que entraste __*no puede tener espacios*__. Trata de nuevo por favor.
		""";
	
	public final String ERROR_MESSAGE_MUST_CONTAIN = 
		"""
		Recuerda que el Email tiene que ser: __**@upr.edu**__. Trata de nuevo por favor.
		""";
	
	public final String ERROR_MESSAGE_MUST_END = 
		"""
		Recuerda que el Email tiene que acabar con: __**@upr.edu**__. Trata de nuevo por favor.
		""";
	
	public final String ERROR_MESSAGE_NO_GMAIL = 
		"""
		Recuerda que el Email tiene que ser el institucional. __**No puede ser Gmail**__. Trata de nuevo por favor.
		""";
	
	public final String ERROR_MESSAGE_NOT_FOUND = 
		"""
		No encuentro ese email en mis registros. Intenta de nuevo:

		Si tu email no aparece y estas seguro de que eres un prepa
		de **INEL**, **ICOM**, **INSO** o **CIIC**, comunícate con
		cualquier estudiante orientador del servidor de discord. Tienen
		el rol de **Estudiante Orientador** o con un Bot Developer
		""";

}
