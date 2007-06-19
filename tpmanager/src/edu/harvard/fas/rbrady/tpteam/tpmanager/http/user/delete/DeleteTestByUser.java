/********************************************************************
 * 
 * File		:	DeleteTestByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for deleting
 * 				a TPTeam Test by first selecting its parent Project,
 * 				for use by a non-administrative Web user
 *  
 ********************************************************************/
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

/*******************************************************************************
 * File 		: 	DeleteTestByUser.java
 * 
 * Description 	: 	Servlet that displays an input form for deleting
 * 					a TPTeam Test by first selecting its parent Project,
 * 					for use by a non-administrative Web user
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class DeleteTestByUser extends DeleteTest implements UserServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Gets all TPTeam Projects that are associated
	 * with the TPTeam user and wraps them 
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
