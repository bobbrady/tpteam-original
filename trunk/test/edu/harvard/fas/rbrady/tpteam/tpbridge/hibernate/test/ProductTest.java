package edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.test;

import java.util.List;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;

/**
 * Generated code for the test suite <b>ProductTest</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.test/edu/harvard/fas/rbrady/tpteam/tpmanager/hibernate/test/ProductTest.testsuite</i>.
 */
public class ProductTest extends HyadesTestCase {
	
	private static final int mProdID = 1;
	
	private static final String mProdName = "TPTeam Dev";
	
	/**
	 * Constructor for ProductTest.
	 * @param name
	 */
	public ProductTest(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>ProductTest</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite productTest = new HyadesTestSuite("ProductTest");
		productTest.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"E090CCFE7CE9A39D37C68F00A66C11DB");
	
		productTest.addTest(new ProductTest("testAddProduct").setId(
				"E090CCFE7CE9A39D50BE7720A66C11DB").setTestInvocationId(
				"E090CCFE7CE9A39D9352AF70A66C11DB"));
	
		productTest.addTest(new ProductTest("testUpdateProduct").setId(
				"E090CCFE7CE9A39D50FBA730A66C11DB").setTestInvocationId(
				"E090CCFE7CE9A39D935EE470A66C11DB"));
	
		productTest.addTest(new ProductTest("testDeleteProduct").setId(
				"E090CCFE7CE9A39D513ECAB0A66C11DB").setTestInvocationId(
				"E090CCFE7CE9A39D93637850A66C11DB"));
	
		productTest.addTest(new ProductTest("testSelectProduct").setId(
				"FDEFD17E4B7BA8088171E9E0A8A811DB").setTestInvocationId(
				"FDEFD17E4B7BA80899C8D710A8A811DB"));
		return productTest;
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
	* testAddProduct
	* @throws Exception
	*/
	public void testAddProduct()
	throws Exception
	{
	// Enter your code here
	}

	/**
	* testUpdateProduct
	* @throws Exception
	*/
	public void testUpdateProduct()
	throws Exception
	{
	// Enter your code here
	}

	/**
	* testDeleteProduct
	* @throws Exception
	*/
	public void testDeleteProduct()
	throws Exception
	{
	// Enter your code here
	}

	/**
	* testSelectProduct
	* @throws Exception
	*/
	public void testSelectProduct()
	throws Exception
	{
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();

			List<Product> prods = s.createQuery(
					"from Product as p order by p.name asc").list();
			assertTrue("testSelectProduct Error: No products selected from test database", prods.size() > 0);
			
			Product prod = (Product)s.load(Product.class, new Integer(mProdID));
			assertNotNull("testSelectProduct Error: Null Product returned for product ID " + mProdID, prod);
			assertTrue("testSelectProduct Error: Selected Product returned invalid ID " + prod.getId() + " when ID " + mProdID + " expected", prod.getId() == mProdID);
			assertTrue("testSelectProduct Error: Selected Product returned invalid name " + prod.getName() + " when name \"" + mProdName + "\" expected", prod.getName().equalsIgnoreCase(mProdName));
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

}
