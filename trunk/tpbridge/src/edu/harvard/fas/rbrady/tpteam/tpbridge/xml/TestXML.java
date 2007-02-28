package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;

public class TestXML {

	public static Element getTestTreeElement(Document dom, Test test) {
		Element testEle = dom.createElement("test");
		testEle.setAttribute("id", String.valueOf(test.getId()));

		Element nameEle = dom.createElement("name");
		Text nameText = dom.createTextNode(test.getName());
		nameEle.appendChild(nameText);
		testEle.appendChild(nameEle);

		Element descEle = dom.createElement("description");
		Text descText = dom.createTextNode(test.getDescription());
		descEle.appendChild(descText);
		testEle.appendChild(descEle);

		Element isFolderEle = dom.createElement("isFolder");
		Text isFolderText = dom.createTextNode(test.getIsFolder().toString());
		isFolderEle.appendChild(isFolderText);
		testEle.appendChild(isFolderEle);

		Element parentIdEle = dom.createElement("parentId");
		Text parentIdText;
		if (test.getParent() != null)
			parentIdText = dom.createTextNode(String.valueOf(test.getParent()
					.getId()));
		else
			parentIdText = dom.createTextNode("0");
		parentIdEle.appendChild(parentIdText);
		testEle.appendChild(parentIdEle);

		Element testTypeEle = dom.createElement("testTypeName");
		Text testTypeText;
		if (test.getTestType() != null)
			testTypeText = dom.createTextNode(test.getTestType().getName());
		else
			testTypeText = dom.createTextNode(null);
		testTypeEle.appendChild(testTypeText);
		testEle.appendChild(testTypeEle);

		return testEle;
	}

	public static TPEntity[] getTPEntityFromDoc(Document dom) {
		HashMap<Integer, TPEntity> tpEntities = new HashMap<Integer, TPEntity>();
		HashMap<Integer, ArrayList<TPEntity>> childEntities = new HashMap<Integer, ArrayList<TPEntity>>();
		try {
			
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();

			XPathExpression expr = xpath.compile("/tests/test");
			NodeList nodes = (NodeList) expr.evaluate(dom,
					XPathConstants.NODESET);


		

			for (int idx = 0; idx < nodes.getLength(); idx++) {
				int parentId = Integer.parseInt(xpath.evaluate("./parentId",
						nodes.item(idx)));
				TPEntity tpEntity = getTPEntityFromNode(xpath, nodes.item(idx));
				tpEntities.put(tpEntity.getID(), tpEntity);
				if (childEntities.get(Integer.valueOf(parentId)) == null) {
					ArrayList<TPEntity> children = new ArrayList<TPEntity>();
					children.add(tpEntity);
					childEntities.put(parentId, children);
				} else {
					childEntities.get(parentId).add(tpEntity);
				}
			}

			for (Integer key : tpEntities.keySet()) {
				TPEntity parentEnt = tpEntities.get(key);
				ArrayList<TPEntity> children = null;
				if ((children = childEntities.get(key)) != null) {
					for (TPEntity childEntity : children) {
						childEntity.setParent(parentEnt);
					}
					parentEnt.setChildren(children.toArray(new TPEntity[0]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childEntities.get(0).toArray(new TPEntity[0]);
	}

	public static TPEntity getTPEntityFromNode(XPath xpath, Node node)
			throws XPathExpressionException {
		TPEntity tpEntity = null;
		String id = xpath.evaluate("./@id", node);
		String name = xpath.evaluate("./name", node);
		String desc = xpath.evaluate("./description", node);
		String isFolder = xpath.evaluate("./isFolder", node);
		String testTypeName = xpath.evaluate("./testTypeName", node);

		if (isFolder != null && isFolder.equalsIgnoreCase("Y")) {
			tpEntity = new TPEntity(Integer.valueOf(id), name, desc,
					TPEntity.TPEntityType.FOLDER);
		} else if (testTypeName != null
				&& testTypeName.equalsIgnoreCase("JUnit")) {
			tpEntity = new TPEntity(Integer.valueOf(id), name, desc,
					TPEntity.TPEntityType.JUNIT_TEST);
		}
		return tpEntity;
	}

	public static TPEntity[] getExample() throws Exception {
	
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance(); // domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document dom = builder.parse("c:/Tests.xml");
        

		TPEntity[] tpEnts = TestXML.getTPEntityFromDoc(dom);
		for(TPEntity tpEnt : tpEnts)
		{
			System.out.println("ID: " + tpEnt.getID() + ", Name: " + tpEnt.getName());
			for(TPEntity child : tpEnt.getChildren())
				System.out.println("\tID: " + child.getID() + ", Name: " + child.getName());
		}
		return tpEnts;
	}

}
