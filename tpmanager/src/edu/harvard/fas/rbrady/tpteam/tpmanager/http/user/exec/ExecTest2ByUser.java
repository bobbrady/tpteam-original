/********************************************************************
 * 
 * File		:	ExecTest2ByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for executing
 * 				a TPTeam Test by selecting it from the Test tree,
 * 				for use by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.exec;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.exec.ExecTest2;

public class ExecTest2ByUser extends ExecTest2 implements UserServlet {
	private static final long serialVersionUID = 1L;
}
