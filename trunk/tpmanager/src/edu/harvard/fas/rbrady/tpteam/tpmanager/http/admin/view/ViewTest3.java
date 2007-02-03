/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view;

import java.io.IOException;
import java.text.ParseException;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestExecution;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class ViewTest3 extends ServletUtil {

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

		StringBuffer reply = new StringBuffer();
		reply.append("<h4>View Test Plan Node</h4>\n");

		reply.append("<table border=\"1\">\n");
		reply.append(getTestRows(mTestID));
		reply.append("</table><p>\n");

		adminHeader(req, resp, null);
		adminReply(req, resp, reply.toString());
		adminFooter(req, resp);
	}

	private String getTestRows(String testId) throws Exception {
		Transaction tx = null;
		StringBuffer updateRows = new StringBuffer();
		try {
			// Standalone session
			//Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, new Integer(testId));
			updateRows.append(getFolderRows(test));

			if (test.getJunitTests().size() > 0) {
				updateRows.append(getJUnitUpdateRows(test));
			}
			
			if(test.getIsFolder() == 'N' || test.getIsFolder() == 'n')
			{
				updateRows.append("<tr><th align=\"left\">Execution Results:</th><td align=\"right\">" 
					+ getTestResults(test.getTestExecutions()) + "</td></tr>\n");
			}

			tx.commit();

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return updateRows.toString();
	}

	private String getJUnitUpdateRows(Test test) {
		StringBuffer updateRows = new StringBuffer();
		for (JunitTest jUnitTest : test.getJunitTests()) {
			updateRows.append( "<tr><th align=\"left\">Eclipse Home:</th><td align=\"right\">"
					+ jUnitTest.getEclipseHome() + "</td></tr>\n");
			updateRows.append( "<tr><th align=\"left\">Eclipse Workspace:</th><td align=\"right\">"
					+ jUnitTest.getWorkspace() + "</td></tr>\n");
			updateRows.append( "<tr><th align=\"left\">Eclipse Project:</th><td align=\"right\">"
					+ jUnitTest.getProject() + "</td></tr>\n");
			updateRows.append( "<tr><th align=\"left\">Test Suite:</th><td align=\"right\">"
					+ jUnitTest.getTestSuite() + "</td></tr>\n");
			updateRows.append( "<tr><th align=\"left\">Report Directory:</th><td align=\"right\">"
					+ jUnitTest.getReportDir() + "</td></tr>\n");
			updateRows.append( "<tr><th align=\"left\">TPTP Connection URL:</th><td align=\"right\">"
					+ jUnitTest.getTptpConnection() + "</td></tr>\n");
		}
		return updateRows.toString();
	}

	private String getFolderRows(Test test) throws ParseException {
		StringBuffer updateRows = new StringBuffer();
		String desc = "";
		if (test.getDescription() != null
				&& !test.getDescription().equalsIgnoreCase("")) {
			desc = test.getDescription();
		}
		updateRows.append( "<tr><th align=\"left\">Name:</th><td align=\"right\">"
				 + test.getName()  + "</td></tr>\n");
		
		updateRows.append( "<tr><th align=\"left\">Description:</th><td align=\"right\">" 
				+ desc + "</td></tr>\n");
		
		updateRows.append( "<tr><th align=\"left\">Created By:</th><td align=\"right\">"
				 + test.getCreatedBy().getLastName() + ", " + test.getCreatedBy().getFirstName()  
				 + " (" + test.getCreatedBy().getUserName() + ")"+ "</td></tr>\n");
		
		updateRows.append( "<tr><th align=\"left\">Creation Date:</th><td align=\"right\">" 
				+ TIMESTAMP_FORMAT.format(test.getCreatedDate()) + "</td></tr>\n");
		
		String modUser = "";
		String modDate = "";
		if(test.getModifiedBy() != null)
		{
			modUser = test.getModifiedBy().getLastName() + ", " + test.getModifiedBy().getFirstName()  
			 + " (" + test.getModifiedBy().getUserName() + ")";
			modDate = TIMESTAMP_FORMAT.format(test.getModifiedDate()).toString(); 
		}
		updateRows.append( "<tr><th align=\"left\">Modified By:</th><td align=\"right\">"
				 + modUser + "</td></tr>\n");
		
		updateRows.append( "<tr><th align=\"left\">Modification Date:</th><td align=\"right\">" 
				+ modDate + "</td></tr>\n");
		
		return updateRows.toString();
	}
	
	private String getTestResults(Set<TestExecution> testExecs)
	{
		StringBuffer results = new StringBuffer();
		String resultRows = "";
		String execDate = "";
		String execBy = "";
		if(testExecs.size() < 1)
		{
			return resultRows;
		}
		TreeSet<TestExecution> testExecTree = new TreeSet<TestExecution>(testExecs);
		for(TestExecution exec : testExecTree)
		{
			String status = "<b><font color=\"green\">PASS</font></b>";
			if(exec.getStatus() == 'F' || exec.getStatus() == 'f')
			{
				status = "<b><font color=\"red\">FAIL</font></b>";
			}
			execDate = TIMESTAMP_FORMAT.format(exec.getExecDate());
			execBy = exec.getTpteamUser().getLastName() + ", " + exec.getTpteamUser().getFirstName()  
			 + " (" + exec.getTpteamUser().getUserName() + ")";
			results.append(status + ", " + execDate + ", " + execBy + "<br>\n");
		}
		return results.toString();
	}
	
	public static void main(String[] args)
	{
		ViewTest3 servlet = new ViewTest3();
		servlet.mTestID = "62";
		try {
			System.out.println(servlet.getTestRows("62"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
