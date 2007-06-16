/********************************************************************
 * 
 * File		:	BarChart.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	TPTeam implementation of a bar chart showing the 
 * 				project distribution of test executions by TPTeam 
 * 				user
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts;

import java.awt.Color;
import java.util.Arrays;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataPoint;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;

/*************************************************************************
 * File 		: 	BarChart.java
 * 
 * Description 	: 	TPTeam implementation of a bar chart showing the 
 * 					distribution of project test executions by 
 * 					TPTeam user
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c)2007 Bob Brady
 ************************************************************************/
public class BarChart extends AbstractChart {

	/**
	 * Default Constructor
	 */
	private BarChart() {
	}

	/**
	 * Gets the static instance of the BarChart for
	 * faster rendering time
	 * 
	 * @return the BarChart instance
	 */
	public static synchronized AbstractChart getInstance() {
		if(mCharts.get(BarChart.class) == null)
		{
			mCharts.put(BarChart.class, new BarChart());
		}
		return mCharts.get(BarChart.class);
	}

	/**
	 * Returns a sample dataset.
	 * 
	 * @return The dataset.
	 */
	public AbstractDataset createDataset() {

		// row keys...
		String series1 = PASS;
		String series2 = FAIL;
		String series3 = ERR;
		String series4 = INCONCL;

		// column keys...
		String user1 = "User 1";
		String user2 = "User 2";
		String user3 = "User 3";
		String user4 = "User 4";

		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(1.0, series1, user1);
		dataset.addValue(4.0, series1, user2);
		dataset.addValue(3.0, series1, user3);
		dataset.addValue(5.0, series1, user4);

		dataset.addValue(5.0, series2, user1);
		dataset.addValue(7.0, series2, user2);
		dataset.addValue(6.0, series2, user3);
		dataset.addValue(8.0, series2, user4);

		dataset.addValue(4.0, series3, user1);
		dataset.addValue(3.0, series3, user2);
		dataset.addValue(2.0, series3, user3);
		dataset.addValue(3.0, series3, user4);

		dataset.addValue(1.0, series4, user1);
		dataset.addValue(3.0, series4, user2);
		dataset.addValue(5.0, series4, user3);
		dataset.addValue(3.0, series4, user4);

		return dataset;

	}

	public AbstractDataset createDataset(ChartDataSet[] dataSets) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Arrays.sort(dataSets);
		for (ChartDataSet tpTeamDataSet : dataSets) {
			TpteamUser user = tpTeamDataSet.getUser();
			String userName = user.getLastName() + ", " + user.getFirstName();
			ChartDataPoint tpTeamDataPoints = tpTeamDataSet
					.getChartDataPoints().get(0);
			dataset.addValue(tpTeamDataPoints.getPass(), PASS, userName);
			dataset.addValue(tpTeamDataPoints.getFail(), FAIL, userName);
			dataset.addValue(tpTeamDataPoints.getError(), ERR, userName);
			dataset.addValue(tpTeamDataPoints.getInconcl(), INCONCL, userName);
			dataset.addValue(tpTeamDataPoints.getNotExec(), NOTEXEC, userName);
		}
		return dataset;
	}

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return The chart.
	 */
	public JFreeChart createChart() {

		CategoryDataset dataset = (CategoryDataset) createDataset();

		// create the chart...
		JFreeChart chart = ChartFactory.createBarChart(
				"Project Users Overview", // title
				"User", // domain axis label
				"Number of Tests", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // URLs
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, GREEN);
		renderer.setSeriesPaint(1, RED);
		renderer.setSeriesPaint(2, YELLOW);
		renderer.setSeriesPaint(3, BLUE);
		renderer.setDrawBarOutline(false);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}

	/**
	 * Creates a bar chart of test execution status for
	 * each user in a project
	 * 
	 * @param dataSets the chart data points
	 * @param projName the name of the test project
	 * @return the bar chart 
	 */
	public JFreeChart createChart(ChartDataSet[] dataSets, String projName) {

		CategoryDataset dataset = (CategoryDataset) createDataset(dataSets);

		// create the chart...
		JFreeChart chart = ChartFactory.createBarChart("Project " + projName
				+ " Users Overview", // title
				"User", // domain axis label
				"Number of Tests", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // URLs
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, GREEN);
		renderer.setSeriesPaint(1, RED);
		renderer.setSeriesPaint(2, YELLOW);
		renderer.setSeriesPaint(3, BLUE);
		renderer.setSeriesPaint(4, PINK);
		renderer.setDrawBarOutline(false);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}
}
