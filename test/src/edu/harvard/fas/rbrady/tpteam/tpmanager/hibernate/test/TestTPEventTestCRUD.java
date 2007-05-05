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
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestType;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.TestXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestUtil;

/**
 * Generated code for the test suite <b>TestTPEventTestCRUD</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpmanager/hibernate/test/TestTPEventTestCRUD.testsuite</i>.
 */
public class TestTPEventTestCRUD extends HyadesTestCase {

	public static final String ECF_ID = "tpteam1@gmail.com";

	public static final String PROJ_ID = "1";

	public static final String PARENT_TEST_ID = "4";

	public static final String TEST_NAME = "TestStub Name";

	public static final String TEST_DESC = "TestStub Desc";

	public static final String ECLIPSE_HOME = "c:/EclipseHome";

	private edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test mTestStub;

	private TpteamUser mTpteamUser;

	/**
	 * Constructor for TestTPEventTestCRUD.
	 * 
	 * @param name
	 */
	public TestTPEventTestCRUD(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestTPEventTestCRUD</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testTPEventTestCRUD = new HyadesTestSuite(
				"TestTPEventTestCRUD");
		testTPEventTestCRUD.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"CA0EB60D618C58AAA948C520FB2211DB");

		testTPEventTestCRUD.addTest(new TestTPEventTestCRUD("testAddTest")
				.setId("CA0EB60D618C58AAB1317570FB2211DB").setTestInvocationId(
						"CA0EB60D618C58AAB975DA50FB2211DB"));

		testTPEventTestCRUD.addTest(new TestTPEventTestCRUD("testUpdateTest")
				.setId("CA0EB60D618C58AAB676A310FB2D11DB").setTestInvocationId(
						"CA0EB60D618C58AABF9896B0FB2D11DB"));
		return testTPEventTestCRUD;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		// Create stub test object
		buildTestStub();
	}

	private void buildTestStub() {
		// Create test stub
		mTestStub = new edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test();
		mTestStub.setName(TEST_NAME);
		mTestStub.setDescription(TEST_DESC);
		mTestStub.setIsFolder('N');
		edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test parent = new edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test();
		parent.setId(Integer.valueOf(PARENT_TEST_ID));
		parent.addChild(mTestStub);
		mTestStub.setParent(parent);
		TestType testType = new TestType();
		// Add JUnit info
		testType.setName("JUnit");
		mTestStub.setTestType(testType);
		JunitTest junit = new JunitTest();
		junit.setEclipseHome(ECLIPSE_HOME);
		junit.setWorkspace("c:/Workspace");
		junit.setProject("edu.harvard.fas.rbrady.tpteam.testplugin");
		junit.setTestSuite("test.testsuite");
		junit.setReportDir("c:/reports");
		junit.setTptpConnection("tptp:rac://localhost:10002/default");
		mTestStub.addJunitTest(junit);
		// Associate a TpteamUser as test creator
		mTpteamUser = new TpteamUser();
		mTpteamUser.setEcfId(ECF_ID);
		mTestStub.setCreatedBy(mTpteamUser);
		Project proj = new Project();
		proj.setId(Integer.parseInt(PROJ_ID));
		mTestStub.setProject(proj);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
	}

	/**
	 * Tests the addition of a new TPTeam Test to the test database via wrapping
	 * an XML serialized Test object inside a TPTeam Event.
	 * 
	 * @throws Exception
	 */
	public void testAddTest() throws Exception {
		edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test addedTest = null;
		try {
			// Get the test to be added in serialized XML format
			String testXML = TestXML.getXML(mTestStub);

			// Wrap it inside a TPEvent
			Hashtable<String, String> dictionary = new Hashtable<String, String>();
			dictionary.put(TPEvent.ID_KEY, String.valueOf(PARENT_TEST_ID));
			dictionary.put(TPEvent.PROJECT_ID_KEY, PROJ_ID);
			dictionary.put(TPEvent.SEND_TO, ECF_ID);
			dictionary.put(TPEvent.FROM, mTpteamUser.getEcfId());
			dictionary.put(TPEvent.TEST_XML_KEY, testXML);
			TPEvent tpEvent = new TPEvent(ITPBridge.TEST_ADD_REQ_TOPIC,
					dictionary);
			// Add to database
			TestUtil.addTest(tpEvent);

			// Validate TPEvent dictionary correctly updated
			assertNotNull("Error: TestUtil did not set newly created test ID.",
					tpEvent.getDictionary().get(TPEvent.ID_KEY));
			assertTrue("Error: Invalid ID for newly created test.", Integer
					.valueOf(tpEvent.getDictionary().get(TPEvent.ID_KEY)) > 0);

			// Get newly added test from database & Validate
			addedTest = getTest(tpEvent);
			assertTrue("Error: Incorrect Test name \"" + addedTest.getName()
					+ "\"added to database.", addedTest.getName().trim()
					.equals(TEST_NAME));
			assertTrue("Error: Incorrect Test description \""
					+ addedTest.getDescription() + "\" added to database.",
					addedTest.getDescription().trim().equals(TEST_DESC));
			String eclipseHome = addedTest.getJunitTests().toArray(
					new JunitTest[0])[0].getEclipseHome();
			assertTrue("Error: Incorrect Test Eclipse Home \"" + eclipseHome
					+ "\" added to database.", eclipseHome.trim().equals(
					ECLIPSE_HOME));
		} finally {
			// Clean-up test database
			deleteTest(addedTest);
		}
	}

	/**
	 * Tests the update of an existing TPTeam Test in the test database via
	 * wrapping an XML serialized Test object inside a TPTeam Event.
	 * 
	 * @throws Exception
	 */
	public void testUpdateTest() throws Exception {
		edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test test = null;
		try {
			// Get the test to be added in serialized XML format
			String testXML = TestXML.getXML(mTestStub);

			// Wrap it inside a TPEvent
			Hashtable<String, String> dictionary = new Hashtable<String, String>();
			dictionary.put(TPEvent.ID_KEY, String.valueOf(PARENT_TEST_ID));
			dictionary.put(TPEvent.PROJECT_ID_KEY, PROJ_ID);
			dictionary.put(TPEvent.SEND_TO, ECF_ID);
			dictionary.put(TPEvent.FROM, mTpteamUser.getEcfId());
			dictionary.put(TPEvent.TEST_XML_KEY, testXML);
			TPEvent tpEvent = new TPEvent(ITPBridge.TEST_ADD_REQ_TOPIC,
					dictionary);
			
			// Add to database
			TestUtil.addTest(tpEvent);
			test = getTest(tpEvent);

			// Change the description and eclipse home
			mTestStub.setId(Integer.parseInt(tpEvent.getID()));
			mTestStub.setDescription(TEST_DESC + " UPDATED");
			mTestStub.getJunitTests().toArray(
					new JunitTest[0])[0].setEclipseHome(ECLIPSE_HOME + "/UPDATED");
			testXML = TestXML.getXML(mTestStub);
			tpEvent.getDictionary().put(TPEvent.TEST_XML_KEY, testXML);
			tpEvent.setTopic(ITPBridge.TEST_UPDATE_REQ_TOPIC);
			
			// Update test database
			TestUtil.updateTest(tpEvent);
			test = getTest(tpEvent);
			
			// Validate results
			assertTrue("Error: Incorrectly Updated Test description \""
					+ test.getDescription() + "\" retrieved from database.",
					test.getDescription().trim().equals(TEST_DESC + " UPDATED"));
			String eclipseHome = test.getJunitTests().toArray(
					new JunitTest[0])[0].getEclipseHome();
			assertTrue("Error: Incorrectly updated Test Eclipse Home \"" + eclipseHome
					+ "\" retrieved from database.", eclipseHome.trim().equals(
					ECLIPSE_HOME + "/UPDATED"));
		} finally {
			// Clean-up test database
			deleteTest(test);
		}
	}

	/**
	 * Helper method to return a temporarily created Tpteam Test from the test
	 * database.
	 * 
	 * @param tpEvent
	 * @return
	 */
	private edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test getTest(
			TPEvent tpEvent) {
		edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test test = null;
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			int testId = Integer.valueOf(tpEvent.getDictionary().get(
					TPEvent.ID_KEY));
			test = (edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test) s
					.load(
							edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test.class,
							testId);
			Hibernate.initialize(test);
			Hibernate.initialize(test.getCreatedBy());
			Hibernate.initialize(test.getJunitTests());
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
		}
		return test;
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
