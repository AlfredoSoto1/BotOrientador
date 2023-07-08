/**
 * 
 */
package application.admin.discord.roles;

import java.awt.Color;

/**
 * @author Alfredo
 *
 */
public class TeamRole {

	private String name;
	private Color color;

	public TeamRole(String name, Color color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}
}
