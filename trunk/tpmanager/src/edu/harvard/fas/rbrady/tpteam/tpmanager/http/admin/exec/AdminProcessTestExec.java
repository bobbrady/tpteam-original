/********************************************************************
 * 
 * File		:	AdminProcessTestExec.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that validates a test request
 * 				and delegates its execution
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.exec;

import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.tptp.TPTestExec;

/*******************************************************************************
 * File 		: 	AdminProcessTestExec.java
 * 
 * Description 	: 	Servlet that validates a test request
 * 					and delegates its execution
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class AdminProcessTestExec extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mRemoteUser;

	protected String mTestID;

	protected TPEvent mTPEvent;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Collects ID of Test be executed, delegates execution, 
	 * and renders execution results
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		mRemoteUser = req.getRemoteUser();
		mTestID = req.getParameter("testID");

		try {
			validateTestReq(req);
			StringBuffer reply = new StringBuffer(execTest());
			showPage(req, resp, reply, null, this);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	/**
	 * Creates a new test execution request TPEvent and requests
	 * and execution from the TPTestExec class
	 * 
	 * @see TPTestExec
	 * @return the test result in HTML format
	 * @throws Exception
	 */
	protected String execTest() throws Exception {

		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		String fromECFID = Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID();
		dictionary.put(TPEvent.FROM, fromECFID);
		mTPEvent = new TPEvent(ITPBridge.TEST_EXEC_REQ_TOPIC, dictionary);

		TPTestExec.runTest(mTestID, mTPEvent);
		TPTestExec.sendTestExecResponse(mTPEvent);

		StringBuffer reply = new StringBuffer();
		reply
				.append("<h3 align=\"center\">TPManager Test Execution Result Page</h3>");
		reply.append("<h3>Test Name: " + mTPEvent.getTestName() + "</h3>");
		reply.append("<h3>Exec Username: " + mRemoteUser
				+ "</h3>\n<h3>testID: " + mTestID + "</h3>");
		reply.append("<h3>TestSuite Execution Verdict: "
				+ mTPEvent.getDictionary().get(TPEvent.VERDICT_KEY) + "</h3>");

		return reply.toString();
	}

	/**
	 * Helper method that ensures a single test definition and not
	 * a folder was selected for execution 
	 * 
	 * @param req The Servlet Request
	 * @throws Exception
	 */
	protected void validateTestReq(HttpServletRequest req) throws Exception {
		Transaction tx = null;
		try {
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, new Integer(mTestID));

			if (Integer.parseInt(mTestID) <= 0 || test.getIsFolder() == 'Y'
					|| test.getIsFolder() == 'y') {
				throw new Exception(
						"Invalid Test Definition Selected for Execution",
						new Throwable(
								"A Test Folders was Selected for Execution"));
			}

			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}
}
