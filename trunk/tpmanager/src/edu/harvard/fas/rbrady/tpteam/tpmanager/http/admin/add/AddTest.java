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
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestType;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class AddTest extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	private boolean mIsProjAvailable = false;

	private String mProjOptions = null;

	private String mTestTypeOptions = null;

	private String mTestName = null;

	private String mTestDesc = null;

	private String mTestTypeIDName = null;

	private String mTestTypeID = null;

	private String mTestTypeName = null;

	private String mProjIDName = null;

	private String mProjID = null;

	private String mProjName = null;

	public static final String ECLIPSE_HOME = "c:/Java/Eclipse3.2.1/eclipse";

	public static final String ECLIPSE_WORKSPACE = "c:/workspace_tpteam_test";

	public static final String ECLIPSE_PROJECT = "edu.harvard.fas.rbrady.tpteam.test";

	public static final String REPORT_DIR = "c:/tpteam/test/reports";

	public static final String TPTP_CONN = "tptp:rac://localhost:10002/default";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mTestName = req.getParameter("testName");
			mTestDesc = req.getParameter("testDesc");
			/*******************************************************************
			 * test type and project are single select lists so getParameter
			 * gets the single value.
			 ******************************************************************/
			mTestTypeIDName = req.getParameter("testType");
			mProjIDName = req.getParameter("proj");

			if (mTestName != null && mTestTypeIDName != null
					&& mProjIDName != null) {
				getPage2(req, resp);
			} else {
				getPage1(req, resp);
			}
		} catch (Exception e) {
			String error = "<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>";
			adminError(req, resp, error);
			return;
		}
	}

	public void getPage1(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		getProjOptions();
		getTestTypeOptions();
		if (mIsProjAvailable == false) {
			throwError(req, resp);
		} else {
			showAddTestPage1(req, resp);
		}
	}

	public void getPage2(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		// Split out IDs and Names from form parameters
		mTestTypeID = mTestTypeIDName.split(":")[0];
		mTestTypeName = mTestTypeIDName.replaceFirst(mTestTypeID + ":", "");
		mProjID = mProjIDName.split(":")[0];
		mProjName = mProjIDName.replaceFirst(mProjID + ":", "");
		showAddTestPage2(req, resp);
	}

	private String getProjOptions() throws Exception {
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();

		// For standalone debug
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		List<Project> projs = null;
		mProjOptions = "";
		try {

			tx = s.beginTransaction();

			projs = s.createQuery("from Project as p order by p.name asc")
					.list();

			// Concatenate id with name to save database call later.
			for (Project proj : projs) {
				mProjOptions += "<option value=\"" + proj.getId() + ":"
						+ proj.getName() + "\">" + proj.getName() + "</option>";
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if (!mProjOptions.equals(""))
			mIsProjAvailable = true;

		mProjOptions = "<option selected>Choose Project</option>"
				+ mProjOptions;

		return mProjOptions;
	}

	private String getTestTypeOptions() throws Exception {
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();

		// For standalone debug
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		List<TestType> types = null;
		mTestTypeOptions = "";
		try {

			tx = s.beginTransaction();

			types = s
					.createQuery(
							"from TestType as type where type.id > 0 order by type.name asc")
					.list();
			// Concatenate id with name to save database call later.
			for (TestType type : types) {
				mTestTypeOptions += "<option value=\"" + type.getId() + ":"
						+ type.getName() + "\">" + type.getName() + "</option>";
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		mTestTypeOptions = "<option selected>Choose Test Type</option>"
				+ "<option value=\"0:FOLDER\">Test Folder</option>"
				+ mTestTypeOptions;

		return mTestTypeOptions;
	}

	private void throwError(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String error = "<h3>Error: No Project Available.  A Project needs to be created first.</h3>";
		adminError(req, resp, error);
	}

	private void showAddTestPage1(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		StringBuffer reply = new StringBuffer();
		reply.append("<h4>Add Test Plan Object, Page 1 of 2</h4>\n");
		reply
				.append("<form name=\"addTestPage1\" method=\"post\" onSubmit=\"return validatePage1Form( );\">\n");
		reply
				.append("<table ><tr><th>Name:</th><td><input type=\"text\" name=\"testName\" size=\"25\"></td></tr>\n");
		reply
				.append("<tr><th>Description:</th><td><input type=\"text\" name=\"testDesc\" size=\"50\"></td></tr>\n");
		reply
				.append("<tr><th>Test Plan Object Type:</th><td><select name=\"testType\">"
						+ mTestTypeOptions + "</select></td></tr>\n");
		reply.append("<tr><th>Project:</th><td><select name=\"proj\">"
				+ mProjOptions + "</select></td></tr>\n");
		reply
				.append("</table>\n<br>\n<input type=\"submit\" value=\"Add\">\n</form>\n");

		adminHeader(req, resp, ServletUtil.ADD_TEST_JS);
		adminReply(req, resp, reply.toString());
		adminFooter(req, resp);
	}

	private void showAddTestPage2(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException,
			Exception {
		StringBuffer reply = new StringBuffer();
		reply.append("<h4>Add Test Plan Element, Page 2 of 2</h4>\n");
		reply
				.append("<form name=\"addTestPage2\" method=\"post\" onSubmit=\"return validatePage2Form( );\">\n");
		reply.append("<table align=\"center\"><tr><th>Name:</th><td>"
				+ mTestName);
		reply.append("<input type=\"hidden\" name=\"testName\" value=\""
				+ mTestName + "\"></td></tr>\n");
		reply.append("<tr><th>Description:</th><td>" + mTestDesc
				+ "<input type=\"hidden\" name=\"testDesc\" value=\""
				+ mTestDesc + "\"></td></tr>\n");
		reply.append("<tr><th>Test Type:</th><td>" + mTestTypeName
				+ "<input type=\"hidden\" name=\"testTypeID\" value=\""
				+ mTestTypeID + "\">"
				+ "<input type=\"hidden\" name=\"testTypeName\" value=\""
				+ mTestTypeName + "\">\n</td></tr>\n");
		reply.append("<tr><th>Project:</th><td>" + mProjName
				+ "<input type=\"hidden\" name=\"projID\" value=\"" + mProjID
				+ "\"></td></tr>\n");
		reply.append("</table>\n<p>\n<h4>Select a Parent Test Folder</h4>\n");
		reply.append("<table border=\"1\"><tr><td>\n");
		reply.append(ServletUtil.getTestTreeFolders(mProjID));
		reply.append("<input type=\"hidden\" name=\"parentID\" value=\"\">\n");
		reply.append("</td></tr></table><p>\n");
		reply.append(getTestTypeFormTable());
		reply.append("<input type=\"submit\" value=\"Add\">\n</form>\n");
		adminHeader(req, resp, getTestTypeJavaScript()
				+ ServletUtil.ADD_TEST_TREE_JS + ServletUtil.ADD_TEST_TREE_CSS);
		adminReply(req, resp, reply.toString());
		adminFooter(req, resp);
	}

	private String getTestTypeFormTable() {
		String testTypeTable = "";
		if (mTestTypeName.equalsIgnoreCase("JUNIT")) {
			testTypeTable = getJUnitFormTable();
		}
		return testTypeTable;
	}

	private String getTestTypeJavaScript() {
		String testTypeJS = "";
		if (mTestTypeName.equalsIgnoreCase("JUNIT")) {
			// testTypeJS = getJUnitJavaScript();
			testTypeJS = ServletUtil.ADD_TEST_TYPE_JS;
		} else if (mTestTypeName.equals("FOLDER")) {
			testTypeJS = ServletUtil.ADD_TEST_FOLDER_JS;
		}
		return testTypeJS;
	}

	private String getJUnitFormTable() {
		StringBuffer reply = new StringBuffer();
		reply.append("<table >\n<caption><b>JUnit Test Properties</b></caption>\n");
		reply.append("<tr><th>Eclipse Home:</th><td><input type=\"text\" name=\"eclipseHome\" size=\"75\" value=\""
				+ ECLIPSE_HOME + "\"></td></tr>\n");
		reply.append("<tr><th>Eclipse Workspace:</th><td><input type=\"text\" name=\"eclipseWorkspace\" size=\"75\" value=\""
				+ ECLIPSE_WORKSPACE + "\"></td></tr>\n");
		reply.append("<tr><th>Eclipse Project:</th><td><input type=\"text\" name=\"eclipseProj\" size=\"75\" value=\""
				+ ECLIPSE_PROJECT + "\"></td></tr>\n");
		reply.append("<tr><th>Test Suite:</th><td><input type=\"text\" name=\"testSuite\" size=\"75\" value=\""
				+ mTestName + ".testsuite\"></td></tr>\n");
		reply.append("<tr><th>Report Directory:</th><td><input type=\"text\" name=\"reportDir\" size=\"75\" value=\""
				+ REPORT_DIR + "\"></td></tr>\n");
		reply.append("<tr><th>TPTP Connection URL:</th><td><input type=\"text\" name=\"tptpConn\" size=\"75\" value=\""
				+ TPTP_CONN + "\"></td></tr>\n");
		reply.append("</table>\n<br>\n");
		return reply.toString();
	}
}
