/********************************************************************
 * 
 * File		:	EventAdminHandler.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	The OSGi Service that handles OSGi Events from the 
 * 				EventAdmin Service and acts as an observer of TPTeam 
 * 				Events received by the TPSharedObject of the TPBridge 
 * 				OSGi Service
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.eventadmin;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.ecf.core.sharedobject.events.ISharedObjectMessageEvent;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import edu.harvard.fas.rbrady.tpteam.tpbridge.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.TPSharedObject;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

/*******************************************************************************
 * File 		: 	EventAdminClient.java
 * 
 * Description 	: 	The OSGi Service that handles OSGi Events from the 
 * 					EventAdmin Service and acts as an observer of TPTeam Events 
 * 					received by the TPSharedObject of the TPBridge OSGi Service
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class EventAdminHandler implements EventHandler, Observer {

	/** Table of key=value pairs of the OSGi Event */
	private Hashtable<String, String[]> mDictionary = new Hashtable<String, String[]>();
	/** A List log of all TPEvents received */
	private ArrayList<TPEvent> mEvents;

	/**
	 * Constructor
	 * 
	 * Sets the OSGi Event topics to be handled by this service
	 * 
	 * Sets the 
	 * @param context
	 */
	public EventAdminHandler(BundleContext context) {
		mEvents = new ArrayList<TPEvent>();
		// Set topics for events where tpbridge will send out of JVM on ECF
		// SharedObect
		String topicsProp = Activator.getTPTeamProps().getProperty(
				ITPBridge.BRIDGE_EA_CLIENT_TOPICS_KEY);
		String topics[] = topicsProp.split(",");
		for (String topic : topics)
			System.out.println(ITPBridge.BRIDGE_EA_CLIENT_TOPICS_KEY + ": "
					+ topic);

		mDictionary.put(EventConstants.EVENT_TOPIC, topics);
		context
				.registerService(EventHandler.class.getName(), this,
						mDictionary);
	}

	/**
	 * Handles an OSGi Event from the EventAdmin Service
	 * and fowards it to the TPBridge Service so it can
	 * be sent out-of-JVM
	 * 
	 * @param event the OSGi Event to be handled
	 */
	public void handleEvent(Event event) {
		// Info message
		System.out.println("TPBridge EventAdminHandler: Got "
				+ event.getTopic() + " Event SEND_TO "
				+ event.getProperty(TPEvent.SEND_TO));
		// Adapt the OSGi Event to a TPEvent and log it
		TPEvent tpEvent = new TPEvent(event);
		mEvents.add(tpEvent);
		
		// If no ECF recipient, skip sending
		String sendTo = tpEvent.getDictionary().get(TPEvent.SEND_TO);
		if (sendTo == null || sendTo.trim().equalsIgnoreCase(""))
			return;

		// Send out-of-JVM via TPBridge OSGi Service
		Activator.getTPBridge().sendECFTPMsg(event);
	}

	/**
	 * Handles incoming out-of-JVM messages from the TPBridge 
	 * shared object and routes to all subscribers via the 
	 * OSGi EventAdmin Service
	 * 
	 * @param observable the TPSharedObject of the TPBridge Service
	 * @param object the ECF shared object message event to be handled
	 */
	public void update(Observable observable, Object object) {
		if (observable instanceof TPSharedObject
				&& object instanceof ISharedObjectMessageEvent) {
			// Extract the encapsulated TPEvent
			ISharedObjectMessageEvent sharedObjEvent = (ISharedObjectMessageEvent) object;
			TPEvent tpEvent = (TPEvent) sharedObjEvent.getData();
			System.out
					.println("TPBridge: Update from SharedObject Got TPEvent topic "
							+ tpEvent.getTopic() + " from " + tpEvent.getDictionary().get(TPEvent.FROM));
			// Included so outbound messages don't get into infinite loop
			if(isTopicForwardable(tpEvent.getTopic()))
			{
				Activator.getEventAdminClient().sendEvent(tpEvent.getTopic(),
					tpEvent.getDictionary());
			}
		}
	}

	/**
	 * Getter 
	 * @return the log list of TPEvents
	 */
	public ArrayList<TPEvent> getEventLog() {
		return mEvents;
	}
	
	/**
	 * Helper method to determine if an event having the given
	 * topic should be forwarded out-of-JVM by the TPBridge 
	 * OSGi Service
	 * @param topic the topic of an event
	 * @return true if it should be forwarded out-of-JVM, false otherwise
	 */
	private boolean isTopicForwardable(String topic)
	{
		for(String bridgeTopic : mDictionary.get(EventConstants.EVENT_TOPIC))
		{
			if(topic.equalsIgnoreCase(bridgeTopic))
				return false;
		}
		return true;
	}
}
