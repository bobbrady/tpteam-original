/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.bridge;

import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.util.ECFException;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import edu.harvard.fas.rbrady.tpteam.tpbridge.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

public class Client {

	protected ServiceTracker mServiceTracker;
	
	public static final String TPMANAGER_ECFID_KEY = "tpmanager.ecfID";
	
	public static final String TPMANAGER_ECFID_PASSWORD = "tpmanager.password";
	
	private String mTPMgrECFID = null;
	
	public Client(BundleContext context) {
		mServiceTracker = new ServiceTracker(context,
				ITPBridge.class.getName(), null);
		mServiceTracker.open();
	}

	public void setTPMgrECFID(String tpMgrECFID)
	{
		mTPMgrECFID = tpMgrECFID;
	}
	
	public String getTPMgrECFID()
	{
		return mTPMgrECFID;
	}
	
	
	public boolean setContainer(IContainer container, String targetIDName, String clientType) throws ECFException
	{
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			System.out.println("TPBridgeClient: tpBridge service ref is null");
			return false;
		}
		tpBridge.setContainer(container, targetIDName, clientType);
		System.out.println("TPBridgeClient: tpBridge service container was set");
		return true;
	}

	public ArrayList<TPEvent> getEventLog()
	{
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			System.out.println("TPBridgeClient: tpBridge service ref is null");
		}
		return tpBridge.getEventLog();
	}
	
	public Properties getTPTeamProps()
	{
		return Activator.getTPTeamProps();
	}

}
