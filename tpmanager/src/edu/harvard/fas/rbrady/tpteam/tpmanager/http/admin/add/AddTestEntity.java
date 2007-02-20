/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestType;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class AddTestEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	String mTestName = null;

	String mTestDesc = null;

	String mTestTypeID = null;

	String mTestTypeName = null;

	String mProjID = null;

	String mParentID = null;

	String mRemoteUser = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		mTestName = req.getParameter("testName");
		mTestDesc = req.getParameter("testDesc");
		mTestTypeID = req.getParameter("testTypeID");
		mTestTypeName = req.getParameter("testTypeName");
		mProjID = req.getParameter("projID");
		mParentID = req.getParameter("parentID");
		mRemoteUser = req.getRemoteUser();

		try {

			Integer testID = saveTest();
			saveTestType(req, resp, testID);
			StringBuffer reply = new StringBuffer("<h3>Add Test " + mTestName
					+ " was Successful</h3>");
			showPage(req, resp, reply, null, this);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	private Integer saveTest() throws Exception {
		Integer testID = null;
		Transaction tx = null;
		try {
			int remoteUserId = ServletUtil.getRemoteUserID(mRemoteUser);
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			// For standalone operation
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			Character isFolder = 'N';
			tx = s.beginTransaction();
			Test test = new Test();
			test.setName(mTestName);
			test.setDescription(mTestDesc);
			TestType testType = (TestType) s.load(TestType.class, new Integer(
					mTestTypeID));
			test.setTestType(testType);
			Project proj = (Project) s
					.load(Project.class, new Integer(mProjID));
			test.setProject(proj);

			Test parent = null;
			if (!mParentID.equalsIgnoreCase("0")) {
				parent = (Test) s.load(Test.class, new Integer(mParentID));
				test.setParent(parent);
			}
			if (mTestTypeID.equalsIgnoreCase("0")) {
				isFolder = 'Y';
			}
			test.setIsFolder(isFolder);
			testID = (Integer) s.save(test);

			if (parent != null) {
				test.setPath(parent.getPath() + "." + testID);
			} else {
				test.setPath(String.valueOf(testID));
			}
			test.setCreatedBy((TpteamUser) s.load(TpteamUser.class,
					remoteUserId));
			test.setCreatedDate(new Date());

			s.saveOrUpdate(test);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return testID;
	}

	private void saveTestType(HttpServletRequest req, HttpServletResponse resp,
			Integer testID) throws Exception {
		if (mTestTypeName.equalsIgnoreCase("JUNIT")) {
			saveJUnitTestType(req, resp, testID);
		}
	}

	private void saveJUnitTestType(HttpServletRequest req,
			HttpServletResponse resp, Integer testID) throws Exception {
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
		// For standalone operation
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, testID);
			JunitTest junit = new JunitTest();
			junit.setId(testID);
			junit.setTest(test);
			junit.setEclipseHome(req.getParameter("eclipseHome"));
			junit.setProject(req.getParameter("eclipseProj"));
			junit.setWorkspace(req.getParameter("eclipseWorkspace"));
			junit.setReportDir(req.getParameter("reportDir"));
			junit.setTptpConnection(req.getParameter("tptpConn"));
			junit.setTestSuite(req.getParameter("testSuite"));
			s.save(junit);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}
}
