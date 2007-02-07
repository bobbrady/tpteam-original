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

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.util.ECFException;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

public class Client {

	protected ServiceTracker mServiceTracker;
	
	public Client(BundleContext context) {
		mServiceTracker = new ServiceTracker(context,
				ITPBridge.class.getName(), null);
		mServiceTracker.open();
	}

	
	public boolean setContainer(IContainer container, String containerType, String clientType) throws ECFException
	{
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			System.out.println("TPBridgeClient: tpBridge service ref is null");
			return false;
		}
		tpBridge.setContainer(container, containerType, clientType);
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
	
	

}
