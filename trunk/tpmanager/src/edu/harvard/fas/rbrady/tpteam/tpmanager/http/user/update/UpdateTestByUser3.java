/********************************************************************
 * 
 * File		:	UpdateTestByUser3.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that loads the details for a particular Test
 * 				into a form so that a user can edit and update them,
 * 				for use by a non-administrative Web user
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateTest3;

public class UpdateTestByUser3 extends UpdateTest3 implements UserServlet{
	private static final long serialVersionUID = 1L;

	protected String mFormAction = "<input type=\"hidden\" name=\"formAction\" value=\"updateTestEntity\">\n";
}
