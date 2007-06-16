/********************************************************************
 * 
 * File		:	PieChart.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	TPTeam implementation of a pie chart showing the 
 * 				top-level current snapshot of test execution status 
 * 				for the given project
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts;

import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataPoint;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;

/*************************************************************************
 * File 		: 	PieChart.java
 * 
 * Description 	: 	TPTeam implementation of a pie chart showing the 
 * 					top-level current snapshot of test execution status 
 * 					for the given project
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c)2007 Bob Brady
 ***********************************************************************/
public class PieChart extends AbstractChart {

	/**
	 * Default Constructor
	 */
	private PieChart() {
	}

	/**
	 * Gets the static instance of the PieChart for
	 * faster rendering time
	 * 
	 * @return the PieChart instance
	 */
	public static synchronized AbstractChart getInstance() {
		if(mCharts.get(PieChart.class) == null)
		{
			mCharts.put(PieChart.class, new PieChart());
		}
		return mCharts.get(PieChart.class);
	}

	/**
	 * Returns a sample dataset.
	 * 
	 * @return The dataset.
	 */
	public AbstractDataset createDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue(PASS, 75.0);
		dataset.setValue(FAIL, 20);
		dataset.setValue(ERR, 4);
		dataset.setValue(INCONCL, 0);
		dataset.setValue(NOTEXEC, 3);
		return dataset;
	}

	/**
	 * Gets the data set in JFreeChart format
	 * 
	 * @param dataSet the TPTeam set of data points
	 * @return the data points in JFreeChart abstract form
	 */
	public AbstractDataset createDataset(ChartDataSet[] dataSet) {
		List<ChartDataPoint> dataPoints = dataSet[0].getChartDataPoints();
		ChartDataPoint dataPoint = dataPoints.get(0);

		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue(PASS, dataPoint.getPass());
		dataset.setValue(FAIL, dataPoint.getFail());
		dataset.setValue(ERR, dataPoint.getError());
		dataset.setValue(INCONCL, dataPoint.getInconcl());
		dataset.setValue(NOTEXEC, dataPoint.getNotExec());
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

		DefaultPieDataset dataset = (DefaultPieDataset) createDataset();

		// create the chart...
		JFreeChart chart = ChartFactory.createPieChart(
				"Project Test Execution Overview\nTotal Tests = 5", dataset,
				true, // legend?
				true, // tooltips?
				false // URLs?
				);

		// Set custom settings
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionPaint(PASS, GREEN);
		plot.setSectionPaint(FAIL, RED);
		plot.setSectionPaint(ERR, YELLOW);
		plot.setSectionPaint(INCONCL, BLUE);
		plot.setSectionPaint(NOTEXEC, PINK);

		return chart;
	}

	/**
	 * Creates a pie chart sliced by test execution status
	 * 
	 * @param dataSet the set of data points
	 * @param projName the name of the TPTeam test project
	 * @return a JFreeChart pie chart 
	 */
	public JFreeChart createChart(ChartDataSet[] dataSet, String projName) {

		DefaultPieDataset dataset = (DefaultPieDataset) createDataset(dataSet);
		int totalTests = 0;
		for (int idx = 0; idx < dataset.getItemCount(); idx++)
			totalTests += dataset.getValue(idx).intValue();

		// create the chart...
		JFreeChart chart = ChartFactory.createPieChart(projName
				+ " Test Plan Overview\nTotal Tests: " + totalTests, dataset,
				true, // legend?
				true, // tooltips?
				false // URLs?
				);

		// Set custom settings
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionPaint(PASS, GREEN);
		plot.setSectionPaint(FAIL, RED);
		plot.setSectionPaint(ERR, YELLOW);
		plot.setSectionPaint(INCONCL, BLUE);
		plot.setSectionPaint(NOTEXEC, PINK);

		return chart;
	}
}