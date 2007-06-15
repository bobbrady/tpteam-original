/********************************************************************
 * 
 * File		:	Client.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Convenience client for the TPBridge OSGi Service
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.bridge;

import java.util.ArrayList;
import java.util.Properties;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.util.ECFException;
import org.hibernate.SessionFactory;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import edu.harvard.fas.rbrady.tpteam.tpbridge.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

/*******************************************************************************
 * File 		: 	Client.java
 * 
 * Description 	: 	Convenience client for the TPBridge OSGi Service
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class Client {
	/** The ServiceTracker for the TPBridge Service */
	protected ServiceTracker mServiceTracker;

	/** The TPManager ECFID property key */
	public static final String TPMANAGER_ECFID_KEY = "tpmanager.ecfID";

	/** The TPManager ECFID value */
	private String mTPMgrECFID = null;

	/** The TPManager ECF account password */
	public static final String TPMANAGER_ECFID_PASSWORD = "tpmanager.password";

	/**
	 * Default Constructor
	 */
	public Client() {

	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The TPBridge Plug-in Context
	 */
	public Client(BundleContext context) {
		mServiceTracker = new ServiceTracker(context,
				ITPBridge.class.getName(), null);
		mServiceTracker.open();
	}

	/**
	 * Setter
	 * 
	 * @param tpMgrECFID
	 *            The TPManager ECFID
	 */
	public void setTPMgrECFID(String tpMgrECFID) {
		mTPMgrECFID = tpMgrECFID;
	}

	/**
	 * Getter
	 * 
	 * @return The TPManager ECFID
	 */
	public String getTPMgrECFID() {
		return mTPMgrECFID;
	}

	/**
	 * Setter
	 * 
	 * @param serviceTracker
	 *            The TPBridge OSGi ServiceTracker
	 */
	public void setServiceTracker(ServiceTracker serviceTracker) {
		mServiceTracker = serviceTracker;
	}

	/**
	 * Getter
	 * 
	 * @return The TPBridge OSGi ServiceTracker
	 */
	public ServiceTracker getServiceTracker() {
		return mServiceTracker;
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
	public boolean setContainer(IContainer container, String targetIDName,
			String clientType) throws ECFException {
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			System.out.println("TPBridgeClient: tpBridge service ref is null");
			return false;
		}
		tpBridge.setContainer(container, targetIDName, clientType);
		System.out
				.println("TPBridgeClient: tpBridge service container was set");
		return true;
	}

	/**
	 * Getter
	 * 
	 * @return The Hibernate database session factory
	 * @throws RuntimeException
	 */
	public SessionFactory getHibernateSessionFactory() throws RuntimeException {
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			System.out.println("TPBridgeClient: tpBridge service ref is null");
			throw new RuntimeException(
					"TPBridge.Client.getHibernateSessionFactory() Error: tpBridge service ref is null.");
		}
		return tpBridge.getHibernateSessionFactory();
	}

	/**
	 * Gets the list of TPTeam Events logged by the TPBridge OSGi Service
	 * 
	 * @return list log of TPTeam Events
	 */
	public ArrayList<TPEvent> getEventLog() {
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			System.out.println("TPBridgeClient: tpBridge service ref is null");
		}
		return tpBridge.getEventLog();
	}

	/**
	 * Getter
	 * 
	 * @return the key=value properties associated with the TPBridge
	 */
	public Properties getTPTeamProps() {
		return Activator.getTPTeamProps();
	}

	/**
	 * Gets the ECFID of the client to the TPBridge Service
	 * 
	 * @return the client ECFID
	 */
	public String getTargetIDName() {
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			System.out.println("TPBridgeClient: tpBridge service ref is null");
		}
		return tpBridge.getTargetIDName();
	}

	/**
	 * Determines if TPTeam shared object has been instantiated and associated
	 * with an ECF communications container
	 * 
	 * @return true if TPSharedObject instantiated, false otherwise
	 */
	public boolean isSharedObjectActive() {
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			return false;
		}
		return tpBridge.isSharedObjectActive();

	}

}
