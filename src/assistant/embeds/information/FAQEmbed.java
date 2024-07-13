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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

/**
 * @author Alfredo
 */
public class FAQEmbed {
	
	public MessageEmbed buildFAQ(Color color, String banner, String bdeRole, String esoRole, int page) {
		
		List<Field> fields = getFieldPage(bdeRole, esoRole);
		
		int fieldsPerPage = 4;
		int maxPage = fields.size() / fieldsPerPage;
		
		EmbedBuilder embed = new EmbedBuilder()
			.setColor(color)
			.setImage(banner)
			.setTitle("Frequently Asked Questions")
			.setFooter(page + " of "+ maxPage);
		
		if (page > maxPage) {
			embed.addField("Oh no! 404", String.format(
				"""
				Hmm creo que no hay más preguntas por acá,
				si no encuentras una pregunta que entiendes que deberia
				de estar aquí, por favor contacta a un estudiante orientador.
				Trata con un rango de páginas de [0-%s]
				""", maxPage), false);
			return embed.build();
		}
		
		for (int i = page * fieldsPerPage; i < (page + 1) * fieldsPerPage; i++) {
			if (i >= fields.size())
				continue;
			embed.addField(fields.get(i));
		}
		return embed.build();
	}
	
	public MessageEmbed buildFAQDM(Color color, String bdeRole, String esoRole, int page) {
		
		List<Field> fields = getFieldPage(bdeRole, esoRole);
		
		int fieldsPerPage = 4;
		int maxPage = fields.size() / fieldsPerPage;
		
		EmbedBuilder embed = new EmbedBuilder()
			.setColor(color)
			.setTitle("Frequently Asked Questions")
			.setFooter(page + " of "+ maxPage);
		
		if (page > maxPage) {
			embed.addField("Oh no! 404", String.format(
				"""
				Hmm creo que no hay más preguntas por acá,
				si no encuentras una pregunta que entiendes que deberia
				de estar aquí, por favor contacta a un estudiante orientador.
				Trata con un rango de páginas de [0-%s]
				""", maxPage), false);
			return embed.build();
		}
		
		for (int i = page * fieldsPerPage; i < (page + 1) * fieldsPerPage; i++) {
			if (i >= fields.size())
				continue;
			embed.addField(fields.get(i));
		}
		return embed.build();
	}
	
	private List<Field> getFieldPage(String bdeRole, String esoRole) {
		return List.of(
			new Field("1. ¿Cómo puedo adiestrarme mejor en Discord?", String.format(
						"""
						Puede leer el PDF que se le proveyó o comunicarse con algunos de los %s.
						""", bdeRole), false),
					
			new Field("2. ¿Por qué no puedo ver los canales de otros grupos?", 
						"""
						Se diseño de esta manera para tener una comunicación efectiva 
						entre los integrantes de cada grupo individual y sus lideres.
						""", false),
				
			new Field("3. Tengo una idea para el bot. ¿Con quién me puedo contactar?", String.format(
						"""
						Con los %s o los %s.
						""", bdeRole, esoRole), false),
					
			new Field("4. ¿Cómo consigo los comandos para el bot?",
						"""
						Escribe `/help` en el chat de su preferencia.
						""", false),
				
			new Field("5. ¿Cómo crearon el bot?", String.format(
						"""
						El bot fue programado inicialmente por nuestros Ex-alumnos Fernando y Gabriel 
						con el lenguaje Python. Pero para esta nueva versión con la que estas interactuando 
						está programado en Java!! Cualquier pregunta le pueden escribir a Alfredo o a cualquier 
						%s. Ellos te pueden explicar y enseñar algunos detalles de como se hizo si estas interesad@!
						""", bdeRole), false),
			
			new Field("6. Me siento indeciso sobre mi departamento. ¿Con quién hablo?",
						"""
						Hable con su asesor académico de su departamento el día de ajustes de matrícula, 
						con uno de sus líderes o con Madeline Rodríguez.
						""", false),
				
			new Field("7. Tengo unas clases convalidadas por el CEEB ¿Con quién hablo?",
						"""
						Atienda bien a las orientaciones de la semana en como se convalidan esos cursos, 
						sino hable con su departamento en ajustes.
						""", false),
			
			new Field("8. ¿A quién voy para conseguir mi curriculo?",
						"""
						Preguntale al bot! El te lo provee en PDF si escribes `/curriculo`.
						""", false),
				
			new Field("9. ¿Qué significa la letra al lado de la sección de una clase?",
						"""
						D - a distancia, sin horario fijo
						E - a distancia, con horario fijo
						H - híbrida
						""", false),
			
			new Field("10. ¿Cuántos créditos necesito?",
						"""
						Para ser estudiante regular se necesita un mínimo de 12 créditos.
						""", false),
					
			new Field("11. ¿Qué es Moodle?",
						"""
						Moodle es la plataforma principal para clases virtuales :link: [Moodle](https://online.upr.edu)
						""", false),
			new Field("12. ¿Dónde me puedo estacionar?",
						"""
						Zoológico de Mayagüez, Palacio de Recreación y Deportes. 
						Luego de las 4:30 se puede estacionar el cualquier lado.
						""", false),
					
			new Field("13. ¿Cuál es la clave del WIFI del Colegio?",
						"""
						Colegio2019
						""", false));
	}
}
