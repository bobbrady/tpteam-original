/********************************************************************
 * 
 * File		:	DeleteProductEntity.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet that deletes a Product from the TPTeam 
 * 				database
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.delete;

import java.io.IOException;

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
 * File 		: 	DeleteProductEntity.java
 * 
 * Description 	: 	Servlet that deletes a Product from the TPTeam 
 * 					database
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class DeleteProductEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Deletes a Product based on ID from selection list.  
	 * Renders results, including errors, to the the user.
	 * 
	 * @param req The Servlet Request
	 * @param resp The Servlet Response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String  name = req.getParameter("prodName");
		String prodId = req.getParameter("prodId");
		
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			Product prod = (Product) s.load(Product.class, new Integer(prodId));
			s.delete(prod);
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			String error = "<h3>Error: " + e.getMessage() + "<br>" + 
			e.getCause() + "</h3>";
			adminError(req, resp, error);
			return;
		}
			adminHeader(req, resp, null);
			String reply = "<h3>Delete Product " + name + " was Successful</h3>";
			adminReply(req, resp, reply);
			adminFooter(req, resp);
	}


}
