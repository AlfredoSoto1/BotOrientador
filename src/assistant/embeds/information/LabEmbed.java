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
package assistant.embeds.information;

import java.awt.Color;
import java.util.List;

import assistant.rest.dto.LabDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Alfredo
 */
public class LabEmbed {
	
	public MessageEmbed buildLab(Color color, String code, List<LabDTO> labs) {
		if (labs.isEmpty()) {
			return new EmbedBuilder()
				.setColor(color)
				.setTitle("Oh No! :fearful:")
				.setDescription(
					"""
					No se ha podido encontrar laboratorios de estudio en este edificio!
					404 :face_with_spiral_eyes: 	
					""")
				.build();
		}
		
		
		EmbedBuilder embed = new EmbedBuilder()
			.setColor(color)
			.setTitle("Aquí estan los labs que pude encontrar para " + code.toUpperCase())
			.setDescription(
				"""
				Estos labs son salones de estudios y en ellos se puede dar clase también.	
				""");
		
		for (LabDTO lab : labs) {
			embed.addField(lab.getName(), String.format(
				"""
				> Lab code: %s
				> Building: %s
				""", 
				lab.getCode(),
				lab.getBuildingName()), false);
		}
		
		return embed.build();
	}
}
