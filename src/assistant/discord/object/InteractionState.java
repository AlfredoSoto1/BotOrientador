/**
 * 
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
