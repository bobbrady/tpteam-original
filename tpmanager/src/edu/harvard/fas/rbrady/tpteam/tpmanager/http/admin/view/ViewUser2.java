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

import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class ViewUser2 extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	private String mUserId = null;

	private String mFirstName = null;

	private String mLastName = null;

	private String mUserName = null;

	private String mPassword = null;

	private String mECFId = null;

	private String mEmail = null;

	private String mPhone = null;

	private String mRole = null;

	private String mProjects = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mUserId = req.getParameter("userId");
			getUser();
			showPage(req, resp);
		} catch (Exception e) {
			String error = "<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>";
			adminError(req, resp, error);
			return;
		}
	}

	public void getUser() throws Exception {
		TpteamUser user = null;
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
		// For standalone
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();

		Transaction tx = null;
		try {

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

	private void getProjects(Set<Project> projects) throws Exception {
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

	private void throwError(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String error = "<h3>Error: No Product or TPTeam Member Available</h3>";
		adminError(req, resp, error);
	}

	private void showPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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

		adminHeader(req, resp, null);
		adminReply(req, resp, reply.toString());
		adminFooter(req, resp);
	}

	public static void main(String[] args) {
		try {
			ViewUser2 viewUser = new ViewUser2();
			viewUser.mUserId = "1";
			viewUser.getUser();
			System.out.println("Projets: " + viewUser.mProjects);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
