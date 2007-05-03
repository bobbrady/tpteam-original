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
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class ViewUser extends ServletUtil {
	private static final long serialVersionUID = 7456848419577223441L;

	protected boolean mIsUserAvailable = false;

	protected String mUserRows = null;

	protected String rowNameHeader = "<tr><form method=\"post\" action=\"viewUser2\"><td>";

	protected String rowIDHeader = "<input type=\"hidden\" name=\"userId\"";

	protected String rowSubmitHeader = "<td><input type=\"submit\" value=\"View\"></td>\n</form></tr>\n";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mUserRows = getUserRows();
			if (mIsUserAvailable == false) {
				StringBuffer error = new StringBuffer("<h3>Error: No Users Exist</h3>");
				throwError(req, resp, error, this);
			} else {
				StringBuffer reply = new StringBuffer("<h4>View User</h4>\n<table border=\"2\">\n<th>Last, First</th><th></th>\n"
					+ mUserRows + "</table>");
				showPage(req, resp, reply, null, this);
			}
		} catch (Exception e) {
			String error = "<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>";
			adminError(req, resp, error);
			return;
		}
	}

	@SuppressWarnings("unchecked")
	protected String getUserRows() throws Exception {
		 Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		// For standalone
		//Session s = HibernateUtil.getSessionFactory().getCurrentSession();

		Transaction tx = null;
		List<TpteamUser> users = null;
		StringBuffer userRows = new StringBuffer();
		try {

			tx = s.beginTransaction();

			users = s.createQuery(
					"from TpteamUser as user order by user.lastName asc")
					.list();
			for (TpteamUser user : users) {
				userRows.append(rowNameHeader + user.getLastName() + ", "
						+ user.getFirstName() + "</td>\n");
				userRows.append(rowIDHeader + " value=\"" + user.getId()
						+ "\">\n");
				userRows.append(rowSubmitHeader);
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if (userRows.length() > 0)
			mIsUserAvailable = true;

		return userRows.toString();
	}
}
