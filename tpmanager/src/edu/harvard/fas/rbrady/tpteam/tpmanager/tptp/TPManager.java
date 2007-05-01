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
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.eventadmin.EventAdminHandler;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestExecutionUtil;

public class TPManager implements Observer {
	
	/** Locale to format test result time */
	public static final Locale USA_LOCALE = new Locale("en", "US");

	/** DateTime format for test reusult */
	public static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss.SSS z", USA_LOCALE);
	
	
	public void update(Observable observable, Object object) {
		if (observable instanceof EventAdminHandler
				&& object instanceof TPEvent) {
			TPEvent tpEvent = (TPEvent) object;
			TPManagerThread tpManagerThread = new TPManagerThread(tpEvent);
			Thread tpThread = new Thread(tpManagerThread);
			tpThread.start();
		}
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

			AutomationClient automationClient = new AutomationClient();
			String verdict = automationClient.run(eclipseHome, workspace,
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
	
	public static String getDateTime() {
		Date now = new Date();
		return SIMPLE_DATE_FORMATTER.format(now);
	}


}
