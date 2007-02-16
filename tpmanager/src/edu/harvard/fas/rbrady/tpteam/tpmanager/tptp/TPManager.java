/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.tptp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.eventadmin.EventAdminHandler;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Test;

public class TPManager implements Observer {

	private AutomationClient mAutomationClient;

	/** Local TPTP report directory */
	public static final String REPORT = "/home/tpteam/tests/reports";

	/** Location of Agent Controller server */
	public static final String CONNECTION = "tptp:rac://localhost:10002/default";

	/** Locale to format test result time */
	private static final Locale USA_LOCALE = new Locale("en", "US");

	/** DateTime format for test reusult */
	private static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss.SSS z", USA_LOCALE);

	public TPManager() {
		mAutomationClient = new AutomationClient();
	}

	public void update(Observable observable, Object object) {
		if (observable instanceof EventAdminHandler
				&& object instanceof TPEvent) {
			TPEvent tpEvent = (TPEvent) object;
			String tpTopic = tpEvent.getTopic();
			System.out.println("TPManager Update: got tpEvent w/topic "
					+ tpTopic);
			
			if(tpTopic.equals(ITPBridge.PROJ_GET_REQ_TOPIC))
			{
				sendProjGetResponse(tpEvent);
			}

			if (tpTopic.equalsIgnoreCase(
					ITPBridge.TEST_EXEC_REQ_TOPIC)) {
				try {
					runTest(tpEvent.getID(), tpEvent);
				} catch (Exception e) {
					// Should throw TPTeam ExceptionEvent here?
					e.printStackTrace();
				}
			}
		}

	}
	
	private void sendProjGetResponse(TPEvent tpEvent) {
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, tpEvent.getDictionary().get(TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.PROJECT_KEY, "TEST_PROJ");
		System.out.println("TPManager.sendProjGetResponse: Send To: " + dictionary.get(TPEvent.SEND_TO) + 
				", From: " + dictionary.get(TPEvent.FROM));
		Activator.getDefault().getEventAdminClient().sendEvent(ITPBridge.PROJ_GET_RESP_TOPIC, dictionary);
	}


	public void runTest(String testID, TPEvent tpEvent) throws Exception {
		Transaction tx = null;
		String testType = null;

		try {
			// For standalone
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();

			Test test = (Test) s.load(Test.class, new Integer(testID));
			testType = test.getTestType().getName();

			s.flush();
			tx.commit();

			if (testType.equalsIgnoreCase("JUNIT")) {
				runJUnitTest(testID, tpEvent);
			}

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}

	}

	public void runJUnitTest(String testID, TPEvent tpEvent) throws Exception {
		Transaction tx = null;
		String eclipseHome = null;
		String workspace = null;
		String project = null;
		String suite = null;
		String tptpConn = null;
		String report = null;

		try {
			// For standalone
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();

			Test test = (Test) s.load(Test.class, new Integer(testID));
			JunitTest junit = test.getJunitTests().toArray(new JunitTest[0])[0];

			eclipseHome = junit.getEclipseHome();
			workspace = junit.getWorkspace();
			project = junit.getProject();
			suite = junit.getTestSuite();
			report = junit.getReportDir();
			tptpConn = junit.getTptpConnection();
			s.flush();
			tx.commit();

			String status = mAutomationClient.run(eclipseHome, workspace,
					project, new String[] { suite }, report, tptpConn);
			System.out.println("Test Status: " + status);
			status += ": " + getDateTime();

			// ********************************************************
			tpEvent.setStatus(status);
			tpEvent.setTopic(ITPBridge.TEST_EXEC_RESULT_TOPIC);
			tpEvent.getDictionary().put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient().getTPMgrECFID());
			System.out
					.println("TPManager runTests(): sending tpEvent w/status "
							+ status + " & topic " + tpEvent.getTopic());
			Activator.getDefault().getEventAdminClient().sendEvent(
					tpEvent.getTopic(), tpEvent.getDictionary());
			// ***********************************************************/

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}
	


	public String getDateTime() {
		Date now = new Date();
		return SIMPLE_DATE_FORMATTER.format(now);
	}

	public static void main(String[] args) {
		TPManager tmMgr = new TPManager();
		// Comment out appropriate lines in runTests above to run null,null
		// params
		try {
			tmMgr.runJUnitTest("4", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
