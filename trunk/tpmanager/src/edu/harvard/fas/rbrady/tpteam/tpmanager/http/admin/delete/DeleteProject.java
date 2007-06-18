/********************************************************************
 * 
 * File		:	DeleteProject.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for deleting
 * 				a TPTeam Project
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		: 	DeleteProject.java
 * 
 * Description 	: 	Servlet that displays an input form for deleting
 * 					a TPTeam Project
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class DeleteProject extends ServletUtil {
	private static final long serialVersionUID = 7456848419577223441L;
	private boolean mIsProjAvailable = false;
	private String mProjRows = null;

	private String rowNameHeader = "<tr><form method=\"post\" onSubmit=\"return validateForm(this);\"><th>Project</th><td>";
	
	private String rowIDHeader = "<input type=\"hidden\" name=\"projId\"";
	
	private String rowSubmitHeader = "<td><input type=\"submit\" value=\"Delete\"></td>\n</form></tr>\n";
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Gathers all Projects as HTML input forms, renders page
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
		getProjRows();
		if(mIsProjAvailable == false)
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
	 * Gets all Projects and wraps them as HTML
	 * selection list options
	 * 
	 * @return the HTML String of Project options
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String getProjRows() throws Exception
	{
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		List<Project> projs = null;
		StringBuffer projRows = new StringBuffer();
		try {

			tx = s.beginTransaction();

			projs = s.createQuery("from Project as p order by p.name asc").list();
			for(Project proj : projs)
			{
				projRows.append(rowNameHeader + proj.getName() + "<input type=\"hidden\" name=\"projName\" value=\"" + proj.getName() + "\">");
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
		String error = "<h3>Error: No Project Available</h3>";
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
		String reply = "<h4>Delete Project</h4>\n<table border=\"2\">" + mProjRows + "</table>";		
		adminHeader(req, resp, DELETE_PROJ_JS);
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}
}
