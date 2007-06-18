/********************************************************************
 * 
 * File		:	DeleteUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for deleting
 * 				a TPTeam User
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
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		: 	DeleteUser.java
 * 
 * Description 	: 	Servlet that displays an input form for deleting
 * 					a TPTeam User
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class DeleteUser extends ServletUtil {
	private static final long serialVersionUID = 7456848419577223441L;

	private boolean mIsUserAvailable = false;

	private String mUserRows = null;

	private String rowNameHeader = "<tr><form method=\"post\" onSubmit=\"return validateForm(this);\"><td>";

	private String rowIDHeader = "<input type=\"hidden\" name=\"userId\"";

	private String rowSubmitHeader = "<td><input type=\"submit\" value=\"Delete\"></td>\n</form></tr>\n";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Gathers the User selection list, renders input form
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mUserRows = getUserRows();
			if (mIsUserAvailable == false) {
				throwError(req, resp);
			} else {
				showPage(req, resp);
			}
		} catch (Exception e) {
			String error = "<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>";
			adminError(req, resp, error);
			return;
		}
	}

	/**
	 * Gets all TPTeam Users and wraps them in HTML
	 * selection list option tags
	 * 
	 * @return the HTML String User option tag
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String getUserRows() throws Exception {
		 Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		List<TpteamUser> users = null;
		StringBuffer userRows = new StringBuffer();
		try {

			tx = s.beginTransaction();

			users = s.createQuery(
					"from TpteamUser as user order by user.lastName asc")
					.list();
			for (TpteamUser user : users) {
				userRows.append(rowNameHeader + user.getLastName() + ", "
						+ user.getFirstName() + "</td>\n");
				userRows.append(rowIDHeader + " value=\"" + user.getId()
						+ "\">\n");
				userRows.append(rowSubmitHeader);
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if (userRows.length() > 0)
			mIsUserAvailable = true;

		return userRows.toString();
	}

	/**
	 * Helper method that renders errors as HTML
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void throwError(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String error = "<h3>Error: No Users Exist</h3>";
		adminError(req, resp, error);
	}

	/**
	 * Helper method that renders HTML input form
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String reply = "<h4>Delete User</h4>\n<table border=\"2\">\n<th>Last, First</th><th></th>\n"
				+ mUserRows + "</table>";
		adminHeader(req, resp, DELETE_USER_JS);
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}
}
