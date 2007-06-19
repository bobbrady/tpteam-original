/********************************************************************
 * 
 * File		:	TPBridgeClient.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Convenience client for the TPBridge OSGi Service
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.tpbridge;

import java.util.ArrayList;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.util.ECFException;
import org.osgi.framework.BundleContext;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.Client;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

/*******************************************************************************
 * File 		: 	TPBridgeClient.java
 * 
 * Description 	: 	Convenience client for the TPBridge OSGi Service
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TPBridgeClient extends Client {

	protected static String CONTAINER_TYPE = "ecf.xmpps.smack";

	private Namespace mNamespace = null;

	private IContainer mContainer = null;

	private ID mTargetID = null;

	/**
	 * Constructor,  initializes the XMPPS communication
	 * container and gets a connection with the ID and password
	 * given in the tpteam.properties file
	 * 
	 * @param context the TPManager plug-in context
	 */
	public TPBridgeClient(BundleContext context) {
		super(context);
		try {
			setupContainer();
			// Then read tpteam.properties file and connect
			setTPMgrECFID(getTPTeamProps().getProperty(TPMANAGER_ECFID_KEY));
			String tpMgrPass = getTPTeamProps().getProperty(
					TPMANAGER_ECFID_PASSWORD);
			doConnect(getTPMgrECFID(), tpMgrPass);
			setContainer(getContainer(), mTargetID.getName(),
					ITPBridge.TPTEAM_MGR);

		} catch (ECFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Creates the ECF XMPPS Container and Namespace
	 * member variables
	 * 
	 * @return the ECF XMPPS Container
	 * @throws ECFException
	 */
	protected IContainer setupContainer() throws ECFException {
		if (mContainer == null) {
			mContainer = ContainerFactory.getDefault().createContainer(
					CONTAINER_TYPE);
			mNamespace = mContainer.getConnectNamespace();
		}
		return mContainer;
	}


	/**
	 * Initializes the ECF XMPPS Container and
	 * gets an XMPPS connection
	 * 
	 * @param account the XMPPS account ID
	 * @param password the XMPPS plain text password
	 * @throws ECFException
	 */
	public void connect(String account, String password) throws ECFException {
		setupContainer();
		doConnect(account, password);
	}

	/**
	 * Binds the ECF XMPPS Container with a connection
	 * 
	 * @param account the XMPPS account ID
	 * @param password the XMPPS plain text password
	 * @throws ECFException
	 */
	protected void doConnect(String account, String password)
			throws ECFException {
		// Now connect
		ID targetID = IDFactory.getDefault().createID(mNamespace, account);
		mContainer.connect(targetID, ConnectContextFactory
				.createPasswordConnectContext(password));
		mTargetID = targetID;
	}

	/**
	 * Determine if the ECF XMPPS Container is
	 * connected
	 * 
	 * @return true if connected, false otherwise
	 */
	public synchronized boolean isConnected() {
		if (mContainer == null)
			return false;
		return (mContainer.getConnectedID() != null);
	}

	/**
	 * Close the ECF XMPPS Container connection
	 */
	public synchronized void close() {
		if (mContainer != null) {
			mContainer.dispose();
			mContainer = null;
			mTargetID = null;
		}
	}

	/**
	 * Gets the list of all TPEvents received out-of-JVM
	 * by the TPBridge OSGi Service
	 * 
	 * @return the List of TPEvents
	 */
	public ArrayList<TPEvent> getEventLog() {
		ITPBridge tpBridge = (ITPBridge) mServiceTracker.getService();
		if (tpBridge == null) {
			System.out
					.println("TPManager TPBridgeClient: tpBridge service ref is null");
		}
		return tpBridge.getEventLog();
	}
	
	// Member variable accessors
	
	protected IContainer getContainer() {
		return mContainer;
	}

	protected Namespace getConnectNamespace() {
		return mNamespace;
	}
}
