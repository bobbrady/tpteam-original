/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.exec;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class ExecTest2 extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mProjID = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mProjID = req.getParameter("projId");
			getPage(req, resp);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	protected void getPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		StringBuffer reply = new StringBuffer();
		reply
				.append("<h4>Select a Test from the Project Tree Below, then Click the Execute Button</h4>\n");
		reply
				.append("<form name=\"execTest\" method=\"post\" onSubmit=\"return validateForm(this);\">\n");

		reply.append("<table border=\"1\"><tr><td>\n");

		reply.append(ServletUtil.getTestTree(mProjID, false));

		reply
				.append("<input type=\"hidden\" name=\"testID\" value=\"\">\n</td></tr></table><p>\n<input type=\"submit\" value=\"Execute\">\n</form>\n");

		showPage(req, resp, reply, ServletUtil.EXEC_TEST_TREE_JS
				+ ServletUtil.ADD_TEST_TREE_JS + ServletUtil.ADD_TEST_TREE_CSS, this);
	}
}