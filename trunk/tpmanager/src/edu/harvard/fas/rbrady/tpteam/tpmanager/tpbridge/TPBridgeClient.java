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

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class TPBridgeClient {

	private ServiceTracker mServiceTracker;

	public TPBridgeClient(BundleContext context) {
		mServiceTracker = new ServiceTracker(context,
				ITPBridge.class.getName(), null);
		mServiceTracker.open();
	}
	
	
	public ArrayList<TPEvent> getEventLog()
	{
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			System.out.println("TPManager TPBridgeClient: tpBridge service ref is null");
		}
		return tpBridge.getEventLog();
	}
	
	

}
