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
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestExecutionXML;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.eventadmin.EventAdminHandler;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.ProjectUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestExecutionUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestUtil;

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
			try {
				if (tpTopic.equals(ITPBridge.PROJ_GET_REQ_TOPIC)) {
					sendProjGetResponse(tpEvent);
				} else if (tpTopic
						.equalsIgnoreCase(ITPBridge.TEST_EXEC_REQ_TOPIC)) {

					runTest(tpEvent.getID(), tpEvent);
					sendTestExecResponse(tpEvent);
				} else if (tpTopic
						.equalsIgnoreCase(ITPBridge.TEST_TREE_GET_REQ_TOPIC)) {
					sendTestTreeGetResponse(tpEvent);
				} else if (tpTopic
						.equalsIgnoreCase(ITPBridge.TEST_DEL_REQ_TOPIC)) {
					deleteTest(tpEvent);
					sendDelTestResponse(tpEvent);
				} else if (tpTopic
						.equalsIgnoreCase(ITPBridge.TEST_DETAIL_REQ_TOPIC)) {
					sendTestDetailResponse(tpEvent);
				} else if (tpTopic
						.equalsIgnoreCase(ITPBridge.TEST_UPDATE_DATA_REQ_TOPIC)) {
					sendTestUpdateDatResponse(tpEvent);
				} else if (tpTopic
						.equalsIgnoreCase(ITPBridge.TEST_UPDATE_REQ_TOPIC)) {
					sendTestUpdateResponse(tpEvent);
				} else if (tpTopic
						.equalsIgnoreCase(ITPBridge.TEST_ADD_REQ_TOPIC)) {
					sendTestAddResponse(tpEvent);
				}
			} catch (Exception e) {
				// Should throw TPTeam ExceptionEvent here?
				e.printStackTrace();
			}
		}
	}

	private void sendProjGetResponse(TPEvent tpEvent) throws Exception {
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, tpEvent.getDictionary().get(
				TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());
		dictionary.put(TPEvent.PROJ_PROD_XML_KEY, ProjectUtil
				.getProjProdXML(tpEvent));
		System.out.println("TPManager.sendProjGetResponse: Send To: "
				+ dictionary.get(TPEvent.SEND_TO) + ", From: "
				+ dictionary.get(TPEvent.FROM));
		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.PROJ_GET_RESP_TOPIC, dictionary);
	}

	private void sendTestTreeGetResponse(TPEvent tpEvent) throws Exception {
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, tpEvent.getDictionary().get(
				TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());
		dictionary.put(TPEvent.TEST_TREE_XML_KEY, TestUtil
				.getTestTreeXML(tpEvent));
		System.out.println("TPManager.sendTestTreeGetResponse: Send To: "
				+ dictionary.get(TPEvent.SEND_TO) + ", From: "
				+ dictionary.get(TPEvent.FROM));
		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.TEST_TREE_GET_RESP_TOPIC, dictionary);
	}

	private void sendDelTestResponse(TPEvent tpEvent) throws Exception {
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, tpEvent.getDictionary().get(
				TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());
		dictionary.put(TPEvent.ID_KEY, tpEvent.getID());
		System.out.println("TPManager.sendTestDelResponse: Send To: "
				+ dictionary.get(TPEvent.SEND_TO) + ", From: "
				+ dictionary.get(TPEvent.FROM));
		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.TEST_DEL_RESP_TOPIC, dictionary);
	}

	private void sendTestExecResponse(TPEvent tpEvent) {
		tpEvent.setTopic(ITPBridge.TEST_EXEC_RESULT_TOPIC);
		tpEvent.getDictionary().put(TPEvent.FROM,
				Activator.getDefault().getTPBridgeClient().getTPMgrECFID());

		TPEntity execEntity = TestExecutionXML
				.getTPEntityFromExecEvent(tpEvent);
		String execXML = TestExecutionXML.getTPEntityXML(execEntity);
		tpEvent.getDictionary().put(TPEvent.TEST_EXEC_XML_KEY, execXML);

		System.out.println("TPManager runTests(): sending tpEvent w/status "
				+ tpEvent.getStatus() + " & topic " + tpEvent.getTopic()
				+ " to " + tpEvent.getDictionary().get(TPEvent.SEND_TO));

		Activator.getDefault().getEventAdminClient().sendEvent(
				tpEvent.getTopic(), tpEvent.getDictionary());
	}

	private void sendTestDetailResponse(TPEvent tpEvent) throws Exception {
		Hashtable<String, String> dictionary = tpEvent.getDictionary();
		dictionary.put(TPEvent.SEND_TO, dictionary.get(TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());

		System.out.println("TPManager.sendTestDetailResponse: Send To: "
				+ dictionary.get(TPEvent.SEND_TO) + ", From: "
				+ dictionary.get(TPEvent.FROM));

		Test test = TestUtil.getTestByID(tpEvent.getID(), true);
		dictionary.put(TPEvent.TEST_PROP_XML_KEY, TestXML.getTestPropXML(test));

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.TEST_DETAIL_RESP_TOPIC, dictionary);
	}

	private void sendTestUpdateDatResponse(TPEvent tpEvent) throws Exception {
		Hashtable<String, String> dictionary = tpEvent.getDictionary();
		dictionary.put(TPEvent.SEND_TO, dictionary.get(TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());

		System.out.println("TPManager.sendTestUpdateDatResponse: Send To: "
				+ dictionary.get(TPEvent.SEND_TO) + ", From: "
				+ dictionary.get(TPEvent.FROM));

		Test test = TestUtil.getTestByID(tpEvent.getID(), false);
		Test testStub = TestUtil.getTestUpdateStub(test);

		dictionary.put(TPEvent.TEST_XML_KEY, TestXML.getXML(testStub));

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.TEST_UPDATE_DATA_RESP_TOPIC, dictionary);
	}

	private void sendTestUpdateResponse(TPEvent tpEvent) throws Exception {
		Hashtable<String, String> dictionary = tpEvent.getDictionary();
		dictionary.put(TPEvent.SEND_TO, dictionary.get(TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());

		System.out.println("TPManager.sendTestUpdateResponse: Send To: "
				+ dictionary.get(TPEvent.SEND_TO) + ", From: "
				+ dictionary.get(TPEvent.FROM));

		String testXML = tpEvent.getDictionary().get(TPEvent.TEST_XML_KEY);

		System.out.println("testXML:\n" + testXML);

		Test testStub = TestXML.getTestFromXML(testXML);
		TpteamUser updateUser = new TpteamUser();
		updateUser.setEcfId(dictionary.get(TPEvent.SEND_TO));
		testStub.setModifiedBy(updateUser);
		TestUtil.updateTest(testStub);

		dictionary.put(TPEvent.TEST_NAME_KEY, testStub.getName());
		dictionary.put(TPEvent.TEST_DESC_KEY, testStub.getDescription());

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.TEST_UPDATE_RESP_TOPIC, dictionary);

	}

	private void sendTestAddResponse(TPEvent tpEvent) throws Exception {
		Hashtable<String, String> dictionary = tpEvent.getDictionary();
		dictionary.put(TPEvent.SEND_TO, dictionary.get(TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());

		System.out.println("TPManager.sendTestAddResponse: Send To: "
				+ dictionary.get(TPEvent.SEND_TO) + ", From: "
				+ dictionary.get(TPEvent.FROM));

		String testXML = tpEvent.getDictionary().get(TPEvent.TEST_XML_KEY);

		System.out.println("testXML:\n" + testXML);

		Test testStub = TestXML.getTestFromXML(testXML);
		TestUtil.addTest(testStub);

		dictionary.put(TPEvent.ID_KEY, String.valueOf(testStub.getId()));
		dictionary.put(TPEvent.TEST_NAME_KEY, testStub.getName());
		if (testStub.getDescription() != null) {
			dictionary.put(TPEvent.TEST_DESC_KEY, testStub.getDescription());
		}
		dictionary.put(TPEvent.TEST_XML_KEY, TestXML.getXML(testStub));

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.TEST_ADD_RESP_TOPIC, dictionary);

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

			// Collect project member ecfIDs
			Set<TpteamUser> users = test.getProject().getTpteamUsers();
			StringBuilder userECFIDs = new StringBuilder();
			int idx = 0;
			for (TpteamUser user : users) {
				if (idx == 0)
					userECFIDs.append(user.getEcfId());
				else
					userECFIDs.append("/" + user.getEcfId());
				idx++;
			}
			String ECFID = tpEvent.getDictionary().get(TPEvent.FROM);
			tpEvent.getDictionary().put(TPEvent.ECFID_KEY, ECFID);
			tpEvent.getDictionary().put(TPEvent.SEND_TO, userECFIDs.toString());
			tpEvent.getDictionary().put(TPEvent.TEST_NAME_KEY, test.getName());

			s.flush();
			tx.commit();

			if (testType.equalsIgnoreCase("JUNIT")) {
				runJUnitTest(testID, tpEvent);
			}
			TestExecutionUtil.insertTestExec(testID, tpEvent);

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

			String verdict = mAutomationClient.run(eclipseHome, workspace,
					project, new String[] { suite }, report, tptpConn);

			System.out.println("Test Verdict: " + verdict);

			tpEvent.getDictionary().put(TPEvent.VERDICT_KEY, verdict);
			tpEvent.getDictionary().put(TPEvent.TIMESTAMP_KEY, getDateTime());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	private void deleteTest(TPEvent tpEvent) throws Exception {
		Transaction tx = null;

		try {
			// For standalone
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();

			Test test = (Test) s.load(Test.class, new Integer(tpEvent.getID()));
			s.delete(test);
			s.flush();
			tx.commit();
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
