/********************************************************************
 * 
 * File		:	UpdateTestEntity.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that updates a Test in the TPTeam 
 * 				database
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update;

import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.tptp.TPTestCRUD;

/*******************************************************************************
 * File 		: 	UpdateTestEntity.java
 * 
 * Description 	: 	Servlet that updates a Test in the TPTeam 
 * 					database
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class UpdateTestEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mTestID = null;

	protected String mTestName = null;

	protected String mTestDesc = null;

	protected String mTestTypeName = null;

	protected String mRemoteUser = null;

	boolean mIsFolder = false;

	protected Test mTestStub = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Updates a Test with the passed in form inputs  
	 * Renders results, including errors, to the the user.
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		mTestID = req.getParameter("testID");
		mTestName = req.getParameter("testName");
		mTestDesc = req.getParameter("testDesc");
		mRemoteUser = req.getRemoteUser();

		try {
			updateTest(req, resp);
			StringBuffer reply = new StringBuffer("<h3>Update Test "
					+ mTestName + " was Successful</h3>");
			showPage(req, resp, reply, null, this);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			e.printStackTrace();
			throwError(req, resp, error, this);
			return;
		}
	}

	/**
	 * Helper method that requests the actual 
	 * Test update.  Creates a test update request
	 * TPEvent and requests that TPTestCRUD perform
	 * the update.
	 * 
	 * @see TPTestCRUD
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */

	protected void updateTest(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		mTestStub = new Test();
		mTestStub.setId(Integer.parseInt(mTestID));
		mTestStub.setName(mTestName);
		mTestStub.setDescription(mTestDesc);
		if (req.getParameter("eclipseHome") != null
				&& req.getParameter("eclipseHome").length() > 0
				&& !req.getParameter("eclipseHome").equals("")) {
			JunitTest junit = new JunitTest();
			junit.setEclipseHome(req.getParameter("eclipseHome"));
			junit.setWorkspace(req.getParameter("eclipseWorkspace"));
			junit.setProject(req.getParameter("eclipseProj"));
			junit.setTestSuite(req.getParameter("testSuite"));
			junit.setReportDir(req.getParameter("reportDir"));
			junit.setTptpConnection(req.getParameter("tptpConn"));
			mTestStub.addJunitTest(junit);
		}
		String testXML = TestXML.getXML(mTestStub);

		// Wrap info into TPEvent
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.ID_KEY, String.valueOf(mTestID));
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient()
				.getTPMgrECFID());
		dictionary.put(TPEvent.TEST_XML_KEY, testXML);

		TPEvent tpEvent = new TPEvent(ITPBridge.TEST_UPDATE_REQ_TOPIC,
				dictionary);

		TPTestCRUD.sendTestUpdateResponse(tpEvent);
	}
}
