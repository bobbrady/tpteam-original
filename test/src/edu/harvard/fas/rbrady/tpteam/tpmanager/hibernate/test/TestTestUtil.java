package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.test;

import java.util.Hashtable;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.TestUtil;

/**
 * Generated code for the test suite <b>TestTestUtil</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpmanager/hibernate/test/TestTestUtil.testsuite</i>.
 */
public class TestTestUtil extends HyadesTestCase {
	
	/** XML element for test database folder 1.1 */
	public static final String TEST_FOLDER_1_1_XML = "<children ID=\"4\" name=\"Folder_1.1\" type=\"FOLDER\" id=\"3\">";
	
	/** XML element for test database folder 2.1 */
	public static final String TEST_FOLDER_2_1_XML = "<children ID=\"5\" name=\"Folder_2.1\" type=\"FOLDER\" id=\"6\">";
	
	/** XML element for test database folder 3.1 */
	public static final String TEST_FOLDER_3_1_XML = "<children ID=\"6\" name=\"Folder_3.1\" type=\"FOLDER\" id=\"8\">"; 
	
	
	/**
	 * Constructor for TestTestUtil.
	 * @param name
	 */
	public TestTestUtil(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestTestUtil</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testTestUtil = new HyadesTestSuite("TestTestUtil");
		testTestUtil.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"E608F660D3D0F0DD663FA410F9E711DB");
	
		testTestUtil.addTest(new TestTestUtil("testGetTestTreeXML").setId(
				"E608F660D3D0F0DD710A65B0F9E711DB").setTestInvocationId(
				"E608F660D3D0F0DD791AE950F9E711DB"));
		return testTestUtil;
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
	* testGetTestTreeXML
	* @throws Exception
	*/
	public void testGetTestTreeXML()
	throws Exception
	{
		// Create mock TPEvent
		Hashtable<String, String> hash = new Hashtable<String, String>();
		hash.put(TPEvent.PROJECT_ID_KEY, TestProjectUtil.TEST_PROJ_ID);
		TPEvent tpEvent = new TPEvent("TEST_TOPIC", hash);
		// Get Test Tree XML from mock TPEvent and Test Database
		String testUtilXML = TestUtil.getTestTreeXML(tpEvent);
		// Get String locations of various folder XML tags
		int folder11Idx = testUtilXML.indexOf(TEST_FOLDER_1_1_XML);
		int folder21Idx = testUtilXML.indexOf(TEST_FOLDER_2_1_XML);
		int folder31Idx = testUtilXML.indexOf(TEST_FOLDER_3_1_XML);
		// Validate results
		assertTrue("Error: TestUtil returned invalid test tree serialization xml for folder 1.1." + testUtilXML + ".", folder11Idx > 0);
		assertTrue("Error: TestUtil returned invalid test tree serialization xml for folder 2.1." + testUtilXML + ".", folder21Idx > 0);
		assertTrue("Error: TestUtil returned invalid test tree serialization xml for folder 3.1." + testUtilXML + ".", folder31Idx > 0);
		assertTrue("Error: TestUtil returned invalid test tree serialization xml for folder 1.1." + testUtilXML + ".", folder21Idx > folder11Idx);
		assertTrue("Error: TestUtil returned invalid test tree serialization xml for folder 1.1." + testUtilXML + ".", folder31Idx > folder21Idx);
	}

}
