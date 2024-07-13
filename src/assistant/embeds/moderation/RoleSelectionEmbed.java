/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
	
	public Pair<MessageEmbed, Consumer<Message>> buildCodingBuffet(Color color, Guild server) {
		List<RichCustomEmoji> reactionEmojis = List.of(
			server.getEmojisByName("JavaScript", true).get(0),
			server.getEmojisByName("TS",         true).get(0),
			server.getEmojisByName("Python",     true).get(0),
			server.getEmojisByName("Java",       true).get(0),
			server.getEmojisByName("C_hashtag",  true).get(0),
			server.getEmojisByName("C_",         true).get(0),
			server.getEmojisByName("CPP",        true).get(0),
			server.getEmojisByName("ASM",        true).get(0)
		);
		
		List<String> emojiMentions_group1 = reactionEmojis.subList(0, 3)
										.stream()
										.map(RichCustomEmoji::getAsMention)
										.collect(Collectors.toList());
		
		List<String> emojiMentions_group2 = reactionEmojis.subList(3, 5)
										.stream()
										.map(RichCustomEmoji::getAsMention)
										.collect(Collectors.toList());
		
		List<String> emojiMentions_group3 = reactionEmojis.subList(5, 8)
										.stream()
							            .map(RichCustomEmoji::getAsMention)
							            .collect(Collectors.toList());
		
		MessageEmbed embed = new EmbedBuilder()
			.setColor(color)
			.setTitle("`// Programming Langauges Role Selection`")
			.setFooter("React below to choose your favorite programming language")
			.addField("", String.format(
				"""
				> %s Java Script
				> \u200B
	            > %s Type Script
				> \u200B
	            > %s Python
				""", emojiMentions_group1.toArray()), true)
			.addField("", String.format(
				"""
	            > %s Java
	            > \u200B
	            > \u200B
	            > %s C#
	            > \u200B
				""", emojiMentions_group2.toArray()), true)
			.addField("", String.format(
				"""
	            > %s C
	            > \u200B
	            > %s C++
	            > \u200B
	            > %s Assembly
				""", emojiMentions_group3.toArray()), true)
			.build();
		
	    Consumer<Message> consumer = message -> {
	    	reactionEmojis.forEach(emoji -> message.addReaction(emoji).queue());
	    };
		return Pair.of(embed, consumer);
	}
	
	public Pair<MessageEmbed, Consumer<Message>> buildGamingBuffet(Color color, Guild server) {
		List<RichCustomEmoji> reactionEmojis = List.of(
			server.getEmojisByName("fortnite",   true).get(0),
			server.getEmojisByName("valorant",   true).get(0),
			server.getEmojisByName("ow",         true).get(0),
			server.getEmojisByName("pokemon",    true).get(0),
			server.getEmojisByName("lol",        true).get(0),
			server.getEmojisByName("smash",      true).get(0),
			server.getEmojisByName("amongus",    true).get(0),
			server.getEmojisByName("minecraft",  true).get(0),
			server.getEmojisByName("thecompany", true).get(0)
		);
		
		List<String> emojiMentions_group1 = reactionEmojis.subList(0, 3)
										.stream()
										.map(RichCustomEmoji::getAsMention)
										.collect(Collectors.toList());
		
		List<String> emojiMentions_group2 = reactionEmojis.subList(3, 6)
										.stream()
										.map(RichCustomEmoji::getAsMention)
										.collect(Collectors.toList());
		
		List<String> emojiMentions_group3 = reactionEmojis.subList(6, 9)
										.stream()
							            .map(RichCustomEmoji::getAsMention)
							            .collect(Collectors.toList());
		
		MessageEmbed embed = new EmbedBuilder()
			.setColor(color)
			.setTitle(":video_game: Gaming Role Selection :joystick:")
			.setFooter("React below to choose your favorite games you play")
			.addField("", String.format(
				"""
				> %s Fortnite
				> \u200B
	            > %s Valorant
	            > \u200B
	            > %s Overwatch
				""", emojiMentions_group1.toArray()), true)
			.addField("", String.format(
				"""
	            > %s Pokémon
	            > \u200B
	            > %s League of Legends
	            > \u200B
	            > %s Super Smash Bros
				""", emojiMentions_group2.toArray()), true)
			.addField("", String.format(
				"""
	            > %s Among Us
	            > \u200B
	            > %s Minecraft
	            > \u200B
	            > %s The Lethal Company
				""", emojiMentions_group3.toArray()), true)
			.build();
		
	    Consumer<Message> consumer = message -> {
	    	reactionEmojis.forEach(emoji -> message.addReaction(emoji).queue());
	    };
		return Pair.of(embed, consumer);
	}

	public Pair<MessageEmbed, Consumer<Message>> buildEntertainmentBuffet(Color color) {
	    // Unicode representations of the emojis
	    List<String> reactionEmojis = List.of(
	        "\uD83D\uDCFA", // :tv:            // series
	        "\uD83C\uDFAC", // :clapper:       // movies
	        "\uD83E\uDD21", // :clown:         // memes
	        "\uD83D\uDCBB", // :desktop:       // techie
	        "\uD83C\uDFA8", // :art:           // art
	        "\uD83C\uDFB5"  // :musical_note:  // music
	    );
	    
	    MessageEmbed embed = new EmbedBuilder()
			.setColor(color)
			.setTitle(":popcorn: Entertainment Role Selection :tv:")
			.setFooter("React below to choose your favorite entertainment topic")
			.addField("", String.format(
				"""
				> %s Series
				> \u200B
				> %s Movies Enthusiast
				> \u200B
				> %s Meme Lord 
				""", reactionEmojis.subList(0, 3).toArray()), true)
			.addField("", String.format(
				"""
				> %s Techie
				> \u200B
				> %s Artist
				> \u200B
				> %s Music Lover
				""", reactionEmojis.subList(3, 6).toArray()), true)
			.build();
	    
	    Consumer<Message> consumer = message -> {
	    	reactionEmojis.forEach(emoji -> message.addReaction(Emoji.fromFormatted(emoji)).queue());
	    };
	    return Pair.of(embed, consumer);
	}
}
