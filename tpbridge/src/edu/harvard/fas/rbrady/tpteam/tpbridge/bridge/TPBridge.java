/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.bridge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.provider.IContainerInstantiator;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.sharedobject.events.ISharedObjectMessageEvent;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.presence.IPresence;
import org.eclipse.ecf.presence.IPresenceListener;
import org.eclipse.ecf.presence.roster.IRosterEntry;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import edu.harvard.fas.rbrady.tpteam.tpbridge.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

public class TPBridge implements ITPBridge, IMessageReceiver, Observer {

	private Hashtable<String, String> mTPBridgeProps = new Hashtable<String, String>();

	/**
	 * Lookups done on this collection with containerID keys when clients
	 * request to send TPEvents
	 */
	private HashMap<String, IContainer> mIContainers;

	/**
	 * A convenience map only for the demo, needed since Bridge ECF connection
	 * done at bundle startup for demo and not via service request from view
	 * action as proposed for production
	 */
	private HashMap<String, String> mContainerIDs;

	private ServiceTracker mServiceTracker;
	
	private String targetIMUser;
	//private TrivialSharedObject sharedObject = null;
	private TPSharedObject sharedObject = null;
	private XMPPClient client = null;
	

	public TPBridge(BundleContext context) {
		mIContainers = new HashMap<String, IContainer>();
		mContainerIDs = new HashMap<String, String>();

		mTPBridgeProps.put(IMPLEMENTATION_TYPE, DEMO_IMPLEMENTATION_TYPE);

		populateContainerFactory(ITPBridge.DEFAULT_CONTAINER_TYPE);

		// Need to add connect dialog to clients, so they perform this operation
		// via osgi service
		connect();
		start(context);
	}

	private void populateContainerFactory(String containerType) {
/*
		// Create IContainerInstantiator manually
		IContainerInstantiator instantiator = new BaseContainerInstantiator();
		// Create a container description that has default ID & KeepAlive
		// descriptions
		
		String[] argTypes = { "org.eclipse.ecf.core.identity.ID",
				"java.lang.Integer" };
		String[] argDefaults = { "", "30000" };
		String[] argNames = { "id", "keepAlive" };
		ContainerTypeDescription cd = new ContainerTypeDescription(
				"ecf.xmpp.smack", instantiator, "ecf.xmpp.smack",
				argTypes, argDefaults, argNames);
		// Add this new description to ContainerFactory
	
		 */
		 /*
		
    	String namespaceName = "ecf.xmpp.smack";
    	String namespaceDescription = "XMPP (Jabber)";
    	Map namespaceProps = new HashMap();
    	namespaceProps.put("org.eclipse.ecf.ui.wizards.JoinGroupWizardPage.usepassword","true");
    	namespaceProps.put("org.eclipse.ecf.ui.wizards.JoinGroupWizardPage.examplegroupid","<user>@<xmppserver>");
    	namespaceProps.put("org.eclipse.ecf.ui.wizards.JoinGroupWizardPage..defaultgroupid","");
    	namespaceProps.put("org.eclipse.ecf.ui.wizards.JoinGroupWizardPage.urlprefix","xmpp:");
    	namespaceProps.put("org.eclipse.ecf.ui.wizards.JoinGroupWizardPage.groupIDLabel","Account:");
    	namespaceProps.put("org.eclipse.ecf.ui.wizards.JoinGroupWizardPage.namespace",namespaceName);
    	ContainerTypeDescription desc1 = new ContainerTypeDescription(namespaceName,namespaceDescription,namespaceProps);
*/
		
		
	//	IContainerFactory factory = ContainerFactory.getDefault();
		//factory.addDescription(cd);
	}

	public void start(BundleContext context) {

		context
				.registerService(ITPBridge.class.getName(), this,
						mTPBridgeProps);

		mServiceTracker = new ServiceTracker(context, EventAdmin.class
				.getName(), null);
		mServiceTracker.open();
	}

	public synchronized String connect() {
		/***********************************************************************
		 * Convenience function for demo only, Connect to defaults on bridge
		 * bundle startup
		 **********************************************************************/
		ISharedObjectContainer container = null;
		String containerType = ITPBridge.DEFAULT_CONTAINER_TYPE;
		String sharedObjIDStr = ITPBridge.DEFAULT_SHARED_OBJECT_ID;
		String targetIDStr = ITPBridge.DEFAULT_TARGET_ID;

		try {
			
			
			client = new XMPPClient(this);
			client.setupContainer();
			
			IPresenceListener presenceListener = new IPresenceListener(){

				public void handleRosterEntryAdd(IRosterEntry arg0) {
					// TODO Auto-generated method stub
					
				}

				public void handleRosterEntryRemove(IRosterEntry arg0) {
					// TODO Auto-generated method stub
					
				}

				public void handleRosterEntryUpdate(IRosterEntry arg0) {
					// TODO Auto-generated method stub
					
				}

				public void handlePresence(ID arg0, IPresence arg1) {
					// TODO Auto-generated method stub
					
				}

						};
			
			client.setPresenceListener(presenceListener);
			client.setupPresence();
			

			// Then connect
			String userName = "tpteam_2";
			String hostName = "jabber.org";
			String password = "hufogani_2";
			
			createTrivialSharedObjectForContainer();
			
			client.doConnect(userName + "@" + hostName, password);
			
			/*
			// Print all groups & entries
			Roster roster = client.getContainer().getRoster();
			Iterator rosterIter = roster.getGroups();
			while(rosterIter.hasNext())
			{
				RosterGroup group = (RosterGroup)rosterIter.next();
				System.out.println("Roster Group: " + group.getName() + ", "+ group.getEntryCount() + " Entries");
				Iterator entryIter = group.getEntries();
				while(entryIter.hasNext())
				{
					RosterEntry entry = (RosterEntry)entryIter.next();
					System.out.println("\tRoster Entry: " + entry.getName() + ", "+ entry.getStatus() + " Status");
				}
			}
			*/
			this.targetIMUser = "tpteam_1@jabber.org";
			// Send initial message for room
			
			/*
			client.sendMessage(targetIMUser,"Hi, I'm an IM robot");
			

			boolean running = true;
			int count = 0;
			// Loop ten times and send ten 'hello there' so messages to targetIMUser
			while (running && count++ < 10) {
				sendSOMessage(userName + ": " + count+"" +
						" hello there");
			wait(10000);
			}
			*/

			/*			
			// Create ECF IDs from String IDs
			ID targetID = IDFactory.getDefault().createStringID(targetIDStr);
			ID sharedObjectID = IDFactory.getDefault().createStringID(
					sharedObjIDStr);

			// Create container instance via ECF container factory
			container = SharedObjectContainerFactory.getDefault()
					.createSharedObjectContainer(containerType);

			// Create actual shared object
			TPSharedObject tpSharedObject = new TPSharedObject();

			// Add TPBridge as Observer to SharedObject
			tpSharedObject.addObserver(Activator.getEventAdminHandler());

			// Add shared object to container
			container.getSharedObjectManager().addSharedObject(sharedObjectID,
					tpSharedObject, new HashMap());

			// Join group identified by groupID
			container.connect(targetID, null);

			// Do this for demo purpose only!
			mContainerIDs.put(containerType + "@" + targetIDStr + "@"
					+ sharedObjIDStr, container.getID().getName());

			// container to hash map
			mIContainers.put(container.getID().getName(), container);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}

	
			System.out.println("client.getContainer.getID.getName: " + client.getContainer().getID().getName());
			mIContainers.put(client.getContainer().getID().getName(), client.getContainer());
		return client.getContainer().getID().getName();
	}

	protected void sendSOMessage(String msg) {
    	if (sharedObject != null) {
    	//	sharedObject.sendMessageTo(client.getID(targetIMUser),msg);
    		try {
				sharedObject.getContext().sendMessage(client.createID(targetIMUser),msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		System.out.println("sent msg to: " + targetIMUser);
    	}
    }
	
	public String connect(String containerType, String sharedObjIDStr,
			String targetIDStr) {
		/***********************************************************************
		 * This will be called by clients through OSGi service
		 * 
		 * Need to add dialog to client view to call this
		 * 
		 * For now, return connectionID obtained by default connect of bridge at
		 * bundle start
		 **********************************************************************/

		// Return containerID for bridge container collection lookups
		/*
		return mContainerIDs.get(containerType + "@" + targetIDStr + "@"
				+ sharedObjIDStr);
				*/
		//return mContainerIDs.get(client.getContainer().getID().getName());
		return "foobar";
	}

	public boolean sendECFTPMsg(String containerID, String sharedObjIDStr,
			String msg) {
		try {
			ISharedObjectContainer container = (ISharedObjectContainer) mIContainers
					.get(containerID);
			ID sharedObjectID = IDFactory.getDefault().createStringID(
					sharedObjIDStr);
			TPSharedObject tpSharedObject = (TPSharedObject) container
					.getSharedObjectManager().getSharedObject(sharedObjectID);
			tpSharedObject.getContext().sendMessage(null, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean sendECFTPMsg(String containerID, String sharedObjIDStr,
			Event event) {
		try {
			/*
			ISharedObjectContainer container = (ISharedObjectContainer) mIContainers
					.get(containerID);
			ID sharedObjectID = IDFactory.getDefault().createStringID(
					sharedObjIDStr);
			TPSharedObject tpSharedObject = (TPSharedObject) container
					.getSharedObjectManager().getSharedObject(sharedObjectID);
				*/
			TPEvent tpEvent = new TPEvent(event);
			//System.out.println("TPBridge.sendECFTPMsg: sent event " + tpEvent.getTestName());
			String[] ECFIDs = tpEvent.getDictionary().get(TPEvent.SEND_TO).split("/");
			for(String ECFID : ECFIDs)
			{
				//sharedObject.getContext().sendMessage(client.getID(targetIMUser),tpEvent);
				sharedObject.getContext().sendMessage(client.createID(ECFID),tpEvent);
				System.out.println("TPBridge.sendECFTPMsg: sent event " + tpEvent.getTestName() + " to " + ECFID);
			}

		//	tpSharedObject.getContext().sendMessage(null, tpEvent);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public ArrayList<TPEvent> getEventLog()
	{
		return Activator.getEventAdminHandler().getEventLog();
	}

	public void stop(BundleContext context) {
		mServiceTracker.close();
	}

	public synchronized void handleMessage(String from, String msg) {
		// TODO Auto-generated method stub
		notifyAll();
		
	}
	
	protected void createTrivialSharedObjectForContainer() throws ECFException {
		// Create a new GUID for new TrivialSharedObject instance
		ID newID = IDFactory.getDefault().createStringID(TPSharedObject.class.getName());
		// Create TrivialSharedObject
		//sharedObject = new TrivialSharedObject();
		sharedObject = new TPSharedObject();
		//sharedObject.addObserver(this);
		sharedObject.addObserver(Activator.getEventAdminHandler());
		// Add shared object to container
		((ISharedObjectContainer) client.getContainer().getAdapter(ISharedObjectContainer.class)).getSharedObjectManager().addSharedObject(newID, sharedObject, null);
		
}

	public void update(Observable observable, Object object) {
		// TODO Auto-generated method stub
		if (observable instanceof TPSharedObject
				&& object instanceof ISharedObjectMessageEvent) {
			System.out.println("Got msg: " + ((ISharedObjectMessageEvent)object).getData());
		}
		
	}


}
