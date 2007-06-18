/********************************************************************
 * 
 * File		:	UpdateUser2.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays the details of a particular
 * 				TPTeam user in a form for editable updating
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Role;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;

/*******************************************************************************
 * File 		: 	UpdateUser2.java
 * 
 * Description 	: 	Servlet that displays the details of a particular
 * 					TPTeam user in a form for editable updating
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class UpdateUser2 extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected String mFormAction = "<input type=\"hidden\" name=\"formAction\" value=\"updateUserEntity\">\n";

	protected String mUserId = null;

	protected String mFirstName = null;

	protected String mLastName = null;

	protected String mUserName = null;

	protected String mPassword = null;

	protected String mECFId = null;

	protected String mEmail = null;

	protected String mPhone = null;

	protected String mRoleId = null;

	protected String mRoleOptions = null;

	protected boolean mIsRoleAvailable;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Gathers the details of TPTeam user selected and the Role  
	 * selection list, renders input form
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			if(this instanceof UserServlet)
			{
				mUserId = String.valueOf(getRemoteUserID(req.getRemoteUser()));
				getUser();
			}
			else
			{
				mUserId = req.getParameter("userId");
				getUser();
				getRoleOptions();
			}
			getPage(req, resp);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	/**
	 * Loads a TPTeam user from the database, populates
	 * member variables for setting form defaults
	 * 
	 * @throws Exception
	 */
	public void getUser() throws Exception {
		TpteamUser user = null;
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			user = (TpteamUser) s.load(TpteamUser.class, new Integer(mUserId));
			mFirstName = user.getFirstName();
			mLastName = user.getLastName();
			mUserName = user.getUserName();
			mPassword = user.getPassword();
			mECFId = user.getEcfId();
			mEmail = user.getEmail();
			mPhone = user.getPhone();
			mRoleId = String.valueOf(user.getRole().getRoleId());

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	/**
	 * Helper method that gets all Roles and wraps 
	 * them in HTML option tags
	 * 
	 * @return The Product option tags
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected String getRoleOptions() throws Exception {
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
		// For standalone
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();

		Transaction tx = null;
		List<Role> roles = null;
		StringBuffer roleOptions = new StringBuffer();
		try {

			tx = s.beginTransaction();

			roles = s.createQuery("from Role as role order by role.name asc")
					.list();
			for (Role role : roles) {
				if (role.getRoleId() == Integer.parseInt(mRoleId)) {
					roleOptions.append("<option value=\"" + role.getRoleId()
							+ "\" selected>" + role.getName() + "</option>\n");
				} else {
					roleOptions.append("<option value=\"" + role.getRoleId()
							+ "\">" + role.getName() + "</option>\n");
				}
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if (roleOptions.length() > 0)
			mIsRoleAvailable = true;

		mRoleOptions = roleOptions.toString();
		return roleOptions.toString();
	}

	/**
	 * Helper method to render the HTML page, including 
	 * form inputs, JavaScript
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void getPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		StringBuffer reply = new StringBuffer();
		reply.append("<h4>Update User</h4>\n");
		reply
				.append("<form method=\"post\" onSubmit=\"return validateForm(this);\">\n");
		reply
				.append("<table ><tr><th>Last Name:</th><td><input type=\"text\" name=\"lastName\"  value=\""
						+ mLastName + "\" size=\"25\"></td></tr>\n");
		reply
				.append("<tr><th>First Name:</th><td><input type=\"text\" name=\"firstName\"  value=\""
						+ mFirstName + "\" size=\"25\"></td></tr>\n");
		reply
				.append("<tr><th>User Name:</th><td><input type=\"text\" name=\"userName\"  value=\""
						+ mUserName + "\" size=\"25\"></td></tr>\n");
		reply
				.append("<tr><th>Password:</th><td><input type=\"password\" name=\"password\" value=\""
						+ mPassword + "\" size=\"50\"></td></tr>\n");
		reply
				.append("<tr><th>Password (confirm):</th><td><input type=\"password\" name=\"passwordConfirm\" value=\""
						+ mPassword + "\" size=\"50\"></td></tr>\n");
		reply
				.append("<tr><th>ECF Id:</th><td><input type=\"text\" name=\"ecfID\"  value=\""
						+ mECFId + "\" size=\"25\"></td></tr>\n");
		reply
				.append("<tr><th>Email:</th><td><input type=\"text\" name=\"email\"  value=\""
						+ mEmail + "\" size=\"25\"></td></tr>\n");
		reply
				.append("<tr><th>Phone (ddd-ddd-dddd):</th><td><input type=\"text\" name=\"phone\"  value=\""
						+ mPhone + "\" size=\"25\"></td></tr>\n");
		if(!(this instanceof UserServlet))
		{
			reply.append("<tr><th>Role:</th><td><select name=\"role\">"
				+ mRoleOptions + "</select></td></tr>\n");
		}
		reply
				.append("</table>\n<br>\n<input type=\"hidden\" name=\"userId\" value=\""
						+ mUserId
						+ "\">\n"
						+ mFormAction
						+ "<input type=\"submit\" value=\"Update\">\n</form>\n");

		showPage(req, resp, reply, UPDATE_USER_JS, this);
	}
}
