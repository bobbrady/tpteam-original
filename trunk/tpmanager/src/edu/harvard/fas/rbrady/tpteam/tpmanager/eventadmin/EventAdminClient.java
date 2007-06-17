/********************************************************************
 * 
 * File		:	EventAdminClient.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A client of the EventAdmin Service 
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.eventadmin;

import java.util.Hashtable;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

/*******************************************************************************
 * File 		: 	EventAdminClient.java
 * 
 * Description 	: 	A client of the EventAdmin Service.  It sends TPTeam Event
 * 					data encapsulated as an OSGi Event
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class EventAdminClient {

	/** The EventAdmin ServiceTracker */
	private ServiceTracker mServiceTracker;

	/**
	 * Constructor, gets and opens a tracker for
	 * the EventAdmin OSGi Service
	 * 
	 * @param context The TPManager plug-in context
	 */
	public EventAdminClient(BundleContext context) {
		mServiceTracker = new ServiceTracker(context, EventAdmin.class
				.getName(), null);
		mServiceTracker.open();
	}

	/**
	 * Sends an OSGi Event to the OSGi EventAdmin Service
	 * @param topic the event topic String
	 * @param dictionary a table of key=value pairs
	 * @return true if message sent successfully, false otherwise
	 */
	public boolean sendEvent(String topic, Hashtable<String, String> dictionary) {
		boolean messageSent = false;

		EventAdmin eventAdmin = (EventAdmin) mServiceTracker.getService();

		if (eventAdmin != null) {
			System.out.println("EventAdminClient: Sent " + topic);
			eventAdmin.sendEvent(new Event(topic, dictionary));
			messageSent = true;
		}
		return messageSent;
	}
}
