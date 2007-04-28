package edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.test;

import java.util.List;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;

public class ProductTest extends HyadesTestCase {

	private static final int mProdID = 1;

	private static final String mProdName = "TPTeam Dev";

	private static final String mTempProdName = "TPTeam Temp Prod";

	private static final String mTempProdDesc = "TPTeam Temp Prod Desc";

	/**
	 * Constructor for ProductTest.
	 * 
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
				"F56EDC6043A497F2642A1EE0F20211DB"));
	
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
	 * 
	 * @throws Exception
	 */
	public void testAddProduct() throws Exception {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			Product prod = new Product();
			prod.setName(mTempProdName);
			prod.setDescription(mTempProdDesc);
			Integer id = (Integer) s.save(prod);
			Product newProd = (Product) s.load(Product.class, id);
			assertTrue(
					"testAddProduct Error: failed to get new Product with name "
							+ mTempProdName, newProd.getName().equals(
							mTempProdName));
			assertTrue(
					"testAddProduct Error: failed to get new Product with description "
							+ mTempProdName, newProd.getDescription().equals(
							mTempProdDesc));
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	/**
	 * testUpdateProduct
	 * 
	 * @throws Exception
	 */
	public void testUpdateProduct() throws Exception {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			Query query = s.createQuery("from Product as p where p.name = ?");
			query.setString(0, mTempProdName);
			List<Product> prods = query.list();
			assertTrue(
					"testUpdateProduct Error: failed to return temp product",
					prods.size() == 1);

			Product prod = prods.get(0);
			prod.setName(mTempProdName + "_update");
			prod.setDescription(mTempProdDesc + "_update");

			Integer id = (Integer) s.save(prod);
			Product updatedProd = (Product) s.load(Product.class, id);
			assertTrue(
					"testUpdateProduct Error: failed to update Product with name "
							+ mTempProdName + "_update", updatedProd.getName()
							.equals(mTempProdName + "_update"));
			assertTrue(
					"testUpdateProduct Error: failed to get new Product with description "
							+ mTempProdName, updatedProd.getDescription()
							.equals(mTempProdDesc + "_update"));
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	/**
	 * testDeleteProduct
	 * 
	 * @throws Exception
	 */
	public void testDeleteProduct() throws Exception {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			Query query = s.createQuery("from Product as p where p.name = ?");
			query.setString(0, mTempProdName + "_update");
			List<Product> prods = query.list();
			assertTrue(
					"testDeleteProduct Error: failed to return temp product",
					prods.size() == 1);

			Product prod = prods.get(0);
			s.delete(prod);
			s.flush();

			prods = query.list();
			assertTrue(
					"testDeleteProduct Error: failed to delete temp product",
					prods.size() == 0);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	/**
	 * testSelectProduct
	 * 
	 * @throws Exception
	 */
	public void testSelectProduct() throws Exception {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();

			List<Product> prods = s.createQuery(
					"from Product as p order by p.name asc").list();
			assertTrue(
					"testSelectProduct Error: No products selected from test database",
					prods.size() > 0);

			Product prod = (Product) s
					.load(Product.class, new Integer(mProdID));
			assertNotNull(
					"testSelectProduct Error: Null Product returned for product ID "
							+ mProdID, prod);
			assertTrue(
					"testSelectProduct Error: Selected Product returned invalid ID "
							+ prod.getId() + " when ID " + mProdID
							+ " expected", prod.getId() == mProdID);
			assertTrue(
					"testSelectProduct Error: Selected Product returned invalid name "
							+ prod.getName() + " when name \"" + mProdName
							+ "\" expected", prod.getName().equalsIgnoreCase(
							mProdName));
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

}