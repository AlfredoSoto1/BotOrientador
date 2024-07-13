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
package assistant.discord.object;

/**
 * @author Alfredo
 */
public enum InteractionState {
	REACTON_ROLE_SELECTION ("reaction_role");
	
	private final String literal;
	
	/**
	 * 
	 * @param literalName
	 */
	private InteractionState(String literal) {
		this.literal = literal;
	}
	
	/**
	 * @return literal name of the interaction state
	 */
	public String getLiteral() {
		return literal;
	}
	
	public static InteractionState asInteraction(String interaction) {
		for(InteractionState is : InteractionState.values())
			if(is.literal.equalsIgnoreCase(interaction))
				return is;
		return null;
	}
}
