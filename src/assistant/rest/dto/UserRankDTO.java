/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class UserRankDTO {
	
	private int userXP;
	private int milestone;
	private int level;
	private int commandsUsed;
	private long rank;
	private String username;
	
	private boolean hasLevelup;
	
	public UserRankDTO() {

	}

	public boolean isHasLevelup() {
		return hasLevelup;
	}

	public void setHasLevelup(boolean hasLevelup) {
		this.hasLevelup = hasLevelup;
	}

	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
	}

	public int getUserXP() {
		return userXP;
	}

	public void setUserXP(int userXP) {
		this.userXP = userXP;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getMilestone() {
		return milestone;
	}

	public void setMilestone(int milestone) {
		this.milestone = milestone;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCommandsUsed() {
		return commandsUsed;
	}

	public void setCommandsUsed(int commandsUsed) {
		this.commandsUsed = commandsUsed;
	}
}
