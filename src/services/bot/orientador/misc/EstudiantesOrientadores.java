/**
 * 
 */
package services.bot.orientador.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.commands.CommandI;

/**
 * @author Alfredo
 *
 */
public class EstudiantesOrientadores implements CommandI {

	private List<OptionData> options;
	
	public EstudiantesOrientadores() {
		this.options = new ArrayList<>();
		
		options.add(new OptionData(OptionType.STRING, "program", "Escoje un programa de estudio", true)
			.addChoice("INEL - Electrical Engineering", "INEL")
			.addChoice("ICOM - Computer Engineering", "ICOM")
			.addChoice("INSO - Software Engineering", "INSO")
			.addChoice("CIIC - Computer Science & Engineering", "CIIC")
		);
	}
	
	@Override
	public String getCommandName() {
		return "estudiantes-orientadores";
	}

	@Override
	public String getDescription() {
		return "Get EOs de un programa";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		OptionMapping programOption = event.getOption("program");
		
		String program = null;

		switch(programOption.getAsString()) {
		case "INEL":program = "Ingeniería de Eléctrica"; 
			break;
		case "ICOM":program = "Ingeniería de Computadora"; 
			break;
		case "INSO":program = "Ingeniería de Software";
			break;
		case "CIIC":program = "Ciencias e Ingeniería de Computación";
			break;
		default:
			event.reply("No hay estudiantes orientadores que estén estudiando ese programa :pensive:");
			return;
		}
		
		EmbedBuilder embedBuider = new EmbedBuilder();
		
		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Estudiantes Orientadores de " + programOption.getAsString());
		embedBuider.setDescription("Aquí están todos los estudiantes orientadores que están estudiando " + program + " como tu!");
		
		embedBuider.addField("Nombre: ", "Username: ", false);
		embedBuider.addField("Nombre: ", "Username: ", false);
		embedBuider.addField("Nombre: ", "Username: ", false);
		embedBuider.addField("Nombre: ", "Username: ", false);
		
		event.replyEmbeds(embedBuider.build()).queue();
	}
}
