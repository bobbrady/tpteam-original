/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.exec;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.tptp.TPTestExec;

public class AdminProcessTestExec extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mRemoteUser;

	protected String mTestID;

	protected TPEvent mTPEvent;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		mRemoteUser = req.getRemoteUser();
		mTestID = req.getParameter("testID");

		try {
			validateTestReq(req);
			StringBuffer reply = new StringBuffer(execTest());
			showPage(req, resp, reply, null, this);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	protected String execTest() throws Exception {

		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		String fromECFID = Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID();
		dictionary.put(TPEvent.FROM, fromECFID);
		mTPEvent = new TPEvent(ITPBridge.TEST_EXEC_REQ_TOPIC, dictionary);

		TPTestExec.runTest(mTestID, mTPEvent);
		TPTestExec.sendTestExecResponse(mTPEvent);

		StringBuffer reply = new StringBuffer();
		reply
				.append("<h3 align=\"center\">TPManager Test Execution Result Page</h3>");
		reply.append("<h3>Test Name: " + mTPEvent.getTestName() + "</h3>");
		reply.append("<h3>Exec Username: " + mRemoteUser
				+ "</h3>\n<h3>testID: " + mTestID + "</h3>");
		reply.append("<h3>TestSuite Execution Verdict: "
				+ mTPEvent.getDictionary().get(TPEvent.VERDICT_KEY) + "</h3>");

		return reply.toString();
	}

	protected void validateTestReq(HttpServletRequest req) throws Exception {
		/*
		 * String testID = req.getParameter("testID"); if(testID == null) return
		 * false; if(!mTests.containsKey(testID)) return false;
		 */
		Transaction tx = null;
		try {
			// For standalone
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, new Integer(mTestID));

			if (Integer.parseInt(mTestID) <= 0 || test.getIsFolder() == 'Y'
					|| test.getIsFolder() == 'y') {
				throw new Exception(
						"Invalid Test Definition Selected for Execution",
						new Throwable(
								"A Test Folders was Selected for Execution"));
			}

			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}
}
