/********************************************************************
 * 
 * File		:	ProjectUtil.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A utility class for TPTeam Project operations
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.HashSet;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.ProjectXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;

/*******************************************************************************
 * File 		: 	ProjectUtil.java
 * 
 * Description 	: 	A utility class for TPTeam Project operations.  Performs 
 * 					XML serialization of Project objects and look-ups by ECF ID.
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ProjectUtil {

	/**
	 * Gets all TPTeam projects associated with a given TPTeam user
	 * 
	 * @param ecfID The ECF ID of the TPTeam user.
	 * @return a Set of all associated projects
	 * @throws Exception
	 */
	public static Set<Project> getProjByECFID(String ecfID) throws Exception {
		Set<Project> projs = null;
		Session s = null;
		Transaction tx = null;
		try {
			// If in OSGi envrionment, grab session from plugin activator
			if (Activator.getDefault() != null) {
				s = Activator.getDefault().getHiberSessionFactory()
						.getCurrentSession();
			} else {
				s = HibernateUtil.getSessionFactory().getCurrentSession();
			}
			tx = s.beginTransaction();

			String hql = "from TpteamUser as user where user.ecfId =:ecfID";
			Query query = s.createQuery(hql);
			query.setString("ecfID", ecfID);
			TpteamUser user = (TpteamUser) query.uniqueResult();
			if (user == null)
				return new HashSet<Project>();
			projs = user.getProjects();
			if (projs == null)
				return new HashSet<Project>();
			for (Project proj : projs) {
				proj.initSkeleton();
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return projs;
	}

	/**
	 * Gets all TPTeam project/product information stored in the
	 * TPTeam database
	 *   
	 * @param tpEvent The TPEvent holding the request information
	 * @return an XML String  
	 * @throws Exception
	 */
	public static String getProjProdXML(TPEvent tpEvent) throws Exception {
		Set<Project> projs = getProjByECFID(tpEvent.getDictionary().get(
				TPEvent.FROM));
		/***********************************************************************
		 * Create stub Projects
		 * 
		 * Betwixt will call all getter methods objects as part of its
		 * introspection. This will cause non-initialized objects loaded by
		 * Hibernate to throw init errors outside of Hibernate session. Stub
		 * objects allow us to use lazy loading and avoid this error.
		 **********************************************************************/
		HashSet<Project> stubProjs = new HashSet<Project>();
		for (Project proj : projs) {
			Product stubProd = new Product(proj.getProduct().getId(), proj
					.getProduct().getName(), null, null);
			Project stubProj = new Project(proj.getId(), stubProd, proj
					.getName(), proj.getDescription(), null, null);
			stubProjs.add(stubProj);
		}
		return ProjectXML.getXML(stubProjs);
	}
}
