package edu.harvard.fas.rbrady.tpteam.tpbridge.model.test;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNodeChangeListener;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;

/**
 * Generated code for the test suite <b>TestTPEntity</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpbridge/model/test/TestTPEntity.testsuite</i>.
 */
public class TestTPEntity extends HyadesTestCase implements ITreeNodeChangeListener{
	
	/** State Change Tracker for tests */
	private boolean mStateChanged = false;
	
	private static final String ID = "ID";
	
	private static final String NAME = "NAME";
	
	private static final String DESCRIPTION = "DESC";
	
	/** TPEntity to be used in tests */
	private static TPEntity mTPEntity;

	
	/**
	 * Constructor for TestTPEntity.
	 * @param name
	 */
	public TestTPEntity(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestTPEntity</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testTPEntity = new HyadesTestSuite("TestTPEntity");
		testTPEntity.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"CA0EB60D618C58AA226B9FD0FB4711DB");
	
		testTPEntity.addTest(new TestTPEntity("testFireUpdated").setId(
				"CA0EB60D618C58AA28282150FB4711DB").setTestInvocationId(
				"CA0EB60D618C58AA32B11F00FB4711DB"));
	
		testTPEntity.addTest(new TestTPEntity("testFireNodeAdded").setId(
				"CA0EB60D618C58AA5656FC80FB4811DB").setTestInvocationId(
				"CA0EB60D618C58AA6341B7F0FB4811DB"));
	
		testTPEntity.addTest(new TestTPEntity("testFireNodeDeleted").setId(
				"CA0EB60D618C58AA5B948A00FB4811DB").setTestInvocationId(
				"CA0EB60D618C58AA66859850FB4811DB"));
		return testTPEntity;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		mStateChanged = false;
		mTPEntity = new TPEntity(ID, NAME, DESCRIPTION, TPEntity.JUNIT_TEST);
		mTPEntity.addChangeListener(this);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		mStateChanged = false;
		mTPEntity.removeChangeListener(this);
		mTPEntity = null;
	}

	/**
	* testFireUpdated
	* @throws Exception
	*/
	public void testFireUpdated()
	throws Exception
	{
		// Create the entity
		mTPEntity.setDescription(DESCRIPTION + " UPDATED");
		assertTrue("Error: TPEntity failed to fire update event on description udpate.", mStateChanged);		
	}
	
	/**
	* testFireNodeAdded
	* @throws Exception
	*/
	public void testFireNodeAdded()
	throws Exception
	{
		TPEntity child = new TPEntity(ID + "_CHILD", "CHILD", DESCRIPTION, TPEntity.EXEC_PASS);
		mTPEntity.addChild(child);
		assertTrue("Error: TPEntity failed to fire add node event when child node added.", mStateChanged);
	}

	/**
	* testFireNodeDeleted
	* @throws Exception
	*/
	public void testFireNodeDeleted()
	throws Exception
	{
		TPEntity child = new TPEntity(ID + "_CHILD", "CHILD", DESCRIPTION, TPEntity.EXEC_PASS);
		mTPEntity.addChild(child);
		assertTrue("Error: TPEntity failed to fire add node event when child node added.", mStateChanged);
		mStateChanged = false;
		mTPEntity.removeChild(child);
		assertTrue("Error: TPEntity failed to fire delete node event when child node deleted.", mStateChanged);
	}


	public void addNode(ITreeNode node) {
		mStateChanged = true;
		
	}

	public void deleteNode(ITreeNode node) {
		mStateChanged = true;
		
	}

	public void updateNode(ITreeNode node) {
		mStateChanged = true;
	}


}
