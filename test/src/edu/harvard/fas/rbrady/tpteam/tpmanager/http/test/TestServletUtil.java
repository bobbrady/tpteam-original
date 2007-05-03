package edu.harvard.fas.rbrady.tpteam.tpmanager.http.test;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.ServletUtil;

/**
 * Generated code for the test suite <b>TestServletUtil</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpmanager/http/test/TestServletUtil.testsuite</i>.
 */
public class TestServletUtil extends HyadesTestCase {

	/** user_name in test database */
	private static final String USER_NAME = "tpteam1";

	/** user ID in test database */
	private static final int USER_ID = 1;

	/** Plain text String for SHA1 hash input */
	private static final String PLAIN_TEXT = "tpteam";

	/** SHA1 hash output */
	private static final String SHA1_HASH = "84FE0D304EE0B3D09C860FA4544E17C5D94D29E0";

	/**
	 * Constructor for TestServletUtil.
	 * 
	 * @param name
	 */
	public TestServletUtil(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestServletUtil</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testServletUtil = new HyadesTestSuite("TestServletUtil");
		testServletUtil.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"F2BDC6C362ADD3C73473CD10F91711DB");
	
		testServletUtil.addTest(new TestServletUtil("testGetRemoteUserID").setId(
				"F2BDC6C362ADD3C751052FA0F91711DB").setTestInvocationId(
				"F2BDC6C362ADD3C76C4AFF60F91711DB"));
	
		testServletUtil.addTest(new TestServletUtil("testGetSHA1Hash").setId(
				"F2BDC6C362ADD3C75B315560F91911DB").setTestInvocationId(
				"F2BDC6C362ADD3C764DFD190F91911DB"));
	
		testServletUtil.addTest(new TestServletUtil("testGetTestTreeFolders")
				.setId("F2BDC6C362ADD3C765E0AA50F91A11DB").setTestInvocationId(
						"F2BDC6C362ADD3C7714D7850F91A11DB"));
		return testServletUtil;
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
	 * testGetRemoteUserID
	 * 
	 * @throws Exception
	 */
	public void testGetRemoteUserID() throws Exception {
		int remoteUserID = ServletUtil.getRemoteUserID(USER_NAME);
		assertTrue("Error: ServletUtil returned incorrect remote user id "
				+ remoteUserID + ".", USER_ID == remoteUserID);
	}

	/**
	 * testGetSHA1Hash
	 * 
	 * @throws Exception
	 */
	public void testGetSHA1Hash() throws Exception {
		String sha1Hash = ServletUtil.getSHA1Hash(PLAIN_TEXT);
		assertTrue("Error: ServletUtil returned incorrect hash for "
				+ PLAIN_TEXT + ".", SHA1_HASH.equals(sha1Hash));
	}

	/**
	* testGetTestTreeFolders
	* @throws Exception
	*/
	public void testGetTestTreeFolders()
	throws Exception
	{
		// Get all folders in test database
		// Seeded: Folder_1 <= Folder_1.1, Folder_2 <= Folder_2.1, Folder_3 <= Folder_3.1
		String testTreeFolders = ServletUtil.getTestTreeFolders("1").replaceAll("\\n", "");
		// First get Root Folder info
		String root = "id=\"folder_0\">Project Root</div>";
		int indexOfRoot = testTreeFolders.indexOf(root);
		// Top level folder Folder_1
		String folder1 = "id=\"folder_1\">Folder_1</div>";
		int indexOfFolder1 = testTreeFolders.indexOf(folder1);
		// Child of folder Folder_1: Folder_1.1
		String folder1_1 = "id=\"folder_4\">Folder_1.1</div>";
		int indexOfFolder1_1 = testTreeFolders.indexOf(folder1_1);
		// All folders should be present, Parents first
		assertTrue(indexOfRoot > 0);
		assertTrue(indexOfFolder1 > 0);
		assertTrue(indexOfFolder1_1 > 0);
		assertTrue(indexOfFolder1 > indexOfRoot);
		assertTrue(indexOfFolder1_1 > indexOfFolder1);
	}

}
