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

import java.util.LinkedList;
import java.util.List;

/**
 * @author Alfredo
 */
public class ContactDTO {
	
	private int id;
	private String email;
	
	private List<ExtensionDTO> extensions;
	private List<WebpageDTO> webpages;
	private List<SocialMediaDTO> socialmedias;
	
	public ContactDTO() {
		this.extensions = new LinkedList<>();
		this.webpages = new LinkedList<>();
		this.socialmedias = new LinkedList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<ExtensionDTO> getExtensions() {
		return extensions;
	}

	public void addExtensions(ExtensionDTO extension) {
		this.extensions.add(extension);
	}

	public List<WebpageDTO> getWebpages() {
		return webpages;
	}

	public void addWebpages(WebpageDTO webpage) {
		this.webpages.add(webpage);
	}

	public List<SocialMediaDTO> getSocialmedias() {
		return socialmedias;
	}

	public void addSocialmedias(SocialMediaDTO socialmedia) {
		this.socialmedias.add(socialmedia);
	}
}
