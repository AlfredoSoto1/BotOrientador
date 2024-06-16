/**
 * 
 */
package assistant.command.information;

import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import service.discord.interaction.CommandI;
import service.discord.interaction.InteractionModel;

/**
 * @author Alfredo
 *
 */
public class FindBuildingCmd extends InteractionModel implements CommandI {

	private static final String COMMAND_LABEL = "location";
	
	private boolean isGlobal;
	
	public FindBuildingCmd() {
		
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
		return "salon";
	}

	@Override
	public String getDescription() {
		return "Obten información acerca de donde queda un salón en el recinto";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of(
			new OptionData(OptionType.STRING, COMMAND_LABEL, "Dispone la localización del edificio en el que se encuentra", true));
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		
		
//		// Obtain from executed command the values inserted as
//		// parameters when member wrote the command
//		OptionMapping commandOptions = event.getOption(COMMAND_LABEL);
//		
//		// Extract the value written and treat it as the building code
//		String buildingCode = commandOptions.getAsString();
//		
//		// Extract the code once formatted
////		Optional<String> code = dbBuildingManager.formatCode(buildingCode);
//		
//		// Look for the building pin inside the table with all
//		// the pins uploaded from database initially
////		Optional<GoogleMapPin> building = dbBuildingManager.getBuilding(code.get());
//		
//		if(building.isPresent()) {
//			// If the google pin is present, reply
//			// to the member the information of where is the building located
//			// with the given building-code as target
//			event.reply(
//				event.getUser().getAsMention() + 
//				", Es posible que el salón '" + buildingCode + "' " +  
//				"se encuentre en el edificio: *" + building.get().getName() +
//				"* \n" + building.get().getLink()
//			)
//			// This message can only be seen by the one
//			// who wrote the command. This is to avoid command
//			// cluttering in the channel
//			.setEphemeral(event.isFromGuild())
//			.queue();
//			
//		} else if(code.get().contains(",")) {
//			event.reply(
//				// If the google pin is not present in the database, reply to
//				// the member other alternatives on how to fix his problem.
//				event.getUser().getAsMention() +
//				", No encuentro en mi base de datos el salón '" + buildingCode + "' :pensive:\n" + 
//				"Quizás te referías a unos de estos: __**" + code.get() + "**__"
//			)
//			// This message can only be seen by the one
//			// who wrote the command. This is to avoid command
//			// cluttering in the channel
//			.setEphemeral(event.isFromGuild())
//			.queue();
//		} else {
//			event.reply(
//				// If the google pin is not present in the database, reply to
//				// the member other alternatives on how to fix his problem.
//				event.getUser().getAsMention() +
//				", No encuentro en mi base de datos el salón '" + buildingCode + "' :pensive:\n" + 
//				"""
//				Si entiendes que el salón que buscas está correctamente escrito, notifícale
//				a un estudiante orientador o a un Bot-Developer para que pueda atender el asunto.		
//				"""
//			)
//			// This message can only be seen by the one
//			// who wrote the command. This is to avoid command
//			// cluttering in the channel
//			.setEphemeral(event.isFromGuild())
//			.queue();
//		}
		
		event.reply(
			// If the google pin is not present in the database, reply to
			// the member other alternatives on how to fix his problem.
			event.getUser().getAsMention() +
			", No encuentro en mi base de datos el salón <unknown:working progress> :pensive:\n" + 
			"""
			Si entiendes que el salón que buscas está correctamente escrito, notifícale
			a un estudiante orientador o a un Bot-Developer para que pueda atender el asunto.		
			"""
		)
		// This message can only be seen by the one
		// who wrote the command. This is to avoid command
		// cluttering in the channel
		.setEphemeral(event.isFromGuild())
		.queue();
	}
}
