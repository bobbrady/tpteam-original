package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;

import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataPoint;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;

public class PieChart extends AbstractChart {

	private static PieChart mPieChart = null;

	private PieChart() {
	}

	public static synchronized PieChart getInstance() {
		if (mPieChart == null)
			mPieChart = new PieChart();
		return mPieChart;
	}

	/**
	 * Returns a sample dataset.
	 * 
	 * @return The dataset.
	 */
	protected AbstractDataset createDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue(PASS, 75.0);
		dataset.setValue(FAIL, 20);
		dataset.setValue(ERR, 4);
		dataset.setValue(INCONCL, 0);
		dataset.setValue(NOTEXEC, 3);
		return dataset;
	}

	protected AbstractDataset createDataset(ChartDataSet dataSet) {
		List<ChartDataPoint> dataPoints = dataSet.getChartDataPoints();
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

	public JFreeChart createChart(ChartDataSet dataSet) {

		DefaultPieDataset dataset = (DefaultPieDataset) createDataset(dataSet);
		int totalTests = 0;
		for (int idx = 0; idx < dataset.getItemCount(); idx++)
			totalTests += dataset.getValue(idx).intValue();

		// create the chart...
		JFreeChart chart = ChartFactory.createPieChart(dataSet.getProjName()
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