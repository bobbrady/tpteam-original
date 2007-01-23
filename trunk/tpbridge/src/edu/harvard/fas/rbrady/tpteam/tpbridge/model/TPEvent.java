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

import edu.harvard.fas.rbrady.tpteam.tpbridge.bridge.ITPBridge;

public class TPEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7318549536622346381L;

	public static final String PROJECT_KEY = "PROJECT";

	public static final String PARENT_CONTAINER_KEY = "PARENT_CONTAINER";

	public static final String TEST_NAME_KEY = "TEST_NAME";

	public static final String ID_KEY = "ID";

	public static final String STATUS_KEY = "STATUS";
	
	private Hashtable<String, String> mDictionary;

	private String mTopic;
	
	public TPEvent(Event event) {
		mDictionary = new Hashtable<String, String>();
		mDictionary.put(PROJECT_KEY, (String)event.getProperty(PROJECT_KEY));
		mDictionary.put(PARENT_CONTAINER_KEY,(String)event.getProperty(PARENT_CONTAINER_KEY));
		mDictionary.put(TEST_NAME_KEY,(String)event.getProperty(TEST_NAME_KEY));
		mDictionary.put(ID_KEY,(String)event.getProperty(ID_KEY));
		mDictionary.put(STATUS_KEY,(String)event.getProperty(STATUS_KEY));
		String ecfContainerID = (String)event.getProperty(ITPBridge.CONTAINER_ID_KEY);
		String ecfSharedObjectID = (String)event.getProperty(ITPBridge.SHARED_OBJECT_ID_KEY);
		
		mDictionary.put(ITPBridge.CONTAINER_ID_KEY, ecfContainerID);
		mDictionary.put(ITPBridge.SHARED_OBJECT_ID_KEY, ecfSharedObjectID);
		
		mTopic = event.getTopic();
	}

	public TPEvent(String topic, String project, String parent, String name, String id, String status) {
		mDictionary = new Hashtable<String, String>();
		mDictionary.put(PROJECT_KEY, project);
		mDictionary.put(PARENT_CONTAINER_KEY,parent);
		mDictionary.put(TEST_NAME_KEY,name);
		mDictionary.put(ID_KEY,id);
		mDictionary.put(STATUS_KEY,status);
		mTopic = topic;
	}

	public TPEvent(String topic, Hashtable<String,String> dictionary) {
		
		mDictionary = dictionary;
		mTopic = topic;
	}

	
	public static TPEvent[] getExamples() {
		TPEvent[] tpEvents = new TPEvent[2];
		tpEvents[0] = new TPEvent("topic1", "project1", "parent1", "test name1", "1", "pass");
		tpEvents[1] = new TPEvent("topic1", "project2", "parent2", "test name2", "2", "fail");
		return tpEvents;
	}
	
	public void setTopic(String topic)
	{
		mTopic = topic;
	}
	
	public String getTopic()
	{
		return mTopic;
	}

	public Hashtable<String,String> getDictionary()
	{
		return mDictionary;
	}
	
	public String getProject()
	{
		return (String)mDictionary.get(PROJECT_KEY);
	}
	
	public String getParentContainer()
	{
		return (String)mDictionary.get(PARENT_CONTAINER_KEY);
	}
	
	public String getTestName()
	{
		return (String)mDictionary.get(TEST_NAME_KEY);
	}
	
	public String getID()
	{
		return (String)mDictionary.get(ID_KEY);
	}
	
	public String getStatus()
	{
		return (String)mDictionary.get(STATUS_KEY);
	}
	
	public void setStatus(String status)
	{
		mDictionary.put(STATUS_KEY, status);
	}

}