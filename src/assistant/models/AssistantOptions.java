/**
 * 
 */
package assistant.models;

/**
 * @author Alfredo
 */
public enum AssistantOptions {
	NONE         ("none"),
	DISCONNECT   ("disconnect"),
	REGISTRATION ("register");
	
	private final String option;
	
	private AssistantOptions(String option) {
		this.option = option;
	}
	
	public String getOption() {
		return option;
	}
	
	public static AssistantOptions asOption(String option) {
		for(AssistantOptions v : AssistantOptions.values())
			if (v.getOption().equals(option))
				return v;
		return AssistantOptions.NONE;
	}
}
