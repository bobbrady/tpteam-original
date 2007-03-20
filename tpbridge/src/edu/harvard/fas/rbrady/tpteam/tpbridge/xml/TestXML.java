package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.AbstractTreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNodeChangeListener;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;

public class TestXML {

	public static TPEntity getTPEntityFromTest(Test test)
	{
		TPEntity tpEntity = null;
		String id = String.valueOf(test.getId());
		String name = test.getName();
		String desc = test.getDescription();
		String isFolder = String.valueOf(test.getIsFolder());
		String testTypeName = test.getTestType().getName();

		if (isFolder != null && isFolder.equalsIgnoreCase("Y")) {
			tpEntity = new TPEntity(id, name, desc,
					TPEntity.FOLDER);
		} else if (testTypeName != null
				&& testTypeName.equalsIgnoreCase("JUnit")) {
			tpEntity = new TPEntity(id, name, desc,
					TPEntity.JUNIT_TEST);
		}
		return tpEntity;
		
	}
	
	public static String getXML(Test test)
	{
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			BeanWriter bWriter = new BeanWriter(baos);
			bWriter.setWriteEmptyElements(false);
			bWriter.enablePrettyPrint();
			bWriter.writeXmlDeclaration("<?xml version='1.0' ?>");
			bWriter.write("test", test);
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  baos.toString();
	}
	
	public static String getTPEntityXML(List<Test> tests, String projName)
	{
		ArrayList<ITreeNode> tpEntities = new ArrayList<ITreeNode>();
		TPEntity rootEntity = new TPEntity("0", projName, projName, TPEntity.FOLDER);

		for(Test test : tests)
		{
			tpEntities.add(getTPEntity(test, rootEntity));
		}
		rootEntity.setChildren(tpEntities);
		
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			BeanWriter bWriter = new BeanWriter(baos);
			bWriter.setWriteEmptyElements(false);
			bWriter.enablePrettyPrint();
			bWriter.writeXmlDeclaration("<?xml version='1.0' ?>");
			bWriter.write("tpEntity", rootEntity);
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toString();
	}
	
	public static TPEntity getTPEntity(Test test, TPEntity parent)
	{
		TPEntity tpEntity = null;
		String id = String.valueOf(test.getId());
		String name = test.getName();
		String desc = null /*test.getDescription()*/;
		String isFolder = String.valueOf(test.getIsFolder());
		String testTypeName = null;
		if(test.getTestType() !=null)
			testTypeName = test.getTestType().getName();

		if (isFolder != null && isFolder.equalsIgnoreCase("Y")) {
			tpEntity = new TPEntity(id, name, desc,
					TPEntity.FOLDER);
		} else if (testTypeName != null
				&& testTypeName.equalsIgnoreCase("JUnit")) {
			tpEntity = new TPEntity(id, name, desc,
					TPEntity.JUNIT_TEST);
		}
		tpEntity.setParent(parent);
		for(Test childTest : test.getChildren())
		{
			TPEntity childEntity = getTPEntity(childTest, tpEntity);
			tpEntity.addChild(childEntity);
		}
		return tpEntity;
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

}