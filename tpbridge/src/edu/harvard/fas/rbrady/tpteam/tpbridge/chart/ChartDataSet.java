/********************************************************************
 * 
 * File		:	ChartDataSet.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A serializable TPTeam test execution data set to be
 * 				encapsulated within a TPTeam Event.
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.chart;

import java.util.ArrayList;
import java.util.List;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;

/*******************************************************************************
 * File 		: 	ChartDataPoint.java
 * 
 * Description 	: 	A serializable TPTeam test execution data set to be
 * 					encapsulated within a TPTeam Event.
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ChartDataSet implements java.io.Serializable, Comparable<ChartDataSet> {
	/** ID used during serialization */
	private static final long serialVersionUID = 1L;
	/** The TPTeam user who is associated with the data points of the set */
	private TpteamUser mUser;
	/** The chart type associated with the data set: Pie, Bar, or Line */
	private String mType;
	/** The name of the TPTeam project associated with the data set */
	private String mProjName;
    /** The list of data points of the set */
	private List<ChartDataPoint> mChartDataPoints = new ArrayList<ChartDataPoint>();
	
	// Convenience public Strings representing chart types
	public static final String PIE = "PIE_CHART";
	public static final String BAR = "BAR_CHART";
	public static final String LINE = "LINE_CHART";
	public static final String CHART_TYPE = "CHART_TYPE";
	
	/**
	 * Default Constructor
	 */
	public ChartDataSet()
	{
		
	}

	/** 
	 * Setter 
	 * @param user The TPTeam user
	 */
	public void setUser(TpteamUser user) {
		mUser = user;
	}

	/**
	 * Getter 
	 * @return the TPTeam user
	 */
	public TpteamUser getUser() {
		return mUser;
	}

	/**
	 * Setter
	 * @param type The chart type
	 */
	public void setType(String type) {
		mType = type;
	}

	/**
	 * Getter
	 * @return the chart type
	 */
	public String getType() {
		return mType;
	}
	
	/**
	 * Setter 
	 * @param projName the TPTeam project name
	 */
	public void setProjName(String projName)
	{
		mProjName = projName;
	}
	
	/**
	 * Getter 
	 * @return the TPTeam project name
	 */
	public String getProjName()
	{
		return mProjName;
	}

	/**
	 * Setter
	 * @param chartDataPoints the data points of the data set
	 */
	public void setChartDataPoints(List<ChartDataPoint> chartDataPoints) {
		mChartDataPoints = chartDataPoints;
	}

	/**
	 * Getter 
	 * @return the data points of the data set
	 */
	public List<ChartDataPoint> getChartDataPoints() {
		return mChartDataPoints;
	}

	/**
	 * Adds a data point to the list of data points 
	 * @param chartDataPoint the data point to add
	 */
	public void addChartDataPoint(ChartDataPoint chartDataPoint) {
			mChartDataPoints.add(chartDataPoint);
	}

	/**
	 * Compares one data set to another.
	 * 
	 * 	Returns -1 if this data set TPTeam user is alphabetically
	 * before parameter data set's TPTeam user, 0 if user names
	 * are equal, and 1 if parameter data set's TPTeam user is 
	 * alphabetically before this data set's TPTeam user.
	 * 
	 * @return  -1, 0, or 1
	 */
	public int compareTo(ChartDataSet chartDataSet) {
		return this.mUser.compareTo(chartDataSet.getUser());
	}
}
