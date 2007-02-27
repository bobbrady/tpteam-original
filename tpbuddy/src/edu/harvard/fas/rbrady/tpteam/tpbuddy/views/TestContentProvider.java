/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;

public class TestContentProvider extends ArrayContentProvider implements
		ITreeContentProvider {

	public Object getParent(Object child) {
		return ((TPEntity) child).getParent();
	}

	public Object[] getChildren(Object parent) {
		return ((TPEntity) parent).getChildren();
	}

	public boolean hasChildren(Object parent) {
		return ((TPEntity)parent).getChildren().length > 0;
	}
}