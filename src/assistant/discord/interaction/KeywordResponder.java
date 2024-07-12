/**
 * 
 */
package assistant.discord.interaction;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import assistant.discord.object.ResponderCategory;
import assistant.discord.object.ResponderTopic;

/**
 * @author Alfredo
 */
public class KeywordResponder {
	
    private Map<String, ResponderTopic> topicKeywords = new HashMap<>();
    private Map<String, ResponderCategory> categoryKeywords = new HashMap<>();
	
	public KeywordResponder() {
		this.topicKeywords = new HashMap<>();
		this.categoryKeywords = new HashMap<>();
		
		// Category keywords
        categoryKeywords.put("que", ResponderCategory.WHAT);
        categoryKeywords.put("puedo", ResponderCategory.WHAT);
        categoryKeywords.put("es", ResponderCategory.WHAT);
        
        categoryKeywords.put("en que", ResponderCategory.WHEN);
        categoryKeywords.put("cuando", ResponderCategory.WHEN);
        
        categoryKeywords.put("quien", ResponderCategory.WHO);
        
        categoryKeywords.put("donde", ResponderCategory.WHERE);
        categoryKeywords.put("hacer", ResponderCategory.WHERE);
        
        categoryKeywords.put("porque", ResponderCategory.WHY);
        
        categoryKeywords.put("como", ResponderCategory.HOW);

        categoryKeywords.put("recomiendan", ResponderCategory.RECOMMENDATION);
        categoryKeywords.put("recomiendas", ResponderCategory.RECOMMENDATION);

        // Topic keywords
        topicKeywords.put("matricula", ResponderTopic.MATRICULA_CONFIRMATION);
        topicKeywords.put("ajuste", ResponderTopic.MATRICULA_CONFIRMATION_AJUSTE);
        topicKeywords.put("ticket", ResponderTopic.MATRICULA_TICKET);
        topicKeywords.put("parking", ResponderTopic.PARKING);
        topicKeywords.put("estacionar", ResponderTopic.PARKING);
        topicKeywords.put("software", ResponderTopic.DOWNLOAD_SOFTWARE);
        topicKeywords.put("edificio", ResponderTopic.BUILDING);
        topicKeywords.put("servicios medicos", ResponderTopic.SERVICIOS_MEDICOS);
        topicKeywords.put("clase", ResponderTopic.CLASSES);
        topicKeywords.put("profe", ResponderTopic.PROFESSOR);
        topicKeywords.put("profesor", ResponderTopic.PROFESSOR);
        topicKeywords.put("codificacion", ResponderTopic.CLASSES_CODE);
        topicKeywords.put("moddle", ResponderTopic.MOODLE);
        topicKeywords.put("shelly", ResponderTopic.RUMAD);
        topicKeywords.put("consejero", ResponderTopic.CONSEJERO);
        topicKeywords.put("quimica", ResponderTopic.QUIMICA);
        topicKeywords.put("asistencia", ResponderTopic.ASISTENCIA_ECONOMICA);
        topicKeywords.put("libro", ResponderTopic.LIBRO);
	}
	
	public List<String> tokenize(String text) {
	    return Arrays.asList(text.toLowerCase().split("\\s+"));
	}
	
	private Map<ResponderCategory, Integer> countCategoryTokens(List<String> tokens) {
	    Map<ResponderCategory, Integer> categoryCount = new HashMap<>();
	    for (String token : tokens) {
	        for (Map.Entry<String, ResponderCategory> entry : categoryKeywords.entrySet()) {
	            if (token.contains(entry.getKey())) {
	                categoryCount.put(entry.getValue(), categoryCount.getOrDefault(entry.getValue(), 0) + 1);
	            }
	        }
	    }
	    return categoryCount;
	}

	private Map<ResponderTopic, Integer> countTopicTokens(List<String> tokens) {
	    Map<ResponderTopic, Integer> topicCount = new HashMap<>();
	    for (String token : tokens) {
	        for (Map.Entry<String, ResponderTopic> entry : topicKeywords.entrySet()) {
	            if (token.contains(entry.getKey())) {
	                topicCount.put(entry.getValue(), topicCount.getOrDefault(entry.getValue(), 0) + 1);
	            }
	        }
	    }
	    return topicCount;
	}
	
	private Optional<ResponderTopic> getMostRelevantTopic(Map<ResponderTopic, Integer> topicCount) {
	    return topicCount.entrySet().stream()
	            .max(Comparator.comparingInt(Map.Entry::getValue))
	            .map(Map.Entry::getKey);
	}
	
    public String generateResponse(String text) {
        List<String> tokens = tokenize(text);
        Map<ResponderCategory, Integer> categoryCount = countCategoryTokens(tokens);
        Map<ResponderTopic, Integer> topicCount = countTopicTokens(tokens);
        Optional<ResponderTopic> mostRelevantTopic = getMostRelevantTopic(topicCount);

        Set<String> responses = new HashSet<>();

        for (ResponderCategory category : categoryCount.keySet()) {
        	if (mostRelevantTopic.isPresent()) {
	    		switch (category) {
	    		case WHAT:
	    			responses.add(generateWhatResponse(mostRelevantTopic.get()));
	    			break;
	    		case WHEN:
	    			responses.add(generateWhenResponse(mostRelevantTopic.get()));
	    			break;
	    		case WHO:
	    			responses.add(generateWhoResponse(mostRelevantTopic.get()));
	    			break;
	    		case WHERE:
	    			responses.add(generateWhereResponse(mostRelevantTopic.get()));
	    			break;
	    		case WHY:
	    			responses.add(generateWhyResponse(mostRelevantTopic.get()));
	    			break;
	    		case HOW:
	    			responses.add(generateHowResponse(mostRelevantTopic.get()));
	    			break;
	    		case RECOMMENDATION:
	    			responses.add(generateRecommendationResponse(mostRelevantTopic.get()));
	    			break;
	    		default:
	    			return "";
	    		}
        	}
        }
        return String.join(" ", responses);
    }
    
    
    private String generateRecommendationResponse(ResponderTopic topic) {
		switch (topic) {
		case MATRICULA_CONFIRMATION:
			return "Te recomiendo buscar asistencia profesional para resolver el asunto.";
		case MATRICULA_CONFIRMATION_AJUSTE:
			return "Te exhorto confirmar primero, luego puedes hacer ajustes.";
		case MATRICULA_TICKET:
			return "Es importante hacer un ticket en ajustes si tu matricula llegase a tener problemas.";
		case PARKING:
			return "Cuidado con las áreas que son inundables, si lo dice es por una razon.";
		case BUILDING:
			return "El mejor edificio es el de Stefani, just sayin'.";
		case CLASSES:
			return "Es bueno tener un plan de estudio. Nunca te confíes de una clase y no te dejes llevar por los demás. Llega a tus propias conclusiones.";
		case PROFESSOR:
			return "Cada profe es una aventura y experiencia única. Sé tú el que decide qué tal te parece el profe.";
		case SERVICIOS_MEDICOS:
			return "Aquí tienes el email de servicios médicos como contacto: servmed@uprm.edu.";
		default:
			return "";
		}
	}

	private String generateWhatResponse(ResponderTopic topic) {
		switch (topic) {
        case MATRICULA_CONFIRMATION:
            return "En el portal viejo, bajo 'student services' en matrícula. Ahí se puede confirmar.";
        case MATRICULA_CONFIRMATION_AJUSTE:
            return "Puedes hacer ajustes después de confirmar.";
        case MATRICULA_TICKET:
            return "En ajustes.uprm.edu, haces login con tu cuenta institucional y preparas un ticket explicando tu situación.";
        case CLASSES:
            return """
                   En 'my courses' asegúrate que diga 'in progress' para ver los cursos que estás tomando ahora mismo.
                   """;
        case PROFESSOR:
            return "No te podría decir acerca de un profe, pero puedes preguntarle a un EO.";
        case SERVICIOS_MEDICOS:
            return "Aquí tienes el email de servicios médicos como contacto: servmed@uprm.edu.";
        case MOODLE:
            return "Las credenciales del portal funcionan pero cuando trata de entrar a Moodle, le dice que no están correctas.";
        case RUMAD:
            return "Tienes que bajar la aplicación y luego poner rumad.uprm.edu en donde dice SSH Server address.";
        case CONSEJERO:
            return "Es el que tiene el role de ConsejeroProfesional, puedes hacerle tag o mandarle un email.";
        case QUIMICA:
            return "Para los labs como los dan los instructores, eso te aparece hasta el mismo dia que te toque coger la clase.";
        case ASISTENCIA_ECONOMICA:
            return "Puedes usar el comando /salon y poner 'ae' y te da un pin.";
        default:
            return "";
        }
    }
    
    private String generateHowResponse(ResponderTopic topic) {
        switch (topic) {
        case MATRICULA_CONFIRMATION:
            return "En el portal viejo, bajo 'student services' en matrícula. Ahí se puede confirmar.";
        case MATRICULA_CONFIRMATION_AJUSTE:
            return "En ajustes.uprm.edu, haces log-in con tu cuenta institucional y haces un ticket.";
        case MATRICULA_TICKET:
            return "A través de ajustes.uprm.edu, ahí puedes hacer un ticket.";
        case BUILDING:
            return "Puedes usar `/salon`.";
        case DOWNLOAD_SOFTWARE:
            return "Google it :).";
        case SERVICIOS_MEDICOS:
            return "Contacta a servmed@uprm.edu.";
        case RUMAD:
            return """
                   Tienes que bajar la aplicación y luego poner rumad.uprm.edu en donde dice SSH Server address.
                   Luego le das 'connect' y ya eso abre Putty.
                   """;
        case MOODLE:
        	return """
                   Las credenciales del portal funcionan pero cuando trata de entrar a Moodle, le dice que no están correctas.
                   Se tiene que dirigir a support@uprm.edu, o contactar directamente el CTI.
                   """;
        case LIBRO:
            return """
                   Muchos de los libros estan online, no tienes que pagar por ningunos.
                   """;
        default:
            return "";
        }
    }
    
    private String generateWhoResponse(ResponderTopic topic) {
        switch (topic) {
        case MATRICULA_CONFIRMATION:
            return "Se confirma de manera virtual.";
        case MATRICULA_CONFIRMATION_AJUSTE:
            return "Puedes hacerlo a través del sistema de tickets en ajustes.";
        case PROFESSOR:
            return "Espera a que el profe te mande un email, sino vas al departamento.";
        case CONSEJERO:
            return "Es el que tiene el role de ConsejeroProfesional, puedes hacerle tag o mandarle un email.";
        case QUIMICA:
            return "Para los labs como los dan los instructores, eso te aparece hasta el mismo dia que te toque coger la clase.";
        default:
            return "";
        }
    }
    
    private String generateWhereResponse(ResponderTopic topic) {
        switch (topic) {
        case MATRICULA_CONFIRMATION:
            return "En el portal viejo, bajo 'student services' en matrícula. Ahí se puede confirmar.";
        case MATRICULA_CONFIRMATION_AJUSTE:
        	return "También se puede hacer en putty manualmente.";
        case MATRICULA_TICKET:
            return "En ajustes.uprm.edu, haces login con tu cuenta institucional y preparas un ticket explicando tu situación.";
        case BUILDING:
            return "Puedes usar `/salon` para ver el pin.";
        case CLASSES:
            return "Pendiente siempre al email y a Moodle.";
        case ASISTENCIA_ECONOMICA:
            return "Puedes usar el comando /salon y poner 'ae' y te da un pin.";
        default:
            return "";
        }
    }
    
    private String generateWhenResponse(ResponderTopic topic) {
        switch (topic) {
        case MATRICULA_CONFIRMATION:
            return "En el portal viejo, bajo 'student services' en matrícula. Ahí se puede confirmar.";
        case MATRICULA_CONFIRMATION_AJUSTE:
            return "Puedes hacer ajustes después de confirmar.";
        case CLASSES:
            return "Pendiente siempre al email y a Moodle.";
        default:
            return "";
        }
    }
    
    private String generateWhyResponse(ResponderTopic topic) {
        switch (topic) {
        case MATRICULA_CONFIRMATION:
            return "Es importante tener la matrícula confirmada para evitar problemas con los cursos.";
        case MATRICULA_CONFIRMATION_AJUSTE:
            return "Se pueden hacer ajustes después de confirmar para asegurarse de que la matrícula esté correcta.";
        case MATRICULA_TICKET:
            return "Es importante hacer un ticket en ajustes si tu matrícula llegase a tener problemas.";
        case PARKING:
            return "Cuidado con las áreas que son inundables.";
        case DOWNLOAD_SOFTWARE:
            return "Google it :).";
        case BUILDING:
            return "Cada edificio tiene su particularidad, es importante conocer su ubicación.";
        case CLASSES:
            return "Pendiente siempre al email y a Moodle.";
        case PROFESSOR:
            return "Cada profe tiene su manera de enseñar, es importante conocer sus expectativas.";
        case SERVICIOS_MEDICOS:
            return "Para cualquier emergencia o servicio médico, están disponibles.";
        default:
            return "";
        }
    }
}
