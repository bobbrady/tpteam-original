/********************************************************************
 * 
 * File		:	ExecTestByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for executing
 * 				a TPTeam Test by first selecting its parent Project,
 * 				for use by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.exec;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.exec.ExecTest;

public class ExecTestByUser extends ExecTest implements UserServlet {
	private static final long serialVersionUID = 1L;
}
