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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class ViewProduct extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	protected boolean mIsProdAvailable = false;

	protected String mProdRows = null;

	protected String mRowHeader = "<tr><th>Name</th><th>Description</th></tr>\n";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			getProdRows();
			if (mIsProdAvailable == false) {
				StringBuffer error = new StringBuffer("<h3>Error: No Product Available</h3>");
				throwError(req, resp, error, this);
			} else {
				StringBuffer reply = new StringBuffer("<h4>View Products</h4>\n<table border=\"1\">"
					+ mRowHeader + mProdRows + "</table>");
				showPage(req, resp, reply, null, this);
			}
		} catch (Exception e) {
			StringBuffer error = new StringBuffer("<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>");
			throwError(req, resp, error, this);
			return;
		}
	}

	@SuppressWarnings("unchecked")
	protected String getProdRows() throws Exception {
		Session s = Activator.getDefault().getHiberSessionFactory()
				.getCurrentSession();
		// For standalone
		// Session s = HibernateUtil.getSessionFactory().getCurrentSession();

		Transaction tx = null;
		List<Product> prods = null;
		StringBuffer prodRows = new StringBuffer();
		try {

			tx = s.beginTransaction();

			prods = s.createQuery("from Product as p order by p.name asc")
					.list();
			for (Product prod : prods) {
				String desc = prod.getDescription();
				if (desc == null || desc.equalsIgnoreCase("null"))
					desc = "";
				prodRows.append("<tr><td>" + prod.getName() + "</td>");
				prodRows.append("<td>" + desc + "</td></tr>\n");
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if (prodRows.length() > 0)
			mIsProdAvailable = true;
		mProdRows = prodRows.toString();
		return mProdRows;
	}
}
