/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.TPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.eventadmin.EventAdminClient;
import edu.harvard.fas.rbrady.tpteam.tpbridge.eventadmin.EventAdminHandler;

public class Activator implements BundleActivator {

	private static TPBridge mTPBridge;
	
	private static EventAdminHandler mEventAdminHandler;
	
	private static EventAdminClient mEventAdminClient;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		mEventAdminHandler = new EventAdminHandler(context);
		mEventAdminClient = new EventAdminClient(context);
		mTPBridge = new TPBridge(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		mTPBridge.stop(context);
	}
	
	public static EventAdminHandler getEventAdminHandler()
	{
		return mEventAdminHandler;
	}
	
	public static TPBridge getTPBridge()
	{
		return mTPBridge;
	}
	
	public static EventAdminClient getEventAdminClient()
	{
		return mEventAdminClient;
	}

}
