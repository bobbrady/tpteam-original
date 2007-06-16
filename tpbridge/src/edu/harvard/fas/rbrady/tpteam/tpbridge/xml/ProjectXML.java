/********************************************************************
 * 
 * File		:	ProjectXML.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Contains various utility methods for the
 * 				XML serialization and deserialization of Project 
 * 				objects.
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;

/*******************************************************************************
 * File : ProjectXML.java
 * 
 * Description : Contains various utility methods for the XML serialization and
 * deserialization of Project objects.
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ProjectXML {

	/**
	 * Gets String XML representation of a Set of Projects in form:
	 * 
	 * <projects> <project id="1" name="proj1"...> <product id="1" name="prod1"
	 * .../> </project> <project id="2" name="proj2"...> <product id="2"
	 * name="prod2" .../> </project> </projects>
	 * 
	 * @param projs
	 *            Set of Projects
	 * @return String representation of Projects in XML
	 */
	public static String getXML(Set<Project> projs) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			BeanWriter bWriter = new BeanWriter(baos);
			bWriter.setWriteEmptyElements(false);
			bWriter.enablePrettyPrint();
			bWriter.writeXmlDeclaration("<?xml version='1.0' ?>");
			bWriter.writeXmlDeclaration("<projects>");
			for (Project proj : projs)
				bWriter.write("project", proj);
			bWriter.writeXmlDeclaration("</projects>");
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toString();
	}

	/**
	 * Extracts a List of Projects from XML String
	 * 
	 * @param xml
	 *            String representing projects
	 * @return ArrayList of project objects
	 */
	@SuppressWarnings("unchecked")
	public static List<Project> getProjsFromXML(String xml) {
		ArrayList<Project> projs = null;
		try {
			BeanReader reader = new BeanReader();
			reader.registerBeanClass("projects", ArrayList.class);
			reader.registerBeanClass("project", Project.class);
			StringReader xmlReader = new StringReader(xml);
			projs = (ArrayList<Project>) reader.parse(xmlReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projs;
	}

}
