/********************************************************************
 * 
 * File		:	AddTestEntityByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet used for adding new Tests to the TPTeam
 * 				database by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.add;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add.AddTestEntity;

public class AddTestEntityByUser extends AddTestEntity implements UserServlet{
	private static final long serialVersionUID = 1L;

}
