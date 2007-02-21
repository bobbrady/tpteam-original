package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XMLUtil {

	public static Document getDocument() {
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true); // never forget this!
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			System.out
					.println("Error while trying to instantiate DocumentBuilder "
							+ pce);
		}

		return db.newDocument();
	}

	public static String getXML(Document dom) {
		StringWriter stringOut = new StringWriter();
		try {
			OutputFormat newFormat = new OutputFormat(dom);
			XMLSerializer serial = new XMLSerializer(stringOut, newFormat);
			serial.asDOMSerializer();
			serial.serialize(dom.getDocumentElement());
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		return stringOut.toString();
	}
	
	public static Document getDocFromXml(String xml){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true); // never forget this!
		DocumentBuilder db = null;
		Document dom = null;
		try {
			db = dbf.newDocumentBuilder();
			dom = db.parse(new InputSource(new StringReader(xml)));
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return dom;
	}


}
