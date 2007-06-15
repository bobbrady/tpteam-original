/********************************************************************
 * 
 * File		:	ChartDataPoint.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A serializable TPTeam test execution data point to be
 * 				encapsulated within a TPTeam Event or ChartDataSet
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.chart;

import java.util.Date;

/*******************************************************************************
 * File 		: 	ChartDataPoint.java
 * 
 * Description 	: 	A serializable TPTeam test execution data point to be
 * 					encapsulated within a TPTeam Event or ChartDataSet
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ChartDataPoint implements java.io.Serializable {
	/** ID used during serialization */
	private static final long serialVersionUID = 1L;
	/** Date retrieved from database corresponding to data creation timestamp */
	private Date mDate;
	/** Number of successful test executions */
	private int mPass;
	/** Number of test executions that ran, but failed */
	private int mFail;
	/** Number of test executions that did not run due to errors */
	private int mError;
	/** Number of test execution inconclusive due to connection time out, etc. */
	private int mInconcl;
	/** Number of test definitions that were never requested to be run */
	private int mNotExec;
	
	/**
	 * Default constructor
	 */
	public ChartDataPoint()
	{
		
	}

	/**
	 * Setter
	 * @param date the data creation time from database
	 */
	public void setDate(Date date) {
		mDate = date;
	}

	/**
	 * Getter
	 * @return the data creation time from database
	 */
	public Date getDate() {
		return mDate;
	}

	/**
	 * Setter
	 * @param pass the number of passed test executions
	 */
	public void setPass(int pass) {
		mPass = pass;
	}

	/**
	 * Getter
	 * @return the number of passed test executions
	 */
	public int getPass() {
		return mPass;
	}

	/**
	 * Setter
	 * @param fail the number of failed executions
	 */
	public void setFail(int fail) {
		mFail = fail;
	}

	/**
	 * Getter
	 * @return the number of failed test executions
	 */
	public int getFail() {
		return mFail;
	}

	/**
	 * Setter
	 * @param error the number of erroneous test executions
	 */
	public void setError(int error) {
		mError = error;
	}

	/**
	 * Getter
	 * @return the number of erroneous test executions
	 */
	public int getError() {
		return mError;
	}

	/**
	 * Setter
	 * @param inconcl the number of inconclusive test executions
	 */
	public void setInconcl(int inconcl) {
		mInconcl = inconcl;
	}

	/**
	 * Getter
	 * @return the number of inconclusive test executions
	 */
	public int getInconcl() {
		return mInconcl;
	}

	/**
	 * Setter
	 * @param notExec the number of test executions never executed
	 */
	public void setNotExec(int notExec) {
		mNotExec = notExec;
	}

	/**
	 * Getter
	 * @return the number of test executions never executed
	 */
	public int getNotExec() {
		return mNotExec;
	}
}
