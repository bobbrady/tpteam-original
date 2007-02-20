/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class UpdateTestEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mTestID = null;

	protected String mTestName = null;

	protected String mTestDesc = null;

	protected String mTestTypeName = null;

	protected String mRemoteUser = null;

	boolean mIsFolder = false;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		mTestID = req.getParameter("testID");
		mTestName = req.getParameter("testName");
		mTestDesc = req.getParameter("testDesc");
		mRemoteUser = req.getRemoteUser();

		try {
			updateTest();
			updateTestType(req, resp, Integer.parseInt(mTestID));
			StringBuffer reply = new StringBuffer("<h3>Update Test " + mTestName + " was Successful</h3>");
			showPage(req, resp, reply, null, this);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	protected void updateTest() throws Exception {
		// For standalone operation
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			int remoteUserId = ServletUtil.getRemoteUserID(mRemoteUser);
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, new Integer(mTestID));
			test.setName(mTestName);
			test.setDescription(mTestDesc);
			if (test.getIsFolder().toString().equalsIgnoreCase("Y")) {
				mIsFolder = true;
			}
			if (test.getJunitTests().size() > 0) {
				mTestTypeName = "JUNIT";
			}
			test.setModifiedBy((TpteamUser)s.load(TpteamUser.class, remoteUserId));
			test.setModifiedDate(new Date());
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	protected void updateTestType(HttpServletRequest req,
			HttpServletResponse resp, Integer testID) throws Exception {
		if (mIsFolder) {
			return;
		}
		if (mTestTypeName.equalsIgnoreCase("JUNIT")) {
			updateJUnitTestType(req, resp, testID);
		}
	}

	protected void updateJUnitTestType(HttpServletRequest req,
			HttpServletResponse resp, Integer testID) throws Exception {
		// For standalone operation
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, testID);
			JunitTest junit = (JunitTest) (test.getJunitTests().toArray(
					new JunitTest[0])[0]);
			junit.setEclipseHome(req.getParameter("eclipseHome"));
			junit.setProject(req.getParameter("eclipseProj"));
			junit.setWorkspace(req.getParameter("eclipseWorkspace"));
			junit.setReportDir(req.getParameter("reportDir"));
			junit.setTptpConnection(req.getParameter("tptpConn"));
			junit.setTestSuite(req.getParameter("testSuite"));
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}
}
