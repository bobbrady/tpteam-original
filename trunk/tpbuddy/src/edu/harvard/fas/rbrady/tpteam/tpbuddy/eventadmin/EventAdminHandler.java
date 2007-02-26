/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
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

public class EventAdminHandler extends Observable implements EventHandler {
	
	private Hashtable<String, String[]> mDictionary = new Hashtable<String, String[]>();
	private ArrayList<Event> mEvents;
	
	public EventAdminHandler(BundleContext context) {
		mEvents = new ArrayList<Event>();
		mDictionary.put(EventConstants.EVENT_TOPIC,
				new String[] {ITPBridge.TEST_EXEC_REQ_TOPIC, ITPBridge.TEST_EXEC_RESULT_TOPIC, 
				ITPBridge.PROJ_GET_REQ_TOPIC, ITPBridge.PROJ_GET_RESP_TOPIC,
				ITPBridge.TEST_TREE_GET_REQ_TOPIC, ITPBridge.TEST_TREE_GET_RESP_TOPIC});
		context.registerService(EventHandler.class.getName(), this, mDictionary);
	}

	public void handleEvent(Event event) {
		/*
		if(event.getTopic().equals(ITPBridge.TEST_EXEC_REQ_TOPIC) || event.getTopic().equals(ITPBridge.TEST_EXEC_RESULT_TOPIC))
		{
			System.out.println("TPBuddy EventAdminHandler: Got " + event.getTopic() + " Event for " + event.getProperty(TPEvent.TEST_NAME_KEY));
			TPEvent tpEvent = new TPEvent(event);
			setChanged();
			notifyObservers(tpEvent);
		}
		*/
		System.out.println("TPBuddy EventAdminHandler: Got " + event.getTopic() + " Event for " + event.getProperty(TPEvent.TEST_NAME_KEY));
		TPEvent tpEvent = new TPEvent(event);
		setChanged();
		notifyObservers(tpEvent);

	}

	public ArrayList getEventLog() {
		return mEvents;
	}

}
