/********************************************************************
 * 
 * File		:	ViewTest2ByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for viewing
 * 				a TPTeam Test by selecting it from the Test tree,
 * 				for use by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.view;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewTest2;

public class ViewTest2ByUser extends ViewTest2 implements UserServlet {
	private static final long serialVersionUID = 1L;
}
