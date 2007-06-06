package edu.harvard.fas.rbrady.tpteam.tpmanager.http.test;

import java.util.HashMap;
import java.util.HashSet;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewProject2;

/**
 * Generated code for the test suite <b>TestViewProject</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpmanager/http/test/TestViewProject.testsuite</i>.
 */
public class TestViewProject extends HyadesTestCase {
	/**
	 * Constructor for TestViewProject.
	 * @param name
	 */
	public TestViewProject(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestViewProject</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testViewProject = new HyadesTestSuite("TestViewProject");
		testViewProject.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"C39EC0FAEC78CFF577B7A550F92211DB");
	
		testViewProject.addTest(new TestViewProject("testGetTeam").setId(
				"C39EC0FAEC78CFF5816DC2A0F92211DB").setTestInvocationId(
				"C39EC0FAEC78CFF58A436B00F92211DB"));
		return testViewProject;
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
	* testGetTeam
	* @throws Exception
	*/
	public void testGetTeam()
	throws Exception
	{
		// Create test input set of TPTeam users
		HashSet<TpteamUser> team = new HashSet<TpteamUser>();
		TpteamUser user1 = new TpteamUser();
		TpteamUser user2 = new TpteamUser();
		TpteamUser user3 = new TpteamUser();
		user1.setUserName("user1");
		user1.setLastName("user1_last");
		user1.setFirstName("user1_first");
		user2.setUserName("user2");
		user2.setLastName("user2_last");
		user2.setFirstName("user2_first");
		user3.setUserName("user3");
		user3.setLastName("user3_last");
		user3.setFirstName("user3_first");
		team.add(user1);
		team.add(user2);
		team.add(user3);
		
		// Feed input to view, check result
		ViewProject2 viewProject = new ViewProject2();
		viewProject.getTeam(team);
		String[] userHTML = viewProject.getTeam().split("\\n");
		// Need verification Hashmap because TPTP JUnit randomizes split
		HashMap<String, Boolean> userHash = new HashMap<String, Boolean>();
		for(String user : userHTML)
			userHash.put(user, new Boolean(true));
		assertTrue("Error: Expected 3 users, got " + userHTML.length  + ".", userHTML.length == 3);
		assertTrue(userHash.get("user1_last, user1_first (user1)<br>"));
		assertTrue(userHash.get("user2_last, user2_first (user2)<br>"));
		assertTrue(userHash.get("user3_last, user3_first (user3)<br>"));
	}

}
