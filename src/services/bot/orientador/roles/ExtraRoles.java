/**
 * 
 */
package services.bot.orientador.roles;

/**
 * @author Alfredo
 *
 */
@Deprecated
public class ExtraRoles {

	private boolean prepa;
	private boolean estudianteGraduado;
	private boolean estudianteOrientador;
	private boolean botDeveloper;
	private boolean consejeroProfesional;

	public ExtraRoles(boolean prepa, boolean estudianteGraduado, boolean estudianteOrientador, boolean botDeveloper, boolean consejeroProfesional) {
		this.prepa = prepa;
		this.estudianteGraduado = estudianteGraduado;
		this.estudianteOrientador = estudianteOrientador;
		this.botDeveloper = botDeveloper;
		this.consejeroProfesional = consejeroProfesional;
	}

	public boolean isPrepa() {
		return prepa;
	}

	public boolean isEstudianteGraduado() {
		return estudianteGraduado;
	}

	public boolean isEstudianteOrientador() {
		return estudianteOrientador;
	}

	public boolean isBotDeveloper() {
		return botDeveloper;
	}

	public boolean isConsejeroProfesional() {
		return consejeroProfesional;
	}
}