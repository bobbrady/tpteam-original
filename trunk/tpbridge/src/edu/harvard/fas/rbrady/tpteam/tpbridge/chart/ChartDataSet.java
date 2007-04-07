package edu.harvard.fas.rbrady.tpteam.tpbridge.chart;

import java.util.ArrayList;
import java.util.List;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TpteamUser;

public class ChartDataSet implements java.io.Serializable, Comparable<ChartDataSet> {

	private static final long serialVersionUID = 1L;

	private TpteamUser mUser;

	private String mType;
	
	private String mProjName;

	private List<ChartDataPoint> mChartDataPoints = new ArrayList<ChartDataPoint>();
	
	public static final String PIE = "PIE_CHART";
	
	public static final String BAR = "BAR_CHART";
	
	public static final String LINE = "LINE_CHART";
	
	public static final String CHART_TYPE = "CHART_TYPE";
	
	public ChartDataSet()
	{
		
	}

	public void setUser(TpteamUser user) {
		mUser = user;
	}

	public TpteamUser getUser() {
		return mUser;
	}

	public void setType(String type) {
		mType = type;
	}

	public String getType() {
		return mType;
	}
	
	public void setProjName(String projName)
	{
		mProjName = projName;
	}
	
	public String getProjName()
	{
		return mProjName;
	}

	public void setChartDataPoints(List<ChartDataPoint> chartDataPoints) {
		mChartDataPoints = chartDataPoints;
	}

	public List<ChartDataPoint> getChartDataPoints() {
		return mChartDataPoints;
	}

	public void addChartDataPoint(ChartDataPoint chartDataPoint) {
			mChartDataPoints.add(chartDataPoint);
	}

	public int compareTo(ChartDataSet chartDataSet) {
		return this.mUser.compareTo(chartDataSet.getUser());
	}
}
