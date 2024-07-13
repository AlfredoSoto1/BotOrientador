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
public enum MemberProgram {
	// Department - level
	INEL("INEL"),
	ICOM("ICOM"),
	INSO("INSO"),
	CIIC("CIIC"),
	ININ("ININ"),
	FISI("FISI"),
	ECON("ECON"),
	GERH("GERH");
	
	/**
	 * Establish literal name that is used
	 * in database for reference. For more details
	 * look for the MemberRole table in schema.
	 */
	private final String literalName;
	
	/**
	 * 
	 * @param literalName
	 */
	private MemberProgram(String literalName) {
		this.literalName = literalName;
	}
	
	/**
	 * @return literal name of the member role
	 */
	public String getLiteral() {
		return literalName;
	}
	
	public static MemberProgram asProgram(String program) {
		for(MemberProgram p : MemberProgram.values())
			if(p.literalName.equalsIgnoreCase(program))
				return p;
		if(program.equalsIgnoreCase("0509 - BACHELOR OF SCIENCE IN SOFTWARE ENGINEERING"))
			return INSO;
		if(program.equalsIgnoreCase("0508 - BACHELOR OF SCIENCE IN COMPUTER SCIENCES AND ENGINEERING"))
			return CIIC;
		if(program.equalsIgnoreCase("0502 - BACHELOR OF SCIENCES IN ELECTRICAL ENGINEERING"))
			return INEL;
		if(program.equalsIgnoreCase("0507 - BACHELOR OF SCIENCES IN COMPUTER ENGINEERING"))
			return ICOM;
		return null;
	}	
	
	public static boolean isProgram(String program) {
		for(MemberProgram p : MemberProgram.values())
			if(p.literalName.equalsIgnoreCase(program))
				return true;
		return false;
	}	
}
