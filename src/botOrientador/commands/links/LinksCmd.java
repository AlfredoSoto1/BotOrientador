/**
 * 
 */
package botOrientador.commands.links;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class LinksCmd implements CommandI {

	private boolean isGlobal;
	private List<OptionData> options;
	
	public LinksCmd() {
		this.options = new ArrayList<>();
		
	}
	
	@Override
	public void init(ReadyEvent event) {

	}

	@Override
	public boolean isGlobal() {
		return isGlobal;
	}

	@Override
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	@Override
	public void dispose() {
		options.clear();
	}
	
	@Override
	public String getCommandName() {
		return "links";
	}

	@Override
	public String getDescription() {
		return "Una lista de links importantes";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Links importantes");

		embedBuider.addField(
			"""
			Accesos a Laboratorios de STEFANI
			""",
			"""
			Info para pedir tarjetas de laboratorios del edificio Stefani, para solamente
			estudiantes de concentración: **INEL/ICOM/INSO/CIIC**.
			Los nombres de los labs son: *INCADEL-S105C, CRAI-S105D, AMADEUS-S121 y el S114*
			• https://ece.uprm.edu/instrucciones-para-solicitar-cuentas-yo-tarjetas/
			""", false);
		
		embedBuider.addField(
			"""
			Oficina de COOP de Ingeniería
			""",
			"""
			• https://www.uprm.edu/engineering/coop/
			""", false);

		embedBuider.addField(
			"""
			Departamento de Colocaciones RUM
			""",
			"""
			• https://www.uprm.edu/placement/home
			""", false);

		embedBuider.addField(
			"""
			Consejo General de Estudiantes
			""",
			"""
			• Página Oficial: http://cge.uprm.edu/
			• Twitter: https://twitter.com/cgerum
			""", false);
		
		embedBuider.addField(
			"""
			Guía prepística
			""",
			"""
			Y no olvides consultar la Guía Prepística para más enlaces importantes en la sección
			de __*ENLACES Y CONTACTOS*__. Utiliza el comando ``/guia-prepistica``
			""", false);

		event.replyEmbeds(embedBuider.build()).queue();
	}

}
