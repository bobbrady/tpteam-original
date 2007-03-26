package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
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

			s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();

			// s = HibernateUtil.getSessionFactory().getCurrentSession();
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

	public static Test getTestByID(String testID, boolean includeExec)
			throws Exception {
		Test test = null;
		Session s = null;
		Transaction tx = null;
		try {

			// s =
			// Activator.getDefault().getHiberSessionFactory().getCurrentSession();

			s = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = s.beginTransaction();
			String hql = "from Test as test where test.id =:testID";
			Query query = s.createQuery(hql);
			query.setString("testID", String.valueOf(testID));
			test = (Test) query.list().get(0);
			test.initProps(includeExec);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return test;
	}

	public static String getTestTreeXML(TPEvent tpEvent) throws Exception {
		List<Test> tests = getTestByProjID(tpEvent.getDictionary().get(
				TPEvent.PROJECT_ID_KEY));
		return TestXML.getTPEntityXML(tests, tpEvent.getDictionary().get(
				TPEvent.PROJECT_KEY));
	}

	public static Test getTestUpdateStub(Test test) {
		Test testStub = new Test();
		testStub.setId(test.getId());
		testStub.setName(test.getName());
		testStub.setDescription(test.getDescription());
		testStub.setIsFolder(testStub.getIsFolder());
		if (test.getJunitTests().size() > 0) {
			JunitTest junit = ((JunitTest[]) test.getJunitTests().toArray(
					new JunitTest[0]))[0];
			JunitTest junitStub = new JunitTest();
			junitStub.setId(junit.getId());
			junitStub.setEclipseHome(junit.getEclipseHome());
			junitStub.setWorkspace(junit.getWorkspace());
			junitStub.setProject(junit.getProject());
			junitStub.setTestSuite(junit.getTestSuite());
			junitStub.setReportDir(junit.getReportDir());
			junitStub.setTptpConnection(junit.getTptpConnection());
			testStub.addJunitTest(junitStub);
		}
		return testStub;
	}

	public static void updateTest(Test testStub) throws Exception {
		//Session s = Activator.getDefault().getHiberSessionFactory().getCurrentSession();

		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, testStub.getId());
			test.setName(testStub.getName());
			test.setDescription(testStub.getDescription());
			if (test.getIsFolder() == 'N' && test.getJunitTests() != null
					&& test.getJunitTests().size() > 0) {
				if (testStub.getJunitTests() != null
						&& testStub.getJunitTests().size() > 0) {
					JunitTest junit = ((JunitTest[]) test.getJunitTests()
							.toArray(new JunitTest[0]))[0];
					JunitTest junitStub = ((JunitTest[]) testStub
							.getJunitTests().toArray(new JunitTest[0]))[0];
					junit.setEclipseHome(junitStub.getEclipseHome());
					junit.setWorkspace(junitStub.getWorkspace());
					junit.setProject(junitStub.getProject());
					junit.setTestSuite(junitStub.getTestSuite());
					junit.setReportDir(junitStub.getReportDir());
					junit.setTptpConnection(junitStub.getTptpConnection());
				}
			}
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	public static void main(String[] args) {
		try {
			Hashtable<String, String> hash = new Hashtable<String, String>();
			hash.put(TPEvent.PROJECT_ID_KEY, "1");
			TPEvent tpEvent = new TPEvent("TEST_TOPIC", hash);
			System.out.println("Test XML: \n" + getTestTreeXML(tpEvent));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
