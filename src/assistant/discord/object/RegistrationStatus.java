/**
 * 
 */
package assistant.discord.object;

/**
 * @author Alfredo
 */
public enum RegistrationStatus {
	SUCCESS ("Registration Success"),
	FAILED  ("Already exists"),
	CANCEL  ("Canceled process"),
	ERROR   ("Something failed internally");
	
	private final String message;
	
	private RegistrationStatus(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public static class AtomicRegistrationStatus {
		private RegistrationStatus status;
		
		public AtomicRegistrationStatus(RegistrationStatus status) {
			this.status = status;
		}
		
		public void set(RegistrationStatus status) {
			this.status = status;
		}
		
		public RegistrationStatus get() {
			return status;
		}
	}
}
