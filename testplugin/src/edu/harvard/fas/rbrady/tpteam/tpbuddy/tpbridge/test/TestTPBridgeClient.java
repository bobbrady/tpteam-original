package edu.harvard.fas.rbrady.tpteam.tpbuddy.tpbridge.test;

import junit.framework.Test;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.tpbridge.TPBridgeClient;

/**
 * Generated code for the test suite <b>TestTPBridgeClient</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpbuddy/tpbridge/test/TestTPBridgeClient.testsuite</i>.
 */
public class TestTPBridgeClient extends HyadesTestCase {
	/**
	 * Constructor for TestTPBridgeClient.
	 * @param name
	 */
	public TestTPBridgeClient(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestTPBridgeClient</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testTPBridgeClient = new HyadesTestSuite(
				"TestTPBridgeClient");
		testTPBridgeClient.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"F968DA8CBEFEFE1A799AE350F11611DB");
	
		testTPBridgeClient.addTest(new TestTPBridgeClient("testTPBridgeClient")
				.setId("F968DA8CBEFEFE1AAC5A86B9F11611DB").setTestInvocationId(
						"F968DA8CBEFEFE1A34EBB2E0F11911DB"));
		return testTPBridgeClient;
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
	* test 1
	* @throws Exception
	*/
	public void testTPBridgeClient()
	throws Exception
	{
		Platform.getBundle("org.eclipse.equinox.event").start();
		Platform.getBundle("edu.harvard.fas.rbrady.tpteam.tpbridge").start();
		Platform.getBundle("edu.harvard.fas.rbrady.tpteam.tpbuddy").start();
		
		// Verify properties file was read and set to project default tpteam1 at gmail
		assertTrue(Activator.getDefault().getTPBridgeClient().getTPMgrECFID().equals("tpteam1@gmail.com"));
		
		// Get an ECF gmail google talk connection
		TPBridgeClient tpBridgeClient = Activator.getDefault().getTPBridgeClient();
		// Make sure starting out w/o connection
		assertFalse(tpBridgeClient.isSharedObjectActive());
		
		// Get ECF communication container
		IContainer container = ContainerFactory.getDefault().createContainer("ecf.xmpps.smack");
		// Create the targetID 
		ID targetID = IDFactory.getDefault().createID(
				container.getConnectNamespace(), "tpteamtest@gmail.com");
		// Get security context
		IConnectContext connectContext = ConnectContextFactory
		.createPasswordConnectContext("tpteamtest");
		
		// Now get connection
		tpBridgeClient.connect(container, targetID, connectContext);
		
		// Confirm connection made successfully by SharedObject being active
		assertTrue(tpBridgeClient.isSharedObjectActive());

	}

}
