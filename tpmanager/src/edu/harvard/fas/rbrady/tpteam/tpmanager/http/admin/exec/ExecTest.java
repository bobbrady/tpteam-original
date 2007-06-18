/********************************************************************
 * 
 * File		:	ExecTest.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for executing
 * 				a TPTeam Test by first selecting its parent Project
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.exec;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
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
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;

/*******************************************************************************
 * File 		: 	ExecTest.java
 * 
 * Description 	: 	Servlet that displays an input form for executing
 * 					a TPTeam Test by first selecting its parent Project
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ExecTest extends ServletUtil {
	private static final long serialVersionUID = 7456848419577223441L;
	protected boolean mIsProjAvailable = false;
	protected String mProjRows = null;
	protected String mRemoteUser = null;

	protected String rowNameHeader = "<tr><form method=\"post\" action=\"execTest2\"><th align=\"left\">Project</th><td align=\"right\">";
	
	protected String rowIDHeader = "<input type=\"hidden\" name=\"projId\"";
	
	protected String rowSubmitHeader = "<td><input type=\"submit\" value=\"View Test Tree\"></td>\n</form></tr>\n";
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Gathers Project select list form and renders so user 
	 * can select parent Project of Test to be executed
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
		mRemoteUser = req.getRemoteUser();
		getProjRows();
		if(mIsProjAvailable == false)
		{
			StringBuffer error = new StringBuffer("<h3>Error: No Project Available</h3>");
			throwError(req, resp, error, this);
		}
		else
		{
			StringBuffer reply = new StringBuffer("<h4>Execute Test: Select Parent Project</h4>\n<table border=\"2\">" + mProjRows + "</table>");
			showPage(req, resp, reply, null, this);
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
	 * Gets all TPTeam Projects and wraps them 
	 * into HTML select option tags
	 * 
	 * @return The Project select option tags
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected String getProjRows() throws Exception
	{
		Transaction tx = null;
		Set<Project> projs = new HashSet<Project>();
		StringBuffer projRows = new StringBuffer();
		int remoteUserId = -1;
		try {
			if (this instanceof UserServlet) {
				remoteUserId = getRemoteUserID(mRemoteUser);
			}
			Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
			tx = s.beginTransaction();
			if (remoteUserId == -1) {
				List<Project> projList = s.createQuery(
						"from Project as p order by p.name asc").list();
				projs.addAll(projList);
			} else {
				TpteamUser user = (TpteamUser) s.createQuery(
						"from TpteamUser as user where user.id = "
								+ remoteUserId).uniqueResult();
				projs = user.getProjects();
			}			
			for(Project proj : projs)
			{
				String desc = proj.getDescription();
				if(desc == null || desc.equalsIgnoreCase("null"))
					desc = "";
				projRows.append(rowNameHeader + proj.getName() + "</td>\n");
				projRows.append(rowIDHeader + " value=\"" + proj.getId() + "\">\n");
				projRows.append(rowSubmitHeader);
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
