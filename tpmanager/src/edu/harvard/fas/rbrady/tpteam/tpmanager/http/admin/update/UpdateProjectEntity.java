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
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Product;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class UpdateProjectEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String projId = req.getParameter("projId");
		String name = req.getParameter("projName");
		String description = req.getParameter("projDesc");
		String prodId = req.getParameter("prod");
		String[] userIds = req.getParameterValues("team");
		if(userIds == null)
		{
			userIds = new String[0];
		}
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		// For standalone debug
		//Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			Project proj = (Project)s.load(Project.class, new Integer(projId));
			proj.setName(name);
			proj.setDescription(description);
			Product prod = (Product) s.load(Product.class, new Integer(prodId));
			proj.setProduct(prod);
			proj.getTpteamUsers().removeAll(proj.getTpteamUsers());
			for (String userId : userIds) {
				TpteamUser tpTeamUser = (TpteamUser) s.load(TpteamUser.class,
						new Integer(userId));
				// Project is not the inverse=true, so use it to associate
				// proj_user map table
				proj.addToTpteamUsers(tpTeamUser);
			}
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
		String reply = "<h3>Update Project " + name + " was Successful</h3>";
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}
}
