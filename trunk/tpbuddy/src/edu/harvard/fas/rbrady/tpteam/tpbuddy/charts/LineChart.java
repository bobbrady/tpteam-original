package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class LineChart extends AbstractChart{
	
	private static LineChart mLineChart = null;
	
	public static final int NUM_DAYS_IN_SERIES = 30;

	private LineChart() {
	}

	public static synchronized LineChart getInstance() {
		if (mLineChart == null)
			mLineChart = new LineChart();
		return mLineChart;
	}


	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            a dataset.
	 * 
	 * @return A chart.
	 */
	public JFreeChart createChart() {
		XYDataset dataset = (XYDataset)createDataset();
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Project Historical Status", "Date", "Number of Tests",
				dataset, true, true, false);
		chart.setBackgroundPaint(Color.white);
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setSeriesPaint(0, GREEN);
		renderer.setSeriesPaint(1, RED);
		renderer.setSeriesPaint(2, YELLOW);
		renderer.setSeriesPaint(3, BLUE);
		if (renderer instanceof StandardXYItemRenderer) {
			StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
		//	rr.setPlotShapes(true);
			rr.setShapesFilled(true);
			rr.setItemLabelsVisible(true);
		}
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("MMM-dd-yy"));

		return chart;
	}
	
	private TimeSeries createTimeSeries(String seriesName)
	{
		Calendar cal = Calendar.getInstance();  
		Date today = new Date();              
		TimeSeries timeSeries = new TimeSeries(seriesName, Day.class);
		for(int idx = 0; idx < NUM_DAYS_IN_SERIES; idx++)
		{
			cal.setTime(today);
			cal.add(Calendar.DATE, -idx);
			if(seriesName.equalsIgnoreCase(PASS))
				timeSeries.add(new Day(cal.getTime()), 130 - idx);
			else if(seriesName.equalsIgnoreCase(FAIL))
				timeSeries.add(new Day(cal.getTime()), 100 + idx);
			else if(seriesName.equalsIgnoreCase(ERR))
				timeSeries.add(new Day(cal.getTime()), 10);
			else if(seriesName.equalsIgnoreCase(INCONCL))
				timeSeries.add(new Day(cal.getTime()), 5);

			
		}
		return timeSeries;
		
	}

	/**
	 * Creates a dataset, consisting of two series of monthly data.
	 * 
	 * @return the dataset.
	 */
	protected AbstractDataset createDataset() {
		TimeSeries passSeries = createTimeSeries(PASS);
		TimeSeries failSeries = createTimeSeries(FAIL);
		TimeSeries errSeries = createTimeSeries(ERR);
		TimeSeries incSeries = createTimeSeries(INCONCL);
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(passSeries);
		dataset.addSeries(failSeries);
		dataset.addSeries(errSeries);
		dataset.addSeries(incSeries);

		return dataset;
	}

}
