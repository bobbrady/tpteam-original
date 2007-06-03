/**
 * File: Activator.java
 *
 * Description:	Controls the lifecycle of the TPBridge Plug-in
 *
 * Author: Bob Brady, rpbrady@gmail.com
 */
package edu.harvard.fas.rbrady.tpteam.tpbridge;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.TPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.eventadmin.EventAdminClient;
import edu.harvard.fas.rbrady.tpteam.tpbridge.eventadmin.EventAdminHandler;

/**
 ***************************************************************************
 * File			:	
 *
 * Description	: 	
 *
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$
 * Copyright (c) 2007 Bob Brady
 ****************************************************************************
 */
public class Activator implements BundleActivator {

	private static TPBridge mTPBridge;
	
	private static EventAdminHandler mEventAdminHandler;
	
	private static EventAdminClient mEventAdminClient;
	
	public static final String TPTEAM_PROP_DIR = "data";

	public static String TPTEAM_PROP_FILE = "tpteam.properties";
	
	private static Properties mTPTeamProps;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		loadTPTeamProps(context);
		mEventAdminHandler = new EventAdminHandler(context);
		mEventAdminClient = new EventAdminClient(context);
		mTPBridge = new TPBridge(context);
		Platform.getBundle("org.eclipse.equinox.event").start();
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

	public static Properties getTPTeamProps()
	{
		return mTPTeamProps;
	}
	
	private void loadTPTeamProps(BundleContext context) {
		try {
			Platform.getLocation().toFile();
			InputStream is = FileLocator.openStream(context.getBundle(),
					new Path(TPTEAM_PROP_DIR + "/" + TPTEAM_PROP_FILE), false);
			mTPTeamProps = new Properties();
			mTPTeamProps.load(is);
			is.close();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
