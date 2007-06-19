/********************************************************************
 * 
 * File		:	Activator.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Controls the lifecycle of the TPManager Plug-in
 * 
 ********************************************************************/
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

/********************************************************************
 * File			:	Activator.java
 *
 * Description	: 	Controls the lifecycle of the TPManager Plug-in
 *
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$
 * Copyright (c) 2007 Bob Brady
 *********************************************************************/
public class Activator implements BundleActivator {

	/** The plug-in shared instance */
	private static Activator mBundle;

	private EventAdminClient mEventAdminClient;

	private EventAdminHandler mEventAdminHandler;

	private TPBridgeClient mTPBridgeClient;

	private TPManager mTPManager;

	private ServiceTracker mHttpServiceTracker;

	/** The database session */
	private SessionFactory mHiberSessionFactory;

	public Activator() {
		mBundle = this;
	}

	/**
	 * Called when plug-in starts its lifecycle
	 * 
	 * Initializes the EventAdmin Service handler and client,
	 * sets the Hibernate database session
	 * 
	 * @param context the plug-in context
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
	}

	/*
	 * Closes the OSGi ServiceTracker and Hibernate
	 * database session
	 * 
	 * @param context the plug-in context
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
	
	// Public accessors

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
