/********************************************************************
 * 
 * File		:	AddTest.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays input forms for creating
 * 				a new TPTeam Test
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestType;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		: 	AddTest.java
 * 
 * Description 	: 	Servlet that displays input forms for creating
 * 					a new TPTeam Test
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class AddTest extends ServletUtil {

	protected static final long serialVersionUID = 7456848419577223441L;

	protected boolean mIsProjAvailable = false;

	protected String mProjOptions = null;

	protected String mTestTypeOptions = null;

	protected String mTestName = null;

	protected String mTestDesc = null;

	protected String mTestTypeIDName = null;

	protected String mTestTypeID = null;

	protected String mTestTypeName = null;

	protected String mProjIDName = null;

	protected String mProjID = null;

	protected String mProjName = null;

	protected String mRemoteUser = null;

	// Default values for JUnit tests
	
	public static final String ECLIPSE_HOME = "c:/Java/Eclipse3.2.1/eclipse";

	public static final String ECLIPSE_WORKSPACE = "c:/workspace_tpteam_test";

	public static final String ECLIPSE_PROJECT = "edu.harvard.fas.rbrady.tpteam.test";

	public static final String REPORT_DIR = "c:/tpteam/test/reports";

	public static final String TPTP_CONN = "tptp:rac://localhost:10002/default";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Renders the appropriate form for a new Test, folder or 
	 * definiton.  Displays any errors to the user.
	 * 
	 * @param req the Servlet request
	 * @param resp the Servlet response
	 * @throws IOException
	 * @throws ServletException
	 */
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
			mRemoteUser = req.getRemoteUser();

			if (mTestName != null && mTestTypeIDName != null
					&& mProjIDName != null) {
				getPage2(req, resp);
			} else {
				getPage1(req, resp);
			}
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	/**
	 * Renders the core data input form for a new Test:
	 * name, description, and type.
	 * 
	 * @param req the Servlet Request
	 * @param resp the Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception
	 */
	public void getPage1(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		getProjOptions();
		getTestTypeOptions();
		if (mIsProjAvailable == false) {
			StringBuffer error = new StringBuffer(
					"<h3>Error: No Project Available.  A Project needs to be created first.</h3>");
			throwError(req, resp, error, this);
		} else {
			showAddTestPage1(req, resp);
		}
	}

	/**
	 * Renders the detailed form input for a new Test:
	 * the parent folder and any test definition details.
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception
	 */
	public void getPage2(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		// Split out IDs and Names from form parameters
		mTestTypeID = mTestTypeIDName.split(":")[0];
		mTestTypeName = mTestTypeIDName.replaceFirst(mTestTypeID + ":", "");
		mProjID = mProjIDName.split(":")[0];
		mProjName = mProjIDName.replaceFirst(mProjID + ":", "");
		showAddTestPage2(req, resp);
	}


	/**
	 * Gets all TPTeam Projects and wraps them 
	 * into HTML select option tags
	 * 
	 * @return The Project select option tags
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected String getProjOptions() throws Exception {
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();

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

	/**
	 * Gets all TPTeam TestTypes and wraps them 
	 * into HTML select option tags
	 * 
	 * @return The TestType select option tags
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected String getTestTypeOptions() throws Exception {
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
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
				+ mTestTypeOptions;

		return mTestTypeOptions;
	}

	/**
	 * Helper method for rendering core input data form
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception
	 */
	protected void showAddTestPage1(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException,
			Exception {

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

		showPage(req, resp, reply, ServletUtil.ADD_TEST_JS, this);
	}

	/**
	 * Helper method for rendering the detailed
	 * input form 
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception
	 */
	protected void showAddTestPage2(HttpServletRequest req,
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
		showPage(req, resp, reply, getTestTypeJavaScript()
				+ ServletUtil.ADD_TEST_TREE_JS + ServletUtil.ADD_TEST_TREE_CSS,
				this);
	}

	/**
	 * Helper method for getting appropriate 
	 * form table based upon test type selected 
	 * by user
	 * 
	 * @return the String HTML form table
	 */
	protected String getTestTypeFormTable() {
		String testTypeTable = "";
		if (mTestTypeName.equalsIgnoreCase("JUNIT")) {
			testTypeTable = getJUnitFormTable();
		}
		return testTypeTable;
	}

	/**
	 * Helper method for injecting the appropriate
	 * JavaScript depending if a new Test folder or
	 * definiton was selected by the user
	 * 
	 * @return The JavaScript reference
	 */
	protected String getTestTypeJavaScript() {
		String testTypeJS = "";
		if (mTestTypeName.equalsIgnoreCase("JUNIT")) {
			testTypeJS = ServletUtil.ADD_TEST_TYPE_JS;
		} else if (mTestTypeName.equalsIgnoreCase("FOLDER")) {
			testTypeJS = ServletUtil.ADD_TEST_FOLDER_JS;
		}
		return testTypeJS;
	}

	/**
	 * Helper method gets the HTML for rendering
	 * a JUnit test type input form table
	 * 
	 * @return The HTML String form table
	 */
	protected String getJUnitFormTable() {
		StringBuffer reply = new StringBuffer();
		reply
				.append("<table >\n<caption><b>JUnit Test Properties</b></caption>\n");
		reply
				.append("<tr><th>Eclipse Home:</th><td><input type=\"text\" name=\"eclipseHome\" size=\"75\" value=\""
						+ ECLIPSE_HOME + "\"></td></tr>\n");
		reply
				.append("<tr><th>Eclipse Workspace:</th><td><input type=\"text\" name=\"eclipseWorkspace\" size=\"75\" value=\""
						+ ECLIPSE_WORKSPACE + "\"></td></tr>\n");
		reply
				.append("<tr><th>Eclipse Project:</th><td><input type=\"text\" name=\"eclipseProj\" size=\"75\" value=\""
						+ ECLIPSE_PROJECT + "\"></td></tr>\n");
		reply
				.append("<tr><th>Test Suite:</th><td><input type=\"text\" name=\"testSuite\" size=\"75\" value=\""
						+ mTestName + ".testsuite\"></td></tr>\n");
		reply
				.append("<tr><th>Report Directory:</th><td><input type=\"text\" name=\"reportDir\" size=\"75\" value=\""
						+ REPORT_DIR + "\"></td></tr>\n");
		reply
				.append("<tr><th>TPTP Connection URL:</th><td><input type=\"text\" name=\"tptpConn\" size=\"75\" value=\""
						+ TPTP_CONN + "\"></td></tr>\n");
		reply.append("</table>\n<br>\n");
		return reply.toString();
	}
}
