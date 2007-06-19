/********************************************************************
 * 
 * File		:	ViewProductByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for selecting
 * 				a particular TPTeam Product for viewing, for use
 * 				by a non-administrative Web user
 *  
 ********************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.view;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewProduct;

public class ViewProductByUser extends ViewProduct implements UserServlet {
	private static final long serialVersionUID = 1L;
}
