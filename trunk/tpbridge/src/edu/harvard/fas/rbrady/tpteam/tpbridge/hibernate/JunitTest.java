package edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate;

// Generated Nov 10, 2006 5:22:58 PM by Hibernate Tools 3.2.0.beta8

/**
 * JunitTest generated by hbm2java
 */
public class JunitTest implements java.io.Serializable {

	// Fields    

	private int id;

	private Test test;

	private String eclipseHome;

	private String workspace;

	private String project;

	private String reportDir;

	private String tptpConnection;
	
	private String testSuite;

	// Constructors

	/** default constructor */
	public JunitTest() {
	}

	/** minimal constructor */
	public JunitTest(int id, Test test) {
		this.id = id;
		this.test = test;
	}

	/** full constructor */
	public JunitTest(int id, Test test, String eclipseHome, String workspace,
			String project, String reportDir, String tptpConnection, String testSuite) {
		this.id = id;
		this.test = test;
		this.eclipseHome = eclipseHome;
		this.workspace = workspace;
		this.project = project;
		this.reportDir = reportDir;
		this.tptpConnection = tptpConnection;
		this.testSuite = testSuite;
	}

	// Property accessors
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Test getTest() {
		return this.test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public String getEclipseHome() {
		return this.eclipseHome;
	}

	public void setEclipseHome(String eclipseHome) {
		this.eclipseHome = eclipseHome;
	}

	public String getWorkspace() {
		return this.workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getProject() {
		return this.project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getReportDir() {
		return this.reportDir;
	}

	public void setReportDir(String reportDir) {
		this.reportDir = reportDir;
	}

	public String getTptpConnection() {
		return this.tptpConnection;
	}

	public void setTptpConnection(String tptpConnection) {
		this.tptpConnection = tptpConnection;
	}
	
	public String getTestSuite() {
		return this.testSuite;
	}

	public void setTestSuite(String testSuite) {
		this.testSuite = testSuite;
	}

}