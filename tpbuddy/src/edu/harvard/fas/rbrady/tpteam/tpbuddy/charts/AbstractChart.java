package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts;

import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.AbstractDataset;

public abstract class AbstractChart {
	
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
	
	protected abstract AbstractDataset createDataset();
	
	public abstract JFreeChart createChart();
}
