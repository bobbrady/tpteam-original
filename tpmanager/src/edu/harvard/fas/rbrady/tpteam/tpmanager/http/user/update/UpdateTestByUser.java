/********************************************************************
 * 
 * File		:	UpdateTestByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for updating
 * 				a TPTeam Test by first selecting its parent Project,
 * 				for use by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateTest;

/*******************************************************************************
 * File 		: 	UpdateTestByUser.java
 * 
 * Description 	: 	Servlet that displays an input form for updating
 * 					a TPTeam Test by first selecting its parent Project,
 * 					for use by a non-administrative Web user
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class UpdateTestByUser extends UpdateTest implements UserServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Gets all TPTeam Projects that the TPTeam
	 * user is associated with and wraps them 
	 * into HTML select option tags
	 * 
	 * @return The Project select option tags
	 * @throws Exception
	 */
	protected String getProjRows() throws Exception {
		Transaction tx = null;
		Set<Project> projs = null;
		StringBuffer projRows = new StringBuffer();
		try {
			int remoteUserId = ServletUtil.getRemoteUserID(mRemoteUser);
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			//Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = s.beginTransaction();
			TpteamUser user = (TpteamUser) s.createQuery(
					"from TpteamUser as user where user.id = " + remoteUserId)
					.uniqueResult();
			projs = user.getProjects();
			// Concatenate id with name to save database call later.
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
