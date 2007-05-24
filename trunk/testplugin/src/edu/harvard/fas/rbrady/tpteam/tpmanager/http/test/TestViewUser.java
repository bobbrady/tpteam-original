package edu.harvard.fas.rbrady.tpteam.tpmanager.http.test;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewUser2;

/**
 * Generated code for the test suite <b>TestViewUser</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpmanager/http/test/TestViewUser.testsuite</i>.
 */
public class TestViewUser extends HyadesTestCase {
	
	/** User ID stored in test database */
	public static final String USER_ID = "1";
	
	/** user_name stored in test database */
	public static final String USER_NAME = "tpteam1";
	
	/** User ECF ID stored in test database */
	public static final String ECF_ID = "tpteam1@gmail.com";
	
	/**
	 * Constructor for TestViewUser.
	 * @param name
	 */
	public TestViewUser(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestViewUser</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testViewUser = new HyadesTestSuite("TestViewUser");
		testViewUser.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"C39EC0FAEC78CFF55646EBC0F92B11DB");
	
		testViewUser.addTest(new TestViewUser("testGetUser").setId(
				"C39EC0FAEC78CFF55B138F00F92B11DB").setTestInvocationId(
				"C39EC0FAEC78CFF561C2FC00F92B11DB"));
		return testViewUser;
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
	* testGetUser
	* @throws Exception
	*/
	public void testGetUser()
	throws Exception
	{
		// Get TpteamUser from database via Servlet View
		ViewUser2 viewUser = new ViewUser2();
		viewUser.setUserId(USER_ID);
		TpteamUser user = viewUser.getUser();
		// Verify with test database info
		assertTrue(user.getUserName().equals(USER_NAME));
		assertTrue(user.getEcfId().equals(ECF_ID));
	}

}
