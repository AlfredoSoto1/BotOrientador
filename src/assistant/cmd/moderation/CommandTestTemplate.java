package assistant.cmd.moderation;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandTestTemplate {
	
    public void init() {
    }

    public void dispose() {
    }

    public String getCommandName() {
        return "assistant-verification";
    }

    public String getDescription() {
        return "Sends an embed to verify user to a channel of choice";
    }

    public void execute(SlashCommandInteractionEvent event) {

    }
    
    @RegisterButton(id="verify-button")
    public void verificationButtonClicked() {
    	
    }
}
