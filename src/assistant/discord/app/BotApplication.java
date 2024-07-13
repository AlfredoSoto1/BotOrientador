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
package assistant.discord.app;

import java.util.EnumSet;
import java.util.concurrent.CountDownLatch;

import assistant.app.settings.TokenHolder;
import assistant.discord.core.ListenerAdapterManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

/**
 * 
 */
public abstract class BotApplication {
	
	private JDA jdaConstructed;
	private JDABuilder jdaBuilder;
	private ListenerAdapterManager listenerManager;

	private CountDownLatch latch;
	
	// Make this later be saved on a config file
	private EnumSet<GatewayIntent> intents = EnumSet.of(
		GatewayIntent.GUILD_MEMBERS, 
		GatewayIntent.GUILD_WEBHOOKS,
		GatewayIntent.GUILD_MESSAGES,
		GatewayIntent.GUILD_PRESENCES,
		GatewayIntent.MESSAGE_CONTENT,
		GatewayIntent.GUILD_MODERATION,
		GatewayIntent.GUILD_MESSAGE_TYPING,
		GatewayIntent.GUILD_MESSAGE_REACTIONS,
		GatewayIntent.GUILD_EMOJIS_AND_STICKERS
	);
	
	/**
	 * Shuts down all live components that
	 * the application is currently handling
	 */
	protected abstract void dispose();

	/**
	 * 
	 * @param listener
	 */
	protected abstract void prepareListeners(ListenerAdapterManager listener);
	
	/**
	 * 
	 * @param token
	 */
	public BotApplication(TokenHolder botToken) {
		// Create a count down latch of 1 unit/time.
		// This is for when the bot gets shutted down it
		// has one unit time to safely terminate itself and
		// clean all memory used by the application.
		latch = new CountDownLatch(1);
		
		jdaBuilder = TokenHolder.buildJDAFromToken(this.getClass(), botToken);
		
		/*
		 * Set bot status and activity
		 * 
		 * TODO: This needs to be separated in a different method
		 */
		jdaBuilder.setStatus(OnlineStatus.ONLINE);
		// TODO: set custom activity
//		jdaBuilder.setActivity(Activity.playing("Your personal assistant here to serve!"));
		
		// Give the bot the necessary permissions
		jdaBuilder.enableIntents(intents);
		
		// Cache all members from guild
		// THIS NEEDS TO BE REMOVED IN THE FUTURE TO REDUCE MEMORY COST
		jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
	}
	
	/**
	 * Starts the bot and other services
	 */
	public void start() {
		// Create a new listener manager to handle the I/O
		// from any Discord server from any user, command, message, etc...
		listenerManager = new ListenerAdapterManager(latch);
		
		// Pass the listener manager to handle by custom bot
		// This lets the programmer have more freedom when developing a custom bot
		prepareListeners(listenerManager);
		
		// Add to the event listener system, all
		// listeners registered by the application that the
		// bot can work on.
		jdaBuilder.addEventListeners(listenerManager);
		
		// Build and start
		try {
			jdaConstructed = jdaBuilder.build().awaitReady();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return; // Exit application
		}
		
		// Awaits for the latch to open. This occurs
		// when the bot get shutdown.
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ends the bot and cleans all
	 * the data used in memory
	 */
	public void shutdown() {
		// Shuts down the jda constructed
		jdaConstructed.shutdown();
		
		// Free all allocated data related to
		// the sub class custom bot application.
		dispose();
	}
	
	public JDA getJDA() {
		return jdaConstructed;
	}
}
