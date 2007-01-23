/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.harvard.fas.rbrady.tpteam.tpbuddy.model.TreeObject;
import edu.harvard.fas.rbrady.tpteam.tpbuddy.model.TreeParent;

public class TestContentProvider implements IStructuredContentProvider,
ITreeContentProvider {

public void inputChanged(Viewer v, Object oldInput, Object newInput) {
}

public void dispose() {
}

public Object[] getElements(Object parent) {
return getChildren(parent);
}

public Object getParent(Object child) {
if (child instanceof TreeObject) {
    return ((TreeObject) child).getParent();
}
return null;
}

public Object[] getChildren(Object parent) {
if (parent instanceof TreeParent) {
    return ((TreeParent) parent).getChildren();
}
return new Object[0];
}

public boolean hasChildren(Object parent) {
if (parent instanceof TreeParent)
    return ((TreeParent) parent).hasChildren();
return false;
}
}