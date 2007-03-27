package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.JunitTest;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.TestExecution;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.AbstractTreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNodeChangeListener;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;

public class TestXML {

	public static TPEntity getTPEntityFromTest(Test test) {
		TPEntity tpEntity = null;
		String id = String.valueOf(test.getId());
		String name = test.getName();
		String desc = test.getDescription();
		String isFolder = String.valueOf(test.getIsFolder());

		if (isFolder != null && isFolder.equalsIgnoreCase("Y")) {
			tpEntity = new TPEntity(id, name, desc, TPEntity.FOLDER);
		} else if (test.getTestType() != null
				&& test.getTestType().getName().equalsIgnoreCase("JUnit")) {
			tpEntity = new TPEntity(id, name, desc, TPEntity.JUNIT_TEST);
		}
		return tpEntity;

	}

	public static String getXML(Test test) {
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
		return baos.toString();
	}

	public static String getTPEntityXML(List<Test> tests, String projName) {
		ArrayList<ITreeNode> tpEntities = new ArrayList<ITreeNode>();
		TPEntity rootEntity = new TPEntity("0", projName, projName,
				TPEntity.FOLDER);

		for (Test test : tests) {
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

	public static TPEntity getTPEntity(Test test, TPEntity parent) {
		TPEntity tpEntity = null;
		String id = String.valueOf(test.getId());
		String name = test.getName();
		String desc = null /* test.getDescription() */;
		String isFolder = String.valueOf(test.getIsFolder());
		String testTypeName = null;
		if (test.getTestType() != null)
			testTypeName = test.getTestType().getName();

		if (isFolder != null && isFolder.equalsIgnoreCase("Y")) {
			tpEntity = new TPEntity(id, name, desc, TPEntity.FOLDER);
		} else if (testTypeName != null
				&& testTypeName.equalsIgnoreCase("JUnit")) {
			tpEntity = new TPEntity(id, name, desc, TPEntity.JUNIT_TEST);
		}
		tpEntity.setParent(parent);
		for (Test childTest : test.getChildren()) {
			TPEntity childEntity = getTPEntity(childTest, tpEntity);
			tpEntity.addChild(childEntity);
		}
		return tpEntity;
	}

	public static TPEntity getTPEntityFromXML(String entityXML) {
		TPEntity tpEntity = null;
		try {
			BeanReader reader = new BeanReader();
			reader.registerBeanClass(AbstractTreeNode.class);
			reader.registerBeanClass(ITreeNode.class);
			reader.registerBeanClass(ITreeNodeChangeListener.class);
			reader.registerBeanClass("tpEntity", TPEntity.class);
			StringReader xmlReader = new StringReader(entityXML);
			tpEntity = (TPEntity) reader.parse(xmlReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tpEntity;
	}
	
	public static Test getTestFromXML(String testXML) {
		Test test = null;
		try {
			BeanReader reader = new BeanReader();
			reader.registerBeanClass("test", Test.class);
			StringReader xmlReader = new StringReader(testXML);
			test = (Test) reader.parse(xmlReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test;
	}

	public static String getTestPropXML(Test test) {
		TPEntity[] tpEntities = getTPEntityTestProps(test);
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			BeanWriter bWriter = new BeanWriter(baos);
			bWriter.setWriteEmptyElements(false);
			bWriter.enablePrettyPrint();
			bWriter.writeXmlDeclaration("<?xml version='1.0' ?>");
			bWriter.writeXmlDeclaration("<tpEntities>");
			for (TPEntity tpEntity : tpEntities)
				bWriter.write("tpEntity", tpEntity);
			bWriter.writeXmlDeclaration("</tpEntities>");
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toString();

	}

	private static TPEntity[] getTPEntityTestProps(Test test) {
		ArrayList<TPEntity> list = new ArrayList<TPEntity>();

		addProp(list, "ID", String.valueOf(test.getId()));
		addProp(list, "Name", test.getName());
		addProp(list, "Description", test.getDescription());
		String name = test.getCreatedBy().getLastName() + ", "
				+ test.getCreatedBy().getLastName();
		addProp(list, "Created By", name);
		addProp(list, "Created Date", test.getCreatedDate().toString());

		if (test.getModifiedBy() != null) {
			name = test.getModifiedBy().getLastName() + ", "
					+ test.getModifiedBy().getLastName();
			addProp(list, "Modified By", name);
			addProp(list, "Modified Date", test.getModifiedDate().toString());
		}
		if (test.getIsFolder() == 'Y') {
			addProp(list, "TestType", TPEntity.FOLDER);
		} else if (test.getTestType().getName().toUpperCase().equals(
				TPEntity.JUNIT_TEST)) {
			addProp(list, "TestType", TPEntity.JUNIT_TEST);
			if (test.getJunitTests() != null)
				addJunitProps(list, test);
		}
		if (test.getTestExecutions() != null) {
			addExecProps(list, test);
		}
		return (TPEntity[]) list.toArray(new TPEntity[list.size()]);
	}

	private static void addProp(ArrayList<TPEntity> list, String type,
			String desc) {
		TPEntity tpEntity = new TPEntity();
		tpEntity.setType(type);
		if (desc != null)
			tpEntity.setDescription(desc);
		else
			tpEntity.setDescription("");
		list.add(tpEntity);
	}

	private static void addJunitProps(ArrayList<TPEntity> list, Test test) {
		for (JunitTest junit : test.getJunitTests()) {
			addProp(list, "Eclipse Home", junit.getEclipseHome());
			addProp(list, "Eclipse Workspace", junit.getWorkspace());
			addProp(list, "Eclipse Project", junit.getProject());
			addProp(list, "TPTP Testsuite", junit.getTestSuite());
			addProp(list, "TPTP Report Dir", junit.getReportDir());
			addProp(list, "TPTP Connection", junit.getTptpConnection());
		}
	}

	private static void addExecProps(ArrayList<TPEntity> list, Test test) {
		for (TestExecution testExec : test.getTestExecutions()) {
			String type = null;
			StringBuilder desc = new StringBuilder();
			if (testExec.getStatus() == 'P') {
				type = TPEntity.EXEC_PASS;
			} else if (testExec.getStatus() == 'F') {
				type = TPEntity.EXEC_FAIL;
			} else if (testExec.getStatus() == 'E') {
				type = TPEntity.EXEC_ERROR;
			} else {
				type = TPEntity.EXEC_INCONCLUSIVE;
			}
			desc.append(testExec.getExecDate().toString());
			desc.append(" " + testExec.getTpteamUser().getLastName() + ", "
					+ testExec.getTpteamUser().getLastName());
			desc.append(" (" + testExec.getTpteamUser().getEcfId() + ")");
			addProp(list, type, desc.toString());
		}
	}

	public static TPEntity[] getTPEntitiesFromXML(String testPropXML) {
		ArrayList<TPEntity> tpEntities = null;
		try {
			BeanReader reader = new BeanReader();
			reader.registerBeanClass("tpEntities", ArrayList.class);
			reader.registerBeanClass("tpEntitity", TPEntity.class);
			StringReader xmlReader = new StringReader(testPropXML);
			tpEntities = (ArrayList<TPEntity>) reader.parse(xmlReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (TPEntity[]) tpEntities.toArray(new TPEntity[tpEntities.size()]);
	}

}
