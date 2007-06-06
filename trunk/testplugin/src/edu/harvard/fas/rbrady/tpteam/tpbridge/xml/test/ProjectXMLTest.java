package edu.harvard.fas.rbrady.tpteam.tpbridge.xml.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import junit.framework.Test;

import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.xml.ProjectXML;

public class ProjectXMLTest extends HyadesTestCase {

	public static final String[] PROJ_XML = { "<?xml version='1.0' ?>",
			"<projects>",
			"<project description=\"proj2 desc\" id=\"2\" name=\"proj2\">",
			"<product description=\"prod2 desc\" id=\"20\" name=\"prod2\">",
			"<projects>", "<project idref=\"2\"/>", "</projects>",
			"</product>", "<tests/>", "<tpteamUsers/>", "</project>",
			"<project description=\"proj1 desc\" id=\"1\" name=\"proj1\">",
			"<product description=\"prod1 desc\" id=\"10\" name=\"prod1\">",
			"<projects>", "<project idref=\"1\"/>", "</projects>",
			"</product>", "<tests/>", "<tpteamUsers/>", "</project>",
			"</projects>" };

	public static final String LINE_SPLIT_REGEX = "\\r\\n";

	/**
	 * Constructor for ProjectXMLTest.
	 * 
	 * @param name
	 */
	public ProjectXMLTest(String name) {
		super(name);
	}

	/**
	 * Returns the JUnit test suite that implements the <b>ProjectXMLTest</b>
	 * definition.
	 */
	public static Test suite() {
		HyadesTestSuite projectXMLTest = new HyadesTestSuite("ProjectXMLTest");
		projectXMLTest.setArbiter(DefaultTestArbiter.INSTANCE).setId(
				"FDB1B8473D226462E726CA80ED4F11DB");
	
		projectXMLTest.addTest(new ProjectXMLTest("testGetXML").setId(
				"FDB1B8473D2264625D6183C0ED5011DB").setTestInvocationId(
				"FDB1B8473D22646276024B30ED5011DB"));
	
		projectXMLTest.addTest(new ProjectXMLTest("testProjsFromXML").setId(
				"FDB1B8473D2264626179CB20ED5011DB").setTestInvocationId(
				"FDB1B8473D2264627B16A5D0ED5011DB"));
		return projectXMLTest;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
	}

	/**
	 * testGetXML
	 * 
	 * @throws Exception
	 */
	public void testGetXML() throws Exception {
		HashSet<Project> projs = new HashSet<Project>();
		Product prod2 = new Product(20, "prod2", "prod2 desc",
				new HashSet<Project>());
		Product prod1 = new Product(10, "prod1", "prod1 desc",
				new HashSet<Project>());
		Project proj2 = new Project(2, prod2, "proj2", "proj2 desc", null, null);
		Project proj1 = new Project(1, prod1, "proj1", "proj1 desc", null, null);
		projs.add(proj1);
		projs.add(proj2);
		prod1.getProjects().add(proj1);
		prod2.getProjects().add(proj2);

		String projsXML = ProjectXML.getXML(projs);

		String[] xmlLines = projsXML.split(LINE_SPLIT_REGEX);
		assertTrue(
				"Error: returned XML number of lines does not equal number in test reference XML",
				xmlLines.length == PROJ_XML.length);
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(String line : PROJ_XML)
		{
			map.put(line, 1);
		}

		for (String line : xmlLines) {
			assertTrue("Error returned XML line " + line.trim()
					+ " not found in test reference XML", map.get(line.trim()) == 1);
		}
	}

	/**
	 * testProjsFromXML
	 * 
	 * @throws Exception
	 */
	public void testProjsFromXML() throws Exception {

		StringBuilder projsXML = new StringBuilder();
		for (String line : PROJ_XML)
			projsXML.append(line);

		List<Project> projs = ProjectXML.getProjsFromXML(projsXML.toString());
		assertTrue("Error: Number of Projects returned from XML "
				+ projs.size() + " != 2", projs.size() == 2);
		assertTrue("Error: Returned Projects do not contain proj1", projs
				.get(1).getName().equals("proj1"));
		assertTrue("Error: Returned Projects do not contain proj2", projs
				.get(0).getName().equals("proj2"));
		assertTrue("Error: Returned Project proj1 does not contain prod1",
				projs.get(1).getProduct().getName().equals("prod1"));
		assertTrue("Error: Returned Project proj2 does not contain prod2",
				projs.get(0).getProduct().getName().equals("prod2"));
		assertTrue(
				"Error: Returned Project proj1 description does not equal \"proj1 desc\"",
				projs.get(1).getDescription().equals("proj1 desc"));
		assertTrue(
				"Error: Returned Project proj2 description does not equal \"prod2 desc\"",
				projs.get(0).getDescription().equals("proj2 desc"));
		assertTrue("Error: Returned Project proj1 ID does not equal \"1\"",
				projs.get(1).getId() == 1);
		assertTrue("Error: Returned Project proj2 ID does not equal \"2\"",
				projs.get(0).getId() == 2);
	}

}
