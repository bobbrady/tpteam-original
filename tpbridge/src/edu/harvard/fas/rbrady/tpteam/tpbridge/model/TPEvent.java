/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

import java.io.Serializable;
import java.util.Hashtable;

import org.osgi.service.event.Event;

public class TPEvent implements Serializable {

	private static final long serialVersionUID = 7318549536622346381L;

	public static final String PROJECT_KEY = "PROJECT";

	public static final String PROJECT_ID_KEY = "PROJ_ID";

	public static final String PROJ_PROD_XML_KEY = "PROD_PROJ_XML";

	public static final String TEST_XML_KEY = "TEST_XML";

	public static final String TEST_TREE_XML_KEY = "TEST_TREE_XML";

	public static final String TEST_EXEC_XML_KEY = "TEST_EXEC_XML";

	public static final String TEST_PROP_XML_KEY = "TEST_PROP_XML";

	public static final String CHART_DATASET_XML_KEY = "CHART_DATASET_XML_KEY";

	public static final String PARENT_CONTAINER_KEY = "PARENT_CONTAINER";

	public static final String VERDICT_KEY = "VERDICT";

	public static final String TEST_NAME_KEY = "TEST_NAME";

	public static final String TEST_DESC_KEY = "TEST_DESC";

	public static final String TIMESTAMP_KEY = "TIMESTAMP";

	public static final String COMMENTS_KEY = "COMMENTS";

	public static final String ID_KEY = "ID";

	public static final String PARENT_ID_KEY = "PARENT_ID";

	public static final String ECFID_KEY = "ECFID";

	public static final String STATUS_KEY = "STATUS";

	public static final String SEND_TO = "SEND_TO";

	public static final String FROM = "FROM";

	private Hashtable<String, String> mDictionary;

	private String mTopic;

	public TPEvent(Event event) {
		mDictionary = new Hashtable<String, String>();
		for (String propName : event.getPropertyNames())
			mDictionary.put(propName, (String) event.getProperty(propName));

		mTopic = event.getTopic();
	}

	public TPEvent(String topic, String project, String parent, String name,
			String id, String status) {
		mDictionary = new Hashtable<String, String>();
		mDictionary.put(PROJECT_KEY, project);
		mDictionary.put(PARENT_CONTAINER_KEY, parent);
		mDictionary.put(TEST_NAME_KEY, name);
		mDictionary.put(ID_KEY, id);
		mDictionary.put(STATUS_KEY, status);
		mTopic = topic;
	}

	public TPEvent(String topic, Hashtable<String, String> dictionary) {

		mDictionary = dictionary;
		mTopic = topic;
	}

	public void setTopic(String topic) {
		mTopic = topic;
	}

	public String getTopic() {
		return mTopic;
	}

	public Hashtable<String, String> getDictionary() {
		return mDictionary;
	}

	public String getProject() {
		return (String) mDictionary.get(PROJECT_KEY);
	}

	public String getParentContainer() {
		return (String) mDictionary.get(PARENT_CONTAINER_KEY);
	}

	public String getTestName() {
		return (String) mDictionary.get(TEST_NAME_KEY);
	}

	public String getID() {
		return (String) mDictionary.get(ID_KEY);
	}

	public String getStatus() {
		return (String) mDictionary.get(STATUS_KEY);
	}

	public void setStatus(String status) {
		mDictionary.put(STATUS_KEY, status);
	}

}