/**
 * 
 */
package application.launch;

import java.sql.PreparedStatement;

import application.core.Application;
import application.core.Configs;
import application.core.RegisterApplication;
import assistant.app.ECEAssistant;
import services.database.connections.DatabaseConnection;
import services.database.connections.DatabaseConnectionManager;

/**
 * @author Alfredo
 * 
 */
@RegisterApplication(name = "Discord Assistants", version = "v2024.2.SO4")
public class AssistantAppEntry extends Application {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create a new assistant application to start running
		Application.run(new AssistantAppEntry());
	}

	@Override
	public void start() {
		
		DatabaseConnectionManager.instance().addDatabaseConnection(
			/**
			 *  Create a new database connection
			 *  
			 *  This is the database that the entire program will be based on.
			 *  Database location is not accessible anywhere inside the project.
			 */
			new DatabaseConnection(Configs.DB_CONNECTION, Configs.DB_DRIVER, Configs.get().databaseCredentials())
		);

		// Initiate the Discord Application
		ECEAssistant BOT_ASSISTANT = new ECEAssistant(Configs.get().token());
		
		BOT_ASSISTANT.start();
		
//		loadPrepas();
	}
	
	@Override
	public void shutdown() {
		DatabaseConnectionManager.dispose();
	}
	
	private void loadPrepas() {
		DatabaseConnectionManager.instance().getConnection(Configs.DB_CONNECTION).get()
		.establishConnection(connection -> {
			
			PreparedStatement smt = connection.prepareStatement(
			"""
			WITH verified_email AS (
			    SELECT verid
			    FROM verification
			    WHERE email = ?
			)
			INSERT INTO orientador (fname, lname, fverid)
			SELECT ?, ?, verid
			FROM verified_email;
			"""
			);
			
//			PreparedStatement smt = connection.prepareStatement(
//			"""
//			WITH verified_email AS (
//			    SELECT verid
//			    FROM verification
//			    WHERE email = ?
//			)
//			UPDATE prepa
//			SET mlname = ?
//			FROM verified_email
//			WHERE prepa.fverid = verified_email.verid;
//			"""
//			);
			
			
			for(int i = 0;i < EOEmails.length;i++) {
				smt.setString(1, EOEmails[i]);
				smt.setString(2, EONames[i]);
				smt.setString(3, EOLastNames[i]);
				smt.addBatch();
			}
			
			smt.executeBatch();
			
			smt.close();
		});
	}
	
	private static int[] EOProgram = {
			1,
			3,
			3,
			1,
			2,
			1,
			1,
			1,
			2,
			2,
			3,
			1,
			3,
			3,
			1,
			1,
			3,
			3,
			1,
			6,
			1,
			3,
			4,
			3,
			3,
	};
	private static String[] EOEmails = {
			"cristian.rivera26@upr.edu",
			"dariana.serrano1@upr.edu",
			"derick.mercado@upr.edu",
			"eduardo.rodriguez59@upr.edu",
			"jaydiemar.vazquez@upr.edu",
			"kamila.sanabria1@upr.edu",
			"loalis.feliciano@upr.edu",
			"robert.ortiz6@upr.edu",
			"ronaldo.flores@upr.edu",
			"Tomas.gomez1@upr.edu",
			"vilmaliz.hernandez@upr.edu",
			"Yaniel.varela@upr.edu",
			"joriel.ortiz@upr.edu",
			"jesmarie.alonso@upr.edu",
			"jose.maldonado39@upr.edu",
			"alfredo.soto2@upr.edu",
			"sebastian.cruz18@upr.edu",
			"alondra.velez16@upr.edu",
			"iris.betancourt1@upr.edu",
			"jesus.colon16@upr.edu",
			"reuel.camacho@upr.edu",
			"josue.colon15@upr.edu",
			"glerysbeth.serrano@upr.edu",
			"alberto.canela@upr.edu",
			"yamil.fuentes@upr.edu",

	};
	private static String[] EOLastNames = {
			"A. Rivera Centeno",
			"Z. Serrano González",
			"G Mercado Rivera",
			"Rodríguez Aviñó",
			"Vázquez Rodríguez",
			"V. Sanabria Rabelo",
			"M. Feliciano Medina",
			"OE. Ortiz Leon",
			"Elier Flores Nazario",
			"Esteban Gomez Mojica",
			"A. Hernandez Mateo",
			"Varela Soler",
			"Ortiz Quiñones",
			"Alonso",
			"Maldonado",
			"Soto",
			"Cruz",
			"Vélez",
			"Betancourt",
			"Colón",
			"Camacho",
			"Cvolón",
			"Serrano Flores",
			"Canela",
			"Fuentes",

	};
	private static String[] EONames = {
			"Cristian",
			"Dariana",
			"Derick",
			"Eduardo",
			"Jaydiemar",
			"Kamila",
			"Loalis",
			"Robert",
			"Ronaldo",
			"Tomas",
			"Vilmaliz",
			"Yaniel",
			"Joriel",
			"Jesmarie",
			"José",
			"Alfredo",
			"Sebastian",
			"Alondra",
			"Iris",
			"Jesús",
			"Reuel",
			"Josue",
			"Glerysbeth",
			"Alberto",
			"Yamil",
	};
	
	private static int[] program = {
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			4,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
			2,
	};
	
	private static String[] emails = {
			"arnaldo.alicea1@upr.edu",
			"edwin.almodovar3@upr.edu",
			"jayson.alvarado@upr.edu",
			"ruben.aviles@upr.edu",
			"gian.badillo1@upr.edu",
			"karen.baez@upr.edu",
			"natalia.camejo@upr.edu",
			"anelys.caminero@upr.edu",
			"fernando.carrion@upr.edu",
			"miguel.collazo12@upr.edu",
			"yeriel.cruz@upr.edu",
			"xavier.diaz11@upr.edu",
			"leilani.eaton@upr.edu",
			"angel.febus@upr.edu",
			"diego.fernandez10@upr.edu",
			"manuel.ferrer6@upr.edu",
			"gabriela.figueroa14@upr.edu",
			"roberto.fuertes@upr.edu",
			"eiram.garcia@upr.edu",
			"gerardo.garcia6@upr.edu",
			"miguel.gonzalez71@upr.edu",
			"normando.hernandez@upr.edu",
			"alejandro.hernandez13@upr.edu",
			"kiara.hernandez20@upr.edu",
			"alejandro.lebron1@upr.edu",
			"johniel.lopez1@upr.edu",
			"chrisyahel.martinez@upr.edu",
			"jose.mendez67@upr.edu",
			"yaddy.mercado@upr.edu",
			"gian.miranda1@upr.edu",
			"adriana.muniz5@upr.edu",
			"claudia.munoz4@upr.edu",
			"kristelle.pabon@upr.edu",
			"keyla.padilla3@upr.edu",
			"marianna.pagan1@upr.edu",
			"marcbraian.perez@upr.edu",
			"bryan.perez20@upr.edu",
			"adrian.reyes10@upr.edu",
			"diego.rivera63@upr.edu",
			"jediel.robles@upr.edu",
			"jabes.robles@upr.edu",
			"moises.rosario1@upr.edu",
			"abdiel.segui@upr.edu",
			"pablo.semidey@upr.edu",
			"david.semidey@upr.edu",
			"gretchen.serrano@upr.edu",
			"elian.soto@upr.edu",
			"samil.toro@upr.edu",
			"luis.valentin28@upr.edu",
			"fabian.vazquez8@upr.edu",
			"iancarlo.vega@upr.edu",
			"juan.acevedo32@upr.edu",
			"aidhan.angelucci@upr.edu",
			"adrian.antommattei@upr.edu",
			"axel.babilonia@upr.edu",
			"michael.badillo@upr.edu",
			"mosley.barbosa@upr.edu",
			"diego.beda@upr.edu",
			"nicolas.behar@upr.edu",
			"kevin.beltran@upr.edu",
			"eduardo.bobonis@upr.edu",
			"claudia.bonilla1@upr.edu",
			"carlos.burgos16@upr.edu",
			"abdiel.caban@upr.edu",
			"christian.caban5@upr.edu",
			"eliacib.caban@upr.edu",
			"yadiel.caban@upr.edu",
			"adrian.caban@upr.edu",
			"sabdriel.calderon@upr.edu",
			"yailanis.caraballo@upr.edu",
			"jim.caraballo@upr.edu",
			"angel.carrasquillo8@upr.edu",
			"norelis.carreras@upr.edu",
			"sergio.cartagena@upr.edu",
			"xavier.castro3@upr.edu",
			"rickmichael.chaparro@upr.edu",
			"alex.collazo3@upr.edu",
			"jose.colon193@upr.edu",
			"alfonso.comes@upr.edu",
			"angel.cordero12@upr.edu",
			"adrian.crescioni@upr.edu",
			"abdiel.cuprill@upr.edu",
			"diego.davila1@upr.edu",
			"jordan.deleon@upr.edu",
			"angel.desantiago1@upr.edu",
			"julian.diaz4@upr.edu",
			"example.example@upr.edu",
			"jey.escobar@upr.edu",
			"diego.espinal@upr.edu",
			"justian.figueroa@upr.edu",
			"kennyel.figueroa@upr.edu",
			"damian.fiol@upr.edu",
			"jose.garcia145@upr.edu",
			"yahdiel.gonzalez@upr.edu",
			"harold.gonzalez2@upr.edu",
			"angel.hernandez59@upr.edu",
			"gianpaulo.hernandez@upr.edu",
			"abdiel.hiraldo@upr.edu",
			"praxedes.irizarry2@upr.edu",
			"william.javier@upr.edu",
			"kiara.jimenez2@upr.edu",
			"diego.juarbe@upr.edu",
			"luis.kuilan1@upr.edu",
			"julian.laracuente@upr.edu",
			"giancarlo.lebron@upr.edu",
			"diego.lizardi@upr.edu",
			"carlos.lopez115@upr.edu",
			"christian.lopez52@upr.edu",
			"ariana.lugo3@upr.edu",
			"gustavo.machado@upr.edu",
			"bryan.maldonado8@upr.edu",
			"favian.maldonado@upr.edu",
			"lenilka.marti@upr.edu",
			"faith.martinez@upr.edu",
			"jorge.martinez70@upr.edu",
			"johan.matos@upr.edu",
			"francisco.maymon@upr.edu",
			"joshuem.medina1@upr.edu",
			"isaac.montalvo@upr.edu",
			"evan.morales@upr.edu",
			"derick.muniz@upr.edu",
			"rafael.munoz13@upr.edu",
			"mia.munoz@upr.edu",
			"daniel.nater@upr.edu",
			"carlos.negron42@upr.edu",
			"gianna.nitta@upr.edu",
			"marvick.nunez@upr.edu",
			"alex.ocasio1@upr.edu",
			"andrew.ocasio@upr.edu",
			"ricardo.perez45@upr.edu",
			"dereck.perez@upr.edu",
			"johnattan.perez@upr.edu",
			"yasiel.perez1@upr.edu",
			"julian.perez11@upr.edu",
			"michael.pietri@upr.edu",
			"marcos.pillot@upr.edu",
			"yulian.plaza@upr.edu",
			"yerik.quiles@upr.edu",
			"ambar.ramirez2@upr.edu",
			"fernando.ramirez10@upr.edu",
			"axel.ramirez5@upr.edu",
			"jan.ramos14@upr.edu",
			"juliana.ramos@upr.edu",
			"jan.rivera36@upr.edu",
			"sebastian.rivera58@upr.edu",
			"joshua.rivera46@upr.edu",
			"ricardo.rivera110@upr.edu",
			"alexander.rivera30@upr.edu",
			"jaziel.rivera@upr.edu",
			"karina.rivera31@upr.edu",
			"xander.roldan@upr.edu",
			"edwin.roman22@upr.edu",
			"william.rosado8@upr.edu",
			"adriel.rosado@upr.edu",
			"sebastian.sanchez3@upr.edu",
			"derek.sanchez@upr.edu",
			"valentina.santiago@upr.edu",
			"armando.santiago4@upr.edu",
			"miguel.santos3@upr.edu",
			"kairymar.solivan@upr.edu",
			"yadiel.soto3@upr.edu",
			"kristopher.suarez@upr.edu",
			"juniel.tejeras@upr.edu",
			"rafael.torres55@upr.edu",
			"lemuel.torres1@upr.edu",
			"alejandro.tosado1@upr.edu",
			"jonathan.troche@upr.edu",
			"miguel.vega18@upr.edu",
			"jezer.vega@upr.edu",
			"yahel.vega@upr.edu",
			"sebastian.velez13@upr.edu",
			"luis.vicenty2@upr.edu",
			"julian.walker@upr.edu",
			"diego.zapata@upr.edu",
	};
	
	private static String[] names = {
			"ARNALDO",
			"EDWIN",
			"JAYSON",
			"RUBEN",
			"GIAN",
			"KAREN",
			"NATALIA",
			"ANELYS",
			"FERNANDO",
			"MIGUEL",
			"YERIEL",
			"XAVIER",
			"LEILANI",
			"ANGEL",
			"DIEGO",
			"MANUEL",
			"GABRIELA",
			"ROBERTO",
			"EIRAM",
			"GERARDO",
			"MIGUEL",
			"NORMANDO",
			"ALEJANDRO",
			"KIARA",
			"ALEJANDRO",
			"JOHNIEL",
			"CHRIS_YAHEL",
			"JOSE",
			"YADDY",
			"GIAN",
			"ADRIANA",
			"CLAUDIA",
			"KRISTELLE",
			"KEYLA",
			"MARIANNA",
			"MARCBRAIAN",
			"BRYAN",
			"ADRIAN",
			"DIEGO",
			"JEDIEL",
			"JABES",
			"MOISES",
			"ABDIEL",
			"PABLO",
			"DAVID",
			"GRETCHEN",
			"ELIAN",
			"SAMIL",
			"LUIS",
			"FABIAN",
			"IANCARLO",
			"JUAN",
			"AIDHAN",
			"ADRIAN",
			"AXEL",
			"MICHAEL",
			"MOSLEY",
			"DIEGO",
			"NICOLAS",
			"KEVIN",
			"EDUARDO",
			"CLAUDIA",
			"CARLOS",
			"ABDIEL",
			"CHRISTIAN",
			"ELIACIB",
			"YADIEL",
			"ADRIAN",
			"SABDRIEL",
			"YAILANIS",
			"JIM",
			"ANGEL",
			"NORELIS",
			"SERGIO",
			"XAVIER",
			"RICKMICHAEL",
			"ALEX",
			"JOSE",
			"ALFONSO",
			"ANGEL",
			"ADRIAN",
			"ABDIEL",
			"DIEGO",
			"JORDAN",
			"ANGEL",
			"JULIAN",
			"EDUARDO_IV",
			"JEY",
			"DIEGO",
			"JUSTIAN",
			"KENNYEL",
			"DAMIAN",
			"JOSE",
			"YAHDIEL",
			"HAROLD",
			"ANGEL",
			"GIANPAULO",
			"ABDIEL",
			"PRAXEDES",
			"WILLIAM",
			"KIARA",
			"DIEGO",
			"LUIS",
			"JULIAN",
			"GIANCARLO",
			"DIEGO",
			"CARLOS",
			"CHRISTIAN",
			"ARIANA",
			"GUSTAVO",
			"BRYAN",
			"FAVIAN",
			"LENILKA",
			"FAITH",
			"JORGE",
			"JOHAN",
			"FRANCISCO",
			"JOSHUEM",
			"ISAAC",
			"EVAN",
			"DERICK",
			"RAFAEL",
			"MIA",
			"DANIEL",
			"CARLOS",
			"GIANNA",
			"MARVICK",
			"ALEX",
			"ANDREW",
			"RICARDO",
			"DERECK",
			"JOHNATTAN",
			"YASIEL",
			"JULIAN",
			"MICHAEL",
			"MARCOS",
			"YULIAN",
			"YERIK",
			"AMBAR",
			"FERNANDO",
			"AXEL",
			"JAN",
			"JULIANA",
			"JAN",
			"SEBASTIAN",
			"JOSHUA",
			"RICARDO",
			"ALEXANDER",
			"JAZIEL",
			"KARINA",
			"XANDER",
			"EDWIN",
			"WILLIAM",
			"ADRIEL",
			"SEBASTIAN",
			"DEREK",
			"VALENTINA",
			"ARMANDO",
			"MIGUEL",
			"KAIRYMAR",
			"YADIEL",
			"KRISTOPHER",
			"JUNIEL",
			"RAFAEL",
			"LEMUEL",
			"ALEJANDRO",
			"JONATHAN",
			"MIGUEL",
			"JEZER",
			"YAHEL",
			"SEBASTIAN",
			"LUIS",
			"JULIAN",
			"DIEGO",
	};
	
	private static String[] firstLastName = {
			"ALICEA",
			"ALMODOVAR",
			"ALVARADO",
			"AVILES",
			"BADILLO",
			"BAEZ",
			"CAMEJO",
			"CAMINERO",
			"CARRION",
			"COLLAZO",
			"CRUZ",
			"DIAZ",
			"EATON",
			"FEBUS",
			"FERNANDEZ",
			"FERRER",
			"FIGUEROA",
			"FUERTES",
			"GARCIA",
			"GARCIA",
			"GONZALEZ",
			"HERNANDEZ",
			"HERNANDEZ",
			"HERNANDEZ",
			"LEBRON",
			"LOPEZ",
			"MARTINEZ",
			"MENDEZ",
			"MERCADO",
			"MIRANDA",
			"MUNIZ",
			"MUNOZ",
			"PABON",
			"PADILLA",
			"PAGAN",
			"PEREZ",
			"PEREZ",
			"REYES",
			"RIVERA",
			"ROBLES",
			"ROBLES",
			"ROSARIO",
			"SEGUI",
			"SEMIDEY",
			"SEMIDEY",
			"SERRANO",
			"SOTO",
			"TORO",
			"VALENTIN",
			"VAZQUEZ",
			"VEGA",
			"ACEVEDO",
			"ANGELUCCI",
			"ANTOMMATTEI",
			"BABILONIA",
			"BADILLO",
			"BARBOSA",
			"BEDA",
			"BEHAR",
			"BELTRAN",
			"BOBONIS",
			"BONILLA",
			"BURGOS",
			"CABAN",
			"CABAN",
			"CABAN",
			"CABAN",
			"CABAN",
			"CALDERON",
			"CARABALLO",
			"CARABALLO",
			"CARRASQUILLO",
			"CARRERAS",
			"CARTAGENA",
			"CASTRO",
			"CHAPARRO",
			"COLLAZO",
			"COLON",
			"COMES",
			"CORDERO",
			"CRESCIONI",
			"CUPRILL",
			"DAVILA",
			"DE_LEON",
			"DE_SANTIAGO",
			"DIAZ",
			"DOTEL",
			"ESCOBAR",
			"ESPINAL",
			"FIGUEROA",
			"FIGUEROA",
			"FIOL",
			"GARCIA",
			"GONZALEZ",
			"GONZALEZ",
			"HERNANDEZ",
			"HERNANDEZ",
			"HIRALDO",
			"IRIZARRY",
			"JAVIER",
			"JIMENEZ",
			"JUARBE",
			"KUILAN",
			"LARACUENTE",
			"LEBRON",
			"LIZARDI",
			"LOPEZ",
			"LOPEZ",
			"LUGO",
			"MACHADO",
			"MALDONADO",
			"MALDONADO",
			"MARTI",
			"MARTINEZ",
			"MARTINEZ",
			"MATOS",
			"MAYMON",
			"MEDINA",
			"MONTALVO",
			"MORALES",
			"MUNIZ",
			"MUNOZ",
			"MUNOZ",
			"NATER",
			"NEGRON",
			"NITTA",
			"NUNEZ",
			"OCASIO",
			"OCASIO",
			"PEREZ",
			"PEREZ",
			"PEREZ",
			"PEREZ",
			"PEREZ",
			"PIETRI",
			"PILLOT",
			"PLAZA",
			"QUILES",
			"RAMIREZ",
			"RAMIREZ",
			"RAMIREZ",
			"RAMOS",
			"RAMOS",
			"RIVERA",
			"RIVERA",
			"RIVERA",
			"RIVERA",
			"RIVERA",
			"RIVERA",
			"RIVERA",
			"ROLDAN",
			"ROMAN",
			"ROSADO",
			"ROSADO",
			"SANCHEZ",
			"SANCHEZ",
			"SANTIAGO",
			"SANTIAGO",
			"SANTOS",
			"SOLIVAN",
			"SOTO",
			"SUAREZ",
			"TEJERAS",
			"TORRES",
			"TORRES",
			"TOSADO",
			"TROCHE",
			"VEGA",
			"VEGA",
			"VEGA",
			"VELEZ",
			"VICENTY",
			"WALKER",
			"ZAPATA",
	};
	
	private static String[] secondLastName = {
			"ROSARIO",
			"RIVERA",
			"RIVERA",
			"BORRALI",
			"DELIZ",
			"SEPULVEDA",
			"DEL_VALLE",
			"SANTIAGO",
			"ORTIZ",
			"RIVERA",
			"DIAZ",
			"LEBRON",
			"_",
			"ROSARIO",
			"ZABALA",
			"ORTIZ",
			"CASTRO",
			"CASSE",
			"GARCIA",
			"ROSA",
			"GONZALEZ",
			"APONTE",
			"FERNANDEZ",
			"VILLAFANE",
			"ALVAREZ",
			"CALVENTE",
			"ROMAN",
			"ALVAREZ",
			"DURAN",
			"ROMAN",
			"PONCE",
			"SOLA",
			"GONZALEZ",
			"ACOSTA",
			"VELEZ",
			"RIVERA",
			"RODRIGUEZ",
			"LOPEZ",
			"CARTAGENA",
			"CINTRON",
			"MANAN",
			"VALENTIN",
			"ACEVEDO",
			"BARALT",
			"SOLIS",
			"GONZALEZ",
			"RAMOS",
			"SILVA",
			"BERMUDEZ",
			"SILVA",
			"ALEJANDRO",
			"MILLAN",
			"NIEVES",
			"GONZALEZ",
			"ARCE",
			"ARCE",
			"VILLANUEVA",
			"GONZALEZ",
			"GUTIERREZ",
			"PENA",
			"PEREZ",
			"MARTINEZ",
			"OSORIO",
			"ACEVEDO",
			"CABAN",
			"LOPEZ",
			"MORELL",
			"RIOS",
			"MONTALVO",
			"CANCEL",
			"VAZQUEZ",
			"DIAZ",
			"CLAUDIO",
			"REYES",
			"RODRIGUEZ",
			"SANCHEZ",
			"MORALES",
			"LUGO",
			"LEBRON",
			"REYES",
			"IZQUIERDO",
			"GUTIERREZ",
			"ALVAREZ",
			"ORAMA",
			"PEREZ",
			"CASANAS",
			"VEGA",
			"RIVERA",
			"RODRIGUEZ",
			"HERNANDEZ",
			"MERCADO",
			"MEDINA",
			"MARRERO",
			"DIAZ",
			"SANTIAGO",
			"HERNANDEZ",
			"PEREZ",
			"CINTRON",
			"NEGRON",
			"BRACERO",
			"OTERO",
			"CUBERO",
			"VAZQUEZ",
			"MARTINEZ",
			"BORRERO",
			"LOPEZ",
			"BORRES",
			"RODRIGUEZ",
			"RODRIGUEZ",
			"PIERALDI",
			"ORTIZ",
			"PEREZ",
			"BORRERO",
			"MALDONADO",
			"ORTIZ",
			"MORALES",
			"BACO",
			"RIOS",
			"AREIZAGA",
			"ACEVEDO",
			"FIGUEROA",
			"JIMENEZ",
			"TROCHE",
			"NIEVES",
			"RAMIREZ",
			"RIVERA",
			"NIEVES",
			"MERCADO",
			"SERRANO",
			"DELGADO",
			"FERNANDEZ",
			"LOPEZ",
			"ORTIZ",
			"ROSADO",
			"RIVERA",
			"CARABALLO",
			"RUIZ",
			"CORTES",
			"CRUZ",
			"GONZALEZ",
			"ROMAN",
			"MARIN",
			"NEGRON",
			"DEL_RIO",
			"JOVE",
			"MORALES",
			"QUINONES",
			"TORRES",
			"TORRES",
			"TORRES",
			"VILLARRUBIA",
			"RUIZ",
			"PINEIRO",
			"REYES",
			"PEREZ",
			"ROSAS",
			"MERCED",
			"MERLE",
			"REYES",
			"VELEZ",
			"PEREZ",
			"OLIVERAS",
			"ACEVEDO",
			"CRUZ",
			"RIOS",
			"VARGAS",
			"BURGOS",
			"CORREA",
			"GONZALEZ",
			"SERRANO",
			"NIEVES",
			"RODRIGUEZ",
			"VARELA",
			"FERRER",
	};
	
	private static String[] initials = {
			"H",
			"Y",
			"J",
			"A",
			"K",
			"E",
			"_",
			"_",
			"I",
			"A",
			"O",
			"_",
			"_",
			"G",
			"J",
			"J",
			"Y",
			"J",
			"G",
			"A",
			"A",
			"_",
			"_",
			"_",
			"D",
			"E",
			"_",
			"A",
			"M",
			"M",
			"E",
			"_",
			"S",
			"M",
			"S",
			"_",
			"J",
			"C",
			"A",
			"M",
			"A",
			"_",
			"O",
			"A",
			"D",
			"E",
			"E",
			"S",
			"D",
			"A",
			"D",
			"R",
			"A",
			"_",
			"D",
			"G",
			"M",
			"_",
			"_",
			"S",
			"A",
			"I",
			"F",
			"_",
			"J",
			"J",
			"U",
			"N",
			"Y",
			"N",
			"_",
			"A",
			"_",
			"A",
			"E",
			"_",
			"J",
			"E",
			"_",
			"G",
			"E",
			"A",
			"A",
			"O",
			"G",
			"_",
			"_",
			"M",
			"A",
			"A",
			"_",
			"A",
			"A",
			"A",
			"A",
			"G",
			"_",
			"_",
			"J",
			"A",
			"V",
			"J",
			"A",
			"A",
			"_",
			"E",
			"M",
			"A",
			"V",
			"A",
			"J",
			"A",
			"M",
			"A",
			"E",
			"K",
			"J",
			"S",
			"D",
			"Y",
			"J",
			"A",
			"D",
			"_",
			"A",
			"K",
			"_",
			"A",
			"E",
			"A",
			"O",
			"A",
			"Y",
			"A",
			"G",
			"A",
			"G",
			"D",
			"Z",
			"O",
			"R",
			"E",
			"B",
			"C",
			"A",
			"A",
			"D",
			"_",
			"O",
			"L",
			"X",
			"A",
			"_",
			"_",
			"J",
			"_",
			"S",
			"J",
			"A",
			"_",
			"J",
			"_",
			"A",
			"G",
			"_",
			"_",
			"G",
			"A",
			"A",
			"M",
			"_",
			"Y",
			"A",
			"A",
	};
	
	private static String[] sex = {
			"M",
			"M",
			"M",
			"M",
			"M",
			"F",
			"_",
			"F",
			"M",
			"M",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"M",
			"F",
			"M",
			"F",
			"F",
			"F",
			"F",
			"F",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"_",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"M",
			"_",
			"M",
			"M",
			"F",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"_",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"F",
			"F",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"F",
			"_",
			"M",
			"F",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"F",
			"_",
			"_",
			"M",
			"M",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"F",
			"M",
			"M",
			"F",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
			"M",
	};
}
