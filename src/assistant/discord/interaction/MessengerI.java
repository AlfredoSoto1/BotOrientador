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
package assistant.discord.interaction;

import java.util.List;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

/**
 * @author Alfredo
 *
 */
public interface MessengerI {

	/**
	 * @return Id of the messenger
	 */
	public List<Long> getMessageID();
	
	/**
	 * @param event
	 */
	public void memberJoin(GuildMemberJoinEvent event);
	
	/**
	 * @param event
	 */
	public void messageReceived(MessageReceivedEvent event);
	
	/**
	 * @param event
	 */
    public void onMessageReaction(GenericMessageReactionEvent event);
}
