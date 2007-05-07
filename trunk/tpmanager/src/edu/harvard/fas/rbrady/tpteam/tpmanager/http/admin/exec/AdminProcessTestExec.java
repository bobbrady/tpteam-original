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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestExecutionXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestExecutionUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

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

		try 
		{
			validateTestReq(req);
			StringBuffer reply = new StringBuffer(execTest());
			showPage(req, resp, reply, null, this);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}
	
	protected String execTest() throws Exception
	{
		StringBuffer reply = new StringBuffer();
		Transaction tx = null;
		String testType = null;
	
		try {
			// For standalone
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			
			
			Test test = (Test) s.load(Test.class, new Integer(mTestID));
			
			testType = test.getTestType().getName();
				
			mTPEvent = new TPEvent(ITPBridge.TEST_EXEC_RESULT_TOPIC,
					"Demo Project (TPTeam)", mRemoteUser, test.getName(),
					mTestID, "status");

			// Add all project user's ECF IDs to SEND_TO field of event
			Project proj = (Project)s.load(Project.class, new Integer(test.getProject().getId()));
			StringBuffer userECF = new StringBuffer();
			for(TpteamUser user : proj.getTpteamUsers())
			{
				if(userECF.length() == 0)
					userECF.append(user.getEcfId());
				else
					userECF.append("/" + user.getEcfId());
			}
			mTPEvent.getDictionary().put(TPEvent.SEND_TO, userECF.toString());
			String ECFID = Activator.getDefault().getTPBridgeClient().getTPMgrECFID();
			mTPEvent.getDictionary().put(TPEvent.ECFID_KEY, ECFID);
			mTPEvent.getDictionary().put(TPEvent.TEST_NAME_KEY, test.getName());

			
			s.flush();
			tx.commit();			
			
			if(testType.equalsIgnoreCase("JUNIT"))
			{
				Activator.getDefault().getTPManager().runJUnitTest(mTestID, mTPEvent);
			}
			TestExecutionUtil.insertTestExec(mTestID, mTPEvent);
			sendTestExecResponse(mTPEvent);

			reply
					.append("<h3 align=\"center\">TPManager Test Execution Result Page</h3>");
			reply.append("<h3>Test Name: " + mTPEvent.getTestName()
					+ "</h3>");
			reply.append("<h3>Exec Username: " + mRemoteUser + "</h3>\n<h3>testID: "
					+ mTestID + "</h3>");
			reply.append("<h3>TestSuite Execution Verdict: "
					+ mTPEvent.getDictionary().get(TPEvent.VERDICT_KEY) + "</h3>");
			

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
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

			if(Integer.parseInt(mTestID) <= 0 || test.getIsFolder() == 'Y' || test.getIsFolder() == 'y')
			{
				throw new Exception("Invalid Test Definition Selected for Execution", new Throwable("A Test Folders was Selected for Execution"));
			}

			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}
	
	private void sendTestExecResponse(TPEvent tpEvent) {
		tpEvent.setTopic(ITPBridge.TEST_EXEC_RESULT_TOPIC);
		tpEvent.getDictionary().put(TPEvent.FROM,
				Activator.getDefault().getTPBridgeClient().getTPMgrECFID());

		TPEntity execEntity = TestExecutionXML
				.getTPEntityFromExecEvent(tpEvent);
		String execXML = TestExecutionXML.getTPEntityXML(execEntity);
		tpEvent.getDictionary().put(TPEvent.TEST_EXEC_XML_KEY, execXML);

		System.out.println("TPManager runTests(): sending tpEvent w/verdict "
				+ tpEvent.getDictionary().get(TPEvent.VERDICT_KEY) + " & topic " + tpEvent.getTopic()
				+ " to " + tpEvent.getDictionary().get(TPEvent.SEND_TO));

		Activator.getDefault().getEventAdminClient().sendEvent(
				tpEvent.getTopic(), tpEvent.getDictionary());
	}
}
