package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;

import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataPoint;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.charts.BarChart;

/**
 * Generated code for the test suite <b>TestBarChart</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.test/edu/harvard/fas/rbrady/tpteam/tpbuddy/charts/test/TestBarChart.testsuite</i>.
 */
public class TestBarChart extends HyadesTestCase {
	/**
	 * Constructor for TestBarChart.
	 * 
	 * @param name
	 */
	public TestBarChart(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestBarChart</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testBarChart = new HyadesTestSuite("TestBarChart");
		testBarChart.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"F8A6DF6D765A872E41957DE0EE1D11DB");
	
		testBarChart.addTest(new TestBarChart("testCreateDataset").setId(
				"F8A6DF6D765A872E45E35430EE1D11DB").setTestInvocationId(
				"F8A6DF6D765A872E4EBBE2C0EE1D11DB"));
		return testBarChart;
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
	 * testCreateDataset
	 * 
	 * @throws Exception
	 */
	public void testCreateDataset() throws Exception {
		Project proj = new Project();
		proj.setName("Test Project");

		TpteamUser user1 = new TpteamUser();
		user1.setLastName("User1Last");
		user1.setFirstName("User1First");

		TpteamUser user2 = new TpteamUser();
		user2.setLastName("User2Last");
		user2.setFirstName("User2First");

		List<ChartDataPoint> dataPoints1 = new ArrayList<ChartDataPoint>();
		ChartDataPoint dataPoint1 = new ChartDataPoint();

		List<ChartDataPoint> dataPoints2 = new ArrayList<ChartDataPoint>();
		ChartDataPoint dataPoint2 = new ChartDataPoint();

		dataPoint1.setDate(new Date());
		dataPoint1.setPass(10);
		dataPoint1.setFail(5);
		dataPoint1.setError(2);
		dataPoint1.setInconcl(1);
		dataPoint1.setNotExec(3);

		dataPoint2.setDate(new Date());
		dataPoint2.setPass(10);
		dataPoint2.setFail(8);
		dataPoint2.setError(6);
		dataPoint2.setInconcl(4);
		dataPoint2.setNotExec(2);

		dataPoints1.add(dataPoint1);
		dataPoints2.add(dataPoint2);

		ChartDataSet dataSet1 = new ChartDataSet();
		dataSet1.setProjName(proj.getName());
		dataSet1.setUser(user1);
		dataSet1.setType(ChartDataSet.BAR);
		dataSet1.setChartDataPoints(dataPoints1);

		ChartDataSet dataSet2 = new ChartDataSet();
		dataSet2.setProjName(proj.getName());
		dataSet2.setUser(user2);
		dataSet2.setType(ChartDataSet.BAR);
		dataSet2.setChartDataPoints(dataPoints2);

		JFreeChart barChart = BarChart.getInstance().createChart(
				new ChartDataSet[] { dataSet1, dataSet2 }, proj.getName());

		// Extract Title and Legends

		String[] titleLines = barChart.getTitle().getText().split("\\n");
		assertTrue("Error: Main Title returned \"" + titleLines[0] + "\"",
				titleLines[0].trim().equals(
						"Project " + proj.getName().trim() + " Users Overview"));

		String legendPass = barChart.getPlot().getLegendItems().get(0)
				.getDescription();
		assertTrue("Error: Legend returned \"" + legendPass + "\"", legendPass
				.equals(BarChart.PASS));
		String legendFail = barChart.getPlot().getLegendItems().get(1)
				.getDescription();
		assertTrue("Error: Legend returned \"" + legendFail + "\"", legendFail
				.equals(BarChart.FAIL));
		String legendErr = barChart.getPlot().getLegendItems().get(2)
				.getDescription();
		assertTrue("Error: Legend returned \"" + legendErr + "\"", legendErr
				.equals(BarChart.ERR));
		String legendIncl = barChart.getPlot().getLegendItems().get(3)
				.getDescription();
		assertTrue("Error: Legend returned \"" + legendIncl + "\"", legendIncl
				.equals(BarChart.INCONCL));
		String legendNE = barChart.getPlot().getLegendItems().get(4)
				.getDescription();
		assertTrue("Error: Legend returned \"" + legendNE + "\"", legendNE
				.equals(BarChart.NOTEXEC));
		
		// Check User Names are Correct for each bar set
		CategoryPlot catPlot = barChart.getCategoryPlot();
		String user1Str = catPlot.getDataset(0).getColumnKey(0).toString().trim();
		String user2Str = catPlot.getDataset(0).getColumnKey(1).toString().trim();
		assertTrue("Error user name returned \"" + user1Str + "\"", user1Str.equals(user1.getLastName() + ", " + user1.getFirstName()));
		assertTrue("Error user name returned \"" + user2Str + "\"", user2Str.equals(user2.getLastName() + ", " + user2.getFirstName()));
	}

}
