/*******************************************************************************
 * Copyright (c) 2006 Robert Brady. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Robert Brady - initial API and implementation
 ******************************************************************************/

package edu.harvard.fas.rbrady.tpteam.tpmanager.tptp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.hyades.automation.client.adapters.java.AutomationClientAdapter;

public class AutomationClient {

	@SuppressWarnings("unchecked")
	public String run(String eclipse, String workspace, String project,
			String[] suites, String report, String connection) {

		// Instantiate TPTP automatable services' Java automation client adapter
		AutomationClientAdapter adapter = new AutomationClientAdapter(eclipse);

		Properties properties = new Properties();
		properties.setProperty("connection", connection);
		properties.setProperty("quiet", "true");

		adapter
				.execute("org.eclipse.hyades.test.tools.core.verify",
						properties);

		boolean verified = Boolean.valueOf(properties.getProperty("verified"))
				.booleanValue();

		if (!verified) {
			return "error";
		}

		System.out.println("AgentController is running...");

		final List results;

		properties = new Properties();
		properties.setProperty("workspace", workspace);
		properties.setProperty("project", project);

		// Create list of test suites for suite property
		int count = suites.length;
		List<String> list = new ArrayList<String>(count);
		for (int i = 0; i < count; i++) {
			list.add(suites[i]);
		}

		// Add list to suite property
		properties.put("suite", list);

		// Execute test results interrogation service
		adapter.execute("org.eclipse.hyades.test.tools.core.execute",
				properties);

		// Retrieve results back from return list
		results = (List) properties.get("results");
		System.out.println("Results collected: " + results);
	

		// Configure service properties for this service invocation
		properties = new Properties();
		properties.setProperty("workspace", workspace);
		properties.setProperty("project", project);
		properties.setProperty("quiet", "true");

		// Add list to results property
		properties.put("results", results);

		// Execute test results interrogation service
		adapter.execute("org.eclipse.hyades.test.tools.core.interrogate",
				properties);

		// Output verdict indicated from test results
		System.out.println("Verdict: '" + properties.getProperty("verdict")
				+ "'");

		return properties.getProperty("verdict");

	}
	
	public static void main(String[] args)
	{
		
		String eclipse = "c:/Java/Eclipse3.2.1_ECF0.9.6/eclipse";
		String workspace = "c:/tpteam/workspace_tpteam_test";
		String project = "edu.harvard.fas.rbrady.tpteam.test";
		String[] suites = new String[]{"edu/harvard/fas/rbrady/tpteam/tpbridge/hibernate/test/ProductTest.testsuite"};
		String connection = "tptp:rac://localhost:10002/default";
		String report = eclipse + "/reports";
		
		
		/*
		String eclipse = "/usr/local/eclipse";
		String workspace = "/home/tpteam/workspace_tpteam_test";
		String project = "edu.harvard.fas.rbrady.tpteam.test";
		String[] suites = new String[]{"edu/harvard/fas/rbrady/tpteam/tpmanager/hibernate/test/ProductTest.testsuite"};
		String connection = "tptp:rac://192.168.0.12:10002/default";
		String report = eclipse + "/reports";
		*/

		
		AutomationClient client = new AutomationClient();
		client.run(eclipse, workspace, project, suites, report, connection);
		
	}

}
