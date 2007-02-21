package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.Hashtable;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.ProjectXML;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.XMLUtil;

public class ProjectUtil {
	public static Set<Project> getProjByECFID(String ecfID) throws Exception {
		Set<Project> projs = null;
		Session s = null;
		Transaction tx = null;
		try {
			/*
			 * s = Activator.getDefault().getHiberSessionFactory()
			 * .getCurrentSession();
			 */
			s = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = s.beginTransaction();

			String hql = "from TpteamUser as user where user.ecfId =:ecfID";
			Query query = s.createQuery(hql);
			query.setString("ecfID", ecfID);
			TpteamUser user = (TpteamUser) query.uniqueResult();
			projs = user.getProjects();
			for (Project proj : projs) {
				Hibernate.initialize(proj);
				Hibernate.initialize(proj.getProduct());
				Hibernate.initialize(proj.getTpteamUsers());
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
		Document dom = XMLUtil.getDocument();
		Element rootEle = dom.createElement("projects");
		dom.appendChild(rootEle);
		for (Project proj : projs) {
			rootEle.appendChild(ProjectXML.getProjProdElement(dom, proj));
		}
		return XMLUtil.getXML(dom);
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
