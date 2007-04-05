package edu.harvard.fas.rbrady.tpteam.tpbridge.chart;

import java.util.Date;

public class ChartDataPoint implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Date mDate;

	private int mPass;

	private int mFail;

	private int mError;

	private int mInconcl;

	private int mNotExec;
	
	public ChartDataPoint()
	{
		
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public Date getDate() {
		return mDate;
	}

	public void setPass(int pass) {
		mPass = pass;
	}

	public int getPass() {
		return mPass;
	}

	public void setFail(int fail) {
		mFail = fail;
	}

	public int getFail() {
		return mFail;
	}

	public void setError(int error) {
		mError = error;
	}

	public int getError() {
		return mError;
	}

	public void setInconcl(int inconcl) {
		mInconcl = inconcl;
	}

	public int getInconcl() {
		return mInconcl;
	}

	public void setNotExec(int notExec) {
		mNotExec = notExec;
	}

	public int getNotExec() {
		return mNotExec;
	}
}
