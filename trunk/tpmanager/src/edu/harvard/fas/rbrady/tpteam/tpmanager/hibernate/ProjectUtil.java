package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.HashSet;
import java.util.Hashtable;
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

public class ProjectUtil {
	public static Set<Project> getProjByECFID(String ecfID) throws Exception {
		Set<Project> projs = null;
		Session s = null;
		Transaction tx = null;
		try {

			//s = Activator.getDefault().getHiberSessionFactory()
			//		.getCurrentSession();

			 s = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = s.beginTransaction();

			String hql = "from TpteamUser as user where user.ecfId =:ecfID";
			Query query = s.createQuery(hql);
			query.setString("ecfID", ecfID);
			TpteamUser user = (TpteamUser) query.uniqueResult();
			projs = user.getProjects();
			if(projs == null)
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

	public static String getProjProdXML(TPEvent tpEvent) throws Exception {
		Set<Project> projs = getProjByECFID(tpEvent.getDictionary().get(
				TPEvent.FROM));
		/****************************************************************
		 * Create stub Projects
		 * 
		 * Betwixt will call all getter methods objects as part of its
		 * introspection.  This will cause non-initialized objects loaded
		 * by Hibernate to throw init errors outside of Hibernate session.
		 * Stub objects allow us to use lazy loading and avoid this error. 
		 ******************************************************************/
		HashSet<Project> stubProjs = new HashSet<Project>();
		for(Project proj : projs)
		{
			Product stubProd = new Product(proj.getProduct().getId(), proj.getProduct().getName(), null, null);
			Project stubProj = new Project(proj.getId(), stubProd, proj.getName(), proj.getDescription(), null, null);
			stubProjs.add(stubProj);
		}
		return ProjectXML.getXML(stubProjs);
	}
	

	public static void main(String[] args) {
		Set<Project> projs;
		try {
			projs = getProjByECFID("tpteam_2@jabber.org");
			for (Project proj : projs) {
				System.out.println("Proj ID: " + proj.getId() + ", Name: "
						+ proj.getName() + ", Desc: " + proj.getDescription());
				System.out.println("Prod ID: " + proj.getProduct().getId()
						+ ", Name: " + proj.getProduct().getName());

				for (TpteamUser user : proj.getTpteamUsers()) {
					System.out.println("TpteamUser ECFID: " + user.getEcfId());
				}
			}

			Hashtable<String, String> hash = new Hashtable<String, String>();
			hash.put(TPEvent.FROM, "tpteam_2@jabber.org");
			TPEvent tpEvent = new TPEvent("topic", hash);
			String xml = getProjProdXML(tpEvent);
			System.out.println("ProjProdXML: \n" + xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
