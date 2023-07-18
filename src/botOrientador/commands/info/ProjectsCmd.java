/**
 * 
 */
package botOrientador.commands.info;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class ProjectsCmd implements CommandI {

	private static final String COMMAND_LABEL = "select-projects";
	
	private static final String OPTION_SELECTED_AIR = "air";
	private static final String OPTION_SELECTED_NASA = "nasa";
	private static final String OPTION_SELECTED_SEDS = "seds";
	private static final String OPTION_SELECTED_SOLAR = "solar";
	private static final String OPTION_SELECTED_ALPHA = "alpha";
	private static final String OPTION_SELECTED_RACING = "racing";
	private static final String OPTION_SELECTED_ROBOBOAT = "boat";
	private static final String OPTION_SELECTED_VANGUARD = "vanguard";
	private static final String OPTION_SELECTED_RUMARINO = "rumarino";
	
	private boolean isGlobal;
	private List<OptionData> options;
	
	public ProjectsCmd() {
		this.options = new ArrayList<>();
		
		this.options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "Escoje un departamento", true)
			.addChoice("RUM-Air", OPTION_SELECTED_AIR)
			.addChoice("NASA RASC-AL", OPTION_SELECTED_NASA)
			.addChoice("RUMarino", OPTION_SELECTED_RUMARINO)
			.addChoice("Alpha Astrum", OPTION_SELECTED_ALPHA)
			.addChoice("Project Vanguard", OPTION_SELECTED_VANGUARD)
			.addChoice("UPRM Roboboat Team", OPTION_SELECTED_ROBOBOAT)
			.addChoice("Colegio Racing Engineering", OPTION_SELECTED_RACING)
			.addChoice("The Solar Engineering Research and Racing Team", OPTION_SELECTED_SOLAR)
			.addChoice("Student for the Exploration and Development of Space", OPTION_SELECTED_SEDS)
		);
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
		return "ls_projects";
	}

	@Override
	public String getDescription() {
		return "Información de los proyectos aqui en el RUM para ustedes de INEL/ICOM/INSO/CIIC";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {

		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		EmbedBuilder embedBuilder = null;
		
		switch(programOption.getAsString()) {
		case OPTION_SELECTED_AIR:
			embedBuilder = createAIREmbed();
			break;
		case OPTION_SELECTED_NASA:
			embedBuilder = createRASCALEmbed();
			break;
		case OPTION_SELECTED_SEDS:
			embedBuilder = createSEDSEmbed();
			break;
		case OPTION_SELECTED_SOLAR:
			embedBuilder = createSERRTEmbed();
			break;
		case OPTION_SELECTED_ALPHA:
			embedBuilder = createALPHAEmbed();
			break;
		case OPTION_SELECTED_RACING:
			embedBuilder = createRACINGEmbed();
			break;
		case OPTION_SELECTED_ROBOBOAT:
			embedBuilder = createRoboboatEmbed();
			break;
		case OPTION_SELECTED_VANGUARD:
			embedBuilder = createVANGUARDEmbed();
			break;
		case OPTION_SELECTED_RUMARINO:
			embedBuilder = createRUMARINOEmbed();
			break;
		default:
			event.reply("Proyecto incorrecto, intenta de nuevo").queue();
			return;
		}
		
		event.replyEmbeds(embedBuilder.build()).queue();
	}
	
	private EmbedBuilder createAIREmbed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("RUM-Air");

		embedBuilder.addField(
			"Descripción",
			"""
			A student team that designs, manufactures and programs
			an autonomous boat and drone. The purpose of this team is to
			encourage female participation by having 50% female members.	
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: roboboat.uprm@gmail.com
			•Website: https://sites.google.com/upr.edu/robo-boat-uprm/home?authuser=0/
			•Facebook: facebook.com/UPRMRoboboat/
			•Instagram: roboboat.uprm	
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createRoboboatEmbed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("UPRM Roboboat Team");

		embedBuilder.addField(
			"Descripción",
			"""
			A student team that designs, manufactures and programs an autonomous boat and drone.
			The purpose of this team is to encourage female participation by having 50% female members.	
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: roboboat.uprm@gmail.com
			•Website: https://sites.google.com/upr.edu/robo-boat-uprm/home?authuser=0/
			•Facebook: facebook.com/UPRMRoboboat/
			•Instagram: roboboat.uprm
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createSERRTEmbed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("The Solar Engineering Research and Racing Team");

		embedBuilder.addField(
			"Descripción",
			"""
			The Solar Engineering Research and Racing Team (SERRT) is one of the flagship engineering projects
			of the University of Puerto Rico at Mayaguez. This team currently develops a solar powered car
			to compete in the American Solar Challenge and Formula Sun Grand Prix.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: serrt.uprm@gmail.com
			•Facebook: facebook.com/Serrt.Uprm/
			•Instagram: serrtuprm
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createALPHAEmbed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Alpha Astrum");

		embedBuilder.addField(
			"Descripción",
			"""
			Alpha Astrum is a non-profit student association at UPRM focused on providing
			students with valuable hands-on experience in the field of rocketry
			and aerospace, as well as expanding the aerospace industry and developing
			sustainable spaceflight capabilities in Puerto Rico.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: alphaastrum@gmail.com
			•Website: https://sites.google.com/view/alphaastrum/home
			•Facebook: facebook.com/alphaastrum2018/
			•Instagram: instagram.com/alphaastrum/
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createRACINGEmbed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Colegio Racing Engineering");

		embedBuilder.addField(
			"Descripción",
			"""
			The main objective of the project is to promote and
			strengthen the passion for engineering and project management,
			among other areas, through the application of the concepts learned in university courses.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: fsae14@uprm.edu
			•Website: https://fsae14.wixsite.com/colegioracing/
			•Facebook: facebook.com/fsae.uprm/
			•Instagram: fsae_uprm
			""", false);
		return embedBuilder;
	}
	
	private EmbedBuilder createRASCALEmbed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("NASA RASC-AL");

		embedBuilder.addField(
			"Descripción",
			"""
			The NASA RASC-AL team from SEDS-UPRM designs and analyzes advanced
			human and robotic space exploration missions that solve
			the greatest challenges in this field.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: wilbert.ruperto@upr.edu
			•Website: https://www.sedsuprm.org/nasa-rasc-al/projects/nasa-rasc-al
			•Facebook: facebook.com/SEDS-UPRM
			•Instagram: instagram.com/seds_uprm
			""", false);
		return embedBuilder;
	}
	
	private EmbedBuilder createVANGUARDEmbed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Project Vanguard");

		embedBuilder.addField(
			"Descripción",
			"""
			An undergraduate research and development team dedicated
			to creating UAV solutions for the betterment of Puerto Rico.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: info@projectvanguardpr.com
			•Facebook: facebook.com/projectvanguardpr/
			•Instagram: projectvanguardpr
			""", false);
		return embedBuilder;
	}
	
	private EmbedBuilder createRUMARINOEmbed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("RUMarino");

		embedBuilder.addField(
			"Descripción",
			"""
			RUMarino is a multidisciplinary team that develops and
			programs its own autonomous underwater vehicle (AUV).
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: rumarino.uprm@gmail.com
			•Website: http://rumarino.com/
			•Facebook: facebook.com/UPRMRUMarino
			•Instagram: rumarino_hydrus
			""", false);
		return embedBuilder;
	}
	
	private EmbedBuilder createSEDSEmbed() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Student for the Exploration and Development of Space");

		embedBuilder.addField(
			"Descripción",
			"""
			Student for the Exploration and Development of Space,
			Mayagüez Chapter, or (SEDS-UPRM) is an interdisciplinary
			student organization focused on space sciences, engineering, and exploration.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: seds.uprm@uprm.edu
			•Website: https://www.sedsuprm.org/
			•Facebook: facebook.com/seds.uprm/
			•Instagram: seds_uprm
			""", false);
		return embedBuilder;
	}
}
