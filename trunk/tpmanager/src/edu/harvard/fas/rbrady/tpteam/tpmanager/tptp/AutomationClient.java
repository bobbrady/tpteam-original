/********************************************************************
 * 
 * File		:	AutomationClient.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	A wrapper around the TPTP AutomationClientAdapter, 
 * 				utilizes the TPTP AgentController to execute TPTP
 * 				test suites on behalf of the TPManager
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpmanager.tptp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.eclipse.hyades.automation.client.adapters.java.AutomationClientAdapter;

/*******************************************************************************
 * File 		: 	AutomationClient.java
 * 
 * Description 	: 	A wrapper around the TPTP AutomationClientAdapter, 
 * 					utilizes the TPTP AgentController to execute TPTP
 * 					test suites on behalf of the TPManager
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class AutomationClient {

	/**
	 * Runs the given TPTP test suites on the
	 * machine configuration specified by input
	 * paramters
	 * 
	 * @param eclipse the Eclipse Home path
	 * @param workspace the Eclipse Workspace path
	 * @param project the Eclipse project inside the workspace
	 * @param suites the TPTP test suites to be run
	 * @param report the path to place any generated reports
	 * @param connection the TPTP connection to the machine, local or remote
	 * @return the test execution verdict
	 */
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
}
