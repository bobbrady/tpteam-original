/********************************************************************
 * 
 * File		:	AbstractChart.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Base class for all TPTeam Chart objects
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.charts;

import java.awt.Color;
import java.util.HashMap;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.AbstractDataset;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;

/*************************************************************************
 * File 		: 	AbstractChart.java
 * 
 * Description 	: 	Base class for all TPTeam Chart objects
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c)2007 Bob Brady
 * ***********************************************************************/
public abstract class AbstractChart {
	
	/** Static chart used for fast rendering */
	protected static AbstractChart mChart = null;
	/** Map of all child objects */ 
	protected static HashMap<Class, AbstractChart> mCharts = new HashMap<Class, AbstractChart>();
	/** Pass test verdict */
	public static final String PASS = "Pass";
	/** Fail test verdict */
	public static final String FAIL = "Fail";
	/** Error test verdict */
	public static final String ERR = "Error";
	/** Inconclusive test verdict */
	public static final String INCONCL = "Inconclusive";
	/** Test never run */
	public static final String NOTEXEC = "Not Executed";
	/** Color representing failed test executions */
	public static final Color RED = new Color(255, 0, 0);
	/** Color representing passed test executions */
	public static final Color GREEN = new Color(34, 139, 34);
	/** Color representing inconclusive test executions */
	public static final Color BLUE = new Color(0, 0, 255);
	/** Color representing test executions stopped due to errors */
	public static final Color YELLOW = new Color(255, 255, 0);
	/** Color representin tests never executed */
	public static final Color PINK = new Color(255, 20, 147);
	
	public abstract AbstractDataset createDataset();
	
	public abstract AbstractDataset createDataset(ChartDataSet[] dataSet);
	
	public abstract JFreeChart createChart();
	
	public abstract JFreeChart createChart(ChartDataSet[] dataSets, String projName);
	
	/**
	 * Returns the static instance of this chart
	 * @return the chart instance
	 */
	public static AbstractChart getInstance()
	{
		return null;
	}
	
}
