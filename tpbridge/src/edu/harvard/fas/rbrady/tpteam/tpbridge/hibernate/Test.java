/********************************************************************
 * 
 * File		:	Test.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Hibernate generated model class supplemented with
 * 				helper methods
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/********************************************************************
 * File			:	Test.java
 *
 * Description	: 	Hibernate generated model class supplemented with
 * 					helper methods
 *
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$
 * Copyright (c) 2007 Bob Brady
 *********************************************************************/
public class Test implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;

	private int id;

	private Project project;

	private TestType testType;

	private TpteamUser createdBy;

	private TpteamUser modifiedBy;

	private Test parent;

	private String name;

	private String description;

	private Character isFolder;

	private String path;

	private Date createdDate;

	private Date modifiedDate;

	private Set<TestExecution> testExecutions = new HashSet<TestExecution>(0);

	private Set<Test> children = new HashSet<Test>(0);

	private Set<JunitTest> junitTests = new HashSet<JunitTest>(0);

	// Constructors

	/** default constructor */
	public Test() {
	}

	/** minimal constructor */
	public Test(int id) {
		this.id = id;
	}

	/** full constructor */
	public Test(int id, Project project, TestType testType,
			TpteamUser createdBy, Test parent, String name, String description,
			Character isFolder, String path, Set<TestExecution> testExecutions,
			Set<Test> children, Set<JunitTest> junitTests) {
		this.id = id;
		this.project = project;
		this.testType = testType;
		this.createdBy = createdBy;
		this.parent = parent;
		this.name = name;
		this.description = description;
		this.isFolder = isFolder;
		this.path = path;
		this.testExecutions = testExecutions;
		this.children = children;
		this.junitTests = junitTests;
	}

	// Property accessors
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public TestType getTestType() {
		return this.testType;
	}

	public void setTestType(TestType testType) {
		this.testType = testType;
	}

	public TpteamUser getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(TpteamUser createdBy) {
		this.createdBy = createdBy;
	}

	public TpteamUser getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(TpteamUser modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Test getParent() {
		return this.parent;
	}

	public void setParent(Test parent) {
		this.parent = parent;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Character getIsFolder() {
		return this.isFolder;
	}

	public void setIsFolder(Character isFolder) {
		this.isFolder = isFolder;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Set<TestExecution> getTestExecutions() {
		return this.testExecutions;
	}

	public void setTestExecutions(Set<TestExecution> testExecutions) {
		this.testExecutions = testExecutions;
	}

	public Set<Test> getChildren() {
		return this.children;
	}

	public void setChildren(Set<Test> children) {
		this.children = children;
	}

	public void addChild(Test child) {
		this.children.add(child);
	}

	public Set<JunitTest> getJunitTests() {
		return this.junitTests;
	}

	public void setJunitTests(Set<JunitTest> junitTests) {
		this.junitTests = junitTests;
	}
	
	public void addJunitTest(JunitTest junitTest)
	{
		this.junitTests.add(junitTest);
	}

	/**
	 * Utility method for printing the test node
	 * tree to a given depth starting with this 
	 * node
	 * 
	 * @param depth
	 */
	public void printNode(int depth) {
		String pad = "";
		for (int idx = 0; idx < depth; idx++)
			pad += "\t";
		System.out.println(pad + "printNode: " + name);
		for (Test child : getChildren())
			child.printNode(depth + 1);
	}

	/**
	 * Initializes a Test "Skeleton" for use in displaying Tests in GUI trees
	 * 
	 * Recursively initializes ID, Name, Description, isFolder, testType,
	 * parent, for this Test and all its children
	 */
	public void initSkeleton() {
		getId();
		getName();
		getDescription();
		getIsFolder();
		Test parent = getParent();
		TestType testType = getTestType();
		if (parent != null)
			parent.getId();
		if (testType != null)
			testType.getName();
		for (Test child : getChildren())
			child.initSkeleton();
	}

	public void initProps(boolean includeExec) {
		getId();
		getName();
		getDescription();
		getCreatedBy().getFirstName();
		getCreatedBy().getLastName();
		getCreatedDate();
		getIsFolder();
		TestType testType = getTestType();
		if (testType != null)
			testType.getName();
		if (getModifiedBy() != null) {
			getModifiedBy().getFirstName();
			getModifiedBy().getLastName();
			getModifiedDate();
		}
		if (getJunitTests() != null) {
			for (JunitTest junit : getJunitTests()) {
				junit.getEclipseHome();
				junit.getWorkspace();
				junit.getProject();
				junit.getTestSuite();
				junit.getReportDir();
				junit.getTptpConnection();
			}
		}
		if (includeExec && getTestExecutions() != null) {
			for (TestExecution testExec : getTestExecutions()) {
				testExec.getStatus();
				testExec.getTpteamUser().getFirstName();
				testExec.getTpteamUser().getLastName();
				testExec.getTpteamUser().getEcfId();
				testExec.getExecDate();
				testExec.getComments();
			}
		}
	}

}
