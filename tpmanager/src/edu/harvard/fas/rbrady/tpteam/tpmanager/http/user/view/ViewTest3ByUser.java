/********************************************************************
 * 
 * File		:	ViewTest3ByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that loads the details for a particular Test
 * 				so that a user can view them, for use by a 
 * 				non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.view;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewTest3;

public class ViewTest3ByUser extends ViewTest3 implements UserServlet {
	private static final long serialVersionUID = 1L;
}
