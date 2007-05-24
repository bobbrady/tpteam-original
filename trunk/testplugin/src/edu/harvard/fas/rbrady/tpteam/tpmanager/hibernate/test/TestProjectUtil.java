package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.test;

import java.util.Hashtable;
import java.util.Set;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.test.TestProduct;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.ProjectUtil;

/**
 * Generated code for the test suite <b>TestProjectUtil</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpmanager/hibernate/test/TestProjectUtil.testsuite</i>.
 */
public class TestProjectUtil extends HyadesTestCase {

	/** user ECF ID in test database to be used */
	public static final String TEST_ECF_ID = "tpteam1@gmail.com";

	/** user ECF ID in test database to be used */
	public static final String TEST_PROJ_ID = "1";

	/** user ECF ID in test database to be used */
	public static final String TEST_PROJ_NAME = "TPTeam Dev Beta";

	/** user ECF ID in test database to be used */
	public static final String TEST_PROJ_DESC = "TPTeam Dev Beta";

	/**
	 * Constructor for TestProjectUtil.
	 * 
	 * @param name
	 */
	public TestProjectUtil(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestProjectUtil</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testProjectUtil = new HyadesTestSuite("TestProjectUtil");
		testProjectUtil.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"DC578CB87FDE029A6975B080F9E111DB");

		testProjectUtil.addTest(new TestProjectUtil("testGetProjectByECFID")
				.setId("DC578CB87FDE029A76978180F9E111DB").setTestInvocationId(
						"DC578CB87FDE029A81624320F9E111DB"));

		testProjectUtil.addTest(new TestProjectUtil("testGetProjProdXML")
				.setId("DC578CB87FDE029ABAD33D60F9E311DB").setTestInvocationId(
						"DC578CB87FDE029ACE799AD0F9E311DB"));
		return testProjectUtil;
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
	 * testGetProjectByECFID
	 * 
	 * @throws Exception
	 */
	public void testGetProjectByECFID() throws Exception {
		Set<Project> projs;
		projs = ProjectUtil.getProjByECFID(TEST_ECF_ID);
		// Only one project in test database
		assertTrue(
				"Error: ProjectUtil returned " + projs.size() + " projects.",
				projs.size() == 1);
		for (Project proj : projs) {
			// Verify returned values are what is in database
			assertTrue("Error: ProjectUtil returned invalid Project Id"
					+ proj.getId() + ".", proj.getId() == 1);
			assertTrue("Error: ProjectUtil returned invalid Project Name"
					+ proj.getName() + ".", proj.getName().equals(
					TEST_PROJ_NAME));
			assertTrue(
					"Error: ProjectUtil returned invalid Project Description"
							+ proj.getDescription() + ".", proj
							.getDescription().equals(TEST_PROJ_DESC));
			assertTrue("Error: ProjectUtil returned invalid Product Id"
					+ proj.getProduct().getId() + ".", proj.getProduct()
					.getId() == TestProduct.mProdID);
			assertTrue("Error: ProjectUtil returned invalid Product Name"
					+ proj.getProduct().getName() + ".", proj.getProduct()
					.getName().equals(TestProduct.mProdName));
		}
	}

	/**
	 * testGetProjProdXML
	 * 
	 * @throws Exception
	 */
	public void testGetProjProdXML() throws Exception {
		// Create a serialized list of Projects from a TPEvent
		Hashtable<String, String> hash = new Hashtable<String, String>();
		hash.put(TPEvent.FROM, TEST_ECF_ID);
		TPEvent tpEvent = new TPEvent("TEST_TOPIC", hash);
		// Get Project info from test database and serialize into XML with
		// betwixt
		String xml = ProjectUtil.getProjProdXML(tpEvent);
		// Make sure project tag element is correct for test database
		// information
		String projTag = "<project description=\"" + TEST_PROJ_DESC
				+ "\" id=\"1\" name=\"" + TEST_PROJ_NAME + "\">";
		assertTrue(
				"Error: ProjectUtil returned invalid Project XML serialization.",
				xml.indexOf(projTag) > 0);
	}

}
