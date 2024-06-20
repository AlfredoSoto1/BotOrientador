/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class AdvancementEntity {
	
	private int id;
	private int memberid;
	
	private String name;
	private int messageCount;
	private int profanityCount;
	private int activityPoints;
	
	public AdvancementEntity() {
		
	}

	public AdvancementEntity(int id, int memberid, String name, int messageCount, int profanityCount, int activityPoints) {
		this.id = id;
		this.memberid = memberid;
		this.name = name;
		this.messageCount = messageCount;
		this.profanityCount = profanityCount;
		this.activityPoints = activityPoints;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public int getProfanityCount() {
		return profanityCount;
	}

	public void setProfanityCount(int profanityCount) {
		this.profanityCount = profanityCount;
	}

	public int getActivityPoints() {
		return activityPoints;
	}

	public void setActivityPoints(int activityPoints) {
		this.activityPoints = activityPoints;
	}
	
}
