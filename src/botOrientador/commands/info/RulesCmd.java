package botOrientador.commands.info;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.managers.BotEventHandler;
import services.bot.managers.CommandI;

/**
 * 
 * @author Alfredo
 *
 */
public class RulesCmd extends BotEventHandler implements CommandI {

	private boolean isGlobal;
	private List<OptionData> options;
	
	public RulesCmd() {
		this.options = new ArrayList<>();
		
	}
	
	@Override
	@Deprecated
	public void init(ReadyEvent event) {
		
	}
	
	@Override
	public void dispose() {
		if(!BotEventHandler.validateEventDispose(this.getClass()))
			return;
		
		options.clear();
		
		BotEventHandler.registerDisposeEvent(this);
	}

	@Override
	public boolean isGlobal() {
		return isGlobal;
	}

	@Override
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	
	@Override
	public String getCommandName() {
		return "rules";
	}

	@Override
	public String getDescription() {
		return "Provee las reglas del servidor";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		EmbedBuilder embedBuider = new EmbedBuilder();

		embedBuider.setColor(new Color(70, 150, 90));
		embedBuider.setTitle(":rotating_light: Aquí están las reglas del servidor :rotating_light:");

		embedBuider.addField(
				"1) __Respeten__ para que lo __respeten.__", 
				"""
				Evite comentarios fuera de lugar a sus compañer@s
				""", false);
		
		embedBuider.addField(
				"2) Palabras soeces están __**PROHIBIDAS.**__",
				"""
				El @Bot Orientador censura cualquier palabra soez detectada así que mucho juicio con su vocabulario.
				""", false);

		embedBuider.addField(
				"3) No spamming flyers. :newspaper: :no_entry_sign:",
				"""
				Está prohibido hacer anuncios de actividades fuera de las que
				estan programadas por el equipo de TeamMade para este servidor
				""", false);

		embedBuider.addField(
				"4) Solo memes sanos. :innocent:",
				"""
				Kuidao con los memes y con los emojis de otros servidores (para los que tienen Nitro)
				""", false);
		
		embedBuider.addField(
				"5) Cada canal tiene un propósito:writing_hand:",
				"""
				El servidor tiene channels para los topics que se tienden hablar con mas frecuencia, please use them accordingly!! 
				En caso de que se quiera hablar de un topic distinto que no esté dentro de los que se muestran, pueden acercarse
				a un @Administrator y presentar su idea para así crear un nuevo channel para atender esos nuevos temas de conversación.				
				""", false);
		
		embedBuider.addField(
				"6) :bangbang:Tengo un problema, a quién puedo dirigirme:bangbang:",
				"Si tiene alguna preocupación, puede hablar directo con cualquiera de los líderes de su grupo o con Made.", false);
		
		embedBuider.addField(
				"7) Problemas con el bot?:robot:",
				"De tener a algún inconveniente con el @Bot Orientador, hablar directo con los @Bot Developer.", false);

		embedBuider.addField(
				"8) Problemas con Discord?",
				"De tener algún inconveniente con su perfil o con algo del servidor de Discord hable directo con los @Administrator.", false);

		embedBuider.addField(
				"9) Aquí todos venimos a aprender!",
				"""
				No hay pregunta tonta, haga su pregunta sin miedo, pero antes pregúntele al bot si lo puede ayudar con los comandos
				que se les proveyeron. Cualquier duda con un comando le pueden preguntar a los @Bot Developer  o usar el ``/help`` command
				para que el bot le proveea más infomación acerca del comando que desea buscar o entender que hace.			
				""", false);
		
		embedBuider.addField(
				"10) No encuentro respuestas a mis dudas!!",
				"En caso de que el bot no pueda ayudarlo, contacte a su líder de grupo.", false);

		embedBuider.addField(
				"11) Quiero conocer a mis estudiantes orientadores!!",
				"""
				Si quiere saber que estudiantes orientadores fuera de sus líderes son de su departamento,
				utilize el ``/estudiantes-orientadores`` para luego escoger el departamento que
				desea conocer a otro @EstudianteOrientador		
				""", false);
		
		event.replyEmbeds(embedBuider.build()).queue();
	}

}
