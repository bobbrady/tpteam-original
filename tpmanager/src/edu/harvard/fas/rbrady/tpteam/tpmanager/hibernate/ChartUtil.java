/********************************************************************
 * 
 * File		:	ChartUtil.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A TPTeam project chart utility class. 
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataPoint;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.ChartDataSetXML;
import edu.harvard.fas.rbrady.tpteam.tpmanager.Activator;

/*******************************************************************************
 * File 		: 	ChartUtil.java
 * 
 * Description 	: 	A TPTeam project chart utility class.  Contains methods for
 * 					creating data points from Hibernate database calls.  Serializes 
 * 					charts into XML Strings.
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ChartUtil {

	/** Number of history days to display on line charts */
	public static final int NUM_LINECHART_DAYS = 30;

	/**
	 * Gets a data point for a single pie chart.  The data point
	 * contains the number of test execution successes, failures,
	 * errors, inconclusive, and not-executed results for a given 
	 * project.
	 * 
	 * @param tpEvent the TPEvent containing project metadata
	 * @return the ChartDataPoint
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static ChartDataPoint getPieChartDataPoint(TPEvent tpEvent)
			throws Exception {
		Session s = null;
		Transaction tx = null;
		int projID = Integer.parseInt(tpEvent.getDictionary().get(
				TPEvent.PROJECT_ID_KEY));
		ChartDataPoint dataPoint = new ChartDataPoint();
		try {

			if (Activator.getDefault() != null) {
				s = Activator.getDefault().getHiberSessionFactory()
						.getCurrentSession();
			} else {
				s = HibernateUtil.getSessionFactory().getCurrentSession();
			}

			tx = s.beginTransaction();

			String SQL_QUERY = "SELECT te.status, count(*) FROM TestExecution as te "
					+ " join te.test as t "
					+ " WHERE t.project.id = ? AND t.isFolder = 'N' "
					+ " AND (te.execDate = "
					+ " (SELECT max(te2.execDate) from TestExecution te2 where te2.test.id = te.test.id) "
					+ " or te.execDate is NULL) GROUP BY te.status";

			Query query = s.createQuery(SQL_QUERY);
			query.setInteger(0, projID);

			int sum = 0;
			for (Iterator it = query.iterate(); it.hasNext();) {
				Object[] row = (Object[]) it.next();
				Character status = (Character) row[0];
				Long count = (Long) row[1];

				if (status == 'P')
					dataPoint.setPass(count.intValue());
				else if (status == 'F')
					dataPoint.setFail(count.intValue());
				else if (status == 'E')
					dataPoint.setError(count.intValue());
				else if (status == 'I')
					dataPoint.setInconcl(count.intValue());
				sum += count.intValue();
			}

			SQL_QUERY = "SELECT count(*) FROM Test as t "
					+ " WHERE t.project.id = ? AND t.isFolder = 'N' ";
			query = s.createQuery(SQL_QUERY);
			query.setInteger(0, projID);

			for (Iterator it = query.iterate(); it.hasNext();) {
				Long totalTests = (Long) it.next();
				int notExec = totalTests.intValue() - sum;
				dataPoint.setNotExec(notExec);
			}

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw e;
		}

		return dataPoint;
	}

	/**
	 * Gets the String XML serialization for a pie chart 
	 * 
	 * @param tpEvent the TPEvent holding the project metadata
	 * @return the XML serialization String
	 * @throws Exception
	 */
	public static String getPieChartXML(TPEvent tpEvent) throws Exception {
		ChartDataPoint dataPoint = getPieChartDataPoint(tpEvent);
		ChartDataSet dataSet = new ChartDataSet();
		TpteamUser user = new TpteamUser();
		user.setFirstName("root");
		user.setLastName("root");
		dataSet.setUser(user);
		dataSet.setProjName(tpEvent.getDictionary().get(TPEvent.PROJECT_KEY));
		dataSet.addChartDataPoint(dataPoint);
		dataSet.setType(ChartDataSet.PIE);
		return ChartDataSetXML.getXML(dataSet);
	}

	/**
	 * Gets the String XML serialization for a bar chart 
	 * 
	 * @param tpEvent the TPEvent holding the project metadata
	 * @return the XML serialization String
	 * @throws Exception
	 */
	public static String getBarChartXML(TPEvent tpEvent) throws Exception {
		List<ChartDataSet> dataSetList = new ArrayList<ChartDataSet>();
		int projID = Integer.parseInt(tpEvent.getDictionary().get(
				TPEvent.PROJECT_ID_KEY));
		String projName = tpEvent.getDictionary().get(TPEvent.PROJECT_KEY);
		TpteamUser[] projUsers = getProjUsers(projID);

		for (TpteamUser projUser : projUsers) {
			ChartDataSet dataSet = new ChartDataSet();
			dataSet.setUser(projUser);
			dataSet.setProjName(projName);
			dataSet.setType(ChartDataSet.BAR);
			ChartDataPoint dataPoint = getBarChartDataPoint(projID, projUser
					.getId());
			dataSet.addChartDataPoint(dataPoint);
			dataSetList.add(dataSet);
		}

		return ChartDataSetXML.getListXML(dataSetList);
	}

	/**
	 * Gets a bar chart data point for a particular user.  
	 * The data point contains the number of test execution 
	 * successes, failures, errors, inconclusive, and 
	 * not-executed results associated with the TPTeam user 
	 * of a given project.
	 * 
	 * @param projID the TPTeam ID of the project
	 * @param userID the TPteam ID of the user
	 * @return the ChartDataPoint
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static ChartDataPoint getBarChartDataPoint(int projID, int userID)
			throws Exception {
		Session s = null;
		Transaction tx = null;
		ChartDataPoint dataPoint = new ChartDataPoint();
		try {

			if (Activator.getDefault() != null) {
				s = Activator.getDefault().getHiberSessionFactory()
						.getCurrentSession();
			} else {
				s = HibernateUtil.getSessionFactory().getCurrentSession();
			}

			tx = s.beginTransaction();

			String SQL_QUERY = "SELECT te.status, count(*) FROM TestExecution as te "
					+ " join te.test as t "
					+ " WHERE t.project.id = ? AND t.createdBy = ? AND t.isFolder = 'N' "
					+ " AND (te.execDate = "
					+ " (SELECT max(te2.execDate) from TestExecution te2 where te2.test.id = te.test.id) "
					+ " or te.execDate is NULL) GROUP BY te.status";

			Query query = s.createQuery(SQL_QUERY);
			query.setInteger(0, projID);
			query.setInteger(1, userID);

			int sum = 0;
			for (Iterator it = query.iterate(); it.hasNext();) {
				Object[] row = (Object[]) it.next();
				Character status = (Character) row[0];
				Long count = (Long) row[1];

				if (status == 'P')
					dataPoint.setPass(count.intValue());
				else if (status == 'F')
					dataPoint.setFail(count.intValue());
				else if (status == 'E')
					dataPoint.setError(count.intValue());
				else if (status == 'I')
					dataPoint.setInconcl(count.intValue());
				sum += count.intValue();
			}

			SQL_QUERY = "SELECT count(*) FROM Test as t "
					+ " WHERE t.project.id = ? AND t.createdBy = ? AND t.isFolder = 'N' ";
			query = s.createQuery(SQL_QUERY);
			query.setInteger(0, projID);
			query.setInteger(1, userID);

			for (Iterator it = query.iterate(); it.hasNext();) {
				Long totalTests = (Long) it.next();
				int notExec = totalTests.intValue() - sum;
				dataPoint.setNotExec(notExec);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return dataPoint;
	}

	/**
	 * Helper method to return the TPTeam users associated
	 * with the given project.
	 * 
	 * @param projID the ID of the project
	 * @return an array of the associated TPTeam users
	 * @throws Exception
	 */
	public static TpteamUser[] getProjUsers(int projID) throws Exception {
		List<TpteamUser> projUsers = new ArrayList<TpteamUser>();
		Session s = null;
		// Use plugin activator if in OSGi runtime
		if (Activator.getDefault() != null) {
			s = Activator.getDefault().getHiberSessionFactory()
					.getCurrentSession();
		} else {
			s = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			Project proj = (Project) s.load(Project.class, projID);
			for (TpteamUser user : proj.getTpteamUsers()) {
				TpteamUser projUser = new TpteamUser();
				projUser.setId(user.getId());
				projUser.setLastName(user.getLastName());
				projUser.setFirstName(user.getFirstName());
				projUser.setEcfId(user.getEcfId());
				projUsers.add(projUser);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return (TpteamUser[]) projUsers
				.toArray(new TpteamUser[projUsers.size()]);
	}

	/**
	 * Gets the String XML serialization for a line chart 
	 * 
	 * @param tpEvent the TPEvent holding the project metadata
	 * @return the XML serialization String
	 * @throws Exception
	 */
	public static String getLineChartXML(TPEvent tpEvent) throws Exception {
		int projID = Integer.parseInt(tpEvent.getDictionary().get(
				TPEvent.PROJECT_ID_KEY));
		String projName = tpEvent.getDictionary().get(TPEvent.PROJECT_KEY);

		ChartDataSet dataSet = new ChartDataSet();
		dataSet.setProjName(projName);
		dataSet.setType(ChartDataSet.LINE);

		Calendar cal = Calendar.getInstance();
		Date today = new Date();
		for (int idx = 0; idx < NUM_LINECHART_DAYS; idx++) {
			cal.setTime(today);
			ChartDataPoint dataPoint = getLineChartDataPoint(projID, idx, cal);
			dataSet.addChartDataPoint(dataPoint);
		}

		return ChartDataSetXML.getXML(dataSet);
	}

	/**
	 * Gets the line chart data point.  
	 * The data point contains the number of test execution 
	 * successes, failures, errors, inconclusive, and 
	 * not-executed results associated with a given project
	 * for a particular number of days.
	 * 
	 * @param projID the TPTeam ID of the project
	 * @param daysPrev the number of previous days to include
	 * @param Calendar a calendar to help with date math
	 * @return the ChartDataPoint
	 * @throws Exception
	 */
	private static ChartDataPoint getLineChartDataPoint(int projID,
			int daysPrev, Calendar cal) throws Exception {
		Session s = null;
		Transaction tx = null;
		ChartDataPoint dataPoint = new ChartDataPoint();
		try {

			if (Activator.getDefault() != null) {
				s = Activator.getDefault().getHiberSessionFactory()
						.getCurrentSession();
			} else {
				s = HibernateUtil.getSessionFactory().getCurrentSession();
			}

			tx = s.beginTransaction();

			/*******************************************************************
			 * Oracle Legacy Code
			 * ************************************************************************
			 * String SQL_QUERY = "SELECT te.status, count(*) FROM TestExecution
			 * as te " + " join te.test as t " + " WHERE t.project.id = ? AND
			 * t.isFolder = 'N' " + " AND te.execDate = " + " (SELECT
			 * max(te2.execDate) from TestExecution te2 where te2.test.id =
			 * te.test.id " + "AND te2.execDate <= sysdate - ?) " + " GROUP BY
			 * te.status";
			 ******************************************************************/

			// MySQL Native SQL
			String SQL_QUERY = "SELECT te.status, count(*) FROM Test_Execution AS te "
					+ " INNER JOIN Test as t on te.test_id =  t.id "
					+ " WHERE t.proj_id = ? AND t.is_Folder = 'N' "
					+ " AND te.exec_Date = "
					+ " (SELECT max(te2.exec_Date) FROM Test_Execution te2 WHERE te2.test_id = te.test_id "
					+ " AND te2.exec_Date < date_sub(now(), INTERVAL ? day))"
					+ " GROUP BY te.status";

			Query query = s.createSQLQuery(SQL_QUERY);
			query.setInteger(0, projID);
			query.setInteger(1, daysPrev);

			int sum = 0;
			for (Object row : query.list()) {
				Object[] cols = (Object[]) row;
				Character status = (Character) cols[0];
				java.math.BigInteger count = (java.math.BigInteger) cols[1];

				if (status == 'P')
					dataPoint.setPass(count.intValue());
				else if (status == 'F')
					dataPoint.setFail(count.intValue());
				else if (status == 'E')
					dataPoint.setError(count.intValue());
				else if (status == 'I')
					dataPoint.setInconcl(count.intValue());
				sum += count.intValue();

			}

			/*******************************************************************
			 * Oracle Legacy Code
			 * *************************************************************************************
			 * SQL_QUERY = "SELECT count(*) FROM Test as t " + " WHERE
			 * t.project.id = ? AND t.isFolder = 'N' AND t.createdDate <=
			 * sysdate - ?";
			 ******************************************************************/

			SQL_QUERY = "SELECT count(*) FROM Test as t "
					+ " WHERE t.proj_id = ? AND t.is_Folder = 'N' "
					+ "AND t.created_Date < date_sub(now(), INTERVAL ? day)";

			query = s.createSQLQuery(SQL_QUERY);
			query.setInteger(0, projID);
			query.setInteger(1, daysPrev);

			for (Object row : query.list()) {
				java.math.BigInteger totalTests = (java.math.BigInteger) row;
				int notExec = totalTests.intValue() - sum;
				dataPoint.setNotExec(notExec);
			}
			cal.add(Calendar.DATE, -daysPrev);
			dataPoint.setDate(cal.getTime());
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return dataPoint;
	}
}
