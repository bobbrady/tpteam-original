package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;
import org.jfree.chart.JFreeChart;

import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataPoint;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.charts.PieChart;

/**
 * Generated code for the test suite <b>TestPieChart</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.test/edu/harvard/fas/rbrady/tpteam/tpbuddy/charts/test/TestPieChart.testsuite</i>.
 */
public class TestPieChart extends HyadesTestCase {
	/**
	 * Constructor for TestPieChart.
	 * 
	 * @param name
	 */
	public TestPieChart(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>TestPieChart</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite testPieChart = new HyadesTestSuite("TestPieChart");
		testPieChart.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"F8A6DF6D765A872EE4843DB0EE1411DB");
	
		testPieChart.addTest(new TestPieChart("testCreateDataset").setId(
				"F8A6DF6D765A872E21809380EE1511DB").setTestInvocationId(
				"F8A6DF6D765A872E349A6860EE1511DB"));
		return testPieChart;
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
		user1.setLastName("User1First");
		user1.setFirstName("User1Last");

		TpteamUser user2 = new TpteamUser();
		user2.setLastName("User2First");
		user2.setFirstName("User2Last");

		List<ChartDataPoint> dataPoints = new ArrayList<ChartDataPoint>();
		ChartDataPoint dataPoint = new ChartDataPoint();

		dataPoint.setDate(new Date());
		dataPoint.setPass(10);
		dataPoint.setFail(5);
		dataPoint.setError(2);
		dataPoint.setInconcl(1);
		dataPoint.setNotExec(3);

		dataPoints.add(dataPoint);

		ChartDataSet dataSet = new ChartDataSet();
		dataSet.setProjName(proj.getName());
		dataSet.setUser(user1);
		dataSet.setType(ChartDataSet.PIE);
		dataSet.setChartDataPoints(dataPoints);

		JFreeChart pieChart = PieChart.getInstance().createChart(new ChartDataSet[]{dataSet}, proj.getName());

		// Extract Title and Legends

		String[] titleLines = pieChart.getTitle().getText().split("\\n");
		assertTrue("Error: Main Title returned \"" + titleLines[0] + "\"",
				titleLines[0].trim().equals(
						proj.getName().trim() + " Test Plan Overview"));

		// Subtitle contains number of total tests, which equals sum of all
		// reported test states
		assertTrue("Error: Sub Title returned " + titleLines[1], titleLines[1]
				.trim().equals("Total Tests: 21"));

		String legendPass = pieChart.getPlot().getLegendItems().get(0)
				.getDescription();
		assertTrue("Error: Legend returned \"" + legendPass + "\"", legendPass
				.equals(PieChart.PASS));
		String legendFail = pieChart.getPlot().getLegendItems().get(1)
				.getDescription();
		assertTrue("Error: Legend returned \"" + legendFail + "\"", legendFail
				.equals(PieChart.FAIL));
		String legendErr = pieChart.getPlot().getLegendItems().get(2)
				.getDescription();
		assertTrue("Error: Legend returned \"" + legendErr + "\"", legendErr
				.equals(PieChart.ERR));
		String legendIncl = pieChart.getPlot().getLegendItems().get(3)
				.getDescription();
		assertTrue("Error: Legend returned \"" + legendIncl + "\"", legendIncl
				.equals(PieChart.INCONCL));
		String legendNE = pieChart.getPlot().getLegendItems().get(4)
				.getDescription();
		assertTrue("Error: Legend returned \"" + legendNE + "\"", legendNE
				.equals(PieChart.NOTEXEC));
	}
}
