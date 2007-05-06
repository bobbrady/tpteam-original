/*******************************************************************************
 * Copyright (c) 2005 Ed Burnette, Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ed Burnette, Composent, Inc. - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.tpbridge;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;

import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.user.User;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;
import org.eclipse.ecf.presence.ui.PresenceUI;
import org.osgi.framework.BundleContext;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.Client;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;

public class TPBridgeClient extends Client{
	PresenceUI presenceContainerUI = null;

	public TPBridgeClient(BundleContext context) {
		super(context);
		setTPMgrECFID(getTPTeamProps().getProperty(TPMANAGER_ECFID_KEY));
	}
	
	/**
	 * Create a new container instance, and connect to a remote server or group.
	 * 
	 * @param containerType the container type used to create the new container instance.  Must not be null.
	 * @param uri the uri that is used to create a targetID for connection.  Must not be null.
	 * @param nickname an optional String nickname.  May be null.
	 * @param connectData optional connection data.  May be null.
	 * @throws Exception
	 */
	public void createAndConnectClient(final String containerType, String uri,
			String nickname, final Object connectData, boolean isHeadless)
			throws Exception {
		// Create the new container 
		final IContainer client = ContainerFactory
				.getDefault().createContainer(containerType);
		// Create the targetID 
		ID targetID = IDFactory.getDefault().createID(client.getConnectNamespace(), uri);
		// Setup username
		String username = setupUsername(targetID,nickname);
	     // Check for IPresenceContainerAdapter....if it is, setup presence UI, if not setup shared object container
		IPresenceContainerAdapter pc = (IPresenceContainerAdapter) client
				.getAdapter(IPresenceContainerAdapter.class);
		if (pc != null && !isHeadless) {
			// Setup presence UI
			presenceContainerUI = new PresenceUI(client,pc);
			presenceContainerUI.showForUser(new User(targetID,username));
		} else throw new NullPointerException("IPresenceContainerAdapter interface not exposed by client with type "+containerType);
		// Now connect
		client.connect(targetID, getJoinContext(username, connectData));
		setContainer(client, targetID.getName(), ITPBridge.TPTEAM_BUDDY);
		sendProjGetRequest(targetID.getName());
	}


	private void sendProjGetRequest(String ecfID) {
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, getTPMgrECFID());
		dictionary.put(TPEvent.FROM, ecfID);
		if(Activator.getDefault() != null)
			Activator.getDefault().getEventAdminClient().sendEvent(ITPBridge.PROJ_GET_REQ_TOPIC, dictionary);
	}

	protected IConnectContext getJoinContext(final String username,
			final Object password) {
		return ConnectContextFactory.createUsernamePasswordConnectContext(username, password);
	}
	protected String setupUsername(ID targetID, String nickname) throws URISyntaxException {
		String username = null;
		if (nickname != null) {
			username = nickname;
		} else {
			username = new URI(targetID.getName()).getUserInfo();
			if (username == null || username.equals(""))
				username = System.getProperty("user.name");
		}
		return username;
	}


}
