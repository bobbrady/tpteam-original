/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager;

import org.hibernate.SessionFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import edu.harvard.fas.rbrady.tpteam.tpmanager.eventadmin.EventAdminClient;
import edu.harvard.fas.rbrady.tpteam.tpmanager.eventadmin.EventAdminHandler;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.HttpServiceTracker;
import edu.harvard.fas.rbrady.tpteam.tpmanager.tpbridge.TPBridgeClient;
import edu.harvard.fas.rbrady.tpteam.tpmanager.tptp.TPManager;

public class Activator implements BundleActivator {

	// The shared instance
	private static Activator mBundle;

	private EventAdminClient mEventAdminClient;

	private EventAdminHandler mEventAdminHandler;

	private TPBridgeClient mTPBridgeClient;

	private TPManager mTPManager;

	private ServiceTracker mHttpServiceTracker;

	private SessionFactory mHiberSessionFactory;

	public Activator() {
		mBundle = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		mEventAdminHandler = new EventAdminHandler(context);
		mEventAdminClient = new EventAdminClient(context);
		mTPBridgeClient = new TPBridgeClient(context);
		mTPManager = new TPManager();
		mEventAdminHandler.addObserver(mTPManager);

		mHttpServiceTracker = new HttpServiceTracker(context);
		mHttpServiceTracker.open();

		mHiberSessionFactory = mTPBridgeClient.getHibernateSessionFactory();

		// OracleTest.dbConnect();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		mHttpServiceTracker.close();
		mHttpServiceTracker = null;
		mHiberSessionFactory.close();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return mBundle;
	}

	public EventAdminClient getEventAdminClient() {
		return mEventAdminClient;
	}

	public EventAdminHandler getEventAdminHandler() {
		return mEventAdminHandler;
	}

	public TPBridgeClient getTPBridgeClient() {
		return mTPBridgeClient;
	}

	public TPManager getTPManager() {
		return mTPManager;
	}

	public SessionFactory getHiberSessionFactory() {
		return mHiberSessionFactory;
	}

}
