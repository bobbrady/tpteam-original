package edu.harvard.fas.rbrady.tpteam.tpbridge.model.test;

import java.util.Hashtable;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;
import org.osgi.service.event.Event;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

/**
 * Generated code for the test suite <b>TestTPEvent</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpbridge/model/test/TestTPEvent.testsuite</i>.
 */
public class TestTPEvent extends HyadesTestCase {
	
	/** Hash dictionary to be used in tests */
	Hashtable<String, String> mDictionary = null;
	
	/** ECF ID to be used in tests */
	private static final String ECF_ID = "tpteam1@gmail.com";
	
	/** Generic ID tag used in tests */
	private static final String ID = "ID";
	
	/** Generic XML String used in tests */
	private static final String XML_STRING = "<xml>TPTeam</xml>";
	
	
	/**
	 * Constructor for TestTPEvent.
	 * 
	 * @param name
	 */
	public TestTPEvent(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestTPEvent</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testTPEvent = new HyadesTestSuite("TestTPEvent");
		testTPEvent.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"CA0EB60D618C58AAAD4E08A0FB4111DB");
	
		testTPEvent.addTest(new TestTPEvent("testCreateFromOSGiEvent").setId(
				"CA0EB60D618C58AA135A2610FB4211DB").setTestInvocationId(
				"CA0EB60D618C58AA1D08A240FB4211DB"));
	
		testTPEvent.addTest(new TestTPEvent("testCreateFromTopicHash").setId(
				"CA0EB60D618C58AA130AE2B0FB4411DB").setTestInvocationId(
				"CA0EB60D618C58AA1F691590FB4411DB"));
		return testTPEvent;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		mDictionary = new Hashtable<String, String>();
		mDictionary.put(TPEvent.ID_KEY, ID);
		mDictionary.put(TPEvent.PROJECT_ID_KEY, TPEvent.PROJECT_ID_KEY);
		mDictionary.put(TPEvent.SEND_TO, TPEvent.SEND_TO);
		mDictionary.put(TPEvent.FROM, ECF_ID);
		mDictionary.put(TPEvent.TEST_XML_KEY, XML_STRING);

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
	}

	/**
	 * testCreateFromOSGiEvent
	 * 
	 * @throws Exception
	 */
	public void testCreateFromOSGiEvent() throws Exception {
	
		// First create the OSGi Event
		Event osgiEvent = new Event(ITPBridge.PROJ_GET_REQ_TOPIC, mDictionary);
		
		// Create the TPEvent
		TPEvent tpEvent = new TPEvent(osgiEvent);
		
		// Validate values
		assertTrue("Error: TPEvent returned different topic \""
				+ tpEvent.getTopic() + "\" than parent OSGi Event.", tpEvent
				.getTopic().equals(osgiEvent.getTopic()));
		assertTrue("Error: TPEvent returned different ID \"" + tpEvent.getID()
				+ "\" than expected.", tpEvent.getID().equals(ID));
		assertTrue("Error: TPEvent returned different ECF From \"" + tpEvent.getDictionary().get(TPEvent.FROM)
				+ "\" than expected.", tpEvent.getDictionary().get(TPEvent.FROM).equals(ECF_ID));
	}

	/**
	* testCreateFromTopicHash
	* @throws Exception
	*/
	public void testCreateFromTopicHash()
	throws Exception
	{
		// First create the TPEvent from component topic and hash
		TPEvent tpEvent = new TPEvent(ITPBridge.TEST_EXEC_REQ_TOPIC, mDictionary);
		
		// Validate values
		assertTrue("Error: TPEvent returned different topic \""
				+ tpEvent.getTopic() + "\" than parent OSGi Event.", tpEvent
				.getTopic().equals(ITPBridge.TEST_EXEC_REQ_TOPIC));
		assertTrue("Error: TPEvent returned different ID \"" + tpEvent.getID()
				+ "\" than expected.", tpEvent.getID().equals(ID));
		assertTrue("Error: TPEvent returned different ECF From \"" + tpEvent.getDictionary().get(TPEvent.FROM)
				+ "\" than expected.", tpEvent.getDictionary().get(TPEvent.FROM).equals(ECF_ID));

	}

}
