/********************************************************************
 * 
 * File		:	UpdateProjectEntity.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that updates a Project in the TPTeam 
 * 				database
 *  
 ********************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		: 	UpdateProjectEntity.java
 * 
 * Description 	: 	Servlet that updates a Project in the TPTeam 
 * 					database
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class UpdateProjectEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Updates a Project with the passed in form inputs  
	 * Renders results, including errors, to the the user.
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
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
