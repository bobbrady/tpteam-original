/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestType;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.tptp.TPTestCRUD;

public class AddTestEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mTestName = null;

	protected String mTestDesc = null;

	protected String mTestTypeID = null;

	protected String mTestTypeName = null;

	protected String mProjID = null;

	protected String mParentID = null;

	protected String mRemoteUser = null;

	protected Test mTestStub = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		mTestName = req.getParameter("testName");
		mTestDesc = req.getParameter("testDesc");
		mTestTypeID = req.getParameter("testTypeID");
		mTestTypeName = req.getParameter("testTypeName");
		mProjID = req.getParameter("projID");
		mParentID = req.getParameter("parentID");
		mRemoteUser = req.getRemoteUser();

		try {

			saveTest(req, resp);
			StringBuffer reply = new StringBuffer("<h3>Add Test " + mTestName
					+ " was Successful</h3>");
			showPage(req, resp, reply, null, this);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	protected void saveTest(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		mTestStub = new Test();
		mTestStub.setName(mTestName);
		mTestStub.setDescription(mTestDesc);
		Test parent = new Test();
		parent.setId(Integer.parseInt(mParentID));
		parent.addChild(mTestStub);
		mTestStub.setParent(parent);
		TestType testType = new TestType();
		if (mTestTypeName.equalsIgnoreCase("Folder")) {
			testType.setName("Folder");
			mTestStub.setIsFolder('Y');
		} else if (mTestTypeName.equalsIgnoreCase("JUnit")) {
			mTestStub.setIsFolder('N');
			testType.setName("JUnit");
			JunitTest junit = new JunitTest();
			junit.setEclipseHome(req.getParameter("eclipseHome"));
			junit.setWorkspace(req.getParameter("eclipseWorkspace"));
			junit.setProject(req.getParameter("eclipseProj"));
			junit.setTestSuite(req.getParameter("testSuite"));
			junit.setReportDir(req.getParameter("reportDir"));
			junit.setTptpConnection(req.getParameter("tptpConn"));
			mTestStub.addJunitTest(junit);
		}
		mTestStub.setTestType(testType);

		TpteamUser addUser = new TpteamUser();
		addUser.setEcfId(Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());
		mTestStub.setCreatedBy(addUser);
		Project proj = new Project();
		proj.setId(Integer.parseInt(mProjID));
		mTestStub.setProject(proj);
		String testXML = TestXML.getXML(mTestStub);

		// Wrap info into TPEvent
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.ID_KEY, String.valueOf(mParentID));
		dictionary.put(TPEvent.PROJECT_ID_KEY, mProjID);
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, addUser.getEcfId());
		dictionary.put(TPEvent.TEST_XML_KEY, testXML);

		TPEvent tpEvent = new TPEvent(ITPBridge.TEST_ADD_REQ_TOPIC, dictionary);

		TPTestCRUD.sendTestAddResponse(tpEvent);
	}
}
