/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.tptp;

import java.util.Observable;
import java.util.Observer;

import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;
import edu.harvard.fas.rbrady.tpteam.tpmanager.eventadmin.EventAdminHandler;

public class TPManager implements Observer {	
	
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
