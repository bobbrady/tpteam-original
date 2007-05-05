package edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.test;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestType;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;

/**
 * Generated code for the test suite <b>TestTPTeamTest</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpbridge/hibernate/test/TestTPTeamTest.testsuite</i>.
 */
public class TestTPTeamTest extends HyadesTestCase {

	/** TpTeam User ID to be used for tests */
	public static final String USER_ID = "1";

	/** TpTeam ECF ID to be used for tests */
	public static final String ECF_ID = "tpteam1@gmail.com";

	/** Project ID in test database */
	public static final String PROJ_ID = "1";

	/** Parent test ID in test database */
	public static final String PARENT_TEST_ID = "4";

	/** Name of temporary test to be created */
	public static final String TEST_NAME = "Test Name";

	/** Description of temporary test to be created */
	public static final String TEST_DESC = "Test Desc";

	/** Use Folder Test Type */
	public static final String TEST_TYPE_ID = "1";

	/** Temporary test to be created */
	private edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test mTest;

	/**
	 * Constructor for TestTPTeamTest.
	 * 
	 * @param name
	 */
	public TestTPTeamTest(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestTPTeamTest</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testTPTeamTest = new HyadesTestSuite("TestTPTeamTest");
		testTPTeamTest.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"CA0EB60D618C58AA1D899640FB3B11DB");

		testTPTeamTest.addTest(new TestTPTeamTest("testAddTPTeamTest").setId(
				"CA0EB60D618C58AA21F5F110FB3B11DB").setTestInvocationId(
				"CA0EB60D618C58AA2AF62BE0FB3B11DB"));

		testTPTeamTest.addTest(new TestTPTeamTest("testUpdateTPTeamTest")
				.setId("CA0EB60D618C58AAD58164E0FB3F11DB").setTestInvocationId(
						"CA0EB60D618C58AADF54A720FB3F11DB"));
		return testTPTeamTest;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		createTest();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		if (mTest != null)
			deleteTest(mTest);
	}

	/**
	 * testAddTPTeamTest
	 * 
	 * @throws Exception
	 */
	public void testAddTPTeamTest() throws Exception {
		// Validate Created Test
		assertTrue("Error: Test created with wrong name \"" + mTest.getName()
				+ "\".", mTest.getName().trim().equals(TEST_NAME));
		assertTrue("Error: Test created with wrong description \""
				+ mTest.getDescription() + "\".", mTest.getDescription().trim()
				.equals(TEST_DESC));
		assertTrue("Error: Test created with wrong createdby \""
				+ mTest.getCreatedBy().getEcfId() + "\".", mTest.getCreatedBy()
				.getEcfId().equals(ECF_ID));
		assertTrue("Error: Test created returned invalid boolean isFolder \""
				+ mTest.getIsFolder() + "\".", mTest.getIsFolder() == 'Y');
	}

	/**
	 * testUpdateTPTeamTest
	 * 
	 * @throws Exception
	 */
	public void testUpdateTPTeamTest() throws Exception {
		// Update the test in the database
		updateTest(mTest);
		// Validate results
		assertTrue("Error: Test updated with wrong name \"" + mTest.getName()
				+ "\".", mTest.getName().equals(TEST_NAME + " UPDATED"));
		assertTrue("Error: Test updated with wrong description \""
				+ mTest.getDescription() + "\".", mTest.getDescription().trim()
				.equals(TEST_DESC + " UPDATED"));
	}

	/**
	 * Helper method to set a temporarily created Tpteam Test from the test
	 * database.
	 */
	private void createTest() {
		mTest = null;
		edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test parent = null;
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			// First create basic test entity
			mTest = new edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test();
			mTest.setName(TEST_NAME);
			mTest.setDescription(TEST_DESC);
			parent = (edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test) s
					.load(
							edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test.class,
							new Integer(PARENT_TEST_ID));
			mTest.setParent(parent);
			mTest.setIsFolder('Y');

			// Associate a Project to test
			Project proj = (Project) s
					.load(Project.class, new Integer(PROJ_ID));
			mTest.setProject(proj);

			// Associate a TpteamUser as test creator
			TpteamUser user = (TpteamUser) s.load(TpteamUser.class,
					new Integer("1"));
			mTest.setCreatedBy(user);

			// Set to Folder test type
			TestType testType = (TestType) s.load(TestType.class, new Integer(
					"1"));
			mTest.setTestType(testType);

			// Save temporary test to test database
			Integer testID = (Integer) s.save(mTest);

			// Update path now that ID is known
			mTest.setPath(PARENT_TEST_ID + "." + testID);

			// Inititalize Data
			Hibernate.initialize(mTest.getCreatedBy());

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

	/**
	 * Helper method to update a temporarily created Tpteam Test from the test
	 * database.
	 * 
	 * @param test
	 */
	private void updateTest(
			edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test test) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			mTest = (edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test) s
					.load(
							edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test.class,
							test.getId());
			mTest.setName(TEST_NAME + " UPDATED");
			mTest.setDescription(TEST_DESC + " UPDATED");
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

	/**
	 * Helper method to clean up test database by deleted temporarily created
	 * Tpteam Test.
	 * 
	 * @param test
	 */
	private void deleteTest(
			edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test test) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			s.delete(test);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
		}
	}

}
