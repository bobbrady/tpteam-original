/********************************************************************
 * 
 * File		:	DeleteProduct.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for deleting
 * 				a TPTeam Product
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/*******************************************************************************
 * File 		: 	DeleteProduct.java
 * 
 * Description 	: 	Servlet that displays an input form for deleting
 * 					a TPTeam Product
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
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

	/**
	 * Gathers the Product selection list, renders input form
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
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
	
	/**
	 * Gets all TPTeam Products and wraps them in HTML
	 * selection list option tags
	 * 
	 * @return the HTML String Product option tag
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String getProdRows() throws Exception
	{
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

	/**
	 * Helper method that renders errors as HTML
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void throwError(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		String error = "<h3>Error: No Product Available</h3>";
		adminError(req, resp, error);
	}
	
	/**
	 * Helper method that renders HTML input form
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showPage(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{		
		String reply = "<h4>Delete Product</h4>\n<table>" + mProdRows + "</table>";		
		adminHeader(req, resp, DELETE_PROD_JS);
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}
}
