/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
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

public class EventAdminHandler implements EventHandler, Observer {

	private Hashtable<String, String[]> mDictionary = new Hashtable<String, String[]>();

	private ArrayList<TPEvent> mEvents;

	public EventAdminHandler(BundleContext context) {
		mEvents = new ArrayList<TPEvent>();
		// Set topics for events where tpbridge will send out of JVM on ECF SharedObect
		String topicsProp = Activator.getTPTeamProps().getProperty(
				ITPBridge.BRIDGE_EA_CLIENT_TOPICS_KEY);
		String topics[] = topicsProp.split(",");
		for(String topic : topics)
			System.out.println(ITPBridge.BRIDGE_EA_CLIENT_TOPICS_KEY + ": " + topic);
			
		mDictionary.put(EventConstants.EVENT_TOPIC, topics);
		context
				.registerService(EventHandler.class.getName(), this,
						mDictionary);
	}

	public void handleEvent(Event event) {

		System.out.println("TPBridge EventAdminHandler: Got " + event.getTopic()
				+ " Event from " + event.getProperty(TPEvent.SEND_TO));

		TPEvent tpEvent = new TPEvent(event);
		mEvents.add(tpEvent);

		String sendTo = tpEvent.getDictionary().get(TPEvent.SEND_TO);

		if (sendTo == null || sendTo.trim().equalsIgnoreCase(""))
			return;

		Activator.getTPBridge().sendECFTPMsg(event);

	}

	public void update(Observable observable, Object object) {
		if (observable instanceof TPSharedObject
				&& object instanceof ISharedObjectMessageEvent) {

			ISharedObjectMessageEvent sharedObjEvent = (ISharedObjectMessageEvent) object;
			TPEvent tpEvent = (TPEvent) sharedObjEvent.getData();
			System.out
					.println("TPBridge: Update from SharedObject Got TPEvent topic "
							+ tpEvent.getTopic());

			Activator.getEventAdminClient().sendEvent(tpEvent.getTopic(),
					tpEvent.getDictionary());

		}
	}

	public ArrayList<TPEvent> getEventLog() {
		return mEvents;
	}

}
