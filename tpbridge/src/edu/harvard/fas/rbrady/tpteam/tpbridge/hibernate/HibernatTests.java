package edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernatTests {

	public static void main(String[] args) {
		try {
			// insertProd();
			// deleteProd(1);
			// addUserToProj(insertProj());
			// insertProj();
			// getProd(0);
			// getTpteamUser(1);
			// insertTest();
			// printTree();
			// insertUser();
			// getProjOptions();
			// updateProd();
			// getProject("21");
			// deleteUser(81);
			// updateProj(1);
			// deleteTest(33);
			// insertTestExec(62);
			// deleteProj(41);
			// getTPEventSendTo(62);
			// getProjByUser(101);
			// Test test = getInitTopLevelTests(1);
			// printTest(test);
			// getPieChartData(1);
			//getBarChartData(1);
			//getLineChartData(1);
			getMySQLLineChartData(1);
			HibernateUtil.getSessionFactory().close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static Integer insertTestExec(int testId) throws Exception {
		System.out.println("Inserting TestExec");
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		Integer id = null;
		String ecfID = "tpteam_2@jabber.org";
		try {

			tx = s.beginTransaction();
			String hql = "from TpteamUser as user where user.ecfId =:ecfID";
			Query query = s.createQuery(hql);
			query.setString("ecfID", ecfID);
			List users = query.list();
			Test test = (Test) s.load(Test.class, new Integer(testId));
			TestExecution testExec = new TestExecution();
			testExec.setTest(test);
			testExec.setStatus('I');
			testExec.setExecDate(new Date());
			testExec.setTpteamUser((TpteamUser) users.get(0));
			s.save(testExec);
			System.out.println("testExec ID: " + testExec.getId());
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return id;
	}

	public static Integer insertUser() throws Exception {
		System.out.println("Inserting User");
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		Integer id = null;
		try {

			tx = s.beginTransaction();
			TpteamUser user = new TpteamUser();
			user.setFirstName("TPTeamFName");
			user.setLastName("TPTeamLName");
			user.setUserName("tpteam_test");
			user.setPassword("tpteam_pass");
			user.setEmail("tpteam@test.com");
			user.setPhone("617-555-1212");
			user.setEcfId("tpteam@jabber.org");

			Project proj1 = (Project) s.load(Project.class, new Integer("1"));
			Project proj21 = (Project) s.load(Project.class, new Integer("21"));
			user.getProjects().add(proj1);
			user.getProjects().add(proj21);

			Role role = (Role) s.load(Role.class, new Integer("1"));
			user.setRole(role);

			proj1.addToTpteamUsers(user);
			proj21.addToTpteamUsers(user);

			s.save(user);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	public static void insertProd() throws Exception {
		System.out.println("Inserting Prod");
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		// factory = new Configuration().configure().buildSessionFactory();
		// Session s = factory.openSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();

			Product prod = new Product();
			// prod.setId(1);
			prod.setName("FooBar Product");
			prod.setDescription("FooBar Description");
			Integer id = (Integer) s.save(prod);
			System.out.println("Insert Done " + id);
			Product updatedProd = (Product) s.load(Product.class, id);
			updatedProd.setName("FooBar Product updated");
			s.save(updatedProd);
			System.out.println("Update Done");

			List<Product> prods = s.createQuery(
					"from Product as p order by p.name asc").list();
			System.out.println(prods.size() + " prod(s) found:");
			for (Product myProd : prods) {
				System.out.println("Found Prod: " + myProd.getName());
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	public static void updateProd() throws Exception {
		System.out.println("Updating Prod");
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			Product prod = (Product) s.load(Product.class, new Integer("21"));
			prod.setName("FooBar Product");
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	public static void updateTest(int testID, Test testStub) throws Exception {
		System.out.println("Updating Test w/ID " + testID);
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, testID);
			test.setName(testStub.getName());
			test.setDescription(testStub.getDescription());
			if (test.getIsFolder() == 'N' && test.getJunitTests() != null
					&& test.getJunitTests().size() > 0) {
				if (testStub.getJunitTests() != null
						&& testStub.getJunitTests().size() > 0) {
					JunitTest junit = ((JunitTest[]) test.getJunitTests()
							.toArray(new JunitTest[0]))[0];
					JunitTest junitStub = ((JunitTest[]) testStub
							.getJunitTests().toArray(new JunitTest[0]))[0];
					junit.setEclipseHome(junitStub.getEclipseHome());
					junit.setWorkspace(junitStub.getWorkspace());
					junit.setProject(junitStub.getProject());
					junit.setTestSuite(junitStub.getTestSuite());
					junit.setReportDir(junitStub.getReportDir());
					junit.setTptpConnection(junitStub.getTptpConnection());
				}
			}
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public static void getProd(int id) throws Exception {
		System.out.println("Getting Prod");
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();

			List<Product> prods = s.createQuery(
					"from Product as p order by p.name asc").list();
			System.out.println(prods.size() + " prod(s) found:");
			for (Product myProd : prods) {
				System.out.println("Found Prod: " + myProd.getName());
				for (Project proj : myProd.getProjects()) {
					System.out.println("\tProj: " + proj.getName());
				}
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}


	@SuppressWarnings("unchecked")
	public static void getTpteamUser(int id) throws Exception {
		System.out.println("Getting TpTeamUser");
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		List<TpteamUser> users = null;
		String teamOptions = "";
		try {

			tx = s.beginTransaction();

			users = s.createQuery(
					"from TpteamUser as team order by team.lastName asc")
					.list();
			for (TpteamUser user : users) {
				teamOptions += "<option value=\"" + user.getId() + "\">"
						+ user.getLastName() + ", " + user.getFirstName()
						+ "</option>\n";
			}

			System.out.println(teamOptions);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}

	}

	public static void deleteUser(int id) throws Exception {
		System.out.println("Deleting User with ID: " + id);
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		// factory = new Configuration().configure().buildSessionFactory();
		// Session s = factory.openSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			TpteamUser user = (TpteamUser) s.load(TpteamUser.class,
					new Integer(id));
			for (Project proj : user.getProjects()) {
				proj.getTpteamUsers().remove(user);
			}
			s.delete(user);
			s.flush();
			System.out.println("Done\n");
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	public static void deleteTest(int id) throws Exception {
		System.out.println("Deleting Test with ID: " + id);
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		// factory = new Configuration().configure().buildSessionFactory();
		// Session s = factory.openSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			Test test = (Test) s.load(Test.class, new Integer(id));
			/*
			 * for(TestExecution testExec : test.getTestExecutions()) {
			 * s.delete(testExec); } for(JunitTest junit : test.getJunitTests()) {
			 * s.delete(junit); }
			 */
			s.delete(test);
			s.flush();
			System.out.println("Done\n");
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	public static void deleteProj(int id) throws Exception {
		System.out.println("Deleting Proj with ID: " + id);
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		// factory = new Configuration().configure().buildSessionFactory();
		// Session s = factory.openSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			Project proj = (Project) s.load(Project.class, new Integer(id));
			/*
			 * for(TestExecution testExec : test.getTestExecutions()) {
			 * s.delete(testExec); } for(JunitTest junit : test.getJunitTests()) {
			 * s.delete(junit); }
			 */
			proj.getTpteamUsers().removeAll(proj.getTpteamUsers());
			s.delete(proj);
			s.flush();
			System.out.println("Done\n");
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	public static void updateProj(int projId) throws Exception {
		System.out.println("Updating proj with ID: " + projId);
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		// factory = new Configuration().configure().buildSessionFactory();
		// Session s = factory.openSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			Project proj = (Project) s.load(Project.class, new Integer(projId));
			proj.setName("TPTeam Demo Proj");
			proj.setDescription("Demo Desc");
			Product prod = (Product) s.load(Product.class, new Integer("1"));
			proj.setProduct(prod);

			// String[] userIds = new String[]{"1"};
			proj.getTpteamUsers().removeAll(proj.getTpteamUsers());
			String[] userIds = new String[0];

			for (String userId : userIds) {
				TpteamUser tpTeamUser = (TpteamUser) s.load(TpteamUser.class,
						new Integer(userId));
				// Project is not the inverse=true, so use it to associate
				// proj_user map table
				proj.addToTpteamUsers(tpTeamUser);
			}
			s.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	public static void deleteProd(int id) throws Exception {
		System.out.println("Deleting Prod with ID: " + id);
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		// factory = new Configuration().configure().buildSessionFactory();
		// Session s = factory.openSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			Object prod = s.load(Product.class, id);
			s.delete(prod);
			System.out.println("Done");
			System.out.println();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			s.close();
		}
	}

	public static Integer insertProj() throws Exception {
		System.out.println("Inserting Proj");
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		Integer id = null;
		try {

			tx = s.beginTransaction();
			Project proj = new Project();
			proj.setName("Test Project");
			proj.setDescription("Test Project Description");
			Product prod = (Product) s.load(Product.class, new Integer("106"));
			proj.setProduct(prod);
			TpteamUser tpTeamUser = (TpteamUser) s.load(TpteamUser.class,
					new Integer("1"));
			// Project is not the inverse=true, so use it to associate proj_user
			// map table
			proj.addToTpteamUsers(tpTeamUser);
			s.save(proj);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}

		return id;
	}

	public static Integer insertTest() throws Exception {
		System.out.println("Inserting Test");
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		Integer id = null;
		try {

			tx = s.beginTransaction();
			// First create basic test entity
			Test test = new Test();
			test.setName("Test Name");
			test.setDescription("Test Description");
			Test parent = (Test) s.load(Test.class, new Integer("33"));
			test.setParent(parent);
			test.setIsFolder('Y');
			// TestType testType = (TestType)s.load(TestType.class, new
			// Integer("1"));
			// test.setTestType(testType);
			Project proj = (Project) s.load(Project.class, new Integer("1"));
			test.setProject(proj);
			Integer testID = (Integer) s.save(test);
			test.setPath("21.26.33." + testID);
			s.saveOrUpdate(test);
			/*
			 * // Next, create JUnit entity, pointing to parent test JunitTest
			 * junit = new JunitTest(); junit.setId(testID);
			 * junit.setTest(test); junit.setEclipseHome("eclipseHome");
			 * junit.setProject("eclipseProject");
			 * junit.setWorkspace("workspace"); junit.setReportDir("reportDir");
			 * junit.setTptpConnection("tptpConnection");
			 * junit.setTestSuite("test.testsuite"); s.save(junit);
			 */
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	public static void printTree() throws Exception {
		System.out.println("Printing Test Tree");
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
	
			List<Test> tests = s
					.createQuery(
							"from Test as test where test.parent is null order by test.path")
					.list();

			printTree(tests);

			tx.commit();

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}

	}

	public static int getDepth(String path) {
		String pathNoDots = path.replaceAll("\\.", "");
		return path.length() - pathNoDots.length();
	}

	public static void printTree(List<Test> tests) {
		Stack<Test> nodes = new Stack<Test>();
		nodes.addAll(tests);
		// Initialize node level to top level
		int currLevel = 0;

		/**
		 * While node stack is not empty, add header String, push child nodes
		 * repeat
		 */
		while (!nodes.empty()) {
			Test test = nodes.pop();

			/**
			 * If depth in tree decreased, print closing </SPAN> tag for
			 * previous parent
			 */
			int depth = getDepth(test.getPath());
			if (depth < currLevel)
				printNodeFooter(depth, currLevel);
			currLevel = depth;

			System.out.println(getNodeHeader(test, depth));

			// push children onto stack
			if (test.getIsFolder() == 'Y' && test.getChildren().size() > 0)
				nodes.addAll(test.getChildren());
			else if (test.getIsFolder() == 'Y')
				printNodeFooter(depth);
		}
		// Add final closing </SPAN> tag
		printNodeFooter(0);
	}

	private static String getNodeHeader(Test test, int depth) {
		String header = "";
		String testId = String.valueOf(test.getId());
		String testName = test.getName();
		if (test.getIsFolder() == 'Y') {
			header += getPad(depth);
			header += "<div class=\"trigger\" onClick=\"showBranch('branch_"
					+ testId + "');swapFolder('folder_" + testId
					+ "');makeBold('" + testId + "');\" id=\"" + testId
					+ "\">\n";
			header += "<img src=\"closed.gif\" border=\"0\" id=\"folder_"
					+ testId + "\">\n";
			header += test.getName() + "\n";
			header += "</div>\n";
			header += "<span class=\"branch\" id=\"branch_" + testId + "\">\n";
		} else {
			header += getPad(depth) + "<img src=\"doc.gif\"> "
					+ "<a href=\"#\" id=\"" + testId
					+ "\" onClick=\"makeBold('" + testId
					+ "'); return false;\">" + testName + "</a><br>\n";
		}
		return header;
	}

	private static void printNodeFooter(int depth, int currLevel) {
		while (currLevel > depth)
			System.out.println(getPad(--currLevel) + "</SPAN>");
	}

	private static void printNodeFooter(int depth) {
		System.out.println(getPad(depth) + "</SPAN>");
	}

	private static String getPad(int depth) {
		String pad = "";
		for (int idx = 0; idx < depth; idx++)
			pad += "\t";
		return pad;
	}


	@SuppressWarnings("unchecked")
	public static Test getInitTopLevelTests(int projID) throws Exception {
		Test root = new Test();
		List<Test> tests = null;
		Session s = null;
		Transaction tx = null;
		try {
			s = HibernateUtil.getSessionFactory().getCurrentSession();
			tx = s.beginTransaction();

			String hql = "from Test as test where test.parent is null and test.project.id =:projID";
			Query query = s.createQuery(hql);
			query.setString("projID", String.valueOf(projID));
			tests = query.list();

			// Initialize fields of interest
			for (Test test : tests) {
				test.initSkeleton();
			}
			root.setName("root");
			root.setId(0);
			root.setChildren(new HashSet<Test>(tests));
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return root;
	}

	public static void printTest(Test test) {
		System.out.println("Test Name: " + test.getName() + "id: "
				+ test.getId());
		for (Test child : test.getChildren())
			printTest(child);
	}

	@SuppressWarnings("unchecked")
	public static void getPieChartData(int id) throws Exception {
		System.out.println("Gettign PieChartData w/Proj ID: " + id);
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();
			/*
			 * String SQL_QUERY = "SELECT nvl(status, 'NA'), count(*) FROM
			 * test_execution te, test t " + "WHERE (t.proj_id = 1 AND
			 * te.test_id(+) = t.id ) AND t.is_folder = 'N' " + "AND
			 * (te.exec_date = " + " (SELECT max(te2.exec_date) from
			 * test_execution te2 where te2.test_id = te.test_id) " + " or
			 * te.exec_date is NULL) GROUP BY status";
			 */

			/*
			 * String SQL_QUERY = "SELECT nvl(te.status, 'NA'), count(*) FROM
			 * TestExecution te, Test t " + " WHERE (t.project.id = 1 AND
			 * te.test.id = t.id ) AND t.isFolder = 'N' " + " AND (te.execDate = " + "
			 * (SELECT max(te2.execDate) from TestExecution te2 where
			 * te2.test.id = te.test.id) " + " or te.execDate is NULL) GROUp BY
			 * te.status";
			 */
			String SQL_QUERY = "SELECT te.status, count(*) FROM TestExecution as te "
					+ " join te.test as t "
					+ " WHERE t.project.id = ? AND t.isFolder = 'N' "
					+ " AND (te.execDate = "
					+ " (SELECT max(te2.execDate) from TestExecution te2 where te2.test.id = te.test.id) "
					+ " or te.execDate is NULL) GROUP BY te.status";

			Query query = s.createQuery(SQL_QUERY);
			query.setInteger(0, id);

			for (Iterator it = query.iterate(); it.hasNext();) {
				Object[] row = (Object[]) it.next();
				System.out.println("Status: " + row[0]);
				System.out.println("Count: " + row[1]);
			}

			SQL_QUERY = "SELECT count(*) FROM Test as t "
					+ " WHERE t.project.id = ? AND t.isFolder = 'N' ";
			query = s.createQuery(SQL_QUERY);
			query.setInteger(0, id);

			for (Iterator it = query.iterate(); it.hasNext();) {
				Object row = (Object) it.next();
				System.out.println("Total Num Tests: " + row);
			}

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public static void getBarChartData(int id) throws Exception {
		System.out.println("Getting BarChartData w/Proj ID: " + id);
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {

			tx = s.beginTransaction();

			Project proj = (Project) s.load(Project.class, id);
			for (TpteamUser user : proj.getTpteamUsers()) {
				System.out.println("user: " + user.getUserName());
				String SQL_QUERY = "SELECT te.status, count(*) FROM TestExecution as te "
						+ " join te.test as t "
						+ " WHERE t.project.id = ? AND t.createdBy.id = ? AND t.isFolder = 'N' "
						+ " AND (te.execDate = "
						+ " (SELECT max(te2.execDate) from TestExecution te2 where te2.test.id = te.test.id) "
						+ " or te.execDate is NULL) GROUP BY te.status";

				Query query = s.createQuery(SQL_QUERY);
				query.setInteger(0, id);
				query.setInteger(1, user.getId());

				for (Iterator it = query.iterate(); it.hasNext();) {
					Object[] row = (Object[]) it.next();
					System.out.println("Status: " + row[0]);
					System.out.println("Count: " + row[1]);
				}

			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw e;
		}
	}
		
		@SuppressWarnings("unchecked")
		public static void getLineChartData(int id) throws Exception {
			System.out.println("Getting LineChartData w/Proj ID: " + id);
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tx = null;
			try {

				tx = s.beginTransaction();

				for (int idx = 0; idx < 30; idx++) {
	
					String SQL_QUERY = "SELECT te.status, count(*) FROM TestExecution as te "
							+ " join te.test as t "
							+ " WHERE t.project.id = ? AND t.isFolder = 'N' "
							+ " AND te.execDate = "
							+ " (SELECT max(te2.execDate) FROM TestExecution te2 WHERE te2.test.id = te.test.id " +
									" AND te2.execDate < now() - ? )"
							+ " GROUP BY te.status";

					Query query = s.createQuery(SQL_QUERY);
					query.setInteger(0, id);
					query.setInteger(1, idx);

					for (Iterator it = query.iterate(); it.hasNext();) {
						Object[] row = (Object[]) it.next();
						System.out.println("Status: " + row[0]);
						System.out.println("Count: " + row[1]);
					}
					
				SQL_QUERY = "SELECT count(*) FROM Test as t "
						+ " WHERE t.project.id = ? AND t.isFolder = 'N' " +
								"AND t.createdDate < now() - ?";
				query = s.createQuery(SQL_QUERY);
				query.setInteger(0, id);
				query.setInteger(1, idx);

				for (Iterator it = query.iterate(); it.hasNext();) {
					Object row = (Object) it.next();
					System.out.println("TotalTests: " + row);
				}


				}
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (tx != null)
					tx.rollback();
				throw e;
			}

	}
		
		public static void getMySQLLineChartData(int id) throws Exception {
			System.out.println("Getting LineChartData w/Proj ID: " + id);
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tx = null;
			try {

				tx = s.beginTransaction();

				for (int idx = 0; idx < 30; idx++) {
	
					
					String SQL_QUERY = "SELECT te.status, count(*) FROM Test_Execution AS te "
							+ " INNER JOIN Test as t on te.test_id =  t.id "
							+ " WHERE t.proj_id = ? AND t.is_Folder = 'N' "
							+ " AND te.exec_Date = "
							+ " (SELECT max(te2.exec_Date) FROM Test_Execution te2 WHERE te2.test_id = te.test_id " +
									" AND te2.exec_Date < date_sub(now(), INTERVAL ? day))"
							+ " GROUP BY te.status";

					Query query = s.createSQLQuery(SQL_QUERY);
					query.setInteger(0, id);
					query.setInteger(1, idx);

					
					for (Object row : query.list()) {
						Object[] cols = (Object[])row;
						System.out.println("Status: " + cols[0]);
						System.out.println("Count: " + cols[1]);
					}
					
					
				SQL_QUERY = "SELECT count(*) FROM Test as t "
						+ " WHERE t.proj_id = ? AND t.is_Folder = 'N' " +
								"AND t.created_Date < date_sub(now(), INTERVAL ? day)";
				
				query = s.createSQLQuery(SQL_QUERY);
				query.setInteger(0, id);
				query.setInteger(1, idx);
				
				
				
				for (Object row : query.list()) {
					Object col = (Object)row;
					System.out.println("TotalTests: " + col);
				}
				


				}
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (tx != null)
					tx.rollback();
				throw e;
			}

	}


}
