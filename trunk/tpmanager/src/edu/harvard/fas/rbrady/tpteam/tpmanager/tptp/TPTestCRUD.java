package edu.harvard.fas.rbrady.tpteam.tpmanager.tptp;

import java.util.Hashtable;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.ChartUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.ProjectUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestUtil;

public class TPTestCRUD {

	public static synchronized void sendTestAddResponse(TPEvent tpEvent)
			throws Exception {

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

	public static void sendTestDetailResponse(TPEvent tpEvent) throws Exception {
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

	public static void sendTestUpdateDatResponse(TPEvent tpEvent)
			throws Exception {
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

	public static synchronized void sendTestUpdateResponse(TPEvent tpEvent)
			throws Exception {

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

	public static synchronized void deleteTest(TPEvent tpEvent)
			throws Exception {
		Transaction tx = null;

		try {
			// If running in OSGi runtime, get Session from plugin
			Session s = null;
			if (Activator.getDefault() != null) {
				s = Activator.getDefault().getHiberSessionFactory()
						.getCurrentSession();

			} else {
				s = HibernateUtil.getSessionFactory().getCurrentSession();
			}

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

	public static void sendDelTestResponse(TPEvent tpEvent)
			throws Exception {
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

	public static void sendProjGetResponse(TPEvent tpEvent) throws Exception {
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

	public static void sendTestTreeGetResponse(TPEvent tpEvent)
			throws Exception {
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

	public static void sendChartDataResponse(TPEvent tpEvent) throws Exception {
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

}
