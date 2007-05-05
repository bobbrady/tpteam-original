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
import java.util.Hashtable;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.util.ECFException;
import org.hibernate.SessionFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import edu.harvard.fas.rbrady.tpteam.tpbridge.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

public class TPBridge implements ITPBridge, IMessageReceiver {

	private Hashtable<String, String> mTPBridgeProps = new Hashtable<String, String>();

	private ServiceTracker mServiceTracker;

	private TPSharedObject mSharedObject = null;

	private IContainer mContainer;

	private String mTargetIDName;

	private String mClientType;

	public TPBridge(BundleContext context) {
		mTPBridgeProps.put(IMPLEMENTATION_TYPE, DEMO_IMPLEMENTATION_TYPE);
		start(context);
	}

	public String getTargetIDName() {
		return mTargetIDName;
	}

	public String getClientType() {
		return mClientType;
	}

	public void start(BundleContext context) {

		context
				.registerService(ITPBridge.class.getName(), this,
						mTPBridgeProps);

		mServiceTracker = new ServiceTracker(context, EventAdmin.class
				.getName(), null);
		mServiceTracker.open();
	}

	public synchronized void setContainer(IContainer container,
			String targetIDName, String clientType) throws ECFException {
		mContainer = container;
		mTargetIDName = targetIDName;
		mClientType = clientType;
		createTrivialSharedObjectForContainer();
	}

	protected void sendSOMessage(String msg, String sendTo) {
		if (mSharedObject != null) {

			try {
				mSharedObject.getContext().sendMessage(createID(sendTo), msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean sendECFTPMsg(Event event) {
		try {
			TPEvent tpEvent = new TPEvent(event);
			String sendTo = tpEvent.getDictionary().get(TPEvent.SEND_TO);

			if (sendTo == null || sendTo.trim().equalsIgnoreCase(""))
				return false;

			String[] ecfIDs = sendTo.split("/");
			for (String ecfID : ecfIDs) {
				if (ecfID.equalsIgnoreCase(mTargetIDName))
					continue;
				mSharedObject.getContext()
						.sendMessage(createID(ecfID), tpEvent);
				System.out.println("TPBridge.sendECFTPMsg: sent event "
						+ tpEvent.getTestName() + " to " + ecfID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public ArrayList<TPEvent> getEventLog() {
		return Activator.getEventAdminHandler().getEventLog();
	}

	public SessionFactory getHibernateSessionFactory() {
		return HibernateUtil.getSessionFactory();
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

		ID newID = IDFactory.getDefault().createStringID(
				TPSharedObject.class.getName());

		// Create TrivialSharedObject
		// sharedObject = new TrivialSharedObject();
		mSharedObject = new TPSharedObject();
		// sharedObject.addObserver(this);
		mSharedObject.addObserver(Activator.getEventAdminHandler());
		// Add shared object to container
		((ISharedObjectContainer) mContainer
				.getAdapter(ISharedObjectContainer.class))
				.getSharedObjectManager().addSharedObject(newID, mSharedObject,
						null);

	}

	public ID createID(String name) {
		try {
			return IDFactory.getDefault().createID(
					mContainer.getConnectNamespace(), name);
		} catch (IDCreateException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isSharedObjectActive() {
		if (mSharedObject != null)
			return mSharedObject.getContext().isActive();
		else
			return false;
	}

}
