/********************************************************************
 * 
 * File		:	UpdateUserProfile.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays the details of the logged-in
 * 				TPTeam user in a form for editable updating, for
 * 				use by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateUser2;

public class UpdateUserProfile extends UpdateUser2 implements UserServlet{
	private static final long serialVersionUID = 1L;

	protected String mRemoteUser = null;
	
	protected String mFormAction = "<input type=\"hidden\" name=\"formAction\" value=\"updateUserEntity\">\n";
}
