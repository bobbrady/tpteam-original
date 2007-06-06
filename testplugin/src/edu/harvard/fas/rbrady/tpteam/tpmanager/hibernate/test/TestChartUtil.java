package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.test;

import java.util.Hashtable;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate.ChartUtil;

/**
 * Generated code for the test suite <b>TestChartUtil</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpmanager/hibernate/test/TestChartUtil.testsuite</i>.
 */
public class TestChartUtil extends HyadesTestCase {

	/** Pie Chart XML that should be formed from test database query */
	public static final String PIE_CHART_PROJ_XML = "<chartDataSet projName=\""
			+ TestProjectUtil.TEST_PROJ_NAME
			+ "\" type=\"PIE_CHART\" id=\"1\">";

	/**
	 * Pie Chart Project Overview data point XML that should result from test
	 * database query
	 */
	public static final String PIE_CHART_DATA_POINT_XML = 
		"<chartDataPoint error=\"0\" fail=\"0\" inconcl=\"0\" notExec=\"1\" pass=\"0\" id=\"2\"/>";

	/**
	 * Constructor for TestChartUtil.
	 * 
	 * @param name
	 */
	public TestChartUtil(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestChartUtil</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testChartUtil = new HyadesTestSuite("TestChartUtil");
		testChartUtil.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"E608F660D3D0F0DD8314C990F9EB11DB");

		testChartUtil.addTest(new TestChartUtil("testGetPieChartXML").setId(
				"E608F660D3D0F0DD9D9B8F10F9EB11DB").setTestInvocationId(
				"E608F660D3D0F0DDA69A6A50F9EB11DB"));
		return testChartUtil;
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
	 * testGetPieChartXML
	 * 
	 * @throws Exception
	 */
	public void testGetPieChartXML() throws Exception {
		// Create a mock TPEvent that will be used to query test database
		Hashtable<String, String> dict = new Hashtable<String, String>();
		dict.put(TPEvent.PROJECT_ID_KEY, TestProjectUtil.TEST_PROJ_ID);
		dict.put(TPEvent.PROJECT_KEY, TestProjectUtil.TEST_PROJ_NAME);
		TPEvent tpEvent = new TPEvent("test_topic", dict);
		// Query test database through ChartUtil and do XML serialization
		String pieChartXML = ChartUtil.getPieChartXML(tpEvent);
		// Validate results from test database
		assertTrue("Error: ChartUtil return invalid chartDataSet tag.",
				pieChartXML.indexOf(PIE_CHART_PROJ_XML) > 0);
		assertTrue("Error: ChartUtil return invalid chartDataPoint tag.",
				pieChartXML.indexOf(PIE_CHART_DATA_POINT_XML) > 0);
	}

}
