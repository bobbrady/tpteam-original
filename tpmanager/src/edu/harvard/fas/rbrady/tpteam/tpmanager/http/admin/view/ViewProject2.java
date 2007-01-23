/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class ViewProject2 extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;
	private String mTeam = null;
	private String mProjId = null;
	private String mProjName = null;
	private String mProjDesc = null;
	private String mProd = null;
	private int[] mUserIds = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try
		{
		mProjId = req.getParameter("projId");
		getProject();
		showPage(req, resp);
		}
		 catch (Exception e) {
				String error = "<h3>Error: " + e.getMessage() + "<br>" + 
				e.getCause() + "</h3>";
				adminError(req, resp, error);
				return;
			}
	}
	
	
	private void getProject() throws Exception
	{
		Project proj = null;
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			proj = (Project)s.load(Project.class, new Integer(mProjId));
			if(proj.getDescription() != null)
			{
				mProjDesc = proj.getDescription();
			}
			else
			{
				mProjDesc = "";
			}
			mProjName = proj.getName();
			mProd = proj.getProduct().getName();
			getTeam(proj.getTpteamUsers());
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}
	
	
	private void getTeam(Set<TpteamUser> team) throws Exception
	{
		StringBuffer teamUsers = new StringBuffer();
		if(team.size() < 1)
		{
			mTeam = "";
			return;
		}
		for(TpteamUser user : team)
		{
			teamUsers.append(user.getLastName() + ", " + user.getFirstName() + " (" + user.getUserName() + ")<br>\n");
		}
		mTeam = teamUsers.toString();
	}
	
		
	private void showPage(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		StringBuffer reply = new StringBuffer();
		reply.append("<h4>View Project</h4>\n");
		reply.append("<table border=\"1\"><tr><th align=\"left\">Name:</th><td align=\"right\">" + mProjName + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">Description:</th><td align=\"right\">" + mProjDesc + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">Product:</th><td align=\"right\">" + mProd + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">Team Members:</th><td align=\"right\">" + mTeam + "</td></tr>\n");
		reply.append("<tr><th align=\"left\">View  Test Tree:</th><td align=\"right\"><form method=\"post\" action=\"viewTest2\">\n<input type=\"hidden\" name=\"projId\" value=\"" + mProjId + "\">\n<input type=\"submit\" value=\"Test Tree Details\">\n</form>\n");
		reply.append("</table>\n<br>\n");
		
		adminHeader(req, resp, null);
		adminReply(req, resp, reply.toString());
		adminFooter(req, resp);
	}


}
