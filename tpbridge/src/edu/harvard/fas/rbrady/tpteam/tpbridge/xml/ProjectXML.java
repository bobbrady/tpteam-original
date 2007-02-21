package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Product;
import edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate.Project;

public class ProjectXML {

	public static Element getProjProdElement(Document dom, Project proj) {

		Element projEle = dom.createElement("project");
		projEle.setAttribute("id", String.valueOf(proj.getId()));

		Element nameEle = dom.createElement("name");
		Text nameText = dom.createTextNode(proj.getName());
		nameEle.appendChild(nameText);
		projEle.appendChild(nameEle);

		Element descEle = dom.createElement("description");
		Text descText = dom.createTextNode(proj.getDescription());
		descEle.appendChild(descText);
		projEle.appendChild(descEle);

		Element prodEle = ProductXML.getProductElement(dom, proj.getProduct());
		projEle.appendChild(prodEle);

		return projEle;
	}

	public static ArrayList<Project> getProjProdFromDoc(Document dom) {
		ArrayList<Project> projs = new ArrayList<Project>();
		try {

			/*
			 * DocumentBuilderFactory domFactory = DocumentBuilderFactory
			 * .newInstance(); //domFactory.setNamespaceAware(true); // never
			 * forget this! DocumentBuilder builder =
			 * domFactory.newDocumentBuilder(); Document doc =
			 * builder.parse("Project.xml");
			 */

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();

			XPathExpression expr = xpath.compile("/projects/project");
			NodeList nodes = (NodeList) expr.evaluate(dom,
					XPathConstants.NODESET);
			int idxp1 = 0;
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				idxp1 = idx + 1;

				String id = xpath.evaluate("/projects/project[" + idxp1
						+ "]/@id", nodes.item(idx));
				String name = xpath.evaluate("/projects/project[" + idxp1
						+ "]/name", nodes.item(idx));
				String desc = xpath.evaluate("/projects/project[" + idxp1
						+ "]/description", nodes.item(idx));

				String prodId = xpath.evaluate("/projects/project[" + idxp1
						+ "]/product/@id", nodes.item(idx));
				String prodName = xpath.evaluate("/projects/project[" + idxp1
						+ "]/product/name", nodes.item(idx));
				String prodDesc = xpath.evaluate("/projects/project[" + idxp1
						+ "]/product/description", nodes.item(idx));

				Product prod = new Product(Integer.parseInt(prodId), prodName,
						prodDesc, null);
				Project proj = new Project(Integer.parseInt(id), prod, name,
						desc, null, null);
				proj.setProduct(prod);

				projs.add(proj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return projs;
	}

	/*
	 * private Project getProjProd(Element el) { // for each <project> element
	 * get text or int values of // int, name, description + values for
	 * <product> child String id = el.getAttribute("id"); String name =
	 * getTextValue(el, "name"); String desc = getTextValue(el, "description"); //
	 * Create a new Employee with the value read from the xml nodes Employee e =
	 * new Employee(name, id, age, type);
	 * 
	 * return null; }
	 */

	public static void main(String[] args) {

		Document dom = XMLUtil.getDocument();
		Product prod = new Product(0, "Prod Test Name", "Prod Test Desc", null);
		Project proj = new Project(0, prod, "Proj Test Name", "Proj Test Desc",
				null, null);
		Product prod2 = new Product(1, "Prod Test Name 2", "Prod Test Desc 2", null);
		Project proj2 = new Project(1, prod2, "Proj Test Name 2", "Proj Test Desc 2",
				null, null);
		Element rootEle = dom.createElement("projects");
		dom.appendChild(rootEle);
		rootEle.appendChild(ProjectXML.getProjProdElement(dom, proj));
		rootEle.appendChild(ProjectXML.getProjProdElement(dom, proj2));
		String xml = XMLUtil.getXML(dom);
		System.out.println("Project XML:\n" + xml);

		ArrayList<Project> projs = getProjProdFromDoc(dom);
		for(Project proj3 : projs)
		{
			System.out.println("Proj Name: " + proj3.getName() + ", Prod Name: " + proj3.getProduct().getName());
		}
	}

}
