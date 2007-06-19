/********************************************************************
 * 
 * File		:	UpdateTestEntityByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that updates a Test in the TPTeam 
 * 				database, for use by a non-administrative
 * 				Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateTestEntity;

public class UpdateTestEntityByUser extends UpdateTestEntity implements UserServlet{

	private static final long serialVersionUID = 1L;
}
