/*******************************************************************************
 * Copyright (c) 2005 Ed Burnette, Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ed Burnette, Composent, Inc. - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.tpbridge;

import java.util.Hashtable;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.presence.ui.PresenceUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.Client;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;

public class TPBridgeClient extends Client {
	PresenceUI presenceContainerUI = null;

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
	 * @param targetID
	 * @param connectContext
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
