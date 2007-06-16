/********************************************************************
 * 
 * File		:	EventAdminHandler.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	The OSGi Service that handles OSGi Events from the 
 * 				EventAdmin Service and acts as an observable of TPTeam 
 * 				Events will notify various TPBuddy views
 *   
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

/*******************************************************************************
 * File 		: 	EventAdminClient.java
 * 
 * Description 	: 	The OSGi Service that handles OSGi Events from the 
 * 					EventAdmin Service and acts as an observable of TPTeam 
 * 					Events will notify various TPBuddy views
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class EventAdminHandler extends Observable implements EventHandler {
	/** Table of key=value pairs of the OSGi Event */
	private Hashtable<String, String[]> mDictionary = new Hashtable<String, String[]>();
	/** A List log of all TPEvents received */
	private ArrayList<Event> mEvents;
	
	/**
	 * Constructor
	 * 
	 * Sets the OSGi Event topics to be handled by this service
	 * 
	 * Sets the 
	 * @param context
	 */
	public EventAdminHandler(BundleContext context) {
		mEvents = new ArrayList<Event>();
		mDictionary.put(EventConstants.EVENT_TOPIC,
				new String[] {ITPBridge.TEST_EXEC_REQ_TOPIC, ITPBridge.TEST_EXEC_RESULT_TOPIC, 
				ITPBridge.PROJ_GET_REQ_TOPIC, ITPBridge.PROJ_GET_RESP_TOPIC,
				ITPBridge.TEST_TREE_GET_REQ_TOPIC, ITPBridge.TEST_TREE_GET_RESP_TOPIC,
				ITPBridge.TEST_DEL_REQ_TOPIC, ITPBridge.TEST_DEL_RESP_TOPIC,
				ITPBridge.TEST_DETAIL_REQ_TOPIC, ITPBridge.TEST_DETAIL_RESP_TOPIC,
				ITPBridge.TEST_UPDATE_DATA_REQ_TOPIC, ITPBridge.TEST_UPDATE_DATA_RESP_TOPIC,
				ITPBridge.TEST_UPDATE_REQ_TOPIC, ITPBridge.TEST_UPDATE_RESP_TOPIC,
				ITPBridge.TEST_ADD_REQ_TOPIC, ITPBridge.TEST_ADD_RESP_TOPIC,
				ITPBridge.CHART_GET_DATA_REQ_TOPIC, ITPBridge.CHART_GET_DATA_RESP_TOPIC});
		
		context.registerService(EventHandler.class.getName(), this, mDictionary);
	}

	/**
	 * Handles an OSGi Event from the EventAdmin Service
	 * and notifies all registered TPBuddy Views so that
	 * they perform any necessary updates.
	 * 
	 * @param event the OSGi Event to be handled
	 */
	public void handleEvent(Event event) {
		System.out.println("TPBuddy EventAdminHandler: Got " + event.getTopic() + " Event for " + event.getProperty(TPEvent.TEST_NAME_KEY));
		TPEvent tpEvent = new TPEvent(event);
		setChanged();
		notifyObservers(tpEvent);
	}

	/**
	 * Getter
	 * @return the List log of TPTeam Events
	 */
	public ArrayList<Event> getEventLog() {
		return mEvents;
	}

}
