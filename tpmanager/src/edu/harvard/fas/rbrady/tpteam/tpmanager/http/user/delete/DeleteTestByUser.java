/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.delete;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteTest;

public class DeleteTestByUser extends DeleteTest implements UserServlet {

	private static final long serialVersionUID = 1L;

	protected String getProjRows() throws Exception {

		Transaction tx = null;
		Set<Project> projs = null;
		StringBuffer projRows = new StringBuffer();
		try {
			int remoteUserId = ServletUtil.getRemoteUserID(mRemoteUser);
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			// For standalone
			// Session s =
			// HibernateUtil.getSessionFactory().getCurrentSession();

			tx = s.beginTransaction();
			TpteamUser user = (TpteamUser) s.createQuery(
					"from TpteamUser as user where user.id = " + remoteUserId)
					.uniqueResult();
			projs = user.getProjects();
			for (Project proj : projs) {
				String desc = proj.getDescription();
				if (desc == null || desc.equalsIgnoreCase("null"))
					desc = "";
				projRows.append(rowNameHeader + proj.getName() + "</td>\n");
				projRows.append(rowIDHeader + " value=\"" + proj.getId()
						+ "\">\n");
				projRows.append(rowSubmitHeader);
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if (projRows.length() > 0)
			mIsProjAvailable = true;
		mProjRows = projRows.toString();
		return mProjRows;
	}

}
