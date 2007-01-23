/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbuddy.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

public class TPTestEntity extends TreeParent {

	public TPTestEntity(String name, int type, String imagePath) {
		super(name, type, imagePath);
	}

	private static final Locale USA_LOCALE = new Locale("en", "US");

	private static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss.SS z", USA_LOCALE);

	private String mProject;

	private String mParentContainer;

	private String mID;

	private String mStatus;

	private String mDesc;

	public void setProject(String project) {
		mProject = project;
	}

	public String getProject() {
		return mProject;
	}

	public void setParentContainer(String parent) {
		mParentContainer = parent;
	}

	public String getParentContainer() {
		return mParentContainer;
	}

	public void setID(String ID) {
		mID = ID;
	}

	public String getID() {
		return mID;
	}

	public void setStatus(String status, String successImage) {
		mStatus = status;
		TreeObject[] children = getChildren();
		for (TreeObject child : children) {
			if (child.getType() == TreeObject.STATUS_TYPE) {
				child.setName(status);
				child.setImagePath(successImage);
				return;
			}
		}
		addChild(new TreeObject(status, TreeObject.STATUS_TYPE, successImage));
	}

	public String getStatus() {
		return mStatus;
	}

	public void setDesc(String desc) {
		mDesc = desc;
		addChild(new TreeObject(desc, TreeObject.ELEMENT_TYPE,
				TreeObject.NO_IMAGE));
	}

	public String getDesc() {
		return mDesc;
	}

	public void setParent(TreeParent parent) {
		super.setParent(parent);
		setParentContainer(parent.getName());

	}

	public Hashtable<String, String> getDictionary() {
		Hashtable<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(TPEvent.PROJECT_KEY, getProject());
		dictionary.put(TPEvent.PARENT_CONTAINER_KEY, getParentContainer());
		dictionary.put(TPEvent.TEST_NAME_KEY, getName());
		dictionary.put(TPEvent.ID_KEY, getID());
		dictionary.put(TPEvent.STATUS_KEY, getStatus());
		return dictionary;
	}

	public static String getDateTime() {
		Date now = new Date();
		return SIMPLE_DATE_FORMATTER.format(now);
	}

}
