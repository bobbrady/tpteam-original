/********************************************************************
 * 
 * File		:	UpdateTest3.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that loads the details for a particular Test
 * 				into a form so that a user can edit and update them
 *  
 ********************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		: 	UpdateTest3.java
 * 
 * Description 	: 	Servlet that loads the details for a particular Test
 * 					into a form so that a user can edit and update them
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class UpdateTest3 extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mTestID = null;

	protected String mJavaScript = null;
	
	protected String mFormAction = "<input type=\"hidden\" name=\"formAction\" value=\"updateTestEntity\">\n";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	
	/**
	 * Gets the ID of the Test selected and renders the details
	 * in a form
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mTestID = req.getParameter("testID");
			showUpdateTestPage3(req, resp);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	/**
	 * Helper method for rendering the form HTML
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception
	 */
	protected void showUpdateTestPage3(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException,
			Exception {

		StringBuffer reply = new StringBuffer("<h4>Update Test Plan Node</h4>\n");
		reply.append("<form name=\"updateTest\" method=\"post\" onSubmit=\"return validateForm(this);\">\n");
		reply.append("<table border=\"1\">\n");
		reply.append(getUpdateTestRows(mTestID));
		reply.append("</table><p>\n");
		reply.append("<input type=\"submit\" value=\"Update\">\n</form>\n");

		showPage(req, resp, reply, mJavaScript, this);
	}

	/**
	 * Loads the Test selected by the user so that
	 * its details can be displayed
	 *  
	 * @param testId the Test ID
	 * @return the Test details as HTML form inputs
	 * @throws Exception
	 */
	protected String getUpdateTestRows(String testId) throws Exception {
		Transaction tx = null;
		StringBuffer updateRows = new StringBuffer();
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
			
			updateRows.append(getFolderUpdateRows(test));

			if (test.getJunitTests().size() > 0) {
				updateRows.append(getJUnitUpdateRows(test));
			}
			tx.commit();

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return updateRows.toString();
	}

	/**
	 * Helper method that renders the HTML form input 
	 * elements for the JUnit details
	 * 
	 * @param test the JUnit Test
	 * @return the HTML form inputs
	 */
	protected String getJUnitUpdateRows(Test test) {
		StringBuffer updateRows = new StringBuffer();
		for (JunitTest jUnitTest : test.getJunitTests()) {
			updateRows.append("<tr><th>Eclipse Home:</th><td><input type=\"text\" name=\"eclipseHome\" size=\"75\" value=\""
					+ jUnitTest.getEclipseHome() + "\"></td></tr>\n");
			updateRows.append("<tr><th>Eclipse Workspace:</th><td><input type=\"text\" name=\"eclipseWorkspace\" size=\"75\" value=\""
					+ jUnitTest.getWorkspace() + "\"></td></tr>\n");
			updateRows.append("<tr><th>Eclipse Project:</th><td><input type=\"text\" name=\"eclipseProj\" size=\"75\" value=\""
					+ jUnitTest.getProject() + "\"></td></tr>\n");
			updateRows.append("<tr><th>Test Suite:</th><td><input type=\"text\" name=\"testSuite\" size=\"75\" value=\""
					+ jUnitTest.getTestSuite() + "\"></td></tr>\n");
			updateRows.append("<tr><th>Report Directory:</th><td><input type=\"text\" name=\"reportDir\" size=\"75\" value=\""
					+ jUnitTest.getReportDir() + "\"></td></tr>\n");
			updateRows.append("<tr><th>TPTP Connection URL:</th><td><input type=\"text\" name=\"tptpConn\" size=\"75\" value=\""
					+ jUnitTest.getTptpConnection() + "\"></td></tr>\n");
		}
		return updateRows.toString();
	}

	/**
	 * Helper method that renders the HTML form input 
	 * elements for the core Test details
	 * 
	 * @param test the Test
	 * @return the HTML form inputs
	 */
	protected String getFolderUpdateRows(Test test) {
		StringBuffer updateRows = new StringBuffer();
		String desc = "";
		if (test.getDescription() != null
				&& !test.getDescription().equalsIgnoreCase("")) {
			desc = test.getDescription();
		}
		updateRows.append("<tr><th>Name:</th><td>"
				+ "<input type=\"text\" name=\"testName\" value=\"" + test.getName() + "\" size=\"75\">" 
				+ "<input type=\"hidden\" name=\"testID\" value=\"" + mTestID + "\"></td></tr>\n");
		
		updateRows.append(mFormAction);
		
		updateRows.append("<tr><th>Description:</th><td>" 
				+ "<input type=\"text\" name=\"testDesc\" value=\""
				+ desc + "\" size=\"75\"></td></tr>\n");
		
		return updateRows.toString();
	}
	
}
