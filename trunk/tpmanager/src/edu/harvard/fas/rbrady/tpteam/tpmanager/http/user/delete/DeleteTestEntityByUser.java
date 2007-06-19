/********************************************************************
 * 
 * File		:	DeleteTestEntityByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that deletes a Test from the TPTeam 
 * 				database, for use by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.delete;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteTestEntity;

public class DeleteTestEntityByUser extends DeleteTestEntity implements UserServlet {
	private static final long serialVersionUID = 1L;
}
