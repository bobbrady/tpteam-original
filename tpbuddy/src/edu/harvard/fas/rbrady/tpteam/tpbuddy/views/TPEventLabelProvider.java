/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

public class TPEventLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public String getColumnText(Object element, int index) {
		TPEvent tpEvent = (TPEvent) element;
		switch (index) {
		case 0:
			String[] topicPath = tpEvent.getTopic().split("/");
			return topicPath[topicPath.length-1];
		case 1:
			return tpEvent.getProject();
		case 2:
			return tpEvent.getTestName();
		case 3:
			return tpEvent.getParentContainer();
		case 4:
			return tpEvent.getID();
		case 5:
			return tpEvent.getStatus();
		default:
			return "unknown " + index;
		}
	}

	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}