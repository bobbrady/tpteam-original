/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.model.TPTest;


public class ProcessTestExecServlet extends HttpServlet {

	private static final long serialVersionUID = 7456848419577223441L;

	private static HashMap<String, String> mUsers;
	
	private static HashMap<String, TPTest> mTests;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// init hashmaps for user & test lookups
		mUsers = new HashMap<String, String>();
		mTests = new HashMap<String, TPTest>();
		mUsers.put("admin", "tpteam");
		
		// init tptests
		/*
		TPTest addUserTest = new TPTest();
		TPTest editTest = new TPTest();
		addUserTest.setSuiteName("AddUser.testsuite");
		addUserTest.setName("TestAddUser");
		editTest.setSuiteName("EditTestSuite.testsuite");
		editTest.setName("TestEditTestSuite");
		mTests.put("2", addUserTest);
		mTests.put("1", editTest);
		*/
		
		TPTest productTest = new TPTest();
		productTest.setSuiteName("ProductTest.testsuite");
		productTest.setName("ProductTest");
		mTests.put("1", productTest);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(authenticateUser(req) == false)
		{
			loginFailure(req, resp);
			return;
		}
		if(validateTestReq(req)== false)
		{
			invalidTestReq(req, resp);
			return;
		}
		
		resp.setContentType("text/html");
		String header = "<html><head><title>TPManager Process Test Exec Response</title></head>";
		String body = "<body>"
				+ "<h3 align=\"center\">TPManager Test Execution Result Page</h3>"
				+ "<hr size=\"2\"><p><div align=\"center\">";
		String username = req.getParameter("username");
		String testID = req.getParameter("testID");
		String reply = "<h3>Username: " + username + "</h3>";
		reply += "<h3>testID: " + testID + "</h3>";
		
		TPTest tpTest = (TPTest)mTests.get(testID);
		reply += "<h3>TestSuite Name: " + tpTest.getSuiteName() + "</h3>";
		
		String footer = "</div><hr size=\"2\"></body></html>";
		
		TPEvent tpEvent = new TPEvent(ITPBridge.TEST_EXEC_RESULT_TOPIC, "Demo Project (TPTeam)","Admin", tpTest.getName(), testID, "status");
		//Activator.getDefault().getTPManager().runTests(tpTest, tpEvent);
		
		reply += "<h3>TestSuite Execution Status: " + tpEvent.getStatus() + "</h3>";
		
		resp.getWriter().write(header + body + reply + footer); //$NON-NLS-1$
	}

	private boolean authenticateUser(HttpServletRequest req) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		if(username == null || password == null)
			return false;
		if(!mUsers.containsKey(username))
			return false;
		String lookupPassword = mUsers.get(username);
		if(!password.trim().equals(lookupPassword.trim()))
			return false;
		return true;
	}
	
	private void loginFailure(HttpServletRequest req, HttpServletResponse resp) 
		throws IOException {
		resp.setContentType("text/html");
		String header = "<html><head><title>TPManager Process Test Exec Response</title></head>";
		String body = "<body>"
				+ "<h3 align=\"center\">TPManager Test Execution Request Page</h3>"
				+ "<hr size=\"2\"><p><div align=\"center\">";
		String username = req.getParameter("username");
		String reply = "<h3>Invalid Username or Password for Username: " + username + "</h3>";
		String footer = "</div><hr size=\"2\"></body></html>";
		resp.getWriter().write(header + body + reply + footer); //$NON-NLS-1$
	}

	private boolean validateTestReq(HttpServletRequest req) {
		String testID = req.getParameter("testID");
		if(testID == null)
			return false;
		if(!mTests.containsKey(testID))
			return false;
		return true;
	}
	
	private void invalidTestReq(HttpServletRequest req, HttpServletResponse resp)
		throws IOException {
		resp.setContentType("text/html");
		String header = "<html><head><title>TPManager Process Test Exec Response</title></head>";
		String body = "<body>"
				+ "<h3 align=\"center\">TPManager Test Execution Request Page</h3>"
				+ "<hr size=\"2\"><p><div align=\"center\">";
		String testID = req.getParameter("testID");
		String reply = "<h3>No TPTest Available for TestID: " + testID + "</h3>";
		String footer = "</div><hr size=\"2\"></body></html>";
		resp.getWriter().write(header + body + reply + footer); //$NON-NLS-1$
	}
	
	public static TPTest getTest(String ID)
	{
		return mTests.get(ID);
	}
	
}
