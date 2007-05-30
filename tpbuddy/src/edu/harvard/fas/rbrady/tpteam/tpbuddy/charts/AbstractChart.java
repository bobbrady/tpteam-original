package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts;

import java.awt.Color;
import java.util.HashMap;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.AbstractDataset;

import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;

public abstract class AbstractChart {
	
	protected static AbstractChart mChart = null;
	
	protected static HashMap<Class, AbstractChart> mCharts = new HashMap<Class, AbstractChart>();
	
	public static final String PASS = "Pass";

	public static final String FAIL = "Fail";

	public static final String ERR = "Error";

	public static final String INCONCL = "Inconclusive";
	
	public static final String NOTEXEC = "Not Executed";

	public static final Color RED = new Color(255, 0, 0);

	public static final Color GREEN = new Color(34, 139, 34);

	public static final Color BLUE = new Color(0, 0, 255);

	public static final Color YELLOW = new Color(255, 255, 0);
	
	public static final Color PINK = new Color(255, 20, 147);
	
	public abstract AbstractDataset createDataset();
	
	public abstract AbstractDataset createDataset(ChartDataSet[] dataSet);
	
	public abstract JFreeChart createChart();
	
	public abstract JFreeChart createChart(ChartDataSet[] dataSets, String projName);
	
	public static AbstractChart getInstance()
	{
		return null;
	}
	
}
