/********************************************************************
 * 
 * File		:	UpdateUserProfileEntity.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that updates a user in the TPTeam 
 * 				database, for use by a non-administrative
 * 				Web user
 *  
 ********************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateUserEntity;

public class UpdateUserProfileEntity extends UpdateUserEntity implements UserServlet {
	private static final long serialVersionUID = 1L;
}
