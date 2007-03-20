package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;

public class TestUtil {
	public static List<Test> getTestByProjID(String projID) throws Exception {
		List<Test> tests = null;
		Session s = null;
		Transaction tx = null;
		try {
			
			 s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();
			 
			//s = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = s.beginTransaction();
			String hql = "from Test as test where test.parent is null and test.project.id =:projID";
			Query query = s.createQuery(hql);
			query.setString("projID", String.valueOf(projID));
			tests = query.list();

			// Initialize fields of interest
			for (Test test : tests) {
				test.initSkeleton();
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
		return TestXML.getTPEntityXML(tests, tpEvent.getDictionary().get(TPEvent.PROJECT_KEY));
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
