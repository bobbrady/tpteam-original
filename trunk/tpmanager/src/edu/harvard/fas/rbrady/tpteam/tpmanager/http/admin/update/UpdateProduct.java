/********************************************************************
 * 
 * File		:	UpdateProduct.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that displays an input form for updating
 * 				a TPTeam Product
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update;

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

/*******************************************************************************
 * File 		: 	UpdateProduct.java
 * 
 * Description 	: 	Servlet that displays an input form for updating
 * 					a TPTeam Product
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class UpdateProduct extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	private boolean mIsProdAvailable = false;

	private String mProdRows = null;

	private String rowNameHeader = "<tr><form method=\"post\" onSubmit=\"return validateForm(this);\"><th>Name:</th><td><input type=\"text\" name=\"prodName\" size=\"25\"";

	private String rowDescHeader = "<th>Description:</th><td><input type=\"text\" name=\"prodDesc\" size=\"50\"";

	private String rowSubmitHeader = "<td><input type=\"submit\" value=\"Update\"></td>\n</form></tr>\n";

	private String rowIDHeader = "<input type=\"hidden\" name=\"prodId\"";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Renders the new Product input form
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			getProdRows();
			if (mIsProdAvailable == false) {
				throwError(req, resp);
			} else {
				showPage(req, resp);
			}
		} catch (Exception e) {
			String error = "<h3>Error: " + e.getMessage() + "<br>"
					+ e.getCause() + "</h3>";
			adminError(req, resp, error);
			return;
		}
	}

	/**
	 * Helper method that gets all Products and wraps 
	 * them in HTML option tags
	 * 
	 * @return The Product option tags
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String getProdRows() throws Exception {
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
				prodRows.append(rowNameHeader + " value=\"" + prod.getName()
						+ "\"></td>\n");
				prodRows.append(rowDescHeader + " value=\"" + desc
						+ "\"></td>\n");
				prodRows.append(rowIDHeader + " value=\"" + prod.getId()
						+ "\">\n");
				prodRows.append(rowSubmitHeader);
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

	/**
	 * Helper method to render errors as HTML
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void throwError(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String error = "<h3>Error: No Product Available</h3>";
		adminError(req, resp, error);
	}

	/**
	 * Helper method to render the HTML page, including 
	 * JavaScript
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String reply = "<h4>Update Product</h4>\n<table>" + mProdRows
				+ "</table>";
		adminHeader(req, resp, UPDATE_PROD_JS);
		adminReply(req, resp, reply);
		adminFooter(req, resp);
	}
}
