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
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestType;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class UpdateTest3 extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	private String mTestID = null;

	private String mJavaScript = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mTestID = req.getParameter("testID");
			getPage(req, resp);
		} catch (Exception e) {
			String error = "<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>";
			adminError(req, resp, error);
			return;
		}
	}

	public void getPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		showUpdateTestPage3(req, resp);
	}

	private void showUpdateTestPage3(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException,
			Exception {

		String reply = "<h4>Update Test Plan Node</h4>\n";
		reply += "<form name=\"updateTest\" method=\"post\" onSubmit=\"return validateForm(this);\">\n";
		reply += "<table border=\"1\">\n";
		reply += getUpdateTestRows(mTestID);
		reply += "</table><p>\n";
		reply += "<input type=\"submit\" value=\"Update\">\n</form>\n";

		adminHeader(req, resp, mJavaScript);
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}

	private String getUpdateTestRows(String testId) throws Exception {
		// Standalone session
		//Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		String updateRows = "";
		try {
			Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, new Integer(testId));

			// Set appropriate JavaScript validation script
			if (test.getIsFolder().toString().equalsIgnoreCase("Y")) {
				mJavaScript = ServletUtil.UPDATE_TEST_FOLDER_JS;
			} else {
				mJavaScript = ServletUtil.UPDATE_TEST_DEF_JS;
			}
			
			updateRows = getFolderUpdateRows(test);

			if (test.getJunitTests().size() > 0) {
				updateRows += getJUnitUpdateRows(test);
			}
			tx.commit();

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return updateRows;
	}

	private String getJUnitUpdateRows(Test test) {
		String updateRows = "";
		for (JunitTest jUnitTest : test.getJunitTests()) {
			updateRows += "<tr><th>Eclipse Home:</th><td><input type=\"text\" name=\"eclipseHome\" size=\"75\" value=\""
					+ jUnitTest.getEclipseHome() + "\"></td></tr>\n";
			updateRows += "<tr><th>Eclipse Workspace:</th><td><input type=\"text\" name=\"eclipseWorkspace\" size=\"75\" value=\""
					+ jUnitTest.getWorkspace() + "\"></td></tr>\n";
			updateRows += "<tr><th>Eclipse Project:</th><td><input type=\"text\" name=\"eclipseProj\" size=\"75\" value=\""
					+ jUnitTest.getProject() + "\"></td></tr>\n";
			updateRows += "<tr><th>Test Suite:</th><td><input type=\"text\" name=\"testSuite\" size=\"75\" value=\""
					+ jUnitTest.getTestSuite() + ".testsuite\"></td></tr>\n";
			updateRows += "<tr><th>Report Directory:</th><td><input type=\"text\" name=\"reportDir\" size=\"75\" value=\""
					+ jUnitTest.getReportDir() + "\"></td></tr>\n";
			updateRows += "<tr><th>TPTP Connection URL:</th><td><input type=\"text\" name=\"tptpConn\" size=\"75\" value=\""
					+ jUnitTest.getTptpConnection() + "\"></td></tr>\n";
		}
		return updateRows;
	}

	private String getFolderUpdateRows(Test test) {
		String updateRows = "";
		String desc = "";
		if (test.getDescription() != null
				&& !test.getDescription().equalsIgnoreCase("")) {
			desc = test.getDescription();
		}
		updateRows += "<tr><th>Name:</th><td>"
				+ "<input type=\"text\" name=\"testName\" value=\"" + test.getName() + "\" size=\"75\">" 
				+ "<input type=\"hidden\" name=\"testID\" value=\"" + mTestID + "\"></td></tr>\n";
		
		updateRows += "<tr><th>Description:</th><td>" 
				+ "<input type=\"text\" name=\"testDesc\" value=\""
				+ desc + "\" size=\"75\"></td></tr>\n";
		
		return updateRows;
	}
	
	public static void main(String[] args)
	{
		UpdateTest3 servlet = new UpdateTest3();
		servlet.mTestID = "81";
		try {
			System.out.println(servlet.getUpdateTestRows("81"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
