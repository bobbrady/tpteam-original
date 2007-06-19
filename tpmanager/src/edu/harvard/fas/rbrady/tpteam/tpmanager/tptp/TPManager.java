/********************************************************************
 * 
 * File		:	TPManager.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Receives out-of-JVM TPEvent requests from remote 
 * 				TPBuddies or Web clients, opens new TPManagerThread 
 * 				to handle them
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.tptp;

import java.util.Observable;
import java.util.Observer;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.eventadmin.EventAdminHandler;

/*******************************************************************************
 * File 		: 	TPManager.java
 * 
 * Description 	: 	Receives out-of-JVM TPEvent requests from remote 
 * 					TPBuddies or Web clients, opens new TPManagerThread 
 * 					to handle them
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TPManager implements Observer {	
	
	/**
	 * Receives notifications of out-of-JVM TPEvent requests or
	 * Web client requests from the TPManager EventAdminHandler,
	 * starts a new TPManagerThread to handle them.
	 * 
	 * @see EventAdminHandler
	 * @see TPManagerThread
	 * @param observable the EventAmdinHandler object being observed
	 * @param object the TPEvent object
	 */
	public void update(Observable observable, Object object) {
		if (observable instanceof EventAdminHandler
				&& object instanceof TPEvent) {
			TPEvent tpEvent = (TPEvent) object;
			TPManagerThread tpManagerThread = new TPManagerThread(tpEvent);
			Thread tpThread = new Thread(tpManagerThread);
			tpThread.start();
		}
	}
}
