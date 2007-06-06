package edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.test;

import java.util.Hashtable;

import junit.framework.Test;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;
import org.osgi.service.event.Event;

import edu.harvard.fas.rbrady.tpteam.testplugin.Activator;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.TPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

/**
 * Generated code for the test suite <b>TestTPBridge</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpbridge/bridge/TestTPBridge.testsuite</i>.
 */
public class TestTPBridge extends HyadesTestCase {

	private final String CONTAINER_TYPE = "ecf.xmpps.smack";

	private final String ECF_ID = "tpteamtest@gmail.com";

	private final String ECF_PASSWORD = "tpteamtest";

	private Namespace mNamespace = null;

	private IContainer mContainer = null;

	private ID mTargetID = null;

	/**
	 * Constructor for TestTPBridge.
	 * 
	 * @param name
	 */
	public TestTPBridge(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestTPBridge</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testTPBridge = new HyadesTestSuite("TestTPBridge");
		testTPBridge.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"FE098806812ADE85B634EFE0FB1111DB");
	
		testTPBridge.addTest(new TestTPBridge("testGetConnection").setId(
				"FE098806812ADE85BB75B1B0FB1111DB").setTestInvocationId(
				"FE098806812ADE8598A32CF0FB1411DB"));
	
		testTPBridge.addTest(new TestTPBridge("testSendECFTPMsg").setId(
				"FE098806812ADE856B25DC90FB1511DB").setTestInvocationId(
				"FE098806812ADE8578601790FB1511DB"));
		return testTPBridge;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		// Start required plugins within OSGi runtime
		Platform.getBundle("org.eclipse.equinox.event").start();
		Platform.getBundle("edu.harvard.fas.rbrady.tpteam.tpbridge").start();

		// Setup container
		setupContainer();

		// Now connect
		doConnect();
	}

	private void setupContainer() {
		try {
			mContainer = ContainerFactory.getDefault().createContainer(
					CONTAINER_TYPE);
		} catch (ContainerCreateException e) {
			fail(e.getMessage());
		}
		mNamespace = mContainer.getConnectNamespace();
	}

	private void doConnect() {
		try {
			mTargetID = IDFactory.getDefault().createID(mNamespace, ECF_ID);
			mContainer.connect(mTargetID, ConnectContextFactory
					.createPasswordConnectContext(ECF_PASSWORD));
			Thread.sleep(3000);
		} catch (IDCreateException e) {
			fail(e.getMessage());
		} catch (ContainerConnectException e) {
			fail(e.getMessage());
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		// Cleanup by disconnecting
		mContainer.disconnect();
		mContainer.dispose();
	}

	/**
	 * testSendECFTPMsg
	 * 
	 * @throws Exception
	 */
	public void testGetConnection() throws Exception {
		System.out.println("ConnectedID: " + mContainer.getConnectedID());
		assertTrue("Error: TPBridge failed to establish XMPPS connection",
				mContainer.getConnectedID() != null);
	}

	/**
	 * testSendECFTPMsg
	 * 
	 * @throws Exception
	 */
	public void testSendECFTPMsg() throws Exception {
		// Create a simple test TPEvent
		Hashtable<String, String> hash = new Hashtable<String, String>();
		hash.put(TPEvent.FROM, ECF_ID);
		hash.put(TPEvent.SEND_TO, ECF_ID);
		ITPBridge tpBridge = new TPBridge(Activator.getDefault().getContext());
		tpBridge.setContainer(mContainer, mTargetID.getName(),
				ITPBridge.TPTEAM_MGR);
		Event osgiEvent = new Event("TEST_TOPIC", hash);
		assertTrue(
				"Error: TPBridge failed to send ECF SharedObject Message over XMPPS.",
				tpBridge.sendECFTPMsg(osgiEvent));
	}
}
