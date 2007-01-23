/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.model;

import java.util.ArrayList;

public class TreeParent extends TreeObject {
    private ArrayList<TreeObject> children;

    public TreeParent(String name, int type, String imagePath) {
        super(name, type, imagePath);
        children = new ArrayList<TreeObject>();
    }

    public void addChild(TreeObject child) {
        children.add(child);
        child.setParent (this);
    }

    public void removeChild(TreeObject child) {
        children.remove(child);
        child.setParent(null);
    }

    public TreeObject[] getChildren() {
        return (TreeObject[]) children.toArray(new TreeObject[children
                .size()]);
    }

    public boolean hasChildren() {
        return children.size() > 0;
    } 
}
