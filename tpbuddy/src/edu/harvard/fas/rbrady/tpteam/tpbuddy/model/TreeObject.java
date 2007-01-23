/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.model;

public class TreeObject {
    private String name;

    private int type;
    
    private String imagePath;
    
    private TreeParent parent;
    
    public static final int FOLDER_TYPE = 0;
    public static final int JUNIT_TYPE = 1;
    public static final int STATUS_TYPE = 2;
    public static final int ELEMENT_TYPE = 3;
    public static final int TEST_SUITE_TYPE = 4;
    public static final int PROJECT_TYPE = 5;
    
    public static final String JUNIT_IMAGE = "icons/junit.gif";
    public static final String TEST_FAIL_IMAGE = "icons/testerr.gif";
    public static final String TEST_OK_IMAGE = "icons/testok.gif";
    public static final String TEST_SUITE_IMAGE = "icons/tsuite.gif";
    public static final String PROJECT_IMAGE = "icons/testhier.gif";
    public static final String NO_IMAGE = "NO_IMAGE";
    public static final String FOLDER_IMAGE = "FOLDER_IMAGE";
    
    public TreeObject(String name, int type, String imagePath) {
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name)
    {
    	this.name = name;
    }

    public void setParent(TreeParent parent) {
        this.parent = parent;
    }

    public TreeParent getParent() {
        return parent;
    }

    public String toString() {
        return getName();
    }
    
    public int getType()
    {
        return type;
    }
    
    public String getImagePath()
    {
        return imagePath;
    }
    
    public void setImagePath(String imagePath)
    {
    	this.imagePath = imagePath;
    }
}
