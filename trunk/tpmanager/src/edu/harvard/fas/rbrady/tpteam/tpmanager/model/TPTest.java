/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.model;

public class TPTest {
	/** Dir where Eclipse installed */
	private String mEclipse = "c:/Java/Eclipse3.2.1/eclipse";
	/** Dir where workspace unpacked */
	private String mWorkspace = "c:/workspace_xmpp";
	/** Project in workspace where testsuite located */
	private String mProject = "edu.harvard.fas.rbrady.tpteam.demo.tests";
	/** Project relative path to testsuite */
	private String mSuiteName =  "AddUser.testsuite";
	/** Name of Test */
	private String mName;

	public TPTest()
	{
		
	}
	
	public TPTest(String eclipse, String workspace, String project, String suiteName, String name)
	{
		mEclipse = eclipse;
		mWorkspace = workspace;
		mProject = project;
		mSuiteName = suiteName;
		mName = name;
	}
	
	public void setEclipse(String eclispe)
	{
		mEclipse = eclispe;
	}
	
	public String getEclipse()
	{
		return mEclipse;
	}
	
	public void setWorkspace(String workspace)
	{
		mWorkspace = workspace;
	}
	
	public String getWorkspace()
	{
		return mWorkspace;
	}
	
	public void setProject(String project)
	{
		mProject = project;
	}
	
	public String getProject()
	{
		return mProject;
	}
	
	public void setSuiteName(String suiteName)
	{
		mSuiteName = suiteName;
	}
	
	public String getSuiteName()
	{
		return mSuiteName;
	}
	
	public void setName(String name)
	{
		mName = name;
	}
	
	public String getName()
	{
		return mName;
	}


}
