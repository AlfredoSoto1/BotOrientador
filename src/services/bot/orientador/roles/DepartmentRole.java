/**
 * 
 */
package services.bot.orientador.roles;

import java.awt.Color;

/**
 * @author Alfredo
 *
 */
@Deprecated
public class DepartmentRole {
	
	private String name;
	private Color color;

	public DepartmentRole(String name, Color color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepartmentRole other = (DepartmentRole) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
