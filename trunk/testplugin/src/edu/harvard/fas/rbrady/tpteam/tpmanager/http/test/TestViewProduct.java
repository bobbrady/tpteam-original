package edu.harvard.fas.rbrady.tpteam.tpmanager.http.test;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.view.ViewProduct;

/**
 * Generated code for the test suite <b>TestViewProduct</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpmanager/http/test/TestViewProduct.testsuite</i>.
 */
public class TestViewProduct extends HyadesTestCase {

	/** Product name in test database */
	public static final String PRODUCT_NAME = "TPTeam Dev";

	/**
	 * Constructor for TestViewProduct.
	 * 
	 * @param name
	 */
	public TestViewProduct(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestViewProduct</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testViewProduct = new HyadesTestSuite("TestViewProduct");
		testViewProduct.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"C39EC0FAEC78CFF51B14AEF0F92811DB");

		testViewProduct.addTest(new TestViewProduct("testGetProdRows").setId(
				"C39EC0FAEC78CFF51FF21B10F92811DB").setTestInvocationId(
				"C39EC0FAEC78CFF5286446B0F92811DB"));
		return testViewProduct;
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
	 * testGetProdRows
	 * 
	 * @throws Exception
	 */
	public void testGetProdRows() throws Exception {
		ViewProduct viewProduct = new ViewProduct();
		// Get products in html table row format from servlet view
		String viewProductRows = viewProduct.getProdRows();
		// Compare to what's in test database
		assertTrue(viewProductRows.trim().equals(
				"<tr><td>" + PRODUCT_NAME + "</td><td></td></tr>"));
	}

}
