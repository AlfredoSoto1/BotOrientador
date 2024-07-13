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

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

/**
 * @author Alfredo
 *
 */
public interface ButtonActionEvent {
	/**
	 * This gets called when the button gets clicked
	 * 
	 * @param event
	 */
	public void onAction(ButtonInteractionEvent event);
}
