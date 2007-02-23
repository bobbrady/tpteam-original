package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.XMLUtil;

public class TestUtil {
	public static List<Test> getTestByProjID(String projID) throws Exception {
		List<Test> tests = null;
		Session s = null;
		Transaction tx = null;
		try {
			/*
			 * s = Activator.getDefault().getHiberSessionFactory()
			 * .getCurrentSession();
			 */
			s = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = s.beginTransaction();

			String hql = "from Test as test where test.project.id =:projID";
			Query query = s.createQuery(hql);
			query.setString("projID", projID);
			tests = query.list();

			// Initialize fields of interest
			for (Test test : tests) {
				test.getId();
				test.getName();
				test.getDescription();
				if(test.getParent() != null)
					test.getParent().getId();
				test.getIsFolder();
				if(test.getTestType() != null)
					test.getTestType().getName();
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return tests;
	}

	public static String getTestTreeXML(TPEvent tpEvent) throws Exception {
		List<Test> tests = getTestByProjID(tpEvent.getDictionary().get(TPEvent.PROJECT_ID_KEY));
		Document dom = XMLUtil.getDocument();
		Element rootEle = dom.createElement("tests");
		dom.appendChild(rootEle);
		for (Test test : tests) {
			rootEle.appendChild(TestXML.getTestTreeElement(dom, test));
		}
		return XMLUtil.getXML(dom);
	}

	public static void main(String[] args) {
		try {
			Hashtable<String,String> hash = new Hashtable<String,String>();
			hash.put(TPEvent.PROJECT_ID_KEY, "1");
			TPEvent tpEvent = new TPEvent("TEST_TOPIC", hash);
			System.out.println("Test XML: \n" + getTestTreeXML(tpEvent));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
