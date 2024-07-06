/**
 * 
 */
package assistant.command.moderation;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import assistant.app.core.Logger;
import assistant.app.core.Logger.LogFeedback;
import assistant.discord.interaction.CommandI;
import assistant.discord.interaction.InteractionModel;
import assistant.discord.interaction.MessengerI;
import assistant.discord.object.InteractionState;
import assistant.embeds.moderation.RoleSelectionEmbed;
import assistant.rest.dto.DiscordServerDTO;
import assistant.rest.dto.InteractionStateDTO;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.utils.tuple.Pair;

/**
 * @author Alfredo
 */
public class RoleSelectionCmd extends InteractionModel implements CommandI, MessengerI {

	private RoleSelectionEmbed embed;
	private List<Long> messagesToReact;
	
	private boolean isGlobal;
	private boolean isOnTest;
	
	public RoleSelectionCmd() {
		this.isOnTest = false;
		this.embed = new RoleSelectionEmbed();
		this.messagesToReact = new LinkedList<>();
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
		return "assistant-role-selection";
	}

	@Override
	public String getDescription() {
		return "Assigns embed for displaying custom role selection";
	}

	@Override
	public List<OptionData> getOptions() {
		return List.of(
			new OptionData(OptionType.STRING, "role-selection-channel", "send role selection", true),
			new OptionData(OptionType.STRING, "cache", "manage cache of the selection embed", true)
				.addChoice("test", "test")
				.addChoice("create", "create")
				.addChoice("delete", "delete"));
	}
	
	@Override
	public void onInit(ReadyEvent event) {
		// TODO In the future:
		// Create the emojis if they do not exist
		// Link them with them with the database and roles
		
		for (Guild server : event.getJDA().getGuilds()) {
			// Obtain all the interaction role state previous
			// to this new instance of the bot-application
			List<InteractionStateDTO> states = super.getCacheInteractionStates(InteractionState.REACTON_ROLE_SELECTION, server.getIdLong());
			
			// Populate the message ids as previous states
			for (InteractionStateDTO state : states)
				messagesToReact.add(state.getState());
		}
	}
	
	@Override
	public void execute(SlashCommandInteractionEvent event) {
		if(!super.validateCommandUse(event))
			return;
		
		String roleChanel = event.getOption("role-selection-channel").getAsString();
		String cache = event.getOption("cache").getAsString();
		
		if ("delete".equalsIgnoreCase(cache)) {
			for (long state : messagesToReact)
				super.deleteCacheInteractionStates(state, event.getGuild().getIdLong());
			event.reply("Deleted cached reaction role selection").setEphemeral(true).queue();
			return;
		} else if ("test".equalsIgnoreCase(cache)) {
			isOnTest = true;
		} else {
			isOnTest = false;
		}
		
		try {
			Long.parseLong(roleChanel);
		} catch (NumberFormatException nfe) {
			event.reply("The id provided for the role-selection-channel is not a valid number").setEphemeral(true).queue();
			return;
		}
		
		// Obtain text channel where the role selection will be set on
		Optional<TextChannel> roleTextChannel = Optional.ofNullable(event.getGuild().getTextChannelById(roleChanel));
		
		if(roleTextChannel.isEmpty()) {
			event.reply("Role selection channel not found").setEphemeral(true).queue();
			return;
		}
		
		// Reply message to keep chat alive
		event.reply("Role selection embed sent to channel").setEphemeral(true).queue();
		
		sendBuffet(roleTextChannel.get());
	}
	
	@Override
	public List<Long> getMessageID() {
		return messagesToReact;
	}
	
	@Override
	public void memberJoin(GuildMemberJoinEvent event) {
		// Do nothing here
	}

	@Override
	public void messageReceived(MessageReceivedEvent event) {
		// Do nothing here
	}

	@Override
	public void onMessageReaction(GenericMessageReactionEvent event) {
		
		// Check if the message to be reacted with is registered
		if (!messagesToReact.contains(event.getMessageIdLong()))
			return;
		
		// Handle the proper event from the given generic one
		if (event instanceof MessageReactionAddEvent addEvent) {
			handleAddReaction(addEvent);
		} else if (event instanceof MessageReactionRemoveEvent removeEvent) {
			handleRemoveReaction(removeEvent);
		}
	}
	
	private void handleAddReaction(MessageReactionAddEvent event) {
		// Obtain the role from the selected emoji in server
		Optional<Role> role = getRoleFromSelection(event.getEmoji(), event.getGuild());
		
		// Apply the role if found in server
		if (role.isPresent()) {
			applyRole(event.getGuild(), event.getMember(), role.get());
		} else {
			Logger.instance().logFile(LogFeedback.WARNING, "Role is not present in server");
		}
	}
	
	private void handleRemoveReaction(MessageReactionRemoveEvent event) {
		// Obtain the role from the selected emoji in server
		Optional<Role> role =  getRoleFromSelection(event.getEmoji(), event.getGuild());
		
		// Apply the role if found in server
		if (role.isPresent()) {
			removeRole(event.getGuild(), event.getMember(), role.get());
		}
	}
	
	private void sendBuffet(TextChannel channel) {
		Guild server = channel.getGuild();
		DiscordServerDTO discordServer = super.getServerOwnerInfo(server.getIdLong());
		Color color = Color.decode("#" + discordServer.getColor());
		
		// Build the buffets to be displayed
		Pair<MessageEmbed, Consumer<Message>> coding = embed.buildCodingBuffet(color, server);
		Pair<MessageEmbed, Consumer<Message>> gaming = embed.buildGamingBuffet(color, server);
		Pair<MessageEmbed, Consumer<Message>> entertainment = embed.buildEntertainmentBuffet(color);
		
		BiConsumer<Message, Consumer<Message>> uploadReaction = (message, reactionApply) -> {
			// Register the message id of the embed
			messagesToReact.add(message.getIdLong());
			if (!isOnTest)
				super.cacheUniqueState(InteractionState.REACTON_ROLE_SELECTION, message.getIdLong(), server.getIdLong());
			// Apply reactions to the message
			reactionApply.accept(message);
		};
		
		// Send the embed with the reactions to select from
		channel.sendMessageEmbeds(coding.getLeft())
			.queue(message -> uploadReaction.accept(message, coding.getRight()));
		channel.sendMessageEmbeds(gaming.getLeft())
			.queue(message -> uploadReaction.accept(message, gaming.getRight()));
		channel.sendMessageEmbeds(entertainment.getLeft())
			.queue(message -> uploadReaction.accept(message, entertainment.getRight()));
	}
	
	private void applyRole(Guild server, Member member, Role role) {
	    try {
	    	server.addRoleToMember(member, role).queue(
    			success -> Logger.instance().logFile(LogFeedback.SUCCESS, "Given Role [%s] to [%s]", role.getName(), member.getEffectiveName()));
		} catch (HierarchyException he) {
			Logger.instance().logFile(LogFeedback.WARNING, "Failed to add role [%s] to member [%s]: %s",
					role.getName(), member.getEffectiveName(), he.getMessage());
		} catch (InsufficientPermissionException ipe) {
			Logger.instance().logFile(LogFeedback.WARNING, "Failed to add role [%s] to member [%s]: %s",
					role.getName(), member.getEffectiveName(), ipe.getMessage());
		}
	}
	
	private void removeRole(Guild server, Member member, Role role) {
	    try {
	    	server.removeRoleFromMember(member, role).queue(
    			success -> Logger.instance().logFile(LogFeedback.SUCCESS, "Removed Role [%s] to [%s]", role.getName(), member.getEffectiveName()));
		} catch (HierarchyException he) {
			Logger.instance().logFile(LogFeedback.WARNING, "Failed to add role [%s] to member [%s]: %s",
					role.getName(), member.getEffectiveName(), he.getMessage());
		} catch (InsufficientPermissionException ipe) {
			Logger.instance().logFile(LogFeedback.WARNING, "Failed to add role [%s] to member [%s]: %s",
					role.getName(), member.getEffectiveName(), ipe.getMessage());
		}
	}
	
	private Optional<Role> getRoleFromSelection(EmojiUnion emoji, Guild server) {
		Role role = null;
		
		switch (emoji.getName().toLowerCase()) {
		case "javascript":
			role = server.getRolesByName("js", true).get(0);
			break;
		case "ts":
			role = server.getRolesByName("ts", true).get(0);
			break;
		case "python":
			role = server.getRolesByName("python", true).get(0);
			break;
		case "java":
			role = server.getRolesByName("java", true).get(0);
			break;
		case "c_hashtag":
			role = server.getRolesByName("c#", true).get(0);
			break;
		case "c_":
			role = server.getRolesByName("c", true).get(0);
			break;
		case "cpp":
			role = server.getRolesByName("c++", true).get(0);
			break;
		case "asm":
			role = server.getRolesByName("asm", true).get(0);
			break;
		
        case "fortnite":
            role = server.getRolesByName("Fortnite", true).get(0);
            break;
        case "valorant":
            role = server.getRolesByName("Valorant", true).get(0);
            break;
        case "pokemon":
            role = server.getRolesByName("Pokemon", true).get(0);
            break;
        case "amongus":
            role = server.getRolesByName("AmongUs", true).get(0);
            break;
        case "minecraft":
            role = server.getRolesByName("Minecraft", true).get(0);
            break;
        case "ow":
            role = server.getRolesByName("Overwatch", true).get(0);
            break;
        case "thecompany":
            role = server.getRolesByName("TheCompany", true).get(0);
            break;
        case "lol":
            role = server.getRolesByName("LOL", true).get(0);
            break;
        case "smash":
            role = server.getRolesByName("Super Smash Bros", true).get(0);
            break;
	    case "\uD83D\uDCFA": // :tv:
	        role = server.getRolesByName("Series", true).get(0);
	        break;
	    case "\uD83C\uDFAC": // :clapper:
	        role = server.getRolesByName("Movies Enthusiast", true).get(0);
	        break;
	    case "\uD83C\uDFB5": // :musical_note:
	        role = server.getRolesByName("Music Lover", true).get(0);
	        break;
	    case "\uD83E\uDD21": // :clown:
	        role = server.getRolesByName("I like Memes", true).get(0);
	        break;
	    case "\uD83C\uDFA8": // :art:
	        role = server.getRolesByName("Artist", true).get(0);
	        break;
	    case "\uD83D\uDCBB": // :desktop:
	        role = server.getRolesByName("hardware", true).get(0);
	        break;
	    }
		return Optional.ofNullable(role);
	}
}
