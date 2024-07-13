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

import assistant.discord.object.MemberProgram;

/**
 * @author Alfredo
 */
public class MemberDTO {
	
	private int id;
	private int userId;
	
	// Member primary info
	private String firstname;
	private String lastname;
	private String initial;
	private String sex;
	
	// Member unique info
	private String email;
	private String username;
	private String funfact;
	private MemberProgram program;
	
	// Verification status
	private boolean verified;
	
	public MemberDTO() {

	}
	
	@Override
	public String toString() {
		return "MemberDTO [id=" + id + ", userId=" + userId + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", initial=" + initial + ", sex=" + sex + ", email=" + email + ", username=" + username + ", funfact="
				+ funfact + ", program=" + program + ", verified=" + verified + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public MemberProgram getProgram() {
		return program;
	}

	public void setProgram(MemberProgram program) {
		this.program = program;
	}

	public String getFunfact() {
		return funfact;
	}

	public void setFunfact(String funfact) {
		this.funfact = funfact;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
}
