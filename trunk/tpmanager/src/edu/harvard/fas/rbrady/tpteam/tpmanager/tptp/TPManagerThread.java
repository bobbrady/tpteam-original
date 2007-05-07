package edu.harvard.fas.rbrady.tpteam.tpmanager.tptp;

import java.util.Hashtable;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestExecutionXML;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.ChartUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.ProjectUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestExecutionUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestUtil;

public class TPManagerThread implements Runnable {
	
	private TPEvent mTPEvent;
	
	private AutomationClient mAutomationClient;

	public TPManagerThread(TPEvent tpEvent) {
		mTPEvent = tpEvent;
		mAutomationClient = new AutomationClient();
	}

	public void run() {
		String tpTopic = mTPEvent.getTopic();
		try {
			System.out.println("NEW THREAD w/Topic: " + tpTopic);
			if (tpTopic.equals(ITPBridge.PROJ_GET_REQ_TOPIC)) {
				sendProjGetResponse(mTPEvent);
			} else if (tpTopic.equalsIgnoreCase(ITPBridge.TEST_EXEC_REQ_TOPIC)) {
				runTest(mTPEvent.getID(), mTPEvent);
				sendTestExecResponse(mTPEvent);
			} else if (tpTopic
					.equalsIgnoreCase(ITPBridge.TEST_TREE_GET_REQ_TOPIC)) {
				sendTestTreeGetResponse(mTPEvent);
			} else if (tpTopic.equalsIgnoreCase(ITPBridge.TEST_DEL_REQ_TOPIC)) {
				deleteTest(mTPEvent);
				sendDelTestResponse(mTPEvent);
			} else if (tpTopic
					.equalsIgnoreCase(ITPBridge.TEST_DETAIL_REQ_TOPIC)) {
				sendTestDetailResponse(mTPEvent);
			} else if (tpTopic
					.equalsIgnoreCase(ITPBridge.TEST_UPDATE_DATA_REQ_TOPIC)) {
				sendTestUpdateDatResponse(mTPEvent);
			} else if (tpTopic
					.equalsIgnoreCase(ITPBridge.TEST_UPDATE_REQ_TOPIC)) {
				sendTestUpdateResponse(mTPEvent);
			} else if (tpTopic.equalsIgnoreCase(ITPBridge.TEST_ADD_REQ_TOPIC)) {
				sendTestAddResponse(mTPEvent);
			} else if (tpTopic
					.equalsIgnoreCase(ITPBridge.CHART_GET_DATA_REQ_TOPIC)) {
				sendChartDataResponse(mTPEvent);
			}
		} catch (Exception e) {
			// Should throw TPTeam ExceptionEvent here?
			e.printStackTrace();
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

	private synchronized void sendDelTestResponse(TPEvent tpEvent) throws Exception {
		Hashtable<String, String> dictionary = tpEvent.getDictionary();
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

	private synchronized void sendTestExecResponse(TPEvent tpEvent) {
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

	private synchronized void sendTestUpdateDatResponse(TPEvent tpEvent) throws Exception {
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

	private synchronized void sendTestUpdateResponse(TPEvent tpEvent) throws Exception {

		TestUtil.updateTest(tpEvent);

		Hashtable<String, String> dictionary = tpEvent.getDictionary();
		dictionary.put(TPEvent.SEND_TO, dictionary.get(TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());

		System.out.println("TPManager.sendTestUpdateResponse: Send To: "
				+ dictionary.get(TPEvent.SEND_TO) + ", From: "
				+ dictionary.get(TPEvent.FROM));

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.TEST_UPDATE_RESP_TOPIC, dictionary);

	}

	private synchronized void sendTestAddResponse(TPEvent tpEvent) throws Exception {

		TestUtil.addTest(tpEvent);

		Hashtable<String, String> dictionary = tpEvent.getDictionary();
		dictionary.put(TPEvent.SEND_TO, dictionary.get(TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());

		System.out.println("TPManager.sendTestAddResponse: Send To: "
				+ dictionary.get(TPEvent.SEND_TO) + ", From: "
				+ dictionary.get(TPEvent.FROM));

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.TEST_ADD_RESP_TOPIC, dictionary);

	}

	private void sendChartDataResponse(TPEvent tpEvent) throws Exception {
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.SEND_TO, tpEvent.getDictionary().get(
				TPEvent.FROM));
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());
		dictionary.put(ChartDataSet.CHART_TYPE, tpEvent.getDictionary().get(
				ChartDataSet.CHART_TYPE));

		if (tpEvent.getDictionary().get(ChartDataSet.CHART_TYPE).equals(
				ChartDataSet.PIE)) {
			dictionary.put(TPEvent.CHART_DATASET_XML_KEY, ChartUtil
					.getPieChartXML(tpEvent));
		} else if (tpEvent.getDictionary().get(ChartDataSet.CHART_TYPE).equals(
				ChartDataSet.BAR)) {
			dictionary.put(TPEvent.CHART_DATASET_XML_KEY, ChartUtil
					.getBarChartXML(tpEvent));
		} else if (tpEvent.getDictionary().get(ChartDataSet.CHART_TYPE).equals(
				ChartDataSet.LINE)) {
			dictionary.put(TPEvent.CHART_DATASET_XML_KEY, ChartUtil
					.getLineChartXML(tpEvent));
		}

		Activator.getDefault().getEventAdminClient().sendEvent(
				ITPBridge.CHART_GET_DATA_RESP_TOPIC, dictionary);
	}


	private synchronized void deleteTest(TPEvent tpEvent) throws Exception {
		Transaction tx = null;

		try {
			// For standalone
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, new Integer(tpEvent.getID()));

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
			tpEvent.getDictionary().put(TPEvent.FROM, userECFIDs.toString());
			tpEvent.getDictionary().put(TPEvent.TEST_NAME_KEY, test.getName());

			s.delete(test);
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}

	}
	
	private synchronized void runTest(String testID, TPEvent tpEvent) throws Exception {
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

	private synchronized void runJUnitTest(String testID, TPEvent tpEvent) throws Exception {
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
			tpEvent.getDictionary().put(TPEvent.TIMESTAMP_KEY, TPManager.getDateTime());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}


}
