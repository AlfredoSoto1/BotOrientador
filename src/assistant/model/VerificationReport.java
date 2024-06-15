/**
 * 
 */
package assistant.model;

/**
 * @author Alfredo
 */
public class VerificationReport {
	
	private String email;
	private String fullname;
	private String funfact;
	private String program;
	
	private String team;
	private String teamOrganization;

	private long teamrole;
	
	private boolean verified;
	
	private VerificationReport(String email, String fullname, String funfact, String program, String team, String teamOrganization, long teamrole, boolean verified) {
		this.email = email;
		this.fullname = fullname;
		this.funfact = funfact;
		this.program = program;
		this.team = team;
		this.teamOrganization = teamOrganization;
		this.teamrole = teamrole;
		this.verified = verified;
	}
	
	public String getEmail() {
		return email;
	}

	public String getFullname() {
		return fullname;
	}

	public String getFunfact() {
		return funfact;
	}

	public String getProgram() {
		return program;
	}

	public String getTeam() {
		return team;
	}

	public String getTeamOrganization() {
		return teamOrganization;
	}

	public long getTeamrole() {
		return teamrole;
	}

	public boolean isVerified() {
		return verified;
	}

	public static class AtomicVerificationReport {
		
		private VerificationReport report;
		
		public AtomicVerificationReport(VerificationReport report) {
			this.report = report;
		}
		
		public void set(VerificationReport report) {
			this.report = report;
		}
		
		public VerificationReport get() {
			return report;
		}
	}
	
	public static class VerificationReportBuilder {
		
		private String email;
		private String fullname;
		private String funfact;
		private String program;
		
		private String team;
		private String teamOrganization;

		private long teamrole;
		private boolean verified;
		
		public VerificationReport build() {
			return new VerificationReport(
				email,
				fullname,
				funfact,
				program,
				team,
				teamOrganization,
				teamrole,
				verified
			);
		}

		public VerificationReportBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		public VerificationReportBuilder fullname(String fullname) {
			this.fullname = fullname;
			return this;
		}
		
		public VerificationReportBuilder funfact(String funfact) {
			this.funfact = funfact;
			return this;
		}
		
		public VerificationReportBuilder program(String program) {
			this.program = program;
			return this;
		}
		
		public VerificationReportBuilder team(String team) {
			this.team = team;
			return this;
		}
		
		public VerificationReportBuilder teamOrganization(String teamOrganization) {
			this.teamOrganization = teamOrganization;
			return this;
		}
		
		public VerificationReportBuilder teamrole(long teamrole) {
			this.teamrole = teamrole;
			return this;
		}

		public VerificationReportBuilder verified(boolean verified) {
			this.verified = verified;
			return this;
		}
	}
}
