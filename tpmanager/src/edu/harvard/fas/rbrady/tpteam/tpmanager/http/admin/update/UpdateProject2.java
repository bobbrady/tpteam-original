/********************************************************************
 * 
 * File		:	UpdateProject2.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays the details for updating
 * 				a particular TPTeam Project
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
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		:	UpdateProject2.java
 * 
 * Description 	: 	Servlet that displays the details for updating
 * 					a particular TPTeam Project
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class UpdateProject2 extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;
	private boolean mIsProdAvailable = false;
	private boolean mIsTeamUserAvailable = false;
	private String mProdOptions = null;
	private String mTeamOptions = null;
	private String mProjId = null;
	private String mProjName = null;
	private String mProjDesc = null;
	private int mProdId = 0;
	private int[] mUserIds = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Gathers the Project selected, the Product, and User 
	 * selection lists, renders input form
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
		mProjId = req.getParameter("projId");
		getProject();
		getProdOptions();
		getTeamOptions();
		if(mIsProdAvailable == false || mIsTeamUserAvailable == false)
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
	 * Loads a Project from the database, populates
	 * member variables for TPTeam user and Product IDs
	 * 
	 * @throws Exception
	 */
	private void getProject() throws Exception
	{
		Project proj = null;
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			proj = (Project)s.load(Project.class, new Integer(mProjId));
			if(proj.getDescription() != null)
			{
				mProjDesc = proj.getDescription();
			}
			else
			{
				mProjDesc = "";
			}
			mProjName = proj.getName();
			mProdId = proj.getProduct().getId();
			mUserIds = new int[proj.getTpteamUsers().size()];
			int idx = 0;
			for(TpteamUser user : proj.getTpteamUsers())
			{
				mUserIds[idx++] = user.getId(); 
			}
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}
	
	/**
	 * Helper method that gets all Products and wraps 
	 * them in HTML option tags
	 * 
	 * @return The Product option tags
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String getProdOptions() throws Exception
	{
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		List<Product> prods = null;
		StringBuffer prodOptions = new StringBuffer();
		
		try {

			tx = s.beginTransaction();

			prods = s.createQuery("from Product as p order by p.name asc").list();
			for(Product prod : prods)
			{
				if(prod.getId() == mProdId)
				{
					prodOptions.append("<option selected value=\"" + prod.getId() + "\">" + prod.getName() + "</option>");
				}
				else
				{
					prodOptions.append("<option value=\"" + prod.getId() + "\">" + prod.getName() + "</option>");
				}
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if(prodOptions.length() > 0)
			mIsProdAvailable = true;
		
		mProdOptions = prodOptions.toString();
		
		return mProdOptions;
	}

	/**
	 * Helper method that gets all TPTeam users and
	 * wraps them into HTML option tags
	 * 
	 * @return The TPTeam user option tags
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String getTeamOptions() throws Exception
	{
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		List<TpteamUser> users = null;
		StringBuffer teamOptions = new StringBuffer();
		try {

			tx = s.beginTransaction();
			
			users = s.createQuery("from TpteamUser as team order by team.lastName asc").list();
			for(TpteamUser user : users)
			{
				if(isUserSelected(user))
				{
					teamOptions.append("<option selected value=\"" + user.getId() + "\">" + user.getLastName() + ", " + user.getFirstName()+ "</option>");
				}
				else
				{
					teamOptions.append("<option value=\"" + user.getId() + "\">" + user.getLastName() + ", " + user.getFirstName()+ "</option>");
				}
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if(teamOptions.length()> 0)
			mIsTeamUserAvailable = true;
		
		mTeamOptions = teamOptions.toString();

		return mTeamOptions;
	}
	
	private boolean isUserSelected(TpteamUser user)
	{
		boolean returnVal = false;
		for(int selectedUserId : mUserIds)
		{
			if(user.getId() == selectedUserId)
			{
				returnVal = true;
				break;
			}
		}
		return returnVal;
	}
	
	/**
	 * Helper method to render errors as HTML
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void throwError(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		String error = "<h3>Error: No Product or TPTeam Member Available</h3>";
		adminError(req, resp, error);
	}
	
	/**
	 * Helper method to render the HTML page, including 
	 * JavaScript
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showPage(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		StringBuffer reply = new StringBuffer();
		reply.append("<h4>Update Project</h4>\n");
		reply.append("<form name=\"updateProj\" method=\"post\" onSubmit=\"return validateForm(this);\">\n");
		reply.append("<table ><tr><th>Name:</th><td><input type=\"text\" name=\"projName\"  value=\"" + mProjName + "\" size=\"25\"></td></tr>\n");
		reply.append("<tr><th>Description:</th><td><input type=\"text\" name=\"projDesc\" value=\"" + mProjDesc + "\" size=\"50\"></td></tr>\n");
		reply.append("<tr><th>Product:</th><td><select name=\"prod\">" + mProdOptions + "</select></td></tr>\n");
		reply.append("<tr><th>Team Members:</th><td><select multiple size=\"5\" name=\"team\">" + mTeamOptions + "</select></td></tr>\n");
		reply.append("</table>\n<br>\n<input type=\"hidden\" name=\"projId\" value=\"" + mProjId + "\">\n<input type=\"submit\" value=\"Update\">\n</form>\n");
		
		adminHeader(req, resp, UPDATE_PROJ_JS);
		adminReply(req, resp, reply.toString());
		adminFooter(req, resp);
	}


}
