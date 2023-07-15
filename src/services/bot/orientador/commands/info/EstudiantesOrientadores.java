/**
 * 
 */
package services.bot.orientador.commands.info;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import application.database.DatabaseConnections;
import application.records.MemberRecord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.commands.CommandI;
import services.bot.orientador.roles.TeamRole;

/**
 * @author Alfredo
 *
 */
public class EstudiantesOrientadores implements CommandI {

	private static final String COMMAND_OPTION = "department";
	private static final String INEL_ID = "INEL-ID";
	private static final String ICOM_ID = "ICOM-ID";
	private static final String INSO_ID = "INSO-ID";
	private static final String CIIC_ID = "CIIC-ID";
	private static final String ININ_ID = "ININ-ID";
	private static final String GERH_ID = "GERH-ID";
	
	private List<OptionData> options;
	
	public EstudiantesOrientadores() {
		this.options = new ArrayList<>();
		
		options.add(new OptionData(OptionType.STRING, COMMAND_OPTION, "Escoje un departamento", true)
			.addChoice("INEL - Electrical Engineering", INEL_ID)
			.addChoice("ICOM - Computer Engineering", ICOM_ID)
			.addChoice("INSO - Software Engineering", INSO_ID)
			.addChoice("CIIC - Computer Science & Engineering", CIIC_ID)
			.addChoice("ININ - Ingeniería Industrial", ININ_ID)
			.addChoice("GERH - Gerencia", GERH_ID)
		);
	}
	
	@Override
	public void dispose() {
		options.clear();
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
		
		OptionMapping programOption = event.getOption(COMMAND_OPTION);
		
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

		List<MemberRecord> records = fetch(program);
		
		if(records.isEmpty()) {
			event.reply(
				"""
				No hay estudiantes orientadores de ese departamento que escogiste. :pensive:
				"""
			).queue();
			return;
		}
		
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Estudiantes Orientadores de " + program);
		embedBuider.setDescription("Aquí están todos los estudiantes orientadores que están estudiando " + department + " como tu!");
		
		for(MemberRecord record : records) {
			embedBuider.addField(record.getFullName(), 
					"\nDiscord user: " + record.getDiscordUser() +
					"\nTeam Name: " + record.getTeam().getName() + 
					"\nDescription: " + record.getBriefInfo(),
				false
			);
		}
		
		event.replyEmbeds(embedBuider.build()).queue();
	}
	
	private List<MemberRecord> fetch(String department) {
		
		List<MemberRecord> matchingRecords = new ArrayList<>();
		
		DatabaseConnections.instance()
		.getTeamMadeConnection()
		.joinConnection(()->{
			
			String SQL = "SELECT FullName, DiscordUser, TeamName, BriefInfo FROM StaffMembers WHERE Department = ? AND LoggedIn = true";
			
			PreparedStatement stmt = DatabaseConnections.instance()
					.getTeamMadeConnection()
					.getConnection()
					.prepareStatement(SQL);
			
			stmt.setString(1, department);
			
			ResultSet result = stmt.executeQuery();
			
			while(result.next()) {
				MemberRecord record = new MemberRecord();
				
				record.setFullName(result.getString(1));
				record.setDiscordUser(result.getString(2));
				record.setTeam(new TeamRole(result.getString(3), Color.black));
				record.setBriefInfo(result.getString(4));
				
				matchingRecords.add(record);
			}
			
			result.close();
			stmt.close();
		});
		
		return matchingRecords;
	}
}
