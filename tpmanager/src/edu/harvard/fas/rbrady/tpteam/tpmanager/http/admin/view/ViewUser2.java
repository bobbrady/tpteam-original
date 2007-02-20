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
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;

public class ViewUser2 extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mRemoteUser = null;

	protected String mUserId = null;

	protected String mFirstName = null;

	protected String mLastName = null;

	protected String mUserName = null;

	protected String mPassword = null;

	protected String mECFId = null;

	protected String mEmail = null;

	protected String mPhone = null;

	protected String mRole = null;

	protected String mProjects = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mRemoteUser = req.getRemoteUser();
			mUserId = req.getParameter("userId");
			getUser();
			showPage(req, resp);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	public void getUser() throws Exception {
		TpteamUser user = null;
		Transaction tx = null;
		try {
			if(this instanceof UserServlet)
			{
				mUserId = String.valueOf(getRemoteUserID(mRemoteUser));
			}
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			// For standalone
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			tx = s.beginTransaction();
			user = (TpteamUser) s.load(TpteamUser.class, new Integer(mUserId));
			mFirstName = user.getFirstName();
			mLastName = user.getLastName();
			mUserName = user.getUserName();
			mECFId = user.getEcfId();
			mEmail = user.getEmail();
			mPhone = user.getPhone();
			mRole = user.getRole().getName();
			getProjects(user.getProjects());
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	protected void getProjects(Set<Project> projects) throws Exception {
		StringBuffer projs = new StringBuffer();
		if (projects == null || projects.size() < 1) {
			mProjects = "";
			return;
		}
		for (Project proj : projects) {
			projs.append(proj.getName() + "<br>");
		}
		mProjects = projs.toString();
	}

	protected void showPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		StringBuffer reply = new StringBuffer();
		reply.append("<h4>View User</h4>\n");
		reply
				.append("<table border=\"1\"><tr><th align=\"left\">Last Name:</th><td align=\"right\">"
						+ mLastName + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">First Name:</th><td align=\"right\">"
						+ mFirstName + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">User Name:</th><td align=\"right\">"
						+ mUserName + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">ECF Id:</th><td align=\"right\">"
				+ mECFId + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">Email:</th><td align=\"right\">"
				+ mEmail + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">Phone (ddd-ddd-dddd):</th><td align=\"right\">"
						+ mPhone + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">Role:</th><td align=\"right\">"
				+ mRole + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">Projects:</th><td align=\"right\">"
						+ mProjects + "</td></tr>\n");
		reply.append("</table>\n");

		showPage(req, resp, reply, null, this);
	}
}
