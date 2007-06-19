/********************************************************************
 * 
 * File		:	ViewProjectByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for viewing
 * 				a particular TPTeam Project, for use by a 
 * 				non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.view;

import java.util.Set;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewProject;

public class ViewProjectByUser extends ViewProject implements UserServlet {
	private static final long serialVersionUID = 1L;

	protected String getProjRows() throws Exception
{
		Transaction tx = null;
		Set<Project> projs = null;
		StringBuffer projRows = new StringBuffer();
		try {
			int remoteUserId = ServletUtil.getRemoteUserID(mRemoteUser);
			// For standalone
			//Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			TpteamUser user = (TpteamUser) s.createQuery(
					"from TpteamUser as user where user.id = " + remoteUserId)
					.uniqueResult();
			projs = user.getProjects();

			for(Project proj : projs)
			{
				projRows.append(rowNameHeader + proj.getName());
				if(proj.getDescription() != null && !proj.getDescription().equalsIgnoreCase("null"))
				{
					projRows.append(": " + proj.getDescription());
				}
				projRows.append("</td>\n" + rowIDHeader + " value=\"" + proj.getId() + "\">\n" + rowSubmitHeader + "\n");
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if(projRows.length() > 0)
			mIsProjAvailable = true;
		
		mProjRows = projRows.toString();
		
		return mProjRows;
	}
}
