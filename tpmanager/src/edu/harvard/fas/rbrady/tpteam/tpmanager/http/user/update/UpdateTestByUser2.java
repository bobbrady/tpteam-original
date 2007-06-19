/********************************************************************
 * 
 * File		:	UpdateTest2ByUser.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for updating
 * 				a TPTeam Test by selecting it from the Test tree,
 * 				for use by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateTest2;

public class UpdateTestByUser2 extends UpdateTest2 implements UserServlet {
	private static final long serialVersionUID = 1L;

	protected String mFormAction = "<input type=\"hidden\" name=\"formAction\" value=\"updateTest3\">\n";
}
