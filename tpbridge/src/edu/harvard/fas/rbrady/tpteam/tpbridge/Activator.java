/********************************************************************
 * 
 * File		:	Activator.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Controls the lifecycle of the TPBridge Plug-in
 * 
 ********************************************************************/
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

/********************************************************************
 * File			:	Activator.java
 *
 * Description	: 	Controls the lifecycle of the TPBridge Plug-in
 *
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$
 * Copyright (c) 2007 Bob Brady
 *********************************************************************/
public class Activator implements BundleActivator {
	
	/** The TPBridge OSGi Service POJO */
	private static TPBridge mTPBridge;
	/** The EventAdmin Service Event Handler */
	private static EventAdminHandler mEventAdminHandler;
	/** The Client of the EventAdmin Service */
	private static EventAdminClient mEventAdminClient;
	/** Directory where TPTeam Java Properties file is stored */
	public static final String TPTEAM_PROP_DIR = "data";
	/** File name of TPTeam Java Properties file */
	public static String TPTEAM_PROP_FILE = "tpteam.properties";
	/** TPTeam Property key=value pairs */
	private static Properties mTPTeamProps;
	
	/*
	 * OSGi framework bundle start hook 
	 */
	public void start(BundleContext context) throws Exception {
		loadTPTeamProps(context);
		mEventAdminHandler = new EventAdminHandler(context);
		mEventAdminClient = new EventAdminClient(context);
		mTPBridge = new TPBridge(context);
		Platform.getBundle("org.eclipse.equinox.event").start();
	}

	/*
	 * OSGi framework bundle stop hook
	 */
	public void stop(BundleContext context) throws Exception {
		mTPBridge.stop(context);
	}
	
	/**
	 * Getter
	 * @return The EventAdmin Service Handler
	 */
	public static EventAdminHandler getEventAdminHandler()
	{
		return mEventAdminHandler;
	}
	
	/**
	 * Getter 
	 * @return The TPBridge Service POJO
	 */
	public static TPBridge getTPBridge()
	{
		return mTPBridge;
	}
	
	/**
	 * Getter
	 * @return The EventAdmin Service client
	 */
	public static EventAdminClient getEventAdminClient()
	{
		return mEventAdminClient;
	}

	/**
	 * Getter
	 * @return The TPTeam Properties
	 */
	public static Properties getTPTeamProps()
	{
		return mTPTeamProps;
	}
	
	/**
	 * Reads in the TPTeam property key=value pairs
	 * @param context The bundle context
	 */
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
