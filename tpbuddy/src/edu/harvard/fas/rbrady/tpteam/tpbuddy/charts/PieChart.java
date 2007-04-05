package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;

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
				"Project Test Execution Overview", dataset, true, // legend?
				true, // tooltips?
				false // URLs?
				);

		// Set custom settings
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionPaint(PASS, GREEN);
		plot.setSectionPaint(FAIL, RED);
		plot.setSectionPaint(ERR, YELLOW);
		plot.setSectionPaint(INCONCL, BLUE);

		return chart;
	}
}