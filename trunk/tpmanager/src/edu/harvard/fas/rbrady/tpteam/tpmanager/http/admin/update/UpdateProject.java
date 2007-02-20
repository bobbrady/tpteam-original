/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class UpdateProject extends ServletUtil {
	private static final long serialVersionUID = 7456848419577223441L;
	private boolean mIsProjAvailable = false;
	private String mProjRows = null;

	private String rowNameHeader = "<tr><form method=\"post\" action=\"updateProject2\"><th>Project</th><td>";
	
	private String rowIDHeader = "<input type=\"hidden\" name=\"projId\"";
	
	private String rowSubmitHeader = "<td><input type=\"submit\" value=\"Update\"></td>\n</form></tr>\n";
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try
		{
		getProjRows();
		if(mIsProjAvailable == false)
		{
			throwError(req, resp);
		}
		else
		{
			showPage(req, resp);
		}
		}
		 catch (Exception e) {
				String error = "<h3>Error: " + e.getMessage() + "<br>" + 
				e.getCause() + "</h3>";
				adminError(req, resp, error);
				return;
			}
	}
	
	private String getProjRows() throws Exception
	{
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		// For standalone
		//Session s = HibernateUtil.getSessionFactory().getCurrentSession();

		Transaction tx = null;
		List<Project> projs = null;
		StringBuffer projRows = new StringBuffer();
		try {

			tx = s.beginTransaction();

			projs = s.createQuery("from Project as p order by p.name asc").list();
			for(Project proj : projs)
			{
				projRows.append(rowNameHeader + proj.getName());
				if(proj.getDescription() != null && !proj.getDescription().equalsIgnoreCase("null"))
				{
					projRows.append(": " + proj.getDescription());
				}
				projRows.append("</td>\n" + rowIDHeader + " value=\"" + proj.getId() + "\">\n" + rowSubmitHeader + "\n");
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if(projRows.length() > 0)
			mIsProjAvailable = true;
		
		mProjRows = projRows.toString();
		
		return mProjRows;
	}

	
	private void throwError(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		String error = "<h3>Error: No Project Available</h3>";
		adminError(req, resp, error);
	}
	
	private void showPage(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{		
		String reply = "<h4>Update Project</h4>\n<table border=\"2\">" + mProjRows + "</table>";		
		adminHeader(req, resp, null);
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}
	
	public static void main(String[] args)
	{
		try
		{
			
		UpdateProject servlet = new UpdateProject();
		System.out.println(servlet.getProjRows());
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
