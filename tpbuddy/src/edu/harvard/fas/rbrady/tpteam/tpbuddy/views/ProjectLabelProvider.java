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

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;

public class ProjectLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public String getColumnText(Object element, int index) {
		Project proj = (Project) element;
		switch (index) {
		case 0:
			return proj.getName();
		case 1:
			return proj.getDescription();
		case 2:
			return proj.getProduct().getName();
		default:
			return "unknown " + index;
		}
	}

	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}