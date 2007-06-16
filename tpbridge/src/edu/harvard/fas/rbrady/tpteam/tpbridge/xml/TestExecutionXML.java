/********************************************************************
 * 
 * File		:	TestExecutionXML.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Contains various utility methods for the
 * 				XML serialization and deserialization of 
 * 				TestExecution objects.
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.AbstractTreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNodeChangeListener;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

/**********************************************************************
 * File 		: 	TestExecutionXML.java
 * 
 * Description 	: 	Contains various utility methods for the XML 
 * 					serialization and deserialization of TestExecution 
 * 					objects.
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ***********************************************************************/
public class TestExecutionXML {

	/**
	 * Extracts a TestExecution TPEntity from a TPEvent
	 * 
	 * @param tpEvent the TPEvent
	 * @return the corresponding TPEntity
	 */
	public static TPEntity getTPEntityFromExecEvent(TPEvent tpEvent)
	{
		TPEntity tpEntity = null;
		String id = tpEvent.getID();
		String name = getExecEntityName(tpEvent);
		String verdict = tpEvent.getDictionary().get(TPEvent.VERDICT_KEY).toUpperCase();
		if(verdict.equals(TPEntity.PASS))
			tpEntity = new TPEntity(id, name, null, TPEntity.EXEC_PASS);
		else if(verdict.equals(TPEntity.FAIL))
			tpEntity = new TPEntity(id, name, null, TPEntity.EXEC_FAIL);
		else if(verdict.equals(TPEntity.ERROR))
			tpEntity = new TPEntity(id, name, null, TPEntity.EXEC_ERROR);
		else
			tpEntity = new TPEntity(id, name, null, TPEntity.EXEC_INCONCLUSIVE);

		String parentID = tpEvent.getDictionary().get(TPEvent.PARENT_ID_KEY);
		TPEntity parent = new TPEntity(parentID, null, null, null);
		parent.addChild(tpEntity);
		tpEntity.setParent(parent);
		return tpEntity;
	}
	
	/**
	 * Gets the TestExecution TPEntity name based on the 
	 * execution verdict, timestamp, and execution ID
	 * @param tpEvent the TPEvent
	 * @return the TPEntity name
	 */
	private static String getExecEntityName(TPEvent tpEvent)
	{
		StringBuilder nameBuffer = new StringBuilder();
		nameBuffer.append(tpEvent.getDictionary().get(TPEvent.VERDICT_KEY));
		nameBuffer.append(" ");
		nameBuffer.append(tpEvent.getDictionary().get(TPEvent.TIMESTAMP_KEY));
		nameBuffer.append(" " + tpEvent.getDictionary().get(TPEvent.ECFID_KEY));
		return nameBuffer.toString();
	}
	
	/**
	 * Gets the String XML serialization of the TPEntity
	 * @param tpEntity the TPEntity to be serialized
	 * @return the String XML serialization
	 */
	public static String getTPEntityXML(TPEntity tpEntity)
	{
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			BeanWriter bWriter = new BeanWriter(baos);
			bWriter.setWriteEmptyElements(false);
			bWriter.enablePrettyPrint();
			bWriter.writeXmlDeclaration("<?xml version='1.0' ?>");
			bWriter.write("tpEntity", tpEntity);
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toString();
	}
	
	/**
	 * Reconstitutes the TPEntity object from its
	 * given String XML serialization
	 * 
	 * @param entityXML the String XML serialization
	 * @return the reconstituted TPEntity object
	 */
	public static TPEntity getTPEntityFromXML(String entityXML)
	{
		TPEntity tpEntity = null;
		try
		{
		BeanReader reader = new BeanReader();
		reader.registerBeanClass(AbstractTreeNode.class);
		reader.registerBeanClass(ITreeNode.class);
		reader.registerBeanClass(ITreeNodeChangeListener.class);
		reader.registerBeanClass("tpEntity", TPEntity.class);
		StringReader xmlReader = new StringReader(entityXML);
		tpEntity = (TPEntity)reader.parse(xmlReader);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return tpEntity;
	}
}
