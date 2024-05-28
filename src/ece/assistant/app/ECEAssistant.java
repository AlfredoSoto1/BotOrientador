/**
 * 
 */
package ece.assistant.app;

import ece.assistant.cmd.managers.ContactsCmdManager;
import ece.assistant.cmd.managers.FileCmdManager;
import ece.assistant.cmd.managers.InfoCmdManager;
import ece.assistant.cmd.managers.LinksCmdManager;
import ece.assistant.cmd.managers.ModeratorCmdManager;
import services.bot.core.BotApplication;
import services.bot.core.ListenerManager;

/**
 * 
 */
public class ECEAssistant extends BotApplication {

	private InfoCmdManager infoControl;
//	private LinksCmdManager linkControl;
//	private ContactsCmdManager contactsControl;
	private ModeratorCmdManager moderatorManager;
//	private FileCmdManager fileUploadControl;
	
	/**
	 * 
	 * @param token
	 */
	public ECEAssistant(String token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void prepareListeners(ListenerManager listener) {
//		this.infoControl = new InfoCmdManager();
//		this.linkControl = new LinksCmdManager();
//		this.contactsControl = new ContactsCmdManager();
//		this.fileUploadControl = new FileCmdManager();
		this.moderatorManager = new ModeratorCmdManager(this);

//		listener.addComponents(infoControl.getComponents());
//		listener.addComponents(linkControl.getComponents());
//		listener.addComponents(contactsControl.getComponents());
		listener.addComponents(moderatorManager.getComponents());
//		listener.addComponents(fileUploadControl.getComponents());
	}

	@Override
	protected void dispose() {
//		this.infoControl.dispose();
//		this.linkControl.dispose();
//		this.contactsControl.dispose();
		this.moderatorManager.dispose();
//		this.fileUploadControl.dispose();
	}
}
