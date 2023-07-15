/**
 * 
 */
package services.bot.orientador.commands.info;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.database.DatabaseConnections;
import application.utils.Pair;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.commands.CommandI;

/**
 * @author Alfredo
 *
 */
public class Salon implements CommandI {

	private List<OptionData> options;
	private BlockingQueue<Pair<String, SlashCommandInteractionEvent>> queuedRequests;
	
	public Salon() {
		this.options = new ArrayList<>();
		this.queuedRequests = new LinkedBlockingQueue<>();

		options.add(new OptionData(OptionType.STRING, "salon", "Dime que salón necesitas encontrar", true));
	}
	
	@Override
	public void dispose() {
		options.clear();
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
		return options;
	}

	public boolean hasQueuedQueries() {
		return !queuedRequests.isEmpty();
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		// From the option box provided to the user,
		// Obtain the Classroom given
		OptionMapping programOption = event.getOption("salon");
		
		// Queue the event to later give
		// a response to the user
		queuedRequests.add(new Pair<>(programOption.getAsString(), event));
		
		// Let the bot think until answer is ready to be displayed
		event.deferReply().queue();
		
		// Delete the message so that it stops thinking
		event.getHook().deleteOriginal().queue();
	}

	public void processQueuedQuery() throws SQLException {
		
		String SQLCommand = "SELECT BuildingName, Links FROM GoogleMapPins WHERE code = ?";

		// Prepare a new SQL statement
		PreparedStatement stmt = DatabaseConnections.instance()
				.getTeamMadeConnection()
				.getConnection()
				.prepareStatement(SQLCommand);

		// Iterate over all queued requests
		// and deliver a message to the corresponding user
		// with the correct information
		while (!queuedRequests.isEmpty()) {
			Pair<?, ?> entry = queuedRequests.poll();

			// Obtain the event to send the message
			SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) entry.getValue();
			
			String code = formatClassroom((String) entry.getKey());

			// Set value to prepared statement
			stmt.setString(1, code.toLowerCase());

			// Process results
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				// These values are from database
				// Use them to fill the reply message
				String name = result.getString(1);
				String link = result.getString(2);
				
				event.getHook().sendMessage(
					event.getUser().getAsMention() + ", Es posible que el salón '" + code + "' " +
					"se encuentre en el edificio: *" + name + "* \n" + link
				).queue();
			} else {
				event.getHook().sendMessage(
					event.getUser().getAsMention() + ", No encuentro en mi base de datos el salón '" + (String) entry.getKey() + "' :pensive:"
				).queue();
			}

			// Free results
			result.close();
		}

		// Free resources
		stmt.close();
	}
	
    private String formatClassroom(String salon) {
        String internal_ = salon.replace("-", "");
        Pattern lettersPattern = Pattern.compile("^\\D+");
        Matcher lettersMatcher = lettersPattern.matcher(internal_);
        Pattern numbersPattern = Pattern.compile("\\d+$");
        Matcher numbersMatcher = numbersPattern.matcher(internal_);

        if (numbersMatcher.find() && lettersMatcher.find()) {
            String letters = lettersMatcher.group();
            String numbers = numbersMatcher.group();
            return letters.toUpperCase() + "-" + numbers;
        }

        if (lettersMatcher.find()) {
            String letters = lettersMatcher.group();
            return letters.toUpperCase();
        }

        return salon;
    }
}
