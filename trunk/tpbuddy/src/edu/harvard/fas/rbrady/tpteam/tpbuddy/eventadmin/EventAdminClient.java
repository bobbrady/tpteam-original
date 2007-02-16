/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;

public class EventAdminClient {

	private ServiceTracker mServiceTracker;

	public EventAdminClient(BundleContext context) {
		mServiceTracker = new ServiceTracker(context, EventAdmin.class
				.getName(), null);
		mServiceTracker.open();
	}

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
