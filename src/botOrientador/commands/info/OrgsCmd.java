/**
 * 
 */
package botOrientador.commands.info;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.bot.managers.CommandI;

/**
 * @author Alfredo
 *
 */
public class OrgsCmd implements CommandI {

	private static final String COMMAND_LABEL = "select-orgs";
	
	private static final String OPTION_SELECTED_EMC = "emc";
	private static final String OPTION_SELECTED_IEEE = "ieee";
	private static final String OPTION_SELECTED_CASHI= "cashi";
	private static final String OPTION_SELECTED_WIE = "wie";
	private static final String OPTION_SELECTED_HKN = "hkn";
	private static final String OPTION_SELECTED_RAS_CSS = "ras";
	private static final String OPTION_SELECTED_COMP_SOC = "compsoc";
	private static final String OPTION_SELECTED_CAS = "cas";
	private static final String OPTION_SELECTED_PES = "pes";
	private static final String OPTION_SELECTED_ACM_CSE = "acmcse";
	private static final String OPTION_SELECTED_SHPE = "shpe";
	private static final String OPTION_SELECTED_ALPHA_AST = "alphaast";
	private static final String OPTION_SELECTED_EBM = "ebm";
	private static final String OPTION_SELECTED_PHOTONICS = "photonics";
	
	private boolean isGlobal;
	private List<OptionData> options;

	public OrgsCmd() {
		this.options = new ArrayList<>();
		
		this.options.add(new OptionData(OptionType.STRING, COMMAND_LABEL, "Escoje una organización", true)
			.addChoice("IEEE", OPTION_SELECTED_IEEE)
			.addChoice("HKN-Eta Kappa Nu", OPTION_SELECTED_HKN)
			.addChoice("WIE-Women in Engineering", OPTION_SELECTED_WIE)
			.addChoice("PES-Power & Energy Society", OPTION_SELECTED_PES)
			.addChoice("ALPHA_AST-Alpha Astrum", OPTION_SELECTED_ALPHA_AST)
			.addChoice("EMC-Electromagnetics Council", OPTION_SELECTED_EMC)
			.addChoice("COMP_SOC-Computer Society", OPTION_SELECTED_COMP_SOC)
			.addChoice("CAS-Circuits And Systems Society", OPTION_SELECTED_CAS)
			.addChoice("PHOTONICS-Photonics Society", OPTION_SELECTED_PHOTONICS)
			.addChoice("ACM_CSE-Association for Computing Machinery", OPTION_SELECTED_ACM_CSE)
			.addChoice("SHPE-Society of Hispanic Professional Engineers", OPTION_SELECTED_SHPE)
			.addChoice("CASHI-Computing Alliance of Hispanic-Serving Institution", OPTION_SELECTED_CASHI)
			.addChoice("RAS_CSS-Robotics and Automation Society & Control Systems Society", OPTION_SELECTED_RAS_CSS)
			.addChoice("EBM-Engineering in Medicine and Biology Society UPRM Student Chapter (EMB)", OPTION_SELECTED_EBM)
		);
	}
	
	@Override
	public void init(ReadyEvent event) {
		
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
		return "ls_student_orgs";
	}

	@Override
	public String getDescription() {
		return "Obten una lista te todas las organizaciones para tí";
	}

	@Override
	public List<OptionData> getOptions() {
		return options;
	}

	@Override
	public void dispose() {
		options.clear();		
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		OptionMapping programOption = event.getOption(COMMAND_LABEL);

		EmbedBuilder embedBuilder = null;

		switch (programOption.getAsString()) {
		case OPTION_SELECTED_EMC:
			embedBuilder = createEMC();
				break;
			case OPTION_SELECTED_IEEE:
				embedBuilder = createIEEE();
				break;
			case OPTION_SELECTED_CASHI:
				embedBuilder = createCASHI();
				break;
			case OPTION_SELECTED_WIE:
				embedBuilder = createWIE();
				break;
			case OPTION_SELECTED_HKN:
				embedBuilder = createHKN();
				break;
			case OPTION_SELECTED_RAS_CSS:
				embedBuilder = createRAS_CSS();
				break;
			case OPTION_SELECTED_COMP_SOC:
				embedBuilder = createCOMP_SOC();
				break;
			case OPTION_SELECTED_CAS:
				embedBuilder = createCAS();
				break;
			case OPTION_SELECTED_PES:
				embedBuilder = createPES();
				break;
			case OPTION_SELECTED_ACM_CSE:
				embedBuilder = createACM_CSE();
				break;
			case OPTION_SELECTED_SHPE:
				embedBuilder = createSHPE();
				break;
			case OPTION_SELECTED_ALPHA_AST:
				embedBuilder = createALPHA_AST();
				break;
			case OPTION_SELECTED_EBM:
				embedBuilder = createEBM();
				break;
			case OPTION_SELECTED_PHOTONICS:
				embedBuilder = createPHOTONICS();
				break;
		default:
			event.reply("Organizació incorrecta, intenta de nuevo").queue();
			return;
		}

		event.replyEmbeds(embedBuilder.build()).queue();
	}

	private EmbedBuilder createEMC() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's information on IEEE Electromagnetics Council:");

		embedBuilder.addField(
			"Descripción",
			"""
			Electromagnetics Council (EMC) is a joint chapter under the IEEE UPRM Branch.
			They are dedicated to promoting the area of Applied Electromagnetics and Electrical
			Engineering in general, as well as providing support to the Department of Electrical
			and Computer Engineering through workshops aimed at students. They help members
			develop technical skills before reaching more advanced courses in their curriculum,
			and give them the opportunity to learn about different areas within Applied Electromagnetics.	
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•President's Email: guelmary.fernandez@upr.edu
			•Vice-President's Email: carolina.rodriguez11@upr.edu
			•Facebook: https://www.facebook.com/emc.uprm
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createIEEE() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's information on IEEE:");

		embedBuilder.addField(
			"Descripción",
			"""
			IEEE’s core purpose is to develop industry leaders in professional and
			technical expertise for them to contribute in our community and society.
			IEEE-UPRM chapter’s mission is to provide its members with the highest
			and most competitive knowledge in diverse areas of engineering in order
			for them to expand their abilities consistent with industry needs.	
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: ieee@uprm.edu
			•Facebook & Twitter: ieeeuprm
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createCASHI() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's information on Computing Alliance of Hispanic-Serving Institution:");

		embedBuilder.addField(
			"Descripción",
			"""
			The Student Branch of the Computer Alliance of Hispanics Serving Institutions
			was established in 2006 to address the low representation of Hispanics in
			computing in both higher education and the workforce. CAHSI sets forth a flexible
			process using the conditions of collective impact that furthers the interchange of
			knowledge creation, adaptation, dissemination, and assessment. Goals include to
			increase the number of Hispanic students who enter the professorate in computing
			areas, or enter the computing workforce with advanced degrees, etc.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: cahsi@uprm.edu
			•Facebook: @uprm.cahsi
			•Twitter: @cahsi_uprm
			•Instagram: @cahsi_uprm
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createWIE() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's information on IEEE Women in Engineering:");

		embedBuilder.addField(
			"Descripción",
			"""
			IEEE-Women in Engineering is an organization dedicated to promote
			women engineers and scientists. Our goal is to encourage the growth
			of women pursuing degrees in engineering fields where they are strongly
			underrepresented. As a student organization, we support students who have
			already chosen an engineering career to continue on their path by
			offering workshops and orientations, which will help them grow academically
			and professionally. We also try to promote careers in engineering for high
			school level students by offering outreach activities such as the STAR Program
			and the Engineering Workshop, which aim to teach them the benefits of pursuing
			a career in engineering.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: wie@uprm.edu
			•Website: http://wie.uprm.edu
			•Facebook: facebook.com/wie.uprm
			•Phone: (787) 265-5402
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createHKN() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's information on Eta Kappa Nu:");

		embedBuilder.addField(
			"Descripción",
			"""
			They are the UPRM Eta Kappa Nu chapter called Lambda Tau. They are dedicated
			to encourage and recognize outstanding students in the fields of Science,
			Technology, Engineering, and Mathematics (STEM) within the UPR’s Mayaguez Campus.
			Candidates and members that have shown professional achievements and academic
			excellence will have the chance to form part of a dynamic environment dedicated
			to help them succeed in all aspects of their personal and professional activities.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: hkn@ece.uprm.edu
			•Facebook: https://www.facebook.com/hkn.uprm/
			•Instagram: @hkn_lambdatau
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createRAS_CSS() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's IEEE Robotics and Automation Society & Control Systems Society");

		embedBuilder.addField(
			"Descripción",
			"""
			The joint chapter of the Robotics and Automation Society & Control
			and Systems Society has as its mission to educate the community,
			both university and non-university, about robotics, process automation
			and control systems. We do this through our projects and workshops
			that we offer to both college students, and high/middle school students,
			to whom we take workshops during the school year.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Emails: ras@uprm.edu, css@uprm.edu
			•Facebook: https://www.facebook.com/RAS.UPRM/
			•Twitter: @Ras_Uprm
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createCOMP_SOC() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's information on IEEE Computer Society:");

		embedBuilder.addField(
			"Descripción",
			"""
			IEEE Computer Society is an IEEE technical branch dedicated to computing
			fields. It's a Hub for students interested in computing fields to network
			and learn, and is a Link between students and companies. Their mission
			and vision is to be the leading provider of technical information, community
			services, and personalized services to the world's computer professionals,
			and to be universally recognized for the contributions in various areas.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: computersociety@uprm.edu
			•Website: https://academic.uprm.edu/computersociety/
			•Facebook: https://www.facebook.com/computersociety.uprm/
			•Twitter: @SocietyIeee
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createCAS() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's information on IEEE Circuits And Systems Society:");

		embedBuilder.addField(
			"Descripción",
			"""
			At the IEEE Circuits and System, we do not focus only in developing brighter
			students; we provide them with the necessary tools and experience to help
			them grow into future professionals and responsible leaders. This we achieve
			through our seminars, technical sessions, distinguished lecturers, workshops,
			and by promoting the participation of the students on the society’s board.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: cas@uprm.edu
			•Website: http://cas.uprm.edu/
			•Facebook: https://www.facebook.com/cas.uprm/
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createPES() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's IEEE Power & Energy Society");

		embedBuilder.addField(
			"Descripción",
			"""
			PES is the leading provider of scientific information on electric power
			and energy for the betterment of society and the preferred professional
			development source for our members.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: pes.upr@gmail.com
			•Website: https://pesupr.wixsite.com/website/
			•Facebook: https://www.facebook.com/ieepesuprm/
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createACM_CSE() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's information on Association for Computing Machinery:");

		embedBuilder.addField(
			"Descripción",
			"""
			The ACM seeks to create an environment of convergence, offering quality
			and challenging learning experience,technical and professional enrichment
			that contribute to the individual development of each of our members.
			At the same time provide an excellent service focused on assisting all
			curricular and extracurricular needs for the course of his career to
			be one rewarding experience.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: acm@ece.uprm.edu
			•Facebook: https://www.facebook.com/ACM.ECE/
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createSHPE() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's some information on Society of Hispanic Professional Engineers:");

		embedBuilder.addField(
			"Descripción",
			"""
			SHPE is the nation's largest association dedicated to fostering
			Hispanic leadership in the STEM field. SHPE offers all Junior, 
			undergraduate, graduate, and professional members the necessary 
			resources to promote SHPE’s mission to realize its fullest potential 
			and to impact the world through STEM awareness, access, support and 
			development. Their objective was to form a national organization of professional 
			engineers to serve as role models in the Hispanic community.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Website: http://shpe.uprm.edu/
			•Facebook: https://www.facebook.com/SHPEUPRM/
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createALPHA_AST() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's some information on Alpha Astrum:");

		embedBuilder.addField(
			"Descripción",
			"""
			Alpha Astrum is a student-led non-profit organization at
			the University of Puerto Rico, Mayaguez Campus that designs,
			builds and launches experimental high-powered rockets. Our
			ultimate goal is to be the first Latin American team to
			launch the first-ever student-built rocket to space. Simultaneously,
			we raise awareness of the rapid growing aerospace industry in Puerto Rico
			and inspire the next-generation of young rocketeers.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: roboboat.uprm@gmail.com
			•Website: https://sites.google.com/view/alphaastrum/
			•Facebook: https://www.facebook.com/alphaastrum2018/
			•Twitter: https://twitter.com/AlphaAstrum
			•YouTube: https://www.youtube.com/channel/UCTbpnHw6zSzS9lITmuFn0Rg
			•Instagram: https://www.instagram.com/alphaastrum
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createEBM() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's some information on Engineering in Medicine and Biology Society UPRM Student Chapter (EMB):");

		embedBuilder.addField(
			"Descripción",
			"""
			The Engineering in Medicine and Biology Society (EMB) has grown into
			the largest international, member-based society of biomedical engineers
			and has made—through its meetings, publications and other
			activities—invaluable contributions to the advancement of biomedical and
			healthcare engineering.
			What you get by being a student member:
			- Workshops delving in areas ranging from basic cell culture to bio-informatics
			- Events such as conferences/seminars, scholarships, industry tours and more
			- Networking with individuals/professionals interested in the same areas as you!
			- Research and internship opportunities
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Email: embs@uprm.edu
			•Facebook: https://www.facebook.com/Engineering-in-Medicine-and-Biology-Society-UPRM-Student-Chapter-EMB-161103697313966
			•Twitter: https://twitter.com/EMBuprm
			""", false);
		
		return embedBuilder;
	}
	
	private EmbedBuilder createPHOTONICS() {
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setColor(new Color(70, 150, 90));
		embedBuilder.setTitle("Here's IEEE Photonics Society");

		embedBuilder.addField(
			"Descripción",
			"""
			The IEEE Photonics Society forms the hub of a vibrant technical
			community of more than 100,000 professionals dedicated to transforming
			breakthroughs in quantum physics into the devices, systems and products
			to revolutionize our daily lives. From ubiquitous and inexpensive global
			communications via fiber optics, to lasers for medical and other applications,
			to flat-screen displays, to photovoltaic devices for solar energy, to LEDs for
			energy-efficient illumination, there are myriad examples of the
			society’s impact on the world around us.
			""", false);

		embedBuilder.addField(
			"Contact Info & Social Media",
			"""
			•Emails: yesenia.rivera7@upr.edu - Presidenta | luis.cuba@upr.edu - Vice-Presidente
			""", false);
		
		return embedBuilder;
	}

}
