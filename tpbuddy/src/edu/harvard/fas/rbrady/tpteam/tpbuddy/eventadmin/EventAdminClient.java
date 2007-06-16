/********************************************************************
 * 
 * File		:	EventAdminClient.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A client of the EventAdmin Service 
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin;

import java.util.Hashtable;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

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
	 * @param context The TPBridge plug-in context
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
		
		System.out.println("TPBuddy EventAdminClient SendEvent");
		EventAdmin eventAdmin = (EventAdmin) mServiceTracker.getService();

		if (eventAdmin != null) {
				dictionary.put(ITPBridge.SHARED_OBJECT_ID_KEY, ITPBridge.DEFAULT_SHARED_OBJECT_ID);
				System.out.println("EventAdminClient: Sent " + topic + " Event for " + dictionary.get(TPEvent.TEST_NAME_KEY) +
					" with containerID " + dictionary.get(ITPBridge.CONTAINER_ID_KEY) + 
					" and soID " + dictionary.get(ITPBridge.SHARED_OBJECT_ID_KEY));
				eventAdmin.sendEvent(new Event(topic, dictionary));
				messageSent = true;
		}
		else
		{
			System.out.println("TPBuddy EventAdminClient: eventAdmin is null");
		}
		return messageSent;
	}
}
