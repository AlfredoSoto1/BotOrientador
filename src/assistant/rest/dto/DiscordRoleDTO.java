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
public class DiscordRoleDTO {
	
	private int id;
	private long roleid;
	private long serverid;        // This is the actual server id from discord
	private String name;          // This is the name that appears on the discord server
	private String effectivename; // This is the global name that we want to refer to this role in this application
	
	public DiscordRoleDTO() {
	
	}

	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getServerid() {
		return serverid;
	}

	public void setServerid(long serverid) {
		this.serverid = serverid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEffectivename() {
		return effectivename;
	}

	public void setEffectivename(String effectivename) {
		this.effectivename = effectivename;
	}
	
}
