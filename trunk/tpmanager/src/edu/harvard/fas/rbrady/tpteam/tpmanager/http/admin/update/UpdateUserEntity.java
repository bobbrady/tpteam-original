/********************************************************************
 * 
 * File		:	UpdateUserEntity.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that updates a user in the TPTeam 
 * 				database
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update;

import java.io.IOException;
import java.util.Date;
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
 * File 		: 	UpdateUserEntity.java
 * 
 * Description 	: 	Servlet that updates a user in the TPTeam 
 * 					database
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class UpdateUserEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Updates a TPTeam user with the passed in form inputs  
	 * Renders results, including errors, to the the browser.
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userId = req.getParameter("userId");
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String ecfId = req.getParameter("ecfID");
		String phone = req.getParameter("phone");
		String roleId = req.getParameter("role");
		Transaction tx = null;
		try {
			int remoteUserId = ServletUtil.getRemoteUserID(req.getRemoteUser());
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			TpteamUser user = (TpteamUser) s.load(TpteamUser.class,
					new Integer(userId));
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setUserName(userName);
			user.setEmail(email);
			user.setPhone(phone);
			user.setEcfId(ecfId);
			user.setModifiedBy(remoteUserId);
			user.setModifiedDate(new Date());
			if (!password.equalsIgnoreCase(user.getPassword())) {
				user.setPassword(ServletUtil.getSHA1Hash(password));
			}

			if(!(this instanceof UserServlet))
			{
				Role role = (Role) s.load(Role.class, new Integer(roleId));
				user.setRole(role);
			}
			tx.commit();

			StringBuffer reply = new StringBuffer("<h3>Update TPTeam User "
					+ lastName + ", " + firstName + " was Successful</h3>");
			showPage(req, resp, reply, null, this);

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}
}
