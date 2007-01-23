/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.Product;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

public class DeleteProduct extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;
	private boolean mIsProdAvailable = false;
	private String mProdRows = null;

	private String rowNameHeader = "<tr><form method=\"post\" onSubmit=\"return validateForm(this);\"><th>Name:</th><td><input type=\"hidden\" name=\"prodName\" size=\"25\"";

	private String rowDescHeader = "<th>Description:</th><td>";
	
	private String rowSubmitHeader = "<td><input type=\"submit\" value=\"Delete\"></td>\n</form></tr>\n";
	
	private String rowIDHeader = "<input type=\"hidden\" name=\"prodId\"";
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try
		{
		getProdRows();
		if(mIsProdAvailable == false)
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
	
	private String getProdRows() throws Exception
	{
//		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		// For standalone
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();

		Transaction tx = null;
		List<Product> prods = null;
		StringBuffer prodRows = new StringBuffer();
		try {

			tx = s.beginTransaction();

			prods = s.createQuery("from Product as p order by p.name asc").list();
			for(Product prod : prods)
			{
				String desc = prod.getDescription();
				if(desc == null || desc.equalsIgnoreCase("null"))
					desc = "";
				prodRows.append(rowNameHeader + " value=\"" + prod.getName() + "\">" + prod.getName() + "</td>\n");
				prodRows.append(rowDescHeader + prod.getDescription() + "</td>\n");
				prodRows.append(rowIDHeader + " value=\"" + prod.getId() + "\">\n");
				prodRows.append(rowSubmitHeader);
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		if(prodRows.length() > 0)
			mIsProdAvailable = true;
		mProdRows = prodRows.toString();
		return mProdRows;
	}

	
	private void throwError(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		String error = "<h3>Error: No Product Available</h3>";
		adminError(req, resp, error);
	}
	
	private void showPage(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{		
		String reply = "<h4>Delete Product</h4>\n<table>" + mProdRows + "</table>";		
		adminHeader(req, resp, DELETE_PROD_JS);
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}
	
	public static void main(String[] args)
	{
		try
		{
			
		DeleteProduct servlet = new DeleteProduct();
		System.out.println(servlet.getProdRows());
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
