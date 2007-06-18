/********************************************************************
 * 
 * File		:	AddProductEntity.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Servlet used for adding new Products to the TPTeam
 * 				database
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.add;

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
 * File 		: 	AddProductEntity.java
 * 
 * Description 	: 	Servlet used for adding new Products to the TPTeam
 * 					database
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class AddProductEntity extends ServletUtil {

	private static final long serialVersionUID = 7456848419577223441L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Creates a new Product based on input parameters
	 * and persists it to the database
	 * 
	 * Displays results, including errors to the user
	 * 
	 * @param req The Servlet Request 
	 * @param resp The Servlet Response
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String  name = req.getParameter("prodName");
		String description = req.getParameter("prodDesc");
		
		Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			Product prod = new Product();
			prod.setName(name);
			prod.setDescription(description);
			s.save(prod);
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
			String reply = "<h3>Add Product " + name + " was Successful</h3>";
			adminReply(req, resp, reply);
			adminFooter(req, resp);
	}


}
