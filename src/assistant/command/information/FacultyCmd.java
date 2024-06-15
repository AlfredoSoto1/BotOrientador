/**
 * 
 */
package assistant.command.information;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import service.discord.interaction.CommandI;
import service.discord.interaction.InteractionModel;

/**
 * @author Alfredo
 *
 */
public class FacultyCmd extends InteractionModel implements CommandI {

	private static final String COMMAND_LABEL = "department";
	
	private static final String OPTION_SELECTED_INEL_ICOM = "INEL/ICOM";
	private static final String OPTION_SELECTED_INSO_CIIC = "INSO/CIIC";
	
	private boolean isGlobal;
	
	public FacultyCmd() {
		
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
		return "facultad";
	}

	@Override
	public String getDescription() {
		return "Información acerca de nuestra facultad";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of(
			new OptionData(OptionType.STRING, COMMAND_LABEL, "Escoje un departamento", true)
				.addChoice("INEL/ICOM - Department", OPTION_SELECTED_INEL_ICOM)
				.addChoice("INSO/CIIC - Department", OPTION_SELECTED_INSO_CIIC)
			);
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		/*
		 * Check if the request is done in a specific server
		 */
		
		OptionMapping programOption = event.getOption(COMMAND_LABEL);
		
		EmbedBuilder embedBuilder = null;
		
		switch(programOption.getAsString()) {
		case OPTION_SELECTED_INEL_ICOM: embedBuilder = createINEL_Embed(); 
			break;
		case OPTION_SELECTED_INSO_CIIC: embedBuilder = createINSOCIIC_Embed(); 
			break;
		default:
			event.reply("Departamento incorrecto, intenta de nuevo").queue();
			return;
		}
		
		event.replyEmbeds(embedBuilder.build()).setEphemeral(event.isFromGuild()).queue();
	}
	
	private EmbedBuilder createINEL_Embed() {
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Facultad de ECE - INEL/ICOM");

		embedBuider.addField(
			"Gerson Beauchamp",
			"""
			Full Time Professor
			gerson.beauchamp@upr.edu
			""", true);
		
		embedBuider.addField(
			"Guillermo Serrano",
			"""
			Full Time Professor
			guillermo.serrano.@upr.edu
			""", true);
		
		embedBuider.addField(
			"Hamed Parsiani Gobadi",
			"""
			Full Time Professor
			hamed.parsiani@upr.edu
			""", true);

		embedBuider.addField(
			"Henrick Ierick",
			"""
			Full Time Professor
			henrick.ierick@upr.edu
			""", true);
		
		embedBuider.addField(
			"Isidoro Couvertier",
			"""
			Full Time Professor
			isidoro.couvertiero@upr.edu
			""", true);
		
		embedBuider.addField(
			"Jose Cedeño",
			"""
			Full Time Professor
			jose.cedeno3@upr.edu
			""", true);
		
		embedBuider.addField(
			"Manuel Jimenez",
			"""
			Full Time Professor
			manuel.jimenez@upr.edu
			""", true);
		
		embedBuider.addField(
			"Nayda Santiago Santiago",
			"""
			Full Time Professor
			nayda.santiago@upr.edu
			""", true);
		
		embedBuider.addField(
			"Rogelio Palomera",
			"""
			Full Time Professor
			rogelio.palomera@upr.edu
			""", true);
		
		embedBuider.addField(
			"Shawn David Hunt",
			"""
			Full Time Professor
			shawndavid.hunt@upr.edu
			""", true);
		
		embedBuider.addField(
			"Para más contactos de Facultad",
			"https://ece.uprm.edu/people/faculty/#cn-top", false);
		
		return embedBuider;
	}

	private EmbedBuilder createINSOCIIC_Embed() {
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle("Facultad de CSE - INSO/CIIC");

		embedBuider.addField(
			"Bienvenido Velez Rivera",
			"""
			Acting Dean of Engineering
			Full Time Professor 
			bienvenido.velez@upr.edu	
			""", true);
		
		embedBuider.addField(
			"Emmanuel Arzuaga Cruz",
			"""
			Associate Director
			Full Time Professor
			earzuaga@ece.uprm.edu	
			""", true);	
		
		embedBuider.addField(
			"Heidy Sierra Gil",
			"""
			Associate Professor
			heidy.sierra1@upr.edu
			""", true);	
		
		embedBuider.addField(
			"Jose L. Melendez",
			"""
			Special Assistant to the Chancellor
			Full Time Professor
			jose.melendez37@upr.edu
			""", true);	
		
		embedBuider.addField(
			"Kejie Lu",
			"""
			Full Time Professor
			kejie.lu@upr.edu
			""", true);	
		
		embedBuider.addField(
			"Manuel Rodriguez Martinez",
			"""
			Full Time Professor
			manuel.rodriguez7@upr.edu
			""", true);			
		
		embedBuider.addField(
			"Marko Schütz Schmuck",
			"""
			Full Time Professor
			marko.schutz@upr.edu
			""", true);			
		
		embedBuider.addField(
			"Pedro I. Rivera Vega",
			"""
			Acting CSE Director
			Full Time Professor
			p.rivera@upr.edu
			""", true);		
		
		embedBuider.addField(
			"Wilson Rivera Gallego",
			"""
			Full Time Professor
			wilson.riveragallego@upr.edu
			""", true);		
		
		embedBuider.addField(
			"Para más contactos de Facultad",
			"https://www.uprm.edu/cse/faculty/", false);	

		return embedBuider;
	}

}
