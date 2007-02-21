package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;

public class ProductXML {

	public static Element getProductElement(Document dom, Product prod) {

		Element prodEle = dom.createElement("product");
		prodEle.setAttribute("id", String.valueOf(prod.getId()));

		Element nameEle = dom.createElement("name");
		Text nameText = dom.createTextNode(prod.getName());
		nameEle.appendChild(nameText);
		prodEle.appendChild(nameEle);

		Element descEle = dom.createElement("description");
		Text descText = dom.createTextNode(prod.getDescription());
		descEle.appendChild(descText);
		prodEle.appendChild(descEle);

		return prodEle;

	}
	
	public static void main(String[] args)
	{
		Document dom = XMLUtil.getDocument();
		Product prod = new Product(0, "Prod Test Name", "Prod Test Desc", null);
		dom.appendChild(ProductXML.getProductElement(dom, prod));
		String xml = XMLUtil.getXML(dom);
		System.out.println("Product XML:\n" + xml);
	}

}
