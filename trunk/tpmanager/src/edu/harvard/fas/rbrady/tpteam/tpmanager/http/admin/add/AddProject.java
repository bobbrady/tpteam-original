/********************************************************************
 * 
 * File		:	AddProject.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for creating
 * 				a new TPTeam Project
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
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		: 	AddProject.java
 * 
 * Description 	: 	Servlet that displays an input form for creating
 * 					a new TPTeam Project
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class AddProject extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;
	private boolean mIsProdAvailable = false;
	private boolean mIsTeamUserAvailable = false;
	private String mProdOptions = null;
	private String mTeamOptions = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Renders the new Project input form
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try
		{
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
		mProdOptions = "";
		try {

			tx = s.beginTransaction();
			prods = s.createQuery("from Product as p order by p.name asc").list();
			for(Product prod : prods)
			{
				mProdOptions += "<option value=\"" + prod.getId() + "\">" + prod.getName() + "</option>";
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if(!mProdOptions.equals(""))
			mIsProdAvailable = true;
		
		mProdOptions = "<option selected>Choose Product</option>" +  mProdOptions;
		
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
		mTeamOptions = "";
		try {

			tx = s.beginTransaction();
			
			users = s.createQuery("from TpteamUser as team order by team.lastName asc").list();
			for(TpteamUser user : users)
			{
				mTeamOptions += "<option value=\"" + user.getId() + "\">" + user.getLastName() + ", " + user.getFirstName()+ "</option>";
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if(!mTeamOptions.equals(""))
			mIsTeamUserAvailable = true;		

		return mTeamOptions;
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
		String javaScript = 
		"<script language=\"JavaScript\">\n<!--\n" +
			"function validateForm ( )\n" +
			"{\n var name = document.addProj.projName.value;\n" +
				"var desc = document.addProj.projDesc.value;\n" +
				"var prod = document.addProj.prod;\n" +
				"if( name == \"\")\n" +
				"{\n alert(\"Please enter at least a project name.\");\n" +
					"return false;\n}\n" + 
			    "else if(prod.selectedIndex == 0)\n" +
			    "{\n alert(\"Please select a product.\");\n" +
				"return false;\n}\n" +
				"document.addProj.action = \"addProjectEntity\";\n" +
				"document.addProj.submit();\n" +
		        "return true;\n}\n-->\n</script>\n";
		
		String reply = "<h4>Add Project</h4>\n";
		reply += "<form name=\"addProj\" method=\"post\" onSubmit=\"return validateForm( );\">\n";
		reply += "<table ><tr><th>Name:</th><td><input type=\"text\" name=\"projName\" size=\"25\"></td></tr>\n";
		reply += "<tr><th>Description:</th><td><input type=\"text\" name=\"projDesc\" size=\"50\"></td></tr>\n";
		reply += "<tr><th>Product:</th><td><select name=\"prod\">" + mProdOptions + "</select></td></tr>\n";
		reply += "<tr><th>Team Members:</th><td><select multiple size=\"5\" name=\"team\">" + mTeamOptions + "</select></td></tr>\n";
		reply += "</table>\n<br>\n<input type=\"submit\" value=\"Add\">\n</form>\n";
		
		adminHeader(req, resp, javaScript);
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}


}
