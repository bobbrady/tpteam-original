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

import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class UpdateTest extends ServletUtil {
	private static final long serialVersionUID = 7456848419577223441L;

	protected boolean mIsProjAvailable = false;

	protected String mProjRows = null;

	protected String mRemoteUser = null;

	protected String rowNameHeader = "<tr><form method=\"post\" action=\"updateTest2\"><th>Project</th><td>";

	protected String rowIDHeader = "<input type=\"hidden\" name=\"projId\"";

	protected String rowSubmitHeader = "<td><input type=\"submit\" value=\"Update Test Tree\"></td>\n</form></tr>\n";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			mRemoteUser = req.getRemoteUser();
			getProjRows();
			if (mIsProjAvailable == false) {
				StringBuffer error = new StringBuffer(
						"<h3>Error: No Project Available</h3>");
				throwError(req, resp, error, this);
			} else {
				StringBuffer reply = new StringBuffer(
						"<h4>Update Test Tree: Select Parent Project</h4>\n");
				reply.append("<table border=\"2\">" + mProjRows + "</table>");
				showPage(req, resp, reply, null, this);
			}
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: "
					+ e.getMessage() + "<br>" + e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	protected String getProjRows() throws Exception {
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
		// For standalone
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();

		Transaction tx = null;
		List<Project> projs = null;
		StringBuffer projRows = new StringBuffer();
		try {

			tx = s.beginTransaction();

			projs = s.createQuery("from Project as p order by p.name asc")
					.list();
			for (Project proj : projs) {
				String desc = proj.getDescription();
				if (desc == null || desc.equalsIgnoreCase("null"))
					desc = "";
				projRows.append(rowNameHeader + proj.getName() + "</td>\n");
				projRows.append(rowIDHeader + " value=\"" + proj.getId()
						+ "\">\n");
				projRows.append(rowSubmitHeader);
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}

		if (projRows.length() > 0) {
			mIsProjAvailable = true;
			mProjRows = projRows.toString();
		}
		return mProjRows;
	}

}
