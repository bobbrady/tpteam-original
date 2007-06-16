/********************************************************************
 * 
 * File		:	TPBridgeClient.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Convenience client for the TPBridge OSGi Service
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.tpbridge;

import java.util.Hashtable;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.Client;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;

/*******************************************************************************
 * File 		: 	TPBridgeClient.java
 * 
 * Description 	: 	Convenience client for the TPBridge OSGi Service
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TPBridgeClient extends Client {

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the TPBuddy plug-in context
	 */
	public TPBridgeClient(BundleContext context) {
		super(context);
		setTPMgrECFID(getTPTeamProps().getProperty(TPMANAGER_ECFID_KEY));
	}

	/**
	 * Connects the ECF container to the communication server, Associates the
	 * connected container with the TPBridge, Sends a project request message to
	 * TPManager
	 * 
	 * @param container
	 *            the ECF XMPPS Container
	 * @param targetID
	 *            the ECF ID of the client
	 * @param connectContext
	 *            the ECF connection context
	 */
	public void connect(IContainer container, ID targetID,
			IConnectContext connectContext) {
		try {
			container.connect(targetID, connectContext);
			setContainer(container, targetID.getName(), ITPBridge.TPTEAM_BUDDY);
			sendProjGetRequest(targetID.getName());
		} catch (final Exception e) {
			MessageDialog.openError(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(),
					"TPBuddy Login Error", "ConnectionException: "
							+ e.getMessage());

		}
	}

	/**
	 * Sends a project request message to TPManager
	 * 
	 * @param ecfID
	 *            the ECF ID of the intended recipient
	 */
	private void sendProjGetRequest(String ecfID) {
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, getTPMgrECFID());
		dictionary.put(TPEvent.FROM, ecfID);
		if (Activator.getDefault() != null)
			Activator.getDefault().getEventAdminClient().sendEvent(
					ITPBridge.PROJ_GET_REQ_TOPIC, dictionary);
	}

}
