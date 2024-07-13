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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alfredo
 */
public class ProjectDTO {
	
	private int id;
	private String name;
	private String description;
	private String email;
	private String website;
	private List<String> platforms;
	private List<String> urlhandle;
	
	public ProjectDTO() {
		this.platforms = new ArrayList<>();
		this.urlhandle = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "ProjectDTO [id=" + id + ", name=" + name + ", description=" + description + ", email=" + email
				+ ", website=" + website + ", platforms=" + platforms + ", urlhandle=" + urlhandle + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<String> getPlatforms() {
		return platforms;
	}

	public void addPlatforms(String platform) {
		this.platforms.add(platform);
	}

	public List<String> getUrlhandle() {
		return urlhandle;
	}

	public void addUrlhandle(String urlhandle) {
		this.urlhandle.add(urlhandle);
	}
	
}
