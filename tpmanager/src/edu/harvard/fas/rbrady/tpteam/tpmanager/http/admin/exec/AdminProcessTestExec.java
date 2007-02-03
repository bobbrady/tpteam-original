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
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestExecution;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class AdminProcessTestExec extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	private String mRemoteUser;

	private String mTestID;
	
	private TPEvent mTPEvent;

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
			String reply = execTest();
			adminHeader(req, resp, null);
			adminReply(req, resp, reply);
			adminFooter(req, resp);
			insertTestExecResult(mTPEvent);

		} catch (Exception e) {
			String error = "<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>";
			adminError(req, resp, error);
			return;
		}
	}
	
	private String execTest() throws Exception
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
			
			s.flush();
			tx.commit();			
			
			if(testType.equalsIgnoreCase("JUNIT"))
			{
				Activator.getDefault().getTPManager().runJUnitTest(mTestID, mTPEvent);
			}

			reply
					.append("<h3 align=\"center\">TPManager Test Execution Result Page</h3>");
			reply.append("<h3>Test Name: " + mTPEvent.getTestName()
					+ "</h3>");
			reply.append("<h3>Exec Username: " + mRemoteUser + "</h3>\n<h3>testID: "
					+ mTestID + "</h3>");
			reply.append("<h3>TestSuite Execution Status: "
					+ mTPEvent.getStatus() + "</h3>");
			

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return reply.toString();
	}

	private void insertTestExecResult(TPEvent tpEvent) throws Exception {
		Transaction tx = null;
		try {
			int remoteUserId = ServletUtil.getRemoteUserID(mRemoteUser);
			// For standalone
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, new Integer(mTestID));
			TestExecution testExec = new TestExecution();
			testExec.setTest(test);
			if (tpEvent.getStatus() != null
					&& (tpEvent.getStatus().trim().indexOf("pass") != -1 || tpEvent
							.getStatus().trim().indexOf("PASS") != -1)) {
				testExec.setStatus('P');
			} else {
				testExec.setStatus('F');
			}
			testExec.setTpteamUser((TpteamUser) s.load(TpteamUser.class,
					remoteUserId));
			testExec.setExecDate(new Date());
			s.save(testExec);
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	private void validateTestReq(HttpServletRequest req) throws Exception {
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
}
