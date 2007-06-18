/********************************************************************
 * 
 * File		:	ViewProject2.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays the view details
 * 				a particular TPTeam Project
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view;

import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		:	ViewProject2.java
 * 
 * Description 	: 	Servlet that displays the view details
 * 					a particular TPTeam Project
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ViewProject2 extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mTeam = null;

	protected String mProjId = null;

	protected String mProjName = null;

	protected String mProjDesc = null;

	protected String mProd = null;

	protected int[] mUserIds = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Gathers the Project selected, the Product, and User 
	 * lists, renders the details
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mProjId = req.getParameter("projId");
			getProject();
			getPage(req, resp);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	/**
	 * Loads a Project from the database, populates
	 * member variables for TPTeam user and Product IDs
	 * 
	 * @throws Exception
	 */
	protected void getProject() throws Exception {
		Project proj = null;
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			proj = (Project) s.load(Project.class, new Integer(mProjId));
			if (proj.getDescription() != null) {
				mProjDesc = proj.getDescription();
			} else {
				mProjDesc = "";
			}
			mProjName = proj.getName();
			mProd = proj.getProduct().getName();
			getTeam(proj.getTpteamUsers());
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	/**
	 * Gets all TPTeam users associated with the Project
	 * 
	 * @param team The TPTeam users
	 * @throws Exception
	 */
	public void getTeam(Set<TpteamUser> team) throws Exception {
		StringBuffer teamUsers = new StringBuffer();
		if (team.size() < 1) {
			mTeam = "";
			return;
		}
		for (TpteamUser user : team) {
			teamUsers.append(user.getLastName() + ", " + user.getFirstName()
					+ " (" + user.getUserName() + ")<br>\n");
		}
		mTeam = teamUsers.toString();
	}

	/**
	 * Helper method to render the HTML page
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void getPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		StringBuffer reply = new StringBuffer();
		reply.append("<h4>View Project</h4>\n");
		reply
				.append("<table border=\"1\"><tr><th align=\"left\">Name:</th><td align=\"right\">"
						+ mProjName + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">Description:</th><td align=\"right\">"
						+ mProjDesc + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">Product:</th><td align=\"right\">"
				+ mProd + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">Team Members:</th><td align=\"right\">"
						+ mTeam + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">View  Test Tree:</th><td align=\"right\"><form method=\"post\" action=\"viewTest2\">\n<input type=\"hidden\" name=\"projId\" value=\""
						+ mProjId
						+ "\">\n<input type=\"submit\" value=\"Test Tree Details\">\n</form>\n");
		reply.append("</table>\n<br>\n");

		showPage(req, resp, reply, null, this);
	}
	
	/**
	 * Getter for TPTeam Project members in HTML format
	 * 
	 * @return team members in html format
	 */
	public String getTeam()
	{
		return mTeam;
	}

}
