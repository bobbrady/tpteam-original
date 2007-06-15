/********************************************************************
 * 
 * File		:	TPBridge.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Implementation of the ITPBridge OSGi Service Interface
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.bridge;

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

/*******************************************************************************
 * File 		: 	TPBridge.java
 * 
 * Description 	: 	Implementation of the ITPBridge OSGi Service Interface
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TPBridge implements ITPBridge {
	/** Map of the TPTeam key=value properties */
	private Hashtable<String, String> mTPBridgeProps = new Hashtable<String, String>();
	/** The ServiceTracker for the EventAdmin Service */
	private ServiceTracker mServiceTracker;
	/** The TPTeam shared object instance */
	private TPSharedObject mSharedObject = null;
	/** The ECF communications container */
	private IContainer mContainer;
	/** The ECFID of the associated TPBridge Service client */
	private String mTargetIDName;
	/** The type of the TPBridge client */
	private String mClientType;

	/**
	 * Constructor
	 * @param conext The TPBridge plug-in context
	 */
	public TPBridge(BundleContext context) {
		mTPBridgeProps.put(IMPLEMENTATION_TYPE, DEMO_IMPLEMENTATION_TYPE);
		start(context);
	}

	/**
	 * Gets the ECFID of the client to the TPBridge Service
	 * 
	 * @return the client ECFID
	 */
	public String getTargetIDName() {
		return mTargetIDName;
	}

	/**
	 * Getter
	 * 
	 * @return The client type
	 */
	public String getClientType() {
		return mClientType;
	}

	/**
	 * Gets an EventAdmin ServiceTracker when TPBridge plug-in
	 * starts its lifecycle
	 * 
	 * @param context The TPBridge plug-in context 
	 */
	public void start(BundleContext context) {

		context
				.registerService(ITPBridge.class.getName(), this,
						mTPBridgeProps);

		mServiceTracker = new ServiceTracker(context, EventAdmin.class
				.getName(), null);
		mServiceTracker.open();
	}

	/**
	 * Associates an ECF Communications Container with the TPBridge Service
	 * 
	 * @param container
	 *            The ECF Container
	 * @param targetIDName
	 *            The ECFID of the client
	 * @param clientType
	 *            The type of client
	 * @return true if operation successful, false otherwise
	 * @throws ECFException
	 */
	public synchronized void setContainer(IContainer container,
			String targetIDName, String clientType) throws ECFException {
		mContainer = container;
		mTargetIDName = targetIDName;
		mClientType = clientType;
		createTrivialSharedObjectForContainer();
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
						+ tpEvent.getTopic() + " to " + ecfID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Gets the list of TPTeam Events logged by the TPBridge OSGi Service
	 * 
	 * @return list log of TPTeam Events
	 */
	public ArrayList<TPEvent> getEventLog() {
		return Activator.getEventAdminHandler().getEventLog();
	}

	/**
	 * Getter
	 * 
	 * @return The Hibernate database session factory
	 * @throws RuntimeException
	 */
	public SessionFactory getHibernateSessionFactory() {
		return HibernateUtil.getSessionFactory();
	}

	/**
	 * Closes the EventAdmin ServiceTracker just before the TPBridge
	 * plug-in ends its lifecycle
	 * 
	 * @param context The plug-in context
	 */
	public void stop(BundleContext context) {
		mServiceTracker.close();
	}

	/**
	 * Instantiates the TPSharedObject associated with the TPBridge
	 * Service, adds the EventAdmin Service handler as an observer,
	 * and associates itself with the ECF Container.
	 * 
	 * @throws ECFException
	 */
	protected void createTrivialSharedObjectForContainer() throws ECFException {
		// Create a new GUID for new TrivialSharedObject instance
		ID newID = IDFactory.getDefault().createStringID(
				TPSharedObject.class.getName());

		mSharedObject = new TPSharedObject();
		mSharedObject.addObserver(Activator.getEventAdminHandler());
		// Add shared object to container
		((ISharedObjectContainer) mContainer
				.getAdapter(ISharedObjectContainer.class))
				.getSharedObjectManager().addSharedObject(newID, mSharedObject,
						null);

	}

	/**
	 * Create a valid ECFID
	 * 
	 * @param name
	 *            The String name of the ID
	 * @return The ECFID
	 */
	public ID createID(String name) {
		try {
			return IDFactory.getDefault().createID(
					mContainer.getConnectNamespace(), name);
		} catch (IDCreateException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Determines if TPTeam shared object has been instantiated and associated
	 * with an ECF communications container
	 * 
	 * @return true if TPSharedObject instantiated, false otherwise
	 */
	public boolean isSharedObjectActive() {
		if (mSharedObject != null)
			return mSharedObject.getContext().isActive();
		else
			return false;
	}

}
