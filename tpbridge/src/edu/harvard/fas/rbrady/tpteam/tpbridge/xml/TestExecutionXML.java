package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

import edu.harvard.fas.rbrady.tpteam.tpbridge.model.AbstractTreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNodeChangeListener;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEvent;

public class TestExecutionXML {

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
	
	private static String getExecEntityName(TPEvent tpEvent)
	{
		StringBuilder nameBuffer = new StringBuilder();
		nameBuffer.append(tpEvent.getDictionary().get(TPEvent.VERDICT_KEY));
		nameBuffer.append(" ");
		nameBuffer.append(tpEvent.getDictionary().get(TPEvent.TIMESTAMP_KEY));
		nameBuffer.append(" " + tpEvent.getDictionary().get(TPEvent.ECFID_KEY));
		return nameBuffer.toString();
	}
	
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
	
	
		public static void main(String[] args)
	{
		String topic = "test_topic";
		Hashtable<String, String> dict = new Hashtable<String, String>();
		dict.put(TPEvent.ID_KEY, TPEntity.EXEC + "_" + "2");
		dict.put(TPEvent.PARENT_ID_KEY, "1");
		dict.put(TPEvent.VERDICT_KEY, "pass");
		dict.put(TPEvent.TIMESTAMP_KEY, new Date().toString());
		dict.put(TPEvent.ECFID_KEY, "foobar.gmail.com");
		TPEvent tpEvent = new TPEvent(topic, dict);

		String xml = getTPEntityXML(getTPEntityFromExecEvent(tpEvent));
		System.out.println("TPEntity XML:\n" + xml);
		
		TPEntity tpEntity = getTPEntityFromXML(xml);
		System.out.println("TPEntity ID: " + tpEntity.getID() + ", Name: " + tpEntity.getName() + ", ParentID: " 
				+ tpEntity.getParent().getID());
	}

}
