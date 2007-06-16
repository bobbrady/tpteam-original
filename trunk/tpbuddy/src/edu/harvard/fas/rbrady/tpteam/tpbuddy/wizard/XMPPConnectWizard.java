/********************************************************************
 * 
 * File		:	XMPPConnectWizard.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides a Wizard dialog for connecting to an XMPP
 * 				account
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.wizard;

import java.util.StringTokenizer;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.user.User;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;
import org.eclipse.ecf.presence.ui.PresenceUI;
import org.eclipse.ecf.ui.IConnectWizard;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.jivesoftware.smack.GoogleTalkConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.tpbridge.TPBridgeClient;

/*******************************************************************************
 * File 		: 	XMPPConnectWizard.java
 * 
 * Description 	: 	Provides a Wizard dialog for connecting to an XMPP
 * 					account
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class XMPPConnectWizard extends Wizard implements IConnectWizard {

	/** Default ECF Connection Container Type */
	public static final String CONTAINER_TYPE = "ecf.xmpps.smack";
	/** Default Google Talk Server */
	public static final String GOOGLE_TALK_HOST = "gmail.com";
	/** The XMPPConnectWizard page used for collecting data */
	XMPPConnectWizardPage page;
    /** The parent Shell */
	private Shell shell;
	/** The ECF Communication Container to be connected */
	private IContainer container;
	/** Ecf ID of user */
	private ID targetID;
	/** User ECF Security Context */
	private IConnectContext connectContext;

	/**
	 * Adds Wizard pages to the Wizard 
	 */
	public void addPages() {
		page = new XMPPConnectWizardPage();
		addPage(page);
	}

	/**
	 * Initialize the Wizard
	 */
	public void init(IWorkbench workbench, IContainer container) {
		shell = workbench.getActiveWorkbenchWindow().getShell();
		this.container = container;
	}

	/**
	 * Extract userName@hostName and password from 
	 * Wizard page, then connect and display buddy roster
	 */
	public boolean performFinish() {
		connectContext = ConnectContextFactory
				.createPasswordConnectContext(page.getPassword());

		try {
			targetID = IDFactory.getDefault().createID(
					container.getConnectNamespace(), page.getConnectID());
		} catch (final IDCreateException e) {
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					MessageDialog.openError(shell, "TPBuddy Login Error",
							"ConnectionException: " + e.getMessage());
				}
			});
			e.printStackTrace();
			return false;
		}
		// Get presence container adapter
		IPresenceContainerAdapter presenceAdapter = (IPresenceContainerAdapter) container
				.getAdapter(IPresenceContainerAdapter.class);
		// Create and show roster view user interface
		new PresenceUI(container, presenceAdapter).showForUser(new User(
				targetID));

		// Extract userName & hostName for quick connection validation
		StringTokenizer st = new StringTokenizer(targetID.getName(), "@");
		String userName = st.nextToken();
		String hostName = st.nextToken();
		boolean isConnValid = false;
		if (hostName.equalsIgnoreCase(GOOGLE_TALK_HOST)) {
			isConnValid = isGoogleTalkConnValid(userName, page.getPassword());
		} else {
			isConnValid = isXMPPConnValid(hostName, userName, page
					.getPassword());
		}

		// If connection not valid, exit to avoid hanging SSL
		if (!isConnValid)
			return true;

		// Proceed to bridge
		final TPBridgeClient bridgeClient = Activator.getDefault()
				.getTPBridgeClient();
		final IContainer bridgeContainer = this.container;
		final ID bridgeTargetID = this.targetID;
		final IConnectContext bridgeContext = this.connectContext;
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				bridgeClient.connect(bridgeContainer, bridgeTargetID,
						bridgeContext);
			}
		});

		return true;
	}

	/**
	 * Helper method to determine if a Google Talk 
	 * connection userName/Password is a valid pair
	 * 
	 * @param userName the name of the Google Talk account
	 * @param password the password of the Google Talk account
	 * @return true if the connection is valid, false otherwise
	 */
	private boolean isGoogleTalkConnValid(String userName, String password) {
		GoogleTalkConnection gtc;
		try {
			gtc = new GoogleTalkConnection();
			gtc.login(userName, page.getPassword());
			gtc.close();
		} catch (final XMPPException e) {
			e.printStackTrace();
			MessageDialog.openError(shell, "TPBuddy Login Error",
					"ConnectionException: " + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Helper method to determine if a generic XMPP 
	 * connection userName/Password is a valid pair
	 * for a given hostName
	 * 
	 * @param hostName name of the XMPP server host
	 * @param userName name of the XMPP account
	 * @param password password of the XMPP account
	 * @return true if the connection is valid, false otherwise
	 */
	private boolean isXMPPConnValid(String hostName, String userName,
			String password) {
		XMPPConnection XMPP;
		try {
			XMPP = new XMPPConnection(hostName);
			XMPP.login(userName, page.getPassword());
			XMPP.close();
		} catch (final XMPPException e) {
			e.printStackTrace();
			MessageDialog.openError(shell, "TPBuddy Login Error",
					"ConnectionException: " + e.getMessage());
			return false;
		}
		return true;
	}

}
