/**
 * 
 */
package java.assistant.command.information;

import java.service.discord.interaction.CommandI;
import java.service.discord.interaction.InteractionModel;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * @author Alfredo
 *
 */
public class EOInfoCmd extends InteractionModel implements CommandI {

	/*
	 * 
	 * These command features, must be implemented in a json file
	 * 
	 */
	private static final String COMMAND_LABEL = "department";
	
	private static final String INEL_ID = "INEL-ID";
	private static final String ICOM_ID = "ICOM-ID";
	private static final String INSO_ID = "INSO-ID";
	private static final String CIIC_ID = "CIIC-ID";
	private static final String ININ_ID = "ININ-ID";
	private static final String GERH_ID = "GERH-ID";
	
	private boolean isGlobal;
	
	public EOInfoCmd() {
		
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
	public String getCommandName() {
		return "estudiantes-orientadores";
	}

	@Override
	public String getDescription() {
		return "Get EOs de un programa";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of(
			new OptionData(OptionType.STRING, COMMAND_LABEL, "Escoje un departamento", true)
				.addChoice("INEL - Electrical Engineering", INEL_ID)
				.addChoice("ICOM - Computer Engineering",   ICOM_ID)
				.addChoice("INSO - Software Engineering",   INSO_ID)
				.addChoice("CIIC - Computer Science & Engineering", CIIC_ID)
				.addChoice("ININ - Ingeniería Industrial", ININ_ID)
				.addChoice("GERH - Gerencia", GERH_ID)
			);
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		String program = null;
		String department = null;

		switch(programOption.getAsString()) {
		case INEL_ID:
			program = "INEL";
			department = "Ingeniería de Eléctrica"; 
			break;
		case ICOM_ID:
			program = "ICOM";
			department = "Ingeniería de Computadora"; 
			break;
		case INSO_ID:
			program = "INSO";
			department = "Ingeniería de Software";
			break;
		case CIIC_ID:
			program = "CIIC";
			department = "Ciencias e Ingeniería de Computación";
			break;
		case ININ_ID:
			program = "ININ";
			department = "Ingeniería Industrial";
			break;
		case GERH_ID:
			program = "GERH";
			department = "Gerencia";
			break;
		default:
			event.reply("No hay estudiantes orientadores que estén estudiando ese programa :pensive:");
			return;
		}

		// FIXME use DAO to obtain the data
//		List<MemberRecord> records = fetch(program);
		
		event.reply(
			"""
			No hay estudiantes orientadores que estén estudiando ese programa :pensive:
			"""
		).setEphemeral(event.isFromGuild()).queue();
		
//		if(records.isEmpty()) {
//			event.reply(
//				"""
//				No hay estudiantes orientadores que estén estudiando ese programa :pensive:
//				"""
//			).setEphemeral(event.isFromGuild()).queue();
//			return;
//		}
//		
//		EmbedBuilder embedBuider = new EmbedBuilder();
//
//		embedBuider.setColor(new Color(70, 150, 90));
//		embedBuider.setTitle("Estudiantes Orientadores de " + program);
//		embedBuider.setDescription("Aquí están todos los estudiantes orientadores que están estudiando " + department + " como tu!");
//		
//		for(MemberRecord record : records) {
//			embedBuider.addField(record.getFullName(), 
//					"\nDiscord user: " + record.getDiscordUser() +
//					"\nTeam Name: " + record.getTeam().getName() + 
//					"\nDescription: " + record.getBriefInfo(),
//				false
//			);
//		}
//		
//		event.replyEmbeds(embedBuider.build()).setEphemeral(event.isFromGuild()).queue();
	}
}
