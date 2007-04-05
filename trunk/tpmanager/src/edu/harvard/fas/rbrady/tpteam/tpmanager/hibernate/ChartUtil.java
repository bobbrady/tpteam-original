package edu.harvard.fas.rbrady.tpteam.tpmanager.hibernate;

import java.util.Hashtable;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataPoint;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.HibernateUtil;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.ChartDataSetXML;

public class ChartUtil {

	private static ChartDataPoint getBarChartDataPoint(TPEvent tpEvent)
			throws Exception {

		Session s = null;
		Transaction tx = null;
		int projID = Integer.parseInt(tpEvent.getDictionary().get(
				TPEvent.PROJECT_ID_KEY));
		ChartDataPoint dataPoint = new ChartDataPoint();
		try {

			s = HibernateUtil.getSessionFactory().getCurrentSession();
			// s =
			// Activator.getDefault().getHiberSessionFactory().getCurrentSession();

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
				System.out.println("Total Tests: " + totalTests + ", sum: " + sum);
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

	public static String getBarChartXML(TPEvent tpEvent) throws Exception {
		ChartDataPoint dataPoint = getBarChartDataPoint(tpEvent);
		ChartDataSet dataSet = new ChartDataSet();
		TpteamUser user = new TpteamUser();
		user.setFirstName("root");
		user.setLastName("root");
		dataSet.setUser(user);
		dataSet.setProjName(tpEvent.getDictionary().get(TPEvent.PROJECT_KEY));
		dataSet.addChartDataPoint(dataPoint);
		dataSet.setType(ChartDataSet.BAR);
		return ChartDataSetXML.getXML(dataSet);
	}

	public static void main(String[] args) {
		Hashtable<String,String> dict = new Hashtable<String,String>();
		dict.put(TPEvent.PROJECT_ID_KEY, "1");
		dict.put(TPEvent.PROJECT_KEY, "Proj Name");
		TPEvent tpEvent = new TPEvent("test_topic", dict);
		try {
			String barChartXML = getBarChartXML(tpEvent);
			System.out.println("barChartXML:\n" + barChartXML);
			ChartDataSet dataSet = ChartDataSetXML.getDataSetFromXML(barChartXML);
			System.out.println("Pass: " + dataSet.getChartDataPoints().get(0).getPass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
