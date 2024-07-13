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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author Alfredo
 */
public class LinksEmbed {
	
	public MessageEmbed buildLinks(Color color) {
		return new EmbedBuilder()
			.setColor(color)
			.setTitle("Links importantes :nerd:")
			.addField(
				"""
				Accesos a Laboratorios de STEFANI
				""",
				"""
				> Info para pedir tarjetas de laboratorios del edificio Stefani, para solamente
				> estudiantes de concentración: **INEL/ICOM/INSO/CIIC**.
				> Los nombres de los labs son: *INCADEL-S105C, CRAI-S105D, AMADEUS-S121 y el S114*
				> - https://ece.uprm.edu/instrucciones-para-solicitar-cuentas-yo-tarjetas/
				""", false)
			
			.addField(
				"""
				Oficina de COOP de Ingeniería
				""",
				"""
				> - https://www.uprm.edu/coop/
				""", false)

			.addField(
				"""
				Departamento de Colocaciones RUM
				""",
				"""
				> - https://www.uprm.edu/placement/home
				""", false)

			.addField(
				"""
				Consejo General de Estudiantes
				""",
				"""
				> - Página Oficial: http://cge.uprm.edu/
				> - Twitter: https://twitter.com/cgerum
				""", false)
			
			.addField(
				"""
				Guía prepística
				""",
				"""
				> Y no olvides consultar la Guía Prepística para más enlaces importantes en la sección
				> de __*ENLACES Y CONTACTOS*__. Utiliza el comando `/guia-prepistica`
				""", false)
			.build();
	}
}
