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
