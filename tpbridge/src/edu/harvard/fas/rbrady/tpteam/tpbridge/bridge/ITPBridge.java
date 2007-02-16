/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.bridge;

import java.util.ArrayList;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.osgi.service.event.Event;

import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;


public interface ITPBridge {

	public static final String TEST_EXEC_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/testexecreq";

	public static final String TEST_EXEC_RESULT_TOPIC = "edu/harvard/fas/rbrady/tpteam/testexecresult";
	
	public static final String PROJ_GET_REQ_TOPIC = "edu/harvard/fas/rbrady/tpteam/projgetreq";
	
	public static final String PROJ_GET_RESP_TOPIC = "edu/harvard/fas/rbrady/tpteam/projgetresp";

	public static final String IMPLEMENTATION_TYPE = "IMPLEMENTATION_TYPE";

	public static final String DEMO_IMPLEMENTATION_TYPE = "DEMO";
	
	public static final String CONTAINER_ID_KEY = "CONTAINER_ID";
	
	public static final String SHARED_OBJECT_ID_KEY = "SHARED_OBJECT_ID";
	
	public static final String TARGET_ID_KEY = "TARGET_ID_KEY";

	public static final String DEFAULT_CONTAINER_TYPE = "ecf.xmpp.smack";

	public static final String DEFAULT_SHARED_OBJECT_ID = "myobject";

	public static final String DEFAULT_TARGET_ID = "tpteam_1@jabber.org";
	
	public static final String CLIENT_TYPE = "CLIENT_TYPE";
	
	public static final String TPTEAM_MGR = "TPTEAM_MGR";
	
	public static final String TPTEAM_BUDDY = "TPTEAM_BUDDY";

	public ID createID(String name);
	
	public void setContainer(IContainer container, String targetIDName, String clientType) throws ECFException;
	
	public String getTargetIDName();
	
	public String getClientType();
	
	public boolean sendECFTPMsg(Event event);
	
	public ArrayList<TPEvent> getEventLog();
}
