/********************************************************************
 * 
 * File		:	ViewUser2.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays the details of a particular
 * 				TPTeam user 
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
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;

/*******************************************************************************
 * File 		: 	ViewUser2.java
 * 
 * Description 	: 	Servlet that displays the details of a particular
 * 					TPTeam user in a form for editable updating
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ViewUser2 extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mRemoteUser = null;

	protected String mUserId = null;

	protected String mFirstName = null;

	protected String mLastName = null;

	protected String mUserName = null;

	protected String mPassword = null;

	protected String mECFId = null;

	protected String mEmail = null;

	protected String mPhone = null;

	protected String mRole = null;

	protected String mProjects = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Gathers the details of TPTeam user selected, renders HTML
	 *
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mRemoteUser = req.getRemoteUser();
			mUserId = req.getParameter("userId");
			getUser();
			showPage(req, resp);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	/**
	 * Loads a TPTeam user from the database, populates
	 * member variables for viewing details
	 * 
	 * @throws Exception
	 */
	public TpteamUser getUser() throws Exception {
		TpteamUser user = null;
		Transaction tx = null;
		try {
			if (this instanceof UserServlet) {
				mUserId = String.valueOf(getRemoteUserID(mRemoteUser));
			}
			Session s = null;
			if (Activator.getDefault() != null) {
				s = Activator.getDefault().getHiberSessionFactory()
						.getCurrentSession();
			} else {
				// For standalone
				s = HibernateUtil.getSessionFactory().getCurrentSession();
			}

			tx = s.beginTransaction();
			user = (TpteamUser) s.load(TpteamUser.class, new Integer(mUserId));
			mFirstName = user.getFirstName();
			mLastName = user.getLastName();
			mUserName = user.getUserName();
			mECFId = user.getEcfId();
			mEmail = user.getEmail();
			mPhone = user.getPhone();
			mRole = user.getRole().getName();
			getProjects(user.getProjects());
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return user;
	}


	/**
	 * Helper method to get all Projects associated with
	 * the selected TPTeam user
	 * 
	 * @param projects the Projects
	 * @throws Exception
	 */
	protected void getProjects(Set<Project> projects) throws Exception {
		StringBuffer projs = new StringBuffer();
		if (projects == null || projects.size() < 1) {
			mProjects = "";
			return;
		}
		for (Project proj : projects) {
			projs.append(proj.getName() + "<br>");
		}
		mProjects = projs.toString();
	}

	/**
	 * Helper method to render the HTML page
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void showPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		StringBuffer reply = new StringBuffer();
		reply.append("<h4>View User</h4>\n");
		reply
				.append("<table border=\"1\"><tr><th align=\"left\">Last Name:</th><td align=\"right\">"
						+ mLastName + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">First Name:</th><td align=\"right\">"
						+ mFirstName + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">User Name:</th><td align=\"right\">"
						+ mUserName + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">ECF Id:</th><td align=\"right\">"
				+ mECFId + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">Email:</th><td align=\"right\">"
				+ mEmail + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">Phone (ddd-ddd-dddd):</th><td align=\"right\">"
						+ mPhone + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">Role:</th><td align=\"right\">"
				+ mRole + "</td></tr>\n");
		reply
				.append("<tr><th align=\"left\">Projects:</th><td align=\"right\">"
						+ mProjects + "</td></tr>\n");
		reply.append("</table>\n");

		showPage(req, resp, reply, null, this);
	}
	
	/** Setter, primarily used for test harness
	 * 
	 * @param userId
	 */
	public void setUserId(String userId)
	{
		mUserId = userId;
	}
}
