/********************************************************************
 * 
 * File		:	ViewTestByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for viewing
 * 				a TPTeam Test by first selecting its parent Project,
 * 				for use by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.view;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewTest;

public class ViewTestByUser extends ViewTest implements UserServlet {
	private static final long serialVersionUID = 1L;
}
