/********************************************************************
 * 
 * File		:	AddProjectEntity.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that creates a new Project and persists
 * 				it to the TPTeam database
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add;

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
 * File 		: 	AddProjectEntity.java
 * 
 * Description 	: 	Servlet that creates a new Project and persists
 * 					it to the TPTeam database
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class AddProjectEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Creates an new Project from input parameters, persists
	 * it to the database.  Renders results, including errors,
	 * to the the user.
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String name = req.getParameter("projName");
		String description = req.getParameter("projDesc");
		String prodId = req.getParameter("prod");
		String[] userIds = req.getParameterValues("team");
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			Project proj = new Project();
			proj.setName(name);
			proj.setDescription(description);
			Product prod = (Product) s.load(Product.class, new Integer(prodId));
			proj.setProduct(prod);
			for (String userId : userIds) {
				TpteamUser tpTeamUser = (TpteamUser) s.load(TpteamUser.class,
						new Integer(userId));
				// Project is not the inverse=true, so use it to associate
				// proj_user map table
				proj.addToTpteamUsers(tpTeamUser);
			}
			s.save(proj);
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
		String reply = "<h3>Add Project " + name + " was Successful</h3>";
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}
}
