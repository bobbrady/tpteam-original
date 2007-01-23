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

import org.eclipse.ecf.core.events.ISharedObjectMessageEvent;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import edu.harvard.fas.rbrady.tpteam.tpbridge.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.TPSharedObject;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

public class EventAdminHandler  implements EventHandler, Observer {
	
	private Hashtable<String, String[]> mDictionary = new Hashtable<String, String[]>();
	private ArrayList<TPEvent> mEvents;
	
	public EventAdminHandler(BundleContext context) {
		mEvents = new ArrayList<TPEvent>();
		mDictionary.put(EventConstants.EVENT_TOPIC,
				new String[] {ITPBridge.TEST_EXEC_REQ_TOPIC, ITPBridge.TEST_EXEC_RESULT_TOPIC});
		

		context.registerService(EventHandler.class.getName(), this, mDictionary);
	}

	public void handleEvent(Event event) {
		if (event.getTopic().equals(ITPBridge.TEST_EXEC_REQ_TOPIC)
				|| event.getTopic().equals(ITPBridge.TEST_EXEC_RESULT_TOPIC)) {
			System.out.println("TPBridge EventAdminHandler: Got " + event.getTopic()
					+ " Event for " + event.getProperty(TPEvent.TEST_NAME_KEY) +
					" with container " + event.getProperty(ITPBridge.CONTAINER_ID_KEY) +
					" and soID " + event.getProperty(ITPBridge.SHARED_OBJECT_ID_KEY));

			TPEvent tpEvent = new TPEvent(event);
		
			String myID = (String)event.getProperty("MYID");
			if(myID != null && !myID.equals(Activator.getEventAdminClient().toString()))
			{
			mEvents.add(tpEvent);
			
			Activator.getTPBridge().sendECFTPMsg(
					(String) event.getProperty(ITPBridge.CONTAINER_ID_KEY),
					(String) event.getProperty(ITPBridge.SHARED_OBJECT_ID_KEY),
					event);
			}
			else if(myID == null)
			{
				mEvents.add(tpEvent);
				
				Activator.getTPBridge().sendECFTPMsg(
						(String) event.getProperty(ITPBridge.CONTAINER_ID_KEY),
						(String) event.getProperty(ITPBridge.SHARED_OBJECT_ID_KEY),
						event);
			}

	
	}
	}
	
	public void update(Observable observable, Object object) {
		if (observable instanceof TPSharedObject
				&& object instanceof ISharedObjectMessageEvent) {
			ISharedObjectMessageEvent sharedObjEvent = (ISharedObjectMessageEvent) object;
			TPEvent tpEvent = (TPEvent) sharedObjEvent.getData();
			System.out
					.println("TPBridge: Update from SharedObject Got TPEvent topic "
							+ tpEvent.getTopic());
			mEvents.add(tpEvent);
			tpEvent.getDictionary().put("MYID", "FOOBAR");
			Activator.getEventAdminClient().sendEvent(tpEvent.getTopic(), tpEvent.getDictionary());
		}
	}
	
	public ArrayList<TPEvent> getEventLog() {
		return mEvents;
	}

	

}
