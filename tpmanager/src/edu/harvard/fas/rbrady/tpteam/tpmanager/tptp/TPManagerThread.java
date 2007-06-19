/********************************************************************
 * 
 * File		:	TPManagerThread.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	The Thread that instantiated to handle each and 
 * 				every TPEvent request to the TPManager
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.tptp;

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

/*******************************************************************************
 * File 		: 	TPManagerThread.java
 * 
 * Description 	: 	The Thread that instantiated to handle each and 
 * 					every TPEvent request to the TPManager
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TPManagerThread implements Runnable {

	/** The TPEvent whose request is being handled */
	private TPEvent mTPEvent;

	/**
	 * Constructor
	 * @param tpEvent the TPEvent request
	 */
	public TPManagerThread(TPEvent tpEvent) {
		mTPEvent = tpEvent;
	}

	/**
	 * Performs the necessary CRUD or execution operation
	 * based upon the TPEvent topic.  Sends a response when
	 * appropriate.
	 */
	public void run() {
		String tpTopic = mTPEvent.getTopic();
		try {
			System.out.println("NEW THREAD w/Topic: " + tpTopic);
			if (tpTopic.equals(ITPBridge.PROJ_GET_REQ_TOPIC)) {
				TPTestCRUD.sendProjGetResponse(mTPEvent);
			} else if (tpTopic.equalsIgnoreCase(ITPBridge.TEST_EXEC_REQ_TOPIC)) {
				TPTestExec.runTest(mTPEvent.getID(), mTPEvent);
				TPTestExec.sendTestExecResponse(mTPEvent);
			} else if (tpTopic
					.equalsIgnoreCase(ITPBridge.TEST_TREE_GET_REQ_TOPIC)) {
				TPTestCRUD.sendTestTreeGetResponse(mTPEvent);
			} else if (tpTopic.equalsIgnoreCase(ITPBridge.TEST_DEL_REQ_TOPIC)) {
				TPTestCRUD.deleteTest(mTPEvent);
				TPTestCRUD.sendDelTestResponse(mTPEvent);
			} else if (tpTopic
					.equalsIgnoreCase(ITPBridge.TEST_DETAIL_REQ_TOPIC)) {
				TPTestCRUD.sendTestDetailResponse(mTPEvent);
			} else if (tpTopic
					.equalsIgnoreCase(ITPBridge.TEST_UPDATE_DATA_REQ_TOPIC)) {
				TPTestCRUD.sendTestUpdateDatResponse(mTPEvent);
			} else if (tpTopic
					.equalsIgnoreCase(ITPBridge.TEST_UPDATE_REQ_TOPIC)) {
				TPTestCRUD.sendTestUpdateResponse(mTPEvent);
			} else if (tpTopic.equalsIgnoreCase(ITPBridge.TEST_ADD_REQ_TOPIC)) {
				TPTestCRUD.sendTestAddResponse(mTPEvent);
			} else if (tpTopic
					.equalsIgnoreCase(ITPBridge.CHART_GET_DATA_REQ_TOPIC)) {
				TPTestCRUD.sendChartDataResponse(mTPEvent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
