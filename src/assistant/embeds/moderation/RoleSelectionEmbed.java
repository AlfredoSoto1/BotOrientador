/**
 * 
 */
package assistant.embeds.moderation;

import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.internal.utils.tuple.Pair;

/**
 * @author Alfredo
 */
public class RoleSelectionEmbed {
	
	public RoleSelectionEmbed() {

	}
	
	public Pair<MessageEmbed, Consumer<Message>> buildGamingBuffet(Color color, Guild server) {
		List<RichCustomEmoji> reactionEmojis = List.of(
			server.getEmojisByName("fortnite",   true).get(0),
			server.getEmojisByName("valorant",   true).get(0),
			server.getEmojisByName("pokemon",    true).get(0),
			server.getEmojisByName("amongus",    true).get(0),
			server.getEmojisByName("minecraft",  true).get(0),
			server.getEmojisByName("ow",         true).get(0),
			server.getEmojisByName("thecompany", true).get(0),
			server.getEmojisByName("lol",        true).get(0),
			server.getEmojisByName("smash",      true).get(0)
		);
		
		List<String> emojiMentions = reactionEmojis.stream()
							            .map(RichCustomEmoji::getAsMention)
							            .collect(Collectors.toList());
		
		MessageEmbed embed = new EmbedBuilder()
			.setColor(color)
			.setTitle(":video_game: Gaming Role Selection :joystick:")
			.setFooter("React below to choose your favorite games you play")
			.addField("", String.format(
				"""
				> %s Fortnite
	            > %s Valorant
	            > %s Pokémon
	            > %s Among Us
	            > %s Minecraft
	            > %s Overwatch
	            > %s The Lethal Company
	            > %s League of Legends
	            > %s Super Smash Bros
				""", emojiMentions.toArray()), false)
			.build();
		
	    Consumer<Message> consumer = message -> {
	    	reactionEmojis.forEach(emoji -> message.addReaction(emoji).queue());
	    };
		return Pair.of(embed, consumer);
	}

	public Pair<MessageEmbed, Consumer<Message>> buildEntertainmentBuffet(Color color) {
	    // Unicode representations of the emojis
	    List<String> reactionEmojis = List.of(
	        "\uD83D\uDCFA", // :tv:
	        "\uD83C\uDFAC", // :clapper:
	        "\uD83C\uDFB5", // :musical_note:
	        "\uD83E\uDD21", // :clown:
	        "\uD83C\uDFA8", // :art:
	        "\uD83D\uDCBB"  // :desktop:
	    );
	    
	    MessageEmbed embed = new EmbedBuilder()
			.setColor(color)
			.setTitle(":popcorn: Entertainment Role Selection :tv:")
			.setFooter("React below to choose your favorite entertainment topic")
			.addField("", String.format(
				"""
				> %s Series
				> %s Movies Enthusiast
				> %s Music Lover
				> %s Meme Lord 
				> %s Artist
				> %s Techie
				""", reactionEmojis.toArray()), false)
			.build();
	    
	    Consumer<Message> consumer = message -> {
	    	reactionEmojis.forEach(emoji -> message.addReaction(Emoji.fromFormatted(emoji)).queue());
	    };
	    return Pair.of(embed, consumer);
	}
	
}
