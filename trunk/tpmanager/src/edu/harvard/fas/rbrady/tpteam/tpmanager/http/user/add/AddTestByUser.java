/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.add;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add.AddTest;

public class AddTestByUser extends AddTest implements UserServlet {
	private static final long serialVersionUID = 1L;

	protected String getProjOptions() throws Exception {

		// For standalone debug
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		Set<Project> projs = null;
		mProjOptions = "";

		try {
			int remoteUserId = ServletUtil.getRemoteUserID(mRemoteUser);
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			TpteamUser user = (TpteamUser) s.createQuery(
					"from TpteamUser as user where user.id = " + remoteUserId)
					.uniqueResult();
			projs = user.getProjects();
			// Concatenate id with name to save database call later.
			for (Project proj : projs) {
				mProjOptions += "<option value=\"" + proj.getId() + ":"
						+ proj.getName() + "\">" + proj.getName() + "</option>";
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if (!mProjOptions.equals(""))
			mIsProjAvailable = true;

		mProjOptions = "<option selected>Choose Project</option>"
				+ mProjOptions;

		return mProjOptions;
	}
}
