/**
 * 
 */
package assistant.rest.dto;

import assistant.discord.object.InteractionState;

/**
 * @author Alfredo
 */
public class InteractionStateDTO {
	
	private long state;
	private long serverId;
	private InteractionState type;
	
	public InteractionStateDTO() {

	}

	public long getState() {
		return state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public InteractionState getType() {
		return type;
	}

	public void setType(InteractionState type) {
		this.type = type;
	}
}
