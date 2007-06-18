/********************************************************************
 * 
 * File		:	DeleteTestEntity.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that deletes a Test from the TPTeam 
 * 				database
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete;

import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.tptp.TPTestCRUD;

/*******************************************************************************
 * File 		: 	DeleteTestEntity.java
 * 
 * Description 	: 	Servlet that deletes a Test from the TPTeam 
 * 					database
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class DeleteTestEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mTestID = null;

	protected String mTestName = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Collects the ID of the Test selected, requests
	 * Test deletion, renders results to user
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		mTestID = req.getParameter("testID");

		try {
			deleteTest();
			StringBuffer reply = new StringBuffer("<h3>Delete Test Node \""
					+ mTestName + " \" was Successful</h3>");
			showPage(req, resp, reply, null, this);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	/**
	 * Helper method, creates a test deletion
	 * request TPEvent and calls upon TPTestCRUD to
	 * delete the Test
	 * 
	 * @see TPTestCRUD
	 * @throws Exception
	 */
	protected void deleteTest() throws Exception {
		// Wrap info into TPEvent and send delete request
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.ID_KEY, String.valueOf(mTestID));
		dictionary.put(TPEvent.SEND_TO, Activator.getDefault()
				.getTPBridgeClient().getTPMgrECFID());
		dictionary.put(TPEvent.FROM, Activator.getDefault().getTPBridgeClient().getTPMgrECFID());
		TPEvent tpEvent = new TPEvent(ITPBridge.TEST_DEL_REQ_TOPIC, dictionary);
		TPTestCRUD.deleteTest(tpEvent);
		mTestName = tpEvent.getDictionary().get(TPEvent.TEST_NAME_KEY);
		TPTestCRUD.sendDelTestResponse(tpEvent);
	}
}
