/********************************************************************
 * 
 * File		:	ITPBridge.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Interface for the TPBridge OSGi Service
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.bridge;

import java.util.ArrayList;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.hibernate.SessionFactory;
import org.osgi.service.event.Event;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

/*******************************************************************************
 * File 		: 	Client.java
 * 
 * Description 	: 	Interface for the TPBridge OSGi Service
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public interface ITPBridge {
	// Convenience public String variables used throughout TPTeam

	// TPTeam Event Topics
	public static final String TEST_EXEC_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/testexecreq";

	public static final String TEST_EXEC_RESULT_TOPIC = "edu/harvard/fas/rbrady/tpteam/testexecresult";

	public static final String TEST_DETAIL_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/testdetailreq";

	public static final String TEST_DETAIL_RESP_TOPIC = "edu/harvard/fas/rbrady/tpteam/testdetailresp";

	public static final String TEST_ADD_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/testaddreq";

	public static final String TEST_ADD_RESP_TOPIC = "edu/harvard/fas/rbrady/tpteam/testaddresp";

	public static final String TEST_UPDATE_DATA_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/testupdatedatreq";

	public static final String TEST_UPDATE_DATA_RESP_TOPIC = "edu/harvard/fas/rbrady/tpteam/testupdatedatresp";

	public static final String TEST_UPDATE_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/testupdatereq";

	public static final String TEST_UPDATE_RESP_TOPIC = "edu/harvard/fas/rbrady/tpteam/testupdateresp";

	public static final String TEST_DEL_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/testdelreq";

	public static final String TEST_DEL_RESP_TOPIC = "edu/harvard/fas/rbrady/tpteam/testdelresp";

	public static final String TEST_TREE_GET_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/testtreegetreq";

	public static final String TEST_TREE_GET_RESP_TOPIC = "edu/harvard/fas/rbrady/tpteam/testtreegetresp";

	public static final String PROJ_GET_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/projgetreq";

	public static final String PROJ_GET_RESP_TOPIC = "edu/harvard/fas/rbrady/tpteam/projgetresp";

	public static final String CHART_GET_DATA_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/chartgetdatareq";

	public static final String CHART_GET_DATA_RESP_TOPIC = "edu/harvard/fas/rbrady/tpteam/chartgetdataresp";

	// Metadata about TPTeam
	public static final String IMPLEMENTATION_TYPE = "IMPLEMENTATION_TYPE";

	public static final String DEMO_IMPLEMENTATION_TYPE = "DEMO";

	public static final String CONTAINER_ID_KEY = "CONTAINER_ID";

	public static final String SHARED_OBJECT_ID_KEY = "SHARED_OBJECT_ID";

	public static final String TARGET_ID_KEY = "TARGET_ID_KEY";

	public static final String DEFAULT_CONTAINER_TYPE = "ecf.xmpp.smack";

	public static final String DEFAULT_SHARED_OBJECT_ID = "myobject";

	public static final String CLIENT_TYPE = "CLIENT_TYPE";

	public static final String TPTEAM_MGR = "TPTEAM_MGR";

	public static final String TPTEAM_BUDDY = "TPTEAM_BUDDY";

	public static final String BRIDGE_EA_CLIENT_TOPICS_KEY = "tpbridge.eventadminclient.topics";

	/**
	 * Create a valid ECFID
	 * 
	 * @param name
	 *            The String name of the ID
	 * @return The ECFID
	 */
	public ID createID(String name);

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
	public void setContainer(IContainer container, String targetIDName,
			String clientType) throws ECFException;

	/**
	 * Gets the ECFID of the client to the TPBridge Service
	 * 
	 * @return the client ECFID
	 */
	public String getTargetIDName();

	/**
	 * Getter
	 * 
	 * @return The client type
	 */
	public String getClientType();

	/**
	 * Getter
	 * 
	 * @return The Hibernate database session factory
	 * @throws RuntimeException
	 */
	public SessionFactory getHibernateSessionFactory();

	/**
	 * Sends an OSGi Event to the OSGi EventAdmin Service
	 * 
	 * @param event
	 *            the OSGi Event to be sent
	 * @return true if message sent successfully, false otherwise
	 */
	public boolean sendECFTPMsg(Event event);

	/**
	 * Gets the list of TPTeam Events logged by the TPBridge OSGi Service
	 * 
	 * @return list log of TPTeam Events
	 */
	public ArrayList<TPEvent> getEventLog();

	/**
	 * Determines if TPTeam shared object has been instantiated and associated
	 * with an ECF communications container
	 * 
	 * @return true if TPSharedObject instantiated, false otherwise
	 */
	public boolean isSharedObjectActive();
}
