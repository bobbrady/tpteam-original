/********************************************************************
 * 
 * File		:	UserProcessTestExec.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that validates a test request
 * 				and delegates its execution, for use by a
 * 				non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.exec;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.exec.AdminProcessTestExec;

public class UserProcessTestExec extends AdminProcessTestExec implements UserServlet {
	private static final long serialVersionUID = 1L;
}
