/********************************************************************
 * 
 * File		:	Activator.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Controls the lifecycle of the TPBuddy Plug-in
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminClient;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.EventAdminHandler;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.tpbridge.TPBridgeClient;

/********************************************************************
 * File			:	Activator.java
 *
 * Description	: 	Controls the lifecycle of the TPBuddy Plug-in
 *
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$
 * Copyright (c) 2007 Bob Brady
 *********************************************************************/
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.harvard.fas.rbrady.tpteam.tpbuddy";

	// The shared instance
	private static Activator plugin;

	private EventAdminClient mEventAdminClient;

	private EventAdminHandler mEventAdminHandler;

	private TPBridgeClient mTPBridgeClient;
	
	private String mProjID;
	
	private String mProjName;
	
	public static final String APPLICATION_WINDOW_TITLE = "TPBuddy";
	public static final int APPLICATION_WINDOW_SIZE_X = 600;
	public static final int APPLICATION_WINDOW_SIZE_Y = 400;
	public static final String CONNECT_WIZARD_PAGE_TITLE = "Connect to XMPP Server";
	public static final String CONNECT_WIZARD_PAGE_DESCRIPTION = "Enter user id below and login";




	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

		mEventAdminHandler = new EventAdminHandler(context);
		mEventAdminClient = new EventAdminClient(context);
		mTPBridgeClient = new TPBridgeClient(context);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	// Public accessors
	
	public  EventAdminClient getEventAdminClient() {
		return mEventAdminClient;
	}

	public  EventAdminHandler getEventAdminHandler() {
		return mEventAdminHandler;
	}
	
	public TPBridgeClient getTPBridgeClient()
	{
		return mTPBridgeClient;
	}
	
	public void setProjID(String projID)
	{
		mProjID = projID;
	}
	
	public String getProjID()
	{
		return mProjID;
	}
	
	public void setProjName(String projName)
	{
		mProjName = projName;
	}
	
	public String getProjName()
	{
		return mProjName;
	}
}
