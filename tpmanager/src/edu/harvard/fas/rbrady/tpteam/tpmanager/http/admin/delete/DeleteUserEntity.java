/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Role;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class DeleteUserEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;
	
	private String mLastName = null;
	
	private String mFirstName = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userId = req.getParameter("userId");
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
		// For standalone debug
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			TpteamUser user = (TpteamUser)s.load(TpteamUser.class, new Integer(userId));
			mLastName = user.getLastName();
			mFirstName = user.getFirstName();
			for(Project proj : user.getProjects())
			{
				proj.getTpteamUsers().remove(user);				
			}
			s.delete(user);
			s.flush();			
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			String error = "<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>";
			adminError(req, resp, error);
			return;
		}
		adminHeader(req, resp, null);
		String reply = "<h3>Deletion of TPTeam User " + mLastName + ", " + mFirstName
				+ " was Successful</h3>";
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}
}
