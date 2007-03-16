/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.eventadmin;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;

public class EventAdminClient {

	private ServiceTracker mServiceTracker;

	public EventAdminClient(BundleContext context) {
		mServiceTracker = new ServiceTracker(context, EventAdmin.class
				.getName(), null);
		mServiceTracker.open();
	}

	public boolean sendEvent(String topic, Hashtable<String, String> dictionary) {
		boolean messageSent = false;

		EventAdmin eventAdmin = (EventAdmin) mServiceTracker.getService();

		if (eventAdmin != null) {
			if (topic.equals(ITPBridge.TEST_EXEC_REQ_TOPIC)
					|| topic.equals(ITPBridge.TEST_EXEC_RESULT_TOPIC)
					|| topic.equals(ITPBridge.PROJ_GET_RESP_TOPIC)
					|| topic.equals(ITPBridge.TEST_TREE_GET_RESP_TOPIC)
					|| topic.equals(ITPBridge.TEST_DEL_RESP_TOPIC)) {
				System.out.println("EventAdminClient: Sent " + topic);
				eventAdmin.sendEvent(new Event(topic, dictionary));
				messageSent = true;
			}

		}
		return messageSent;
	}

}
