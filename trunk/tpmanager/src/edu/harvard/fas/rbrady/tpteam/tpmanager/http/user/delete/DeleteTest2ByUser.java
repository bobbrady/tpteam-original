/********************************************************************
 * 
 * File		:	DeleteTest2ByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for deleting
 * 				a TPTeam Test by selecting it from the Test tree,
 * 				for by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.delete;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete.DeleteTest2;

public class DeleteTest2ByUser extends DeleteTest2 implements UserServlet {
	private static final long serialVersionUID = 1L;
}
