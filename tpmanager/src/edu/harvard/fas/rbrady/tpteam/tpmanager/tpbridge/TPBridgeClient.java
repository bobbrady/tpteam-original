/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.tpbridge;

import java.util.ArrayList;

import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.util.ECFException;
import org.osgi.framework.BundleContext;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.Client;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;

public class TPBridgeClient extends Client{

	protected static String CONTAINER_TYPE = "ecf.xmpps.smack";
	private Namespace mNamespace = null;
	private IContainer mContainer = null;
	private ID mTargetID = null;
	


	public TPBridgeClient(BundleContext context) {
		super(context);
		try {
			setupContainer();
			// Then connect
			setTPMgrECFID(getTPTeamProps().getProperty(TPMANAGER_ECFID_KEY));
			String tpMgrPass = getTPTeamProps().getProperty(TPMANAGER_ECFID_PASSWORD);
			doConnect(getTPMgrECFID(), tpMgrPass);
			setContainer(getContainer(), mTargetID.getName(), ITPBridge.TPTEAM_MGR);

		} catch (ECFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	protected IContainer setupContainer() throws ECFException {
		if (mContainer == null) {
			mContainer = ContainerFactory.getDefault().createContainer(
					CONTAINER_TYPE);
			mNamespace = mContainer.getConnectNamespace();
		}
		return mContainer;
	}
	
	protected IContainer getContainer() {
		return mContainer;
	}

	protected Namespace getConnectNamespace() {
		return mNamespace;
	}

	public void connect(String account, String password) throws ECFException {
		setupContainer();
		doConnect(account, password);
	}

	protected void doConnect(String account, String password)
			throws ECFException {
		// Now connect
		ID targetID = IDFactory.getDefault().createID(mNamespace, account);
		mContainer.connect(targetID, ConnectContextFactory
				.createPasswordConnectContext(password));
		createID(account);
		mTargetID = targetID;
	}

	public ID createID(String name) {
		try {
			return IDFactory.getDefault().createID(mNamespace, name);
		} catch (IDCreateException e) {
			e.printStackTrace();
			return null;
		}
	}

	public synchronized boolean isConnected() {
		if (mContainer == null)
			return false;
		return (mContainer.getConnectedID() != null);
	}

	public synchronized void close() {
		if (mContainer != null) {
			mContainer.dispose();
			mContainer = null;
			mTargetID = null;
		}
	}



	public ArrayList<TPEvent> getEventLog() {
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			System.out
					.println("TPManager TPBridgeClient: tpBridge service ref is null");
		}
		return tpBridge.getEventLog();
	}

}
