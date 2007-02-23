package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Test;

public class TestXML {
	
	public static Element getTestTreeElement(Document dom, Test test)
	{
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
		if(test.getParent() != null)
			parentIdText = dom.createTextNode(String.valueOf(test.getParent().getId()));
		else
			parentIdText = dom.createTextNode("0");
		parentIdEle.appendChild(parentIdText);
		testEle.appendChild(parentIdEle);
		
		Element testTypeEle = dom.createElement("testTypeName");
		Text testTypeText;
		if(test.getTestType() != null)
			testTypeText = dom.createTextNode(test.getTestType().getName());
		else
			testTypeText = dom.createTextNode(null);
		testTypeEle.appendChild(testTypeText);
		testEle.appendChild(testTypeEle);

		return testEle;
	}

}
