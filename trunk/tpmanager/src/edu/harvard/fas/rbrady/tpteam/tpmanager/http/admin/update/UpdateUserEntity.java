/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Role;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;

public class UpdateUserEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userId = req.getParameter("userId");
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String ecfId = req.getParameter("ecfID");
		String phone = req.getParameter("phone");
		String roleId = req.getParameter("role");
		Transaction tx = null;
		try {
			int remoteUserId = ServletUtil.getRemoteUserID(req.getRemoteUser());
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			// For standalone debug
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			tx = s.beginTransaction();
			TpteamUser user = (TpteamUser) s.load(TpteamUser.class,
					new Integer(userId));
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setUserName(userName);
			user.setEmail(email);
			user.setPhone(phone);
			user.setEcfId(ecfId);
			user.setModifiedBy(remoteUserId);
			user.setModifiedDate(new Date());
			if (!password.equalsIgnoreCase(user.getPassword())) {
				user.setPassword(ServletUtil.getSHA1Hash(password));
			}

			if(!(this instanceof UserServlet))
			{
				Role role = (Role) s.load(Role.class, new Integer(roleId));
				user.setRole(role);
			}
			tx.commit();

			StringBuffer reply = new StringBuffer("<h3>Update TPTeam User "
					+ lastName + ", " + firstName + " was Successful</h3>");
			showPage(req, resp, reply, null, this);

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}
}
