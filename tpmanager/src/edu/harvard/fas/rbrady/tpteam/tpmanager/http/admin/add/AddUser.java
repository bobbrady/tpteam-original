/********************************************************************
 * 
 * File		:	AddUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for creating
 * 				a new TPTeam User
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Role;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		: 	AddUser.java
 * 
 * Description 	: 	Servlet that displays an input form for creating
 * 					a new TPTeam User
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class AddUser extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;
	private boolean mIsProjAvailable = false;
	private boolean mIsRoleAvailable = false;
	private String mProjOptions = null;
	private String mRoleOptions = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Gathers the Project and Role selection lists and
	 * calls helper method to render data input form
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try
		{
		getProjOptions();
		getRoleOptions();
		if(mIsProjAvailable == false || mIsRoleAvailable == false)
		{
			throwError(req, resp);
		}
		else
		{
			showPage(req, resp);
		}
		}
		 catch (Exception e) {
				String error = "<h3>Error: " + e.getMessage() + "<br>" + 
				e.getCause() + "</h3>";
				adminError(req, resp, error);
				return;
			}
	}
	
	/**
	 * Helper method that gets all Projects in TPTeam 
	 * and wraps into HTML select option tags
	 * 
	 * @return The HTML Project option tags
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String getProjOptions() throws Exception
	{
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		List<Project> projs = null;
		mProjOptions = "";
		try {

			tx = s.beginTransaction();

			projs = s.createQuery("from Project as p order by p.name asc").list();
			for(Project proj : projs)
			{
				mProjOptions += "<option value=\"" + proj.getId() + "\">" + proj.getName() + "</option>\n";
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if(!mProjOptions.equals(""))
			mIsProjAvailable = true;
		
		mProjOptions = "<option selected>Choose Project</option>\n" +  mProjOptions;
		
		return mProjOptions;
	}

	/**
	 * Helper method that gets all Roles in TPTeam 
	 * and wraps into HTML select option tags
	 * 
	 * @return The HTML Role option tags
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	private String getRoleOptions() throws Exception
	{
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		List<Role> roles = null;
		mRoleOptions = "";
		try {

			tx = s.beginTransaction();
			
			roles = s.createQuery("from Role as role order by role.name asc").list();
			for(Role role : roles)
			{
				mRoleOptions += "<option value=\"" + role.getRoleId() + "\">" + role.getName() + "</option>\n";
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if(!mRoleOptions.equals(""))
			mIsRoleAvailable = true;		
		
		mRoleOptions = "<option selected>Choose Role</option>\n" +  mRoleOptions;

		return mRoleOptions;
	}
	
	/**
	 * Helper method that renders HTML error messages
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void throwError(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		String error = "<h3>Error: No Project or TPTeam Role Available</h3>";
		adminError(req, resp, error);
	}
	
	/**
	 * Helper method that renders the HTML form
	 * as a table
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showPage(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		
		String reply = "<h4>Add User</h4>\n";
		reply += "<form name=\"addUser\" method=\"post\" onSubmit=\"return validateForm( );\">\n";
		reply += "<table ><tr><th>First Name:</th><td><input type=\"text\" name=\"firstName\" size=\"25\"></td></tr>\n";
		reply += "<tr><th>Last Name:</th><td><input type=\"text\" name=\"lastName\" size=\"25\"></td></tr>\n";
		reply += "<tr><th>User Name:</th><td><input type=\"text\" name=\"userName\" size=\"25\"></td></tr>\n";
		reply += "<tr><th>Password:</th><td><input type=\"password\" name=\"password\" size=\"25\"></td></tr>\n";
		reply += "<tr><th>Email:</th><td><input type=\"text\" name=\"email\" size=\"50\"></td></tr>\n";
		reply += "<tr><th>Phone (ddd-ddd-dddd):</th><td><input type=\"text\" name=\"phone\" size=\"25\"></td></tr>\n";
		reply += "<tr><th>ECF ID:</th><td><input type=\"text\" name=\"ecfID\" size=\"50\"></td></tr>\n";
		reply += "<tr><th>Role:</th><td><select name=\"role\">" + mRoleOptions + "</select></td></tr>\n";
		reply += "<tr><th>Projects:</th><td><select multiple size=\"5\" name=\"projects\">" + mProjOptions + "</select></td></tr>\n";
		reply += "</table>\n<br>\n<input type=\"submit\" value=\"Add\">\n</form>\n";
		
		adminHeader(req, resp, ADD_USER_JS);
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}


}
