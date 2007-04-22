package edu.harvard.fas.rbrady.tpteam.tpbuddy.eventadmin.test;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import junit.framework.Test;

import org.eclipse.core.runtime.Platform;
import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.Activator;

/**
 * Generated code for the test suite <b>TestEventAdmin</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpbuddy/eventadmin/test/TestEventAdmin.testsuite</i>.
 */
public class TestEventAdmin extends HyadesTestCase implements Observer {
	
	private TPEvent mTPEvent;
	/**
	 * Constructor for TestEventAdmin.
	 * @param name
	 */
	public TestEventAdmin(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestEventAdmin</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testEventAdmin = new HyadesTestSuite("TestEventAdmin");
		testEventAdmin.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"C5B3E7717F7410AB77467CF0F11F11DB");
	
		testEventAdmin.addTest(new TestEventAdmin("testTPEvent").setId(
				"C5B3E7717F7410AB8A3BB2D9F11F11DB").setTestInvocationId(
				"C5B3E7717F7410AB94181CD0F11F11DB"));
		return testEventAdmin;
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
	public void testTPEvent()
	throws Exception
	{
		// Start up required bundles
		Platform.getBundle("org.eclipse.equinox.event").start();
		Platform.getBundle("edu.harvard.fas.rbrady.tpteam.tpbridge").start();
		Platform.getBundle("edu.harvard.fas.rbrady.tpteam.tpbuddy").start();

		// Preconditions: must have active event handler and sender
		assertNotNull(Activator.getDefault().getEventAdminHandler());
		assertNotNull(Activator.getDefault().getEventAdminClient());
		
		// Add test class as observer so can listen and get events
		Activator.getDefault().getEventAdminHandler().addObserver(this);
	
		// Make sure can send and recieve at least one pair of event topics
		
		Activator.getDefault().getEventAdminClient().sendEvent(ITPBridge.PROJ_GET_REQ_TOPIC, new Hashtable<String,String>());
		if(mTPEvent != null)
			assertTrue(mTPEvent.getTopic().equals(ITPBridge.PROJ_GET_REQ_TOPIC));

		Activator.getDefault().getEventAdminClient().sendEvent(ITPBridge.PROJ_GET_RESP_TOPIC, new Hashtable<String,String>());
		if(mTPEvent != null)
			assertTrue(mTPEvent.getTopic().equals(ITPBridge.PROJ_GET_RESP_TOPIC));
	}

	/**
	 * Part of the observer impl, need to get updated by event handler
	 * in order to get TPEvents
	 */
	public void update(Observable o, Object tpEvent) {
		mTPEvent = (TPEvent)tpEvent;
		
	}

}
