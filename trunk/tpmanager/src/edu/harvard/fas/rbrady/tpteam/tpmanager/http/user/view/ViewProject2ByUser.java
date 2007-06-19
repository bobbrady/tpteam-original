/********************************************************************
 * 
 * File		:	ViewProject2ByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays the view details
 * 				a particular TPTeam Project, for use by a
 * 				non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.view;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewProject2;

public class ViewProject2ByUser extends ViewProject2 implements UserServlet {
	private static final long serialVersionUID = 1L;
}
