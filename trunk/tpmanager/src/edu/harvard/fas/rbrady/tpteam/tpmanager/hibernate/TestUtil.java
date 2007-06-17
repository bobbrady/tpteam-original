/********************************************************************
 * 
 * File		:	TestUtil.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A utility class for TPTeam Test operations
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestType;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;

/*******************************************************************************
 * File 		: 	TestUtil.java
 * 
 * Description 	: 	A utility class for TPTeam Test operations.  Performs 
 * 					XML serialization and database operations upon Test objects.
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TestUtil {
	
	/**
	 * Gets a List of all Test objects associated with
	 * a given project 
	 * @param projID the ID of the project
	 * @return the List of Tests
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<Test> getTestByProjID(String projID) throws Exception {
		List<Test> tests = null;
		Session s = null;
		Transaction tx = null;
		try {
			// Use plugin activator if in OSGi runtime
			if (Activator.getDefault() != null) {
				s = Activator.getDefault().getHiberSessionFactory()
						.getCurrentSession();
			} else {
				s = HibernateUtil.getSessionFactory().getCurrentSession();
			}
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

	/**
	 * Gets a Test object from the database 
	 * @param testID the ID of the Test
	 * @param includeExec true if test executions should be 
	 * 	included, false otherwise
	 * @return the Test object
	 * @throws Exception
	 */
	public static Test getTestByID(String testID, boolean includeExec)
			throws Exception {
		Test test = null;
		Session s = null;
		Transaction tx = null;
		try {
			// Use plugin activator if in OSGi runtime
			if (Activator.getDefault() != null) {
				s = Activator.getDefault().getHiberSessionFactory()
						.getCurrentSession();
			} else {
				s = HibernateUtil.getSessionFactory().getCurrentSession();
			}
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

	/**
	 * Gets the XML String serialization of a project test tree
	 * 
	 * @param tpEvent the TPEvent containing the request
	 * @return the XML serialization
	 * @throws Exception
	 */
	public static String getTestTreeXML(TPEvent tpEvent) throws Exception {
		List<Test> tests = getTestByProjID(tpEvent.getDictionary().get(
				TPEvent.PROJECT_ID_KEY));
		return TestXML.getTPEntityXML(tests, tpEvent.getDictionary().get(
				TPEvent.PROJECT_KEY));
	}

	/**
	 * Gets a stubbed Test object for update operations
	 * 
	 * @param test the Test object to be stubbed
	 * @return a stubbed test object
	 */
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

	/**
	 * Updates a Test in the datbase
	 * 
	 * @param tpEvent the TPEvent containing the request
	 * @throws Exception
	 */
	public static void updateTest(TPEvent tpEvent) throws Exception {
		Session s = null;
		// Use plugin activator if in OSGi runtime
		if (Activator.getDefault() != null) {
			s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
		} else {
			s = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		Transaction tx = null;
		try {

			String testXML = tpEvent.getDictionary().get(TPEvent.TEST_XML_KEY);
			Test testStub = TestXML.getTestFromXML(testXML);

			tx = s.beginTransaction();

			// Get the user who is requesting update
			String hql = "from TpteamUser as user where user.ecfId =:ecfID";
			Query query = s.createQuery(hql);
			query.setString("ecfID", tpEvent.getDictionary().get(TPEvent.FROM));
			TpteamUser updateUser = (TpteamUser) query.list().get(0);

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
			test.setModifiedBy(updateUser);
			test.setModifiedDate(new Date());

			// Collect project member ecfIDs
			Set<TpteamUser> users = test.getProject().getTpteamUsers();
			StringBuilder userECFIDs = new StringBuilder();
			int idx = 0;
			for (TpteamUser user : users) {
				if (idx == 0)
					userECFIDs.append(user.getEcfId());
				else
					userECFIDs.append("/" + user.getEcfId());
				idx++;
			}
			String ECFID = tpEvent.getDictionary().get(TPEvent.FROM);
			tpEvent.getDictionary().put(TPEvent.ECFID_KEY, ECFID);
			tpEvent.getDictionary().put(TPEvent.FROM, userECFIDs.toString());
			tpEvent.getDictionary().put(TPEvent.TEST_NAME_KEY, test.getName());
			if (test.getDescription() != null)
				tpEvent.getDictionary().put(TPEvent.TEST_DESC_KEY,
						test.getDescription());

			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	/**
	 * Adds a Test object to the database
	 * 
	 * @param tpEvent the TPEvent containing the request
	 * @throws Exception
	 */
	public static void addTest(TPEvent tpEvent) throws Exception {
		Session s = null;
		// Use plugin activator if in OSGi runtime
		if (Activator.getDefault() != null) {
			s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
		} else {
			s = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		Transaction tx = null;
		try {

			String testXML = tpEvent.getDictionary().get(TPEvent.TEST_XML_KEY);
			Test testStub = TestXML.getTestFromXML(testXML);

			tx = s.beginTransaction();

			Test test = new Test();
			// First, data common to both folders and tests
			test.setName(testStub.getName());
			test.setDescription(testStub.getDescription());
			test.setIsFolder(testStub.getIsFolder());
			if (testStub.getParent().getId() > 0) {
				Test parent = (Test) s.load(Test.class, testStub.getParent()
						.getId());
				test.setParent(parent);
			}
			Project proj = (Project) s.load(Project.class, testStub
					.getProject().getId());
			test.setProject(proj);

			// Get the user who is requesting update
			String hql = "from TpteamUser as user where user.ecfId =:ecfID";
			Query query = s.createQuery(hql);
			query.setString("ecfID", String.valueOf(testStub.getCreatedBy()
					.getEcfId()));
			TpteamUser addUser = (TpteamUser) query.list().get(0);
			test.setCreatedBy(addUser);
			test.setCreatedDate(new Date());

			// Set the test type
			hql = "from TestType as testType where testType.name =:name";
			query = s.createQuery(hql);
			query.setString("name", testStub.getTestType().getName());
			TestType testType = (TestType) query.list().get(0);
			test.setTestType(testType);

			// Now, add test definition data
			if (testStub.getIsFolder() == 'N') {
				// Need to save now so can associate test definition with test
				// ID in database
				Integer testID = (Integer) s.save(test);
				// Next, create JUnit entity, pointing to parent test
				JunitTest junitStub = ((JunitTest[]) testStub.getJunitTests()
						.toArray(new JunitTest[0]))[0];
				JunitTest junit = new JunitTest();
				junit.setId(testID);
				junit.setTest(test);
				junit.setEclipseHome(junitStub.getEclipseHome());
				junit.setProject(junitStub.getProject());
				junit.setWorkspace(junitStub.getWorkspace());
				junit.setReportDir(junitStub.getReportDir());
				junit.setTptpConnection(junitStub.getTptpConnection());
				junit.setTestSuite(junitStub.getTestSuite());
				s.save(junit);
			}
			s.saveOrUpdate(test);
			s.flush();

			// If parent is null, then is top level folder in project
			if (test.getParent() != null)
				test.setPath(test.getParent().getPath() + "." + test.getId());
			else
				test.setPath(String.valueOf(test.getId()));

			testStub.setId(test.getId());

			// Collect project member ecfIDs
			Set<TpteamUser> users = test.getProject().getTpteamUsers();
			StringBuilder userECFIDs = new StringBuilder();
			int idx = 0;
			for (TpteamUser user : users) {
				if (idx == 0)
					userECFIDs.append(user.getEcfId());
				else
					userECFIDs.append("/" + user.getEcfId());
				idx++;
			}
			String ECFID = tpEvent.getDictionary().get(TPEvent.FROM);
			tpEvent.getDictionary().put(TPEvent.ECFID_KEY, ECFID);
			tpEvent.getDictionary().put(TPEvent.FROM, userECFIDs.toString());
			tpEvent.getDictionary().put(TPEvent.ID_KEY,
					String.valueOf(test.getId()));
			tpEvent.getDictionary().put(TPEvent.TEST_NAME_KEY, test.getName());
			if (test.getDescription() != null)
				tpEvent.getDictionary().put(TPEvent.TEST_DESC_KEY,
						test.getDescription());
			tpEvent.getDictionary().put(TPEvent.TEST_XML_KEY,
					TestXML.getXML(testStub));
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}
}
