/********************************************************************
 * 
 * File		:	ViewProject.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for viewing
 * 				a particular TPTeam Project
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view;

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
 * File 		: 	ViewProject.java
 * 
 * Description 	: 	Servlet that displays an input form for viewing
 * 					a particular TPTeam Project
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ViewProject extends ServletUtil {
	private static final long serialVersionUID = 7456848419577223441L;

	protected boolean mIsProjAvailable = false;

	protected String mProjRows = null;

	protected String mRemoteUser = null;

	protected String rowNameHeader = "<tr><form method=\"post\" action=\"viewProject2\"><th>Project</th><td>";

	protected String rowIDHeader = "<input type=\"hidden\" name=\"projId\"";

	protected String rowSubmitHeader = "<td><input type=\"submit\" value=\"View\"></td>\n</form></tr>\n";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Renders the update Project input form for selecting one Project
	 * 
	 * @param req
	 *            The Servlet Request
	 * @param resp
	 *            The Servlet Response
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mRemoteUser = req.getRemoteUser();
			getProjRows();
			if (mIsProjAvailable == false) {
				StringBuffer error = new StringBuffer(
						"<h3>Error: No Project Available</h3>");
				throwError(req, resp, error, this);
			} else {
				StringBuffer reply = new StringBuffer(
						"<h4>View Project</h4>\n<table border=\"2\">"
								+ mProjRows + "</table>");
				showPage(req, resp, reply, null, this);
			}
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	/**
	 * Helper method that gets all Project and wraps them in HTML option tags
	 * 
	 * @return The Product option tags
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected String getProjRows() throws Exception {
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
		Transaction tx = null;
		List<Project> projs = null;
		StringBuffer projRows = new StringBuffer();
		try {

			tx = s.beginTransaction();

			projs = s.createQuery("from Project as p order by p.name asc")
					.list();
			for (Project proj : projs) {
				projRows.append(rowNameHeader + proj.getName());
				if (proj.getDescription() != null
						&& !proj.getDescription().equalsIgnoreCase("null")) {
					projRows.append(": " + proj.getDescription());
				}
				projRows.append("</td>\n" + rowIDHeader + " value=\""
						+ proj.getId() + "\">\n" + rowSubmitHeader + "\n");
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if (projRows.length() > 0)
			mIsProjAvailable = true;

		mProjRows = projRows.toString();

		return mProjRows;
	}
}
