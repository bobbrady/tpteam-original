/********************************************************************
 * 
 * File		:	AddUserEntity.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that creates a new User and persists
 * 				it to the TPTeam database
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Role;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		: 	AddUserEntity.java
 * 
 * Description 	: 	Servlet that creates a new User and persists
 * 					it to the TPTeam database
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class AddUserEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Creates an new User from input parameters, persists
	 * it to the database.  Renders results, including errors,
	 * to the the user.
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String ecfId = req.getParameter("ecfID");
		String phone = req.getParameter("phone");
		String roleId = req.getParameter("role");
		String[] projIds = req.getParameterValues("projects");
		Transaction tx = null;
		try {
			int remoteUserId = ServletUtil.getRemoteUserID(req.getRemoteUser());
			Session s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
			tx = s.beginTransaction();
			TpteamUser user = new TpteamUser();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setUserName(userName);
			user.setPassword(ServletUtil.getSHA1Hash(password));
			user.setEmail(email);
			user.setPhone(phone);
			user.setEcfId(ecfId);
			user.setCreatedBy(remoteUserId);
			user.setCreatedDate(new Date());
			Role role = (Role) s.load(Role.class, new Integer(roleId));
			user.setRole(role);

			for (String projId : projIds) {
				Project project = (Project) s.load(Project.class, new Integer(
						projId));
				project.addToTpteamUsers(user);
				user.getProjects().add(project);
			}
			s.save(user);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			String error = "<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>";
			adminError(req, resp, error);
			return;
		}
		adminHeader(req, resp, null);
		String reply = "<h3>Add TPTeam User " + userName
				+ " was Successful</h3>";
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}

}
