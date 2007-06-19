/********************************************************************
 * 
 * File		:	TPTestExec.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides synchronized methods for performing 
 * 				TPTeam Test executions and sending the resulting 
 * 				responses
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.tptp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestExecutionXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestExecutionUtil;

/*******************************************************************************
 * File 		: 	TPTestExec.java
 * 
 * Description 	: 	Provides synchronized methods for performing 
 * 					TPTeam Test executions and sending the resulting 
 * 					responses
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TPTestExec {
	
	/** Locale to format test result time */
	public static final Locale USA_LOCALE = new Locale("en", "US");

	/** DateTime format for test reusult */
	public static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss.SSS z", USA_LOCALE);

	/**
	 * Loads the test metadata from the TPTeam database
	 * for a given Test and then executes the Test
	 * 
	 * @param testID the TPTeam ID of the Test
	 * @param tpEvent the TPEvent get test execution request
	 * @throws Exception
	 */
	public static synchronized void runTest(String testID, TPEvent tpEvent)
			throws Exception {
		Transaction tx = null;
		String testType = null;

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

	/**
	 * Helper method that collects the JUnit metadata for
	 * a given test and requests the AutomationClient 
	 * execute it
	 * 
	 * @see AutomationClient
	 * @param testID the TPTeam ID of the Test
	 * @param tpEvent the TPEvent get test execution request
	 * @throws Exception
	 */
	private static synchronized void runJUnitTest(String testID, TPEvent tpEvent)
			throws Exception {
		Transaction tx = null;
		String eclipseHome = null;
		String workspace = null;
		String project = null;
		String suite = null;
		String tptpConn = null;
		String report = null;

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

			AutomationClient automationClient = new AutomationClient();
			String verdict = automationClient.run(eclipseHome, workspace,
					project, new String[] { suite }, report, tptpConn);

			System.out.println("Test Verdict: " + verdict);

			tpEvent.getDictionary().put(TPEvent.VERDICT_KEY, verdict);
			tpEvent.setStatus(verdict);
			tpEvent.getDictionary().put(TPEvent.TIMESTAMP_KEY, getDateTime());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	/**
	 * Sends a Test execution response TPEvent
	 * when the requested Test execution has 
	 * completed
	 * 
	 * @param tpEvent the TPEvent Test execution request
	 */
	public static void sendTestExecResponse(TPEvent tpEvent) {
		tpEvent.setTopic(ITPBridge.TEST_EXEC_RESULT_TOPIC);
		tpEvent.getDictionary().put(TPEvent.FROM,
				Activator.getDefault().getTPBridgeClient().getTPMgrECFID());

		TPEntity execEntity = TestExecutionXML
				.getTPEntityFromExecEvent(tpEvent);
		String execXML = TestExecutionXML.getTPEntityXML(execEntity);
		tpEvent.getDictionary().put(TPEvent.TEST_EXEC_XML_KEY, execXML);

		System.out.println("TPManager runTests(): sending tpEvent w/verdict "
				+ tpEvent.getDictionary().get(TPEvent.VERDICT_KEY)
				+ " & topic " + tpEvent.getTopic() + " to "
				+ tpEvent.getDictionary().get(TPEvent.SEND_TO));

		Activator.getDefault().getEventAdminClient().sendEvent(
				tpEvent.getTopic(), tpEvent.getDictionary());
	}
	
	/**
	 * Helper method to get properly
	 * formatted test execution timestamp
	 * 
	 * @return the timestamp
	 */
	private static String getDateTime() {
		Date now = new Date();
		return SIMPLE_DATE_FORMATTER.format(now);
	}

}
