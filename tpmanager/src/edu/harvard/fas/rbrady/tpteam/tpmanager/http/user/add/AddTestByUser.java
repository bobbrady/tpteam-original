/********************************************************************
 * 
 * File		:	AddTestByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays input forms for creating
 * 				a new TPTeam Test by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.add;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add.AddTest;

/*******************************************************************************
 * File 		: 	AddTestByUser.java
 * 
 * Description 	: 	Servlet that displays input forms for creating
 * 					a new TPTeam Test by a non-administrative Web user
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class AddTestByUser extends AddTest implements UserServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Gets all TPTeam Projects that the TPTeam user is
	 * associated with and wraps them  into HTML 
	 * select option tags
	 * 
	 * @return The Project select option tags
	 * @throws Exception
	 */
	protected String getProjOptions() throws Exception {
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
