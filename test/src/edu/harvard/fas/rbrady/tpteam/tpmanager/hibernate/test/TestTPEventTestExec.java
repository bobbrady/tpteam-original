package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.test;

import java.util.Hashtable;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestExecution;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestExecutionUtil;

/**
 * Generated code for the test suite <b>TestTPEventTestExec</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpmanager/hibernate/test/TestTPEventTestExec.testsuite</i>.
 */
public class TestTPEventTestExec extends HyadesTestCase {

	/** ECF XMPP ID to be used in tests */
	public static final String ECF_ID = "tpteam1@gmail.com";

	/** ID of test stored in test database */
	public static final String TEST_ID = "22";

	/** Verdict to be used in testing */
	public static final String VERDICT = "pass";

	/**
	 * Constructor for TestTPEventTestExec.
	 * 
	 * @param name
	 */
	public TestTPEventTestExec(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestTPEventTestExec</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testTPEventTestExec = new HyadesTestSuite(
				"TestTPEventTestExec");
		testTPEventTestExec.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"CA0EB60D618C58AAEE4E89F0FB3311DB");

		testTPEventTestExec.addTest(new TestTPEventTestExec("testAddExec")
				.setId("CA0EB60D618C58AAF2B7D780FB3311DB").setTestInvocationId(
						"CA0EB60D618C58AAFB426D20FB3311DB"));
		return testTPEventTestExec;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
	}

	/**
	 * testAddExec
	 * 
	 * @throws Exception
	 */
	public void testAddExec() throws Exception {
		TestExecution testExec = null;
		try {
			// Wrap test exec create request inside a TPEvent
			Hashtable<String, String> dictionary = new Hashtable<String, String>();
			dictionary.put(TPEvent.ECFID_KEY, ECF_ID);
			dictionary.put(TPEvent.VERDICT_KEY, VERDICT);
			TPEvent tpEvent = new TPEvent(ITPBridge.TEST_EXEC_REQ_TOPIC,
					dictionary);
			
			// Add to database
			TestExecutionUtil.insertTestExec(TEST_ID, tpEvent);
			
			// Retrieve newly created test execution from test database
			testExec = getTestExecution(tpEvent);
			
			// Validate data
			assertNotNull("Error: TestExecution not retrieved from test database.", testExec);
			assertTrue("Error: TestExecution has incorrect verdict, \"" + testExec.getStatus() + "\" .",
					testExec.getStatus().equals(VERDICT.toUpperCase().charAt(0)));
			assertTrue("Error: TestExecution has incorrect parent test ID, \"" + testExec.getTest().getId() + "\" .",
					testExec.getTest().getId() == Integer.valueOf(TEST_ID));
		} finally {
			deleteTestExecution(testExec);
		}
	}

	/**
	 * Helper method to get newly created Tpteam TestExecution.
	 * 
	 * @param tpEvent
	 * @return
	 */
	private TestExecution getTestExecution(TPEvent tpEvent) {
		TestExecution testExec = null;
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			int testExecId = Integer.valueOf(tpEvent.getDictionary().get(
					TPEvent.ID_KEY).replaceAll("EXEC_", ""));
			testExec = (TestExecution) s.load(TestExecution.class, testExecId);
			Hibernate.initialize(testExec);
			Hibernate.initialize(testExec.getTest());
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
		}
		return testExec;
	}
	
	/**
	 * Helper method to clean up test database by deleted temporarily created
	 * Tpteam TestExecution.
	 * 
	 * @param test
	 */
	private void deleteTestExecution(TestExecution testExec) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			s.delete(testExec);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
		}
	}

}
